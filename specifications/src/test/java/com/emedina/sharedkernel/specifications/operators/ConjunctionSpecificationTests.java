package com.emedina.sharedkernel.specifications.operators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.emedina.sharedkernel.specifications.CompositeSpecification;
import com.emedina.sharedkernel.specifications.FluentSpecification;
import com.emedina.sharedkernel.specifications.Specification;

/**
 * Comprehensive unit tests for {@link ConjunctionSpecification} class.
 * 
 * @author Enrique Medina Montenegro
 */
@DisplayName("ConjunctionSpecification")
class ConjunctionSpecificationTests {

    @Test
    @DisplayName("should create instance with valid specifications")
    void shouldCreateInstanceWithValidSpecifications() {
        // Given
        Specification<String> spec1 = _ -> true;
        Specification<String> spec2 = _ -> false;

        // When
        ConjunctionSpecification<String> conjunctionSpecification = new ConjunctionSpecification<>(spec1, spec2);

        // Then
        assertNotNull(conjunctionSpecification);
        assertInstanceOf(ConjunctionSpecification.class, conjunctionSpecification);
        assertInstanceOf(CompositeSpecification.class, conjunctionSpecification);
        assertInstanceOf(Specification.class, conjunctionSpecification);
        assertInstanceOf(FluentSpecification.class, conjunctionSpecification);
    }

    @Test
    @DisplayName("should handle empty specifications array")
    void shouldHandleEmptySpecificationsArray() {
        // Given & When
        ConjunctionSpecification<String> conjunctionSpecification = new ConjunctionSpecification<>();

        // Then
        assertNotNull(conjunctionSpecification);
        // Empty conjunction should be satisfied (logical AND with no operands is true)
        assertThat(conjunctionSpecification.isSatisfiedBy("test")).isTrue();
    }

    @Test
    @DisplayName("should handle null specifications")
    void shouldHandleNullSpecifications() {
        // Given & When
        ConjunctionSpecification<String> conjunctionSpecification = new ConjunctionSpecification<>(null, null);

        // Then
        assertNotNull(conjunctionSpecification);
    }

    @Test
    @DisplayName("should evaluate conjunction logic correctly with all true")
    void shouldEvaluateConjunctionLogicCorrectlyWithAllTrue() {
        // Given
        Specification<String> spec1 = _ -> true;
        Specification<String> spec2 = _ -> true;
        Specification<String> spec3 = _ -> true;
        ConjunctionSpecification<String> conjunctionSpecification = new ConjunctionSpecification<>(spec1, spec2, spec3);

        // When
        boolean result = conjunctionSpecification.isSatisfiedBy("test");

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("should evaluate conjunction logic correctly with one false")
    void shouldEvaluateConjunctionLogicCorrectlyWithOneFalse() {
        // Given
        Specification<String> spec1 = _ -> true;
        Specification<String> spec2 = _ -> false;
        Specification<String> spec3 = _ -> true;
        ConjunctionSpecification<String> conjunctionSpecification = new ConjunctionSpecification<>(spec1, spec2, spec3);

        // When
        boolean result = conjunctionSpecification.isSatisfiedBy("test");

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("should evaluate conjunction logic correctly with all false")
    void shouldEvaluateConjunctionLogicCorrectlyWithAllFalse() {
        // Given
        Specification<String> spec1 = _ -> false;
        Specification<String> spec2 = _ -> false;
        Specification<String> spec3 = _ -> false;
        ConjunctionSpecification<String> conjunctionSpecification = new ConjunctionSpecification<>(spec1, spec2, spec3);

        // When
        boolean result = conjunctionSpecification.isSatisfiedBy("test");

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("should short-circuit evaluation when encountering false")
    void shouldShortCircuitEvaluationWhenEncounteringFalse() {
        // Given
        boolean[] spec1Called = { false };
        boolean[] spec2Called = { false };
        boolean[] spec3Called = { false };

        Specification<String> spec1 = _ -> {
            spec1Called[0] = true;
            return true;
        };
        Specification<String> spec2 = _ -> {
            spec2Called[0] = true;
            return false;
        };
        Specification<String> spec3 = _ -> {
            spec3Called[0] = true;
            throw new RuntimeException("Should not be called");
        };

        ConjunctionSpecification<String> conjunctionSpecification = new ConjunctionSpecification<>(spec1, spec2, spec3);

        // When
        boolean result = conjunctionSpecification.isSatisfiedBy("test");

        // Then
        assertThat(result).isFalse();
        assertThat(spec1Called[0]).isTrue();
        assertThat(spec2Called[0]).isTrue();
        assertThat(spec3Called[0]).isFalse();
    }

    @Test
    @DisplayName("should handle null candidate")
    void shouldHandleNullCandidate() {
        // Given
        Specification<String> spec1 = candidate -> candidate != null;
        Specification<String> spec2 = _ -> true;
        ConjunctionSpecification<String> conjunctionSpecification = new ConjunctionSpecification<>(spec1, spec2);

        // When
        boolean result = conjunctionSpecification.isSatisfiedBy(null);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("should work with complex specifications")
    void shouldWorkWithComplexSpecifications() {
        // Given
        Specification<String> lengthSpec = candidate -> candidate != null && candidate.length() > 3;
        Specification<String> startsWithSpec = candidate -> candidate != null && candidate.startsWith("test");
        Specification<String> endsWithSpec = candidate -> candidate != null && candidate.endsWith("ing");
        ConjunctionSpecification<String> conjunctionSpecification = new ConjunctionSpecification<>(
            lengthSpec, startsWithSpec, endsWithSpec);

        // When & Then
        assertThat(conjunctionSpecification.isSatisfiedBy("testing")).isTrue(); // Satisfies all conditions
        assertThat(conjunctionSpecification.isSatisfiedBy("test")).isFalse(); // Doesn't end with "ing"
        assertThat(conjunctionSpecification.isSatisfiedBy("running")).isFalse(); // Doesn't start with "test"
        assertThat(conjunctionSpecification.isSatisfiedBy("hi")).isFalse(); // Too short
        assertThat(conjunctionSpecification.isSatisfiedBy(null)).isFalse(); // Null fails all specs
    }

    @Test
    @DisplayName("should support fluent interface operations")
    void shouldSupportFluentInterfaceOperations() {
        // Given
        Specification<String> spec1 = _ -> true;
        Specification<String> spec2 = _ -> true;
        ConjunctionSpecification<String> conjunctionSpecification = new ConjunctionSpecification<>(spec1, spec2);
        Specification<String> anotherSpec = _ -> false;

        // When & Then
        assertThat(conjunctionSpecification.and(anotherSpec)).isInstanceOf(AndSpecification.class);
        assertThat(conjunctionSpecification.or(anotherSpec)).isInstanceOf(OrSpecification.class);
        assertThat(conjunctionSpecification.not()).isInstanceOf(NotSpecification.class);
    }

    @Test
    @DisplayName("should handle exception in specification")
    void shouldHandleExceptionInSpecification() {
        // Given
        Specification<String> spec1 = _ -> true;
        Specification<String> spec2 = _ -> {
            throw new RuntimeException("Spec error");
        };
        ConjunctionSpecification<String> conjunctionSpecification = new ConjunctionSpecification<>(spec1, spec2);

        // When & Then
        try {
            conjunctionSpecification.isSatisfiedBy("test");
            assertThat(false).as("Expected RuntimeException to be thrown").isTrue();
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Spec error");
        }
    }

    @Test
    @DisplayName("should work with single specification")
    void shouldWorkWithSingleSpecification() {
        // Given
        Specification<String> spec = candidate -> candidate != null && candidate.length() > 3;
        ConjunctionSpecification<String> conjunctionSpecification = new ConjunctionSpecification<>(spec);

        // When & Then
        assertThat(conjunctionSpecification.isSatisfiedBy("test")).isTrue();
        assertThat(conjunctionSpecification.isSatisfiedBy("hi")).isFalse();
        assertThat(conjunctionSpecification.isSatisfiedBy(null)).isFalse();
    }
}
