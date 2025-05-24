package com.emedina.sharedkernel.specifications;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.emedina.sharedkernel.specifications.operators.AndNotSpecification;
import com.emedina.sharedkernel.specifications.operators.AndSpecification;
import com.emedina.sharedkernel.specifications.operators.ConjunctionSpecification;
import com.emedina.sharedkernel.specifications.operators.NotSpecification;
import com.emedina.sharedkernel.specifications.operators.OrNotSpecification;
import com.emedina.sharedkernel.specifications.operators.OrSpecification;

/**
 * Unit tests for {@link CompositeSpecification} class.
 * 
 * @author Enrique Medina Montenegro
 */
@DisplayName("CompositeSpecification")
class CompositeSpecificationTest {

    private TestCompositeSpecification specification;

    @BeforeEach
    void setUp() {
        specification = new TestCompositeSpecification(true);
    }

    @Test
    @DisplayName("should create AndSpecification when using and() method")
    void shouldCreateAndSpecificationWhenUsingAndMethod() {
        // Given
        Specification<String> otherSpec = new TestCompositeSpecification(false);

        // When
        Specification<String> result = specification.and(otherSpec);

        // Then
        assertThat(result).isInstanceOf(AndSpecification.class);
    }

    @Test
    @DisplayName("should create AndNotSpecification when using andNot() method")
    void shouldCreateAndNotSpecificationWhenUsingAndNotMethod() {
        // Given
        Specification<String> otherSpec = new TestCompositeSpecification(false);

        // When
        Specification<String> result = specification.andNot(otherSpec);

        // Then
        assertThat(result).isInstanceOf(AndNotSpecification.class);
    }

    @Test
    @DisplayName("should create OrSpecification when using or() method")
    void shouldCreateOrSpecificationWhenUsingOrMethod() {
        // Given
        Specification<String> otherSpec = new TestCompositeSpecification(false);

        // When
        Specification<String> result = specification.or(otherSpec);

        // Then
        assertThat(result).isInstanceOf(OrSpecification.class);
    }

    @Test
    @DisplayName("should create OrNotSpecification when using orNot() method")
    void shouldCreateOrNotSpecificationWhenUsingOrNotMethod() {
        // Given
        Specification<String> otherSpec = new TestCompositeSpecification(false);

        // When
        Specification<String> result = specification.orNot(otherSpec);

        // Then
        assertThat(result).isInstanceOf(OrNotSpecification.class);
    }

    @Test
    @DisplayName("should create NotSpecification when using not() method")
    void shouldCreateNotSpecificationWhenUsingNotMethod() {
        // When
        Specification<String> result = specification.not();

        // Then
        assertThat(result).isInstanceOf(NotSpecification.class);
    }

    @Test
    @DisplayName("should create ConjunctionSpecification when using conjunction() method")
    void shouldCreateConjunctionSpecificationWhenUsingConjunctionMethod() {
        // Given
        Specification<String> otherSpec = new TestCompositeSpecification(false);

        // When
        Specification<String> result = specification.conjunction(otherSpec);

        // Then
        assertThat(result).isInstanceOf(ConjunctionSpecification.class);
    }

    @Test
    @DisplayName("should support method chaining")
    void shouldSupportMethodChaining() {
        // Given
        TestCompositeSpecification spec1 = new TestCompositeSpecification(true);
        TestCompositeSpecification spec2 = new TestCompositeSpecification(false);
        TestCompositeSpecification spec3 = new TestCompositeSpecification(true);

        // When
        Specification<String> andResult = specification.and(spec1);
        Specification<String> orResult = specification.or(spec2);
        Specification<String> andNotResult = specification.andNot(spec3);

        // Then
        assertThat(andResult).isNotNull();
        assertThat(andResult).isInstanceOf(AndSpecification.class);
        assertThat(orResult).isNotNull();
        assertThat(orResult).isInstanceOf(OrSpecification.class);
        assertThat(andNotResult).isNotNull();
        assertThat(andNotResult).isInstanceOf(AndNotSpecification.class);
    }

    @Test
    @DisplayName("should maintain fluent interface contract")
    void shouldMaintainFluentInterfaceContract() {
        // Given
        Specification<String> otherSpec = new TestCompositeSpecification(false);

        // When & Then
        assertThat(specification.and(otherSpec)).isInstanceOf(FluentSpecification.class);
        assertThat(specification.or(otherSpec)).isInstanceOf(FluentSpecification.class);
        assertThat(specification.andNot(otherSpec)).isInstanceOf(FluentSpecification.class);
        assertThat(specification.orNot(otherSpec)).isInstanceOf(FluentSpecification.class);
        assertThat(specification.not()).isInstanceOf(FluentSpecification.class);
        assertThat(specification.conjunction(otherSpec)).isInstanceOf(FluentSpecification.class);
    }

    @Test
    @DisplayName("should work with complex specification chains")
    void shouldWorkWithComplexSpecificationChains() {
        // Given
        TestCompositeSpecification alwaysTrue = new TestCompositeSpecification(true);
        TestCompositeSpecification alwaysFalse = new TestCompositeSpecification(false);

        // When
        Specification<String> step1 = alwaysTrue.and(alwaysFalse.not());
        Specification<String> step2 = alwaysTrue.or(alwaysFalse);
        Specification<String> step3 = alwaysTrue.andNot(alwaysFalse);

        // Then
        assertThat(step1).isNotNull();
        assertThat(step1.isSatisfiedBy("test")).isTrue(); // true AND (NOT false) = true AND true = true

        assertThat(step2).isNotNull();
        assertThat(step2.isSatisfiedBy("test")).isTrue(); // true OR false = true

        assertThat(step3).isNotNull();
        assertThat(step3.isSatisfiedBy("test")).isTrue(); // true AND NOT false = true AND true = true
    }

    @Test
    @DisplayName("should handle null specifications gracefully in operations")
    void shouldHandleNullSpecificationsGracefullyInOperations() {
        // Given
        Specification<String> nullSpec = null;

        // When & Then - These should not throw exceptions but create valid specifications
        assertThat(specification.and(nullSpec)).isInstanceOf(AndSpecification.class);
        assertThat(specification.or(nullSpec)).isInstanceOf(OrSpecification.class);
        assertThat(specification.andNot(nullSpec)).isInstanceOf(AndNotSpecification.class);
        assertThat(specification.orNot(nullSpec)).isInstanceOf(OrNotSpecification.class);
        assertThat(specification.conjunction(nullSpec)).isInstanceOf(ConjunctionSpecification.class);
    }

    // Test implementation of CompositeSpecification
    private static class TestCompositeSpecification extends CompositeSpecification<String> {
        private final boolean result;

        public TestCompositeSpecification(boolean result) {
            this.result = result;
        }

        @Override
        public boolean isSatisfiedBy(String candidate) {
            return result;
        }
    }
}
