package com.emedina.sharedkernel.application.annotation;

import java.lang.annotation.*;

/**
 * Identifies a Use Case as an Input Port interface.
 *
 * @author Enrique Medina Montenegro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface UseCase {
}
