package com.emedina.sharedkernel.domain.identity.type;

/**
 * An identifiable type, i.e. anything that exposes an {@link Identifier}.
 *
 * @author Enrique Medina Montenegro
 */
public interface Identifiable<ID extends Identifier> {

    /**
     * Returns the identifier.
     *
     * @return
     */
    ID id();

}
