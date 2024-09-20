package com.emedina.sharedkernel.specifications;

/**
 * Base interface for type-safe Specifications.
 *
 * @param <T> the type
 * @author Enrique Medina Montenegro
 * @see <a href="https://martinfowler.com/apsupp/spec.pdf">Specifications</a> by Eric Evans & Martin Fowler</p>
 */
public interface Specification<T> {

    boolean isSatisfiedBy(final T candidate);

}