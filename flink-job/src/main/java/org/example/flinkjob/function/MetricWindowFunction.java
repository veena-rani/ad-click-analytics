package org.example.flinkjob.function;


import org.example.sharedevents.MetricRow;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class MetricWindowFunction extends ProcessWindowFunction<Long, MetricRow, String, TimeWindow> {

    @Override
    public void process(String adId,
                        Context context,
                        Iterable<Long> elements,
                        Collector<MetricRow> out) {
        long count = elements.iterator().next();
        long minuteBucketEpoch = context.window().getStart() / 1000;
        out.collect(new MetricRow(adId, minuteBucketEpoch, count));
    }
}
