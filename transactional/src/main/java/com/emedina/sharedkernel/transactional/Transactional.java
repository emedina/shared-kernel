package com.emedina.sharedkernel.transactional;

import java.lang.annotation.*;

/**
 * Annotation inspired by Spring's @Transactional.
 *
 * @author Enrique Medina Montenegro
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Transactional {

    /**
     * Defines zero (0) or more transaction labels.
     * <p>Labels may be used to describe a transaction, and they can be evaluated
     * by individual transaction managers. Labels may serve a solely descriptive
     * purpose or map to pre-defined transaction manager-specific options.
     */
    String value() default "";
    String[] label() default {};

    /**
     * The transaction propagation type.
     * <p>Defaults to {@link Propagation#REQUIRED}.
     */
    Propagation propagation() default Propagation.REQUIRED;

    /**
     * The transaction isolation level.
     * <p>Defaults to {@link Isolation#DEFAULT}.
     * <p>Exclusively designed for use with {@link Propagation#REQUIRED} or
     * {@link Propagation#REQUIRES_NEW} since it only applies to newly started
     * transactions. Consider switching the "validateExistingTransactions" flag to
     * "true" on your transaction manager if you'd like isolation level declarations
     * to get rejected when participating in an existing transaction with a different
     * isolation level.
     */
    Isolation isolation() default Isolation.DEFAULT;

    /**
     * The timeout for this transaction (in seconds).
     * <p>Defaults to the default timeout of the underlying transaction system.
     * <p>Exclusively designed for use with {@link Propagation#REQUIRED} or
     * {@link Propagation#REQUIRES_NEW} since it only applies to newly started
     * transactions.
     *
     * @return the timeout in seconds
     */
    int timeout() default -1;

    /**
     * The timeout for this transaction (in seconds).
     * <p>Defaults to the default timeout of the underlying transaction system.
     * <p>Exclusively designed for use with {@link Propagation#REQUIRED} or
     * {@link Propagation#REQUIRES_NEW} since it only applies to newly started
     * transactions.
     *
     * @return the timeout in seconds as a String value, e.g. a placeholder
     */
    String timeoutString() default "";

    /**
     * A boolean flag that can be set to {@code true} if the transaction is
     * effectively read-only, allowing for corresponding optimizations at runtime.
     * <p>Defaults to {@code false}.
     * <p>This just serves as a hint for the actual transaction subsystem;
     * it will <i>not necessarily</i> cause failure of write access attempts.
     * A transaction manager which cannot interpret the read-only hint will
     * <i>not</i> throw an exception when asked for a read-only transaction
     * but rather silently ignore the hint.
     */
    boolean readOnly() default false;

    /**
     * Defines zero (0) or more exception {@linkplain Class types}, which must be
     * subclasses of {@link Throwable}, indicating which exception types must cause
     * a transaction rollback.
     * <p>By default, a transaction will be rolled back on {@link RuntimeException}
     * and {@link Error} but not on checked exceptions (business exceptions).
     * <p>This is the preferred way to construct a rollback rule (in contrast to
     * {@link #rollbackForClassName}), matching the exception type and its subclasses
     * in a type-safe manner. See the {@linkplain Transactional class-level javadocs}
     * for further details on rollback rule semantics.
     *
     * @see #rollbackForClassName
     */
    Class<? extends Throwable>[] rollbackFor() default {};
    Class<?>[] rollbackForWithEither() default {};

    /**
     * Defines zero (0) or more exception name patterns (for exceptions which must be a
     * subclass of {@link Throwable}), indicating which exception types must cause
     * a transaction rollback.
     * <p>See the {@linkplain Transactional class-level javadocs} for further details
     * on rollback rule semantics, patterns, and warnings regarding possible
     * unintentional matches.
     *
     * @see #rollbackFor
     */
    String[] rollbackForClassName() default {};
    String[] rollbackForClassNameWithEither() default {};

    /**
     * Defines zero (0) or more exception {@link Class types}, which must be
     * subclasses of {@link Throwable}, indicating which exception types must
     * <b>not</b> cause a transaction rollback.
     * <p>This is the preferred way to construct a rollback rule (in contrast to
     * {@link #noRollbackForClassName}), matching the exception type and its subclasses
     * in a type-safe manner. See the {@linkplain Transactional class-level javadocs}
     * for further details on rollback rule semantics.
     *
     * @see #noRollbackForClassName
     */
    Class<? extends Throwable>[] noRollbackFor() default {};
    Class<?>[] noRollbackForWithEither() default {};

    /**
     * Defines zero (0) or more exception name patterns (for exceptions which must be a
     * subclass of {@link Throwable}) indicating which exception types must <b>not</b>
     * cause a transaction rollback.
     * <p>See the {@linkplain Transactional class-level javadocs} for further details
     * on rollback rule semantics, patterns, and warnings regarding possible
     * unintentional matches.
     *
     * @see #noRollbackFor
     */
    String[] noRollbackForClassName() default {};
    String[] noRollbackForClassNameWithEither() default {};
}
