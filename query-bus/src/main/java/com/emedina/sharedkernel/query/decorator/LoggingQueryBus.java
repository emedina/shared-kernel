package com.emedina.sharedkernel.query.decorator;

import com.emedina.sharedkernel.query.Query;
import com.emedina.sharedkernel.query.core.QueryBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Query bus decorator which logs every execution of a query with its elapsed time.
 *
 * @author Enrique Medina Montenegro
 */
public class LoggingQueryBus implements QueryBus {

    static final Logger LOG = LoggerFactory.getLogger(LoggingQueryBus.class);

    private QueryBus decorated;

    /**
     * Creates a new query bus that logs timing, and delegates the execution to the given query bus.
     *
     * @param decorated query bus to decorate
     */
    public LoggingQueryBus(final QueryBus decorated) {
        this.decorated = decorated;
    }

    @Override
    public <R, Q extends Query> R query(final Q query) {
        LOG.info("started execution of query {}", query.getClass().getSimpleName());

        Timer timer = new Timer();
        try {
            return this.decorated.query(query);
        } finally {
            LOG.info("finished execution of query {} in {}", query.getClass().getSimpleName(), timer);
        }
    }

}
