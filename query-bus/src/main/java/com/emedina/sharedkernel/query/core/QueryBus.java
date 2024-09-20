package com.emedina.sharedkernel.query.core;

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
     */
    <R, Q extends Query> R query(final Q query);

}