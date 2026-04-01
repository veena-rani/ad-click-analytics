package org.example.flinkjob.function;

import org.example.sharedevents.ClickEvent;
import org.example.sharedevents.EventSerde;
import org.apache.flink.api.common.functions.MapFunction;

public class ClickEventDeserializationSchema implements MapFunction<String, ClickEvent> {
    @Override
    public ClickEvent map(String value) {
        return EventSerde.fromJson(value, ClickEvent.class);
    }
}