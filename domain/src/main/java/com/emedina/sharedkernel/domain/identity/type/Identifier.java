package com.emedina.sharedkernel.domain.identity.type;

/**
 * Marker interface for identifiers. Exists primarily to easily identify types that are supposed to be identifiers
 * within the code base and let the compiler verify the correctness of declared relationships.
 *
 * @author Enrique Medina Montenegro
 * @see Identifiable
 */
public interface Identifier<T> {
}
