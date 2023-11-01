package spendreport;

import entity.DetailedAlert;
import entity.DetailedTransaction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import sink.DetailedAlertSink;
import source.DetailedTransactionSource;

public class DetailedFraudDetectionJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<DetailedTransaction> transactions = env
                .addSource(new DetailedTransactionSource())
                .name("transactions");

        DataStream<DetailedAlert> alerts = transactions
                .keyBy(DetailedTransaction::getAccountId)
                .process(new DetailedFraudDetector())
                .name("fraud-detector");

        alerts
                .addSink(new DetailedAlertSink())
                .name("send-alerts");

        env.execute("Fraud Detection");
    }
}
