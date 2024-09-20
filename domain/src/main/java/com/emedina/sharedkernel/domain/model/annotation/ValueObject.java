package com.emedina.sharedkernel.domain.model.annotation;

import java.lang.annotation.*;

/**
 * Identifies a {@link com.emedina.sharedkernel.domain.model.type.ValueObject}. Domain concepts that are modeled as
 * value objects have no conceptual identity or lifecycle.
 * Implementations should be immutable, operations on it are side effect free.
 *
 * @author Enrique Medina Montenegro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ValueObject {
}