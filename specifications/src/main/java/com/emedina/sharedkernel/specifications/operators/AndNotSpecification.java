package com.emedina.sharedkernel.specifications.operators;

import com.emedina.sharedkernel.specifications.CompositeSpecification;
import com.emedina.sharedkernel.specifications.Specification;

/**
 * Implementation of the AND NOT operator for specifications.
 *
 * @param <T> the type
 * @author Enrique Medina Montenegro
 */
public class AndNotSpecification<T> extends CompositeSpecification<T> {

    private Specification<T> leftSpecification;
    private Specification<T> rightSpecification;

    public AndNotSpecification(final Specification<T> left, final Specification<T> right) {
        this.setLeftSpecification(left);
        this.setRightSpecification(right);
    }

    public Specification<T> getLeftSpecification() {
        return this.leftSpecification;
    }

    public void setLeftSpecification(final Specification<T> leftSpecification) {
        this.leftSpecification = leftSpecification;
    }

    public Specification<T> getRightSpecification() {
        return this.rightSpecification;
    }

    public void setRightSpecification(final Specification<T> rightSpecification) {
        this.rightSpecification = rightSpecification;
    }

    @Override
    public boolean isSatisfiedBy(final T candidate) {
        return this.leftSpecification.isSatisfiedBy(candidate) && !this.rightSpecification.isSatisfiedBy(candidate);
    }

}
