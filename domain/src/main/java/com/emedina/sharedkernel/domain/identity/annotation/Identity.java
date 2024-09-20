package com.emedina.sharedkernel.domain.identity.annotation;

import com.emedina.sharedkernel.domain.model.type.AggregateRoot;
import com.emedina.sharedkernel.domain.model.type.Entity;

import java.lang.annotation.*;

/**
 * Declares a field (or a getter) of a class to constitute the identity of the corresponding class. Primarily used in
 * {@link AggregateRoot} and {@link Entity} types.
 *
 * @author Enrique Medina Montenegro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Documented
public @interface Identity {
}