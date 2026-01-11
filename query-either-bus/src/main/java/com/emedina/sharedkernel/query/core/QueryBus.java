package com.emedina.sharedkernel.query.core;

import io.vavr.control.Either;
import com.emedina.sharedkernel.query.Query;

/**
 * A query bus is able to execute queries, by passing the query object to its appropriate handler, hence
 * decoupling the requester from the executor using best practices.
 *
 * @author Enrique Medina Montenegro
 */
public interface QueryBus {

    /**
     * Looks up the handler and passes the query to it.
     *
     * @param query query object
     * @param <R>   type of return value
     * @param <Q>   type of query
     * @return either success with result, or an error if anything goes wrong
     */
    <R, Q extends Query> Either<?, R> query(final Q query);

}
