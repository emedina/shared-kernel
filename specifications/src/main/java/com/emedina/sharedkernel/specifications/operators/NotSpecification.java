package com.emedina.sharedkernel.specifications.operators;

import com.emedina.sharedkernel.specifications.CompositeSpecification;
import com.emedina.sharedkernel.specifications.Specification;

/**
 * Implementation of the NOT operator for specifications.
 *
 * @param <T> the type
 * @author Enrique Medina Montenegro
 */
public class NotSpecification<T> extends CompositeSpecification<T> {

    private Specification<T> wrapped;

    public NotSpecification(Specification<T> specification) {
        this.setWrapped(specification);
    }

    public Specification<T> getWrapped() {
        return this.wrapped;
    }

    public void setWrapped(Specification<T> wrappedSpecification) {
        this.wrapped = wrappedSpecification;
    }

    @Override
    public boolean isSatisfiedBy(T candidate) {
        return !this.wrapped.isSatisfiedBy(candidate);
    }

}
