package spendreport;

import entity.DetailedAlert;
import entity.DetailedTransaction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;


public class DetailedFraudDetector extends KeyedProcessFunction<Long, DetailedTransaction, DetailedAlert> {
    private static final long serialVersionUID = 1L;

    private static final double SMALL_AMOUNT = 1.00;
    private static final double LARGE_AMOUNT = 500.00;
    private static final long ONE_MINUTE = 60 * 1000;

    private transient ValueState<Boolean> flagState;
    private transient ValueState<Long> timerState;

    private transient ValueState<String> locationState;

    @Override
    public void open(Configuration parameters) {
        ValueStateDescriptor<Boolean> flagDescriptor = new ValueStateDescriptor<>(
                "flag",
                Types.BOOLEAN);
        flagState = getRuntimeContext().getState(flagDescriptor);

        ValueStateDescriptor<Long> timerDescriptor = new ValueStateDescriptor<>(
                "timer-state",
                Types.LONG);
        timerState = getRuntimeContext().getState(timerDescriptor);

        ValueStateDescriptor<String> locationDescriptor = new ValueStateDescriptor<String>(
                "location-state",
                Types.STRING
        );
        locationState = getRuntimeContext().getState(locationDescriptor);
    }

    @Override
    public void processElement(
            DetailedTransaction transaction,
            Context context,
            Collector<DetailedAlert> collector) throws Exception {

        // Get the current state for the current key
        Boolean lastTransactionWasSmall = flagState.value();
        String lastSmallTxnLocation = locationState.value();

        // Check if the flag is set
        if (lastTransactionWasSmall != null && lastSmallTxnLocation != null) {
            if (transaction.getAmount() > LARGE_AMOUNT && transaction.getZipcode().equals(lastSmallTxnLocation)) {
                // location set for last transaction and current is same along with flagged amount. Raise alert.
                // Output an alert downstream
                DetailedAlert alert = new DetailedAlert();
                alert.setAccountId(transaction.getAccountId());
                alert.setAmount(transaction.getAmount());
                alert.setZipcode(transaction.getZipcode());
                alert.setTimestamp(transaction.getTimestamp());

                collector.collect(alert);
            }
            // Clean up our state
            cleanUp(context);
        }

        if (transaction.getAmount() < SMALL_AMOUNT) {
            // set the flag to true
            flagState.update(true);
            //we only store location if a small amount was flagged and then check in the next txn.
            locationState.update(transaction.getZipcode());

            long timer = context.timerService().currentProcessingTime() + ONE_MINUTE;
            context.timerService().registerProcessingTimeTimer(timer);

            timerState.update(timer);
        }
    }

    @Override
    public void onTimer(long timestamp, OnTimerContext ctx, Collector<DetailedAlert> out) {
        // remove flag after 1 minute
        timerState.clear();
        flagState.clear();
        locationState.clear();
    }

    private void cleanUp(Context ctx) throws Exception {
        // delete timer
        Long timer = timerState.value();
        ctx.timerService().deleteProcessingTimeTimer(timer);

        // clean up all state
        timerState.clear();
        flagState.clear();
        locationState.clear();
    }
}
