package com.emedina.sharedkernel.application.annotation;

import java.lang.annotation.*;

/**
 * Identifies an Application Service.
 *
 * @author Enrique Medina Montenegro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ApplicationService {
}
