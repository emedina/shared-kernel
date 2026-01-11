package com.emedina.sharedkernel.query.core;

import com.emedina.sharedkernel.query.Query;

import io.vavr.control.Either;

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
     * @return either success with result, or an error if anything goes wrong
     */
    Either<?, R> handle(final Q query);

}
