package org.example.flinkjob.sink;

import org.example.sharedevents.MetricRow;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ClickHouseSink extends RichSinkFunction<MetricRow> {

    private final String jdbcUrl;
    private final String username;
    private final String password;

    private transient Connection connection;
    private transient PreparedStatement statement;

    public ClickHouseSink(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        connection = DriverManager.getConnection(jdbcUrl, username, password);
        statement = connection.prepareStatement(
                "INSERT INTO ad_click_metrics_minute (ad_id, minute_bucket, click_count) VALUES (?, toDateTime(?), ?)"
        );
    }

    @Override
    public void invoke(MetricRow value, Context context) throws Exception {
        statement.setString(1, value.getAdId());
        statement.setLong(2, value.getMinuteBucketEpoch());
        statement.setLong(3, value.getClickCount());
        statement.executeUpdate();
    }

    @Override
    public void close() throws Exception {
        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }
}