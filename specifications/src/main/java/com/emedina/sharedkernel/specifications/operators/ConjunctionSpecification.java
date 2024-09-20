package com.emedina.sharedkernel.specifications.operators;

import com.emedina.sharedkernel.specifications.CompositeSpecification;
import com.emedina.sharedkernel.specifications.Specification;

/**
 * Implementation of the CONJUNCTION operator for specifications.
 *
 * @param <T> the type
 * @author Enrique Medina Montenegro
 */
public class ConjunctionSpecification<T> extends CompositeSpecification<T> {

    private final Specification<T>[] specifications;

    @SafeVarargs
    public ConjunctionSpecification(final Specification<T>... specifications) {
        this.specifications = specifications;
    }

    public boolean isSatisfiedBy(final T candidate) {
        for (Specification<T> specification : specifications) {
            if (!specification.isSatisfiedBy(candidate)) {
                return false;
            }
        }

        return true;
    }

}
