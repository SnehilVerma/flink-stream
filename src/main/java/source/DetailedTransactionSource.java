package source;

import entity.DetailedTransaction;
import org.apache.flink.streaming.api.functions.source.FromIteratorFunction;

import java.io.Serializable;
import java.util.Iterator;

public class DetailedTransactionSource extends FromIteratorFunction<DetailedTransaction> {
    private static final long serialVersionUID = 1L;

    public DetailedTransactionSource() {
        super(new DetailedTransactionSource.RateLimitedIterator<>(DetailedTransactionIterator.unbounded()));
    }

    private static class RateLimitedIterator<T> implements Iterator<T>, Serializable {
        private static final long serialVersionUID = 1L;
        private final Iterator<T> inner;

        private RateLimitedIterator(Iterator<T> inner) {
            this.inner = inner;
        }

        public boolean hasNext() {
            return this.inner.hasNext();
        }

        public T next() {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException var2) {
                throw new RuntimeException(var2);
            }

            return this.inner.next();
        }
    }
}
