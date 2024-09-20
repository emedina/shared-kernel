package com.emedina.sharedkernel.domain.service.annotation;

import java.lang.annotation.*;

/**
 * Identifies a domain {@link DomainService}. A domain service is a significant process or transformation in the domain
 * that is not a natural responsibility of an entity or value object. Give the service a name, which also
 * becomes part of the ubiquitous language.
 *
 * @author Enrique Medina Montenegro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface DomainService {
}