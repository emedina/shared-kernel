package com.emedina.sharedkernel.domain.model.type;

import com.emedina.sharedkernel.domain.identity.type.Identifier;

/**
 * Identifies an aggregate root, i.e. the root entity of an aggregate. An aggregate forms a cluster of consistent rules
 * usually formed around a set of entities by defining invariants based on the properties of the aggregate that have to
 * be met before and after operations on it.
 * <p>Aggregates usually refer to other aggregates by their identifier.</p>
 * <p>References to aggregate internals should be avoided and at least not considered strongly consistent (i.e. a reference
 * held could possibly have been gone or become invalid at any point in time). They also act as scope of consistency,
 * i.e. changes on a single aggregate are expected to be strongly consistent while changes across multiple ones should
 * only expect eventual consistency.</p>
 *
 * @author Enrique Medina Montenegro
 */
public interface AggregateRoot<A extends AggregateRoot<A, ID>, ID extends Identifier> extends Entity<A, ID> {
}