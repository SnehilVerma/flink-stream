package sink;

import entity.DetailedAlert;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.walkthrough.common.sink.AlertSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DetailedAlertSink implements SinkFunction<DetailedAlert> {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(AlertSink.class);

    public DetailedAlertSink() {
    }

    public void invoke(DetailedAlert value, SinkFunction.Context context) {
        LOG.info(value.toString());
    }
}
