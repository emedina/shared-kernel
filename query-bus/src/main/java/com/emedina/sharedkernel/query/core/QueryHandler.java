package com.emedina.sharedkernel.query.core;

import com.emedina.sharedkernel.query.Query;

/**
 * A handler for a {@link Query}.
 *
 * @param <R> type of return value
 * @param <Q> type of query
 * @author Enrique Medina Montenegro
 * @see <a href="https://martinfowler.com/bliki/CommandQuerySeparation.html">CQS - Command-Query Separation</a>
 */
public interface QueryHandler<R, Q extends Query> {

    /**
     * Handles the query.
     *
     * @param query query to handle
     * @return an optional return value as specified in {@link Query}
     */
    <R> R handle(final Q query);

}