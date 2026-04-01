package org.example.flinkjob;

import org.example.flinkjob.function.ClickEventDeserializationSchema;
import org.example.flinkjob.function.MetricWindowFunction;
import org.example.flinkjob.function.MinuteAggregateFunction;
import org.example.flinkjob.sink.ClickHouseSink;
import org.example.sharedevents.ClickEvent;
import org.example.sharedevents.MetricRow;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.time.Duration;

public class ClickAggregationJob {

    public static void main(String[] args) throws Exception {
        final String kafkaBootstrap = getArg(args, 0, "kafka:9092");
        final String kafkaTopic = getArg(args, 1, "ad-clicks");
        final String clickhouseJdbcUrl = getArg(args, 2, "jdbc:clickhouse://clickhouse:8123/default");
        final String clickhouseUser = getArg(args, 3, "vrani");
        final String clickhousePassword = getArg(args, 4, "veena");
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
                .assignTimestampsAndWatermarks(watermarkStrategy)
                .name("parse-and-watermark-click-events");

        DataStream<MetricRow> aggregated = events
                .keyBy(ClickEvent::getAdId)
                .window(TumblingEventTimeWindows.of(Time.minutes(1)))
                .aggregate(new MinuteAggregateFunction(), new MetricWindowFunction())
                .name("aggregate-clicks-per-ad-per-minute");

        aggregated
                .addSink(new ClickHouseSink(clickhouseJdbcUrl, clickhouseUser, clickhousePassword))
                .name("clickhouse-sink");

        env.execute("ad-click-aggregation-job");
    }

    private static String getArg(String[] args, int index, String defaultValue) {
        if (args.length > index && args[index] != null && !args[index].isBlank()) {
            return args[index];
        }
        return defaultValue;
    }
}