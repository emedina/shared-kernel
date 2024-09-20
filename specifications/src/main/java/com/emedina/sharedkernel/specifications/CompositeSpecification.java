package com.emedina.sharedkernel.specifications;

import com.emedina.sharedkernel.specifications.operators.AndNotSpecification;
import com.emedina.sharedkernel.specifications.operators.AndSpecification;
import com.emedina.sharedkernel.specifications.operators.ConjunctionSpecification;
import com.emedina.sharedkernel.specifications.operators.NotSpecification;
import com.emedina.sharedkernel.specifications.operators.OrNotSpecification;
import com.emedina.sharedkernel.specifications.operators.OrSpecification;

/**
 * Base class to support the composition of specifications using fluent operators.
 *
 * @param <T> the type
 * @author Enrique Medina Montenegro
 */
public abstract class CompositeSpecification<T> implements FluentSpecification<T> {

    @Override
    public Specification<T> and(final Specification<T> condition) {
        return new AndSpecification<>(this, condition);
    }

    @Override
    public Specification<T> andNot(final Specification<T> condition) {
        return new AndNotSpecification<>(this, condition);
    }

    @Override
    public Specification<T> or(final Specification<T> condition) {
        return new OrSpecification<>(this, condition);
    }

    @Override
    public Specification<T> orNot(final Specification<T> condition) {
        return new OrNotSpecification<>(this, condition);
    }

    @Override
    public Specification<T> not() {
        return new NotSpecification<>(this);
    }

    @Override
    public Specification<T> conjunction(final Specification<T> condition) {
        return new ConjunctionSpecification<>(this, condition);
    }

}
