package com.emedina.sharedkernel.domain.repository.type;

import com.emedina.sharedkernel.domain.identity.type.Identifier;
import com.emedina.sharedkernel.domain.model.type.AggregateRoot;

/**
 * Identifies a {@link Repository}. Repositories simulate a collection of aggregates to which aggregate instances can be
 * added and removed. They usually also expose API to select a subset of aggregates matching certain criteria. Access to
 * projections of an aggregate might be provided as well but also via a dedicated separate abstraction.
 * <p>
 * Implementations use a dedicated persistence mechanism appropriate to the data structure and query requirements at
 * hand. However, they should make sure that no persistence mechanism specific APIs leak into client code.
 * </p>
 *
 * @author Enrique Medina Montenegro
 * @see AggregateRoot
 */
public interface Repository<R extends AggregateRoot<R, ID>, ID extends Identifier> {
}