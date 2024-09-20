package com.emedina.sharedkernel.domain.factory.annotation;

import com.emedina.sharedkernel.domain.model.type.AggregateRoot;

import java.lang.annotation.*;

/**
 * Identifies a {@link Factory}. Factories encapsulate the responsibility of creating complex objects in general and
 * Aggregates in particular. Objects returned by the factory methods are guaranteed to be in valid state.
 *
 * @author Enrique Medina Montenegro
 * @see AggregateRoot
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface Factory {
}