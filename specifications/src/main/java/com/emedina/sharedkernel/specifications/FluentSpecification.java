package com.emedina.sharedkernel.specifications;

/**
 * Defines fluent operators to allow different combinations of specifications.
 *
 * @param <T> the type
 * @author Enrique Medina Montenegro
 */
public interface FluentSpecification<T> extends Specification<T> {

    Specification<T> and(final Specification<T> condition);

    Specification<T> andNot(final Specification<T> condition);

    Specification<T> or(final Specification<T> condition);

    Specification<T> orNot(final Specification<T> condition);

    Specification<T> not();

    Specification<T> conjunction(final Specification<T> condition);

}