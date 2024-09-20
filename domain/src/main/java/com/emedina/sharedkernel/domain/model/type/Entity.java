package com.emedina.sharedkernel.domain.model.type;

import com.emedina.sharedkernel.domain.identity.type.Identifiable;
import com.emedina.sharedkernel.domain.identity.type.Identifier;

import java.io.Serializable;

/**
 * An entity, as explained in the DDD book.
 *
 * @author Enrique Medina Montenegro
 */
public interface Entity<E extends AggregateRoot<E, ?>, ID extends Identifier> extends Identifiable<ID>, Serializable {

    /**
     * Entities compare by identity, not by attributes.
     *
     * @param other the other entity
     * @return <code>true</code> if the identities are the same, regardless of other attributes
     */
    boolean sameIdentityAs(final E other);

}