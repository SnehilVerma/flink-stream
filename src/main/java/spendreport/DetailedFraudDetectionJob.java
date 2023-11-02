package spendreport;

import entity.DetailedAlert;
import entity.DetailedTransaction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import sink.DetailedAlertSink;
import source.DetailedTransactionSource;

/*
 * This is the main class which runs StreamExecutionEnvironment job and finally output the alert if it's fraudulent
 */
public class DetailedFraudDetectionJob {
    public static void main(String[] args) throws Exception {
        //Setting up the stream environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //Creating transaction source
        DataStream<DetailedTransaction> transactions = env
                .addSource(new DetailedTransactionSource())
                .name("transactions");

        //Creating alert for DetailedFraudDetector  group by user id
        DataStream<DetailedAlert> alerts = transactions
                .keyBy(DetailedTransaction::getAccountId)
                .process(new DetailedFraudDetector())
                .name("fraud-detector");

        // output of alerts
        alerts
                .addSink(new DetailedAlertSink())
                .name("send-alerts");

        env.execute("Fraud Detection");
    }
}
