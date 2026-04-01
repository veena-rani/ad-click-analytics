package org.example.flinkjob;

import org.ad.flinkjob.function.ClickEventDeserializationSchema;
import org.ad.flinkjob.function.MetricWindowFunction;
import org.ad.flinkjob.function.MinuteAggregateFunction;
import org.ad.flinkjob.sink.ClickHouseSink;
import org.ad.sharedevents.ClickEvent;
import org.ad.sharedevents.MetricRow;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public class ClickAggregationJob {

    public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        InputStream input = ClickAggregationJob.class
                .getClassLoader()
                .getResourceAsStream("application.properties");

        if (input == null) {
            throw new RuntimeException("application.properties not found");
        }
        props.load(input);

        final String kafkaBootstrap = props.getProperty("kafka.bootstrap.servers");
        final String kafkaTopic = props.getProperty("kafka.topic");
        final String clickhouseJdbcUrl = props.getProperty("clickhouse.jdbc.url");
        final String clickhouseUser = props.getProperty("clickhouse.user");
        final String clickhousePassword = props.getProperty("clickhouse.password");

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(30000);

        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers(kafkaBootstrap)
                .setTopics(kafkaTopic)
                .setGroupId("flink-click-aggregator")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        WatermarkStrategy<ClickEvent> watermarkStrategy = WatermarkStrategy
                .<ClickEvent>forBoundedOutOfOrderness(Duration.ofSeconds(10))
                .withTimestampAssigner((event, ts) -> event.getEventTimeMillis());

        DataStream<ClickEvent> events = env
                .fromSource(source, WatermarkStrategy.noWatermarks(), "kafka-source")
                .map(new ClickEventDeserializationSchema())
                .assignTimestampsAndWatermarks(watermarkStrategy);

        DataStream<MetricRow> aggregated = events
                .keyBy(ClickEvent::getAdId)
                .window(TumblingEventTimeWindows.of(Time.minutes(1)))
                .aggregate(new MinuteAggregateFunction(), new MetricWindowFunction());

        aggregated.addSink(new ClickHouseSink(clickhouseJdbcUrl, clickhouseUser, clickhousePassword));

        env.execute("ad-click-aggregation-job");
    }

    private static String getArg(String[] args, int index, String defaultValue) {
        if (args.length > index && args[index] != null && !args[index].isBlank()) {
            return args[index];
        }
        return defaultValue;
    }
}
}
