package com.emedina.sharedkernel.specifications.operators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.emedina.sharedkernel.specifications.CompositeSpecification;
import com.emedina.sharedkernel.specifications.FluentSpecification;
import com.emedina.sharedkernel.specifications.Specification;

/**
 * Comprehensive unit tests for {@link NotSpecification} class.
 * 
 * @author Enrique Medina Montenegro
 */
@DisplayName("NotSpecification")
class NotSpecificationTests {

    @Test
    @DisplayName("should create instance with valid specification")
    void shouldCreateInstanceWithValidSpecification() {
        // Given
        Specification<String> wrappedSpec = _ -> true;

        // When
        NotSpecification<String> notSpecification = new NotSpecification<>(wrappedSpec);

        // Then
        assertNotNull(notSpecification);
        assertInstanceOf(NotSpecification.class, notSpecification);
        assertInstanceOf(CompositeSpecification.class, notSpecification);
        assertInstanceOf(Specification.class, notSpecification);
        assertInstanceOf(FluentSpecification.class, notSpecification);
    }

    @Test
    @DisplayName("should handle null specification in constructor")
    void shouldHandleNullSpecificationInConstructor() {
        // Given & When
        NotSpecification<String> notSpecification = new NotSpecification<>(null);

        // Then
        assertNotNull(notSpecification);
    }

    @ParameterizedTest
    @DisplayName("should evaluate NOT logic correctly")
    @CsvSource({
        "true, false",
        "false, true"
    })
    void shouldEvaluateNotLogicCorrectly(boolean wrappedResult, boolean expectedResult) {
        // Given
        Specification<String> wrappedSpec = _ -> wrappedResult;
        NotSpecification<String> notSpecification = new NotSpecification<>(wrappedSpec);

        // When
        boolean result = notSpecification.isSatisfiedBy("test");

        // Then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("should handle null candidate")
    void shouldHandleNullCandidate() {
        // Given
        Specification<String> wrappedSpec = candidate -> candidate != null;
        NotSpecification<String> notSpecification = new NotSpecification<>(wrappedSpec);

        // When
        boolean result = notSpecification.isSatisfiedBy(null);

        // Then
        assertThat(result).isTrue(); // Because wrapped spec returns false for null
    }

    @Test
    @DisplayName("should work with complex specifications")
    void shouldWorkWithComplexSpecifications() {
        // Given
        Specification<String> lengthSpec = candidate -> candidate != null && candidate.length() > 3;
        NotSpecification<String> notSpecification = new NotSpecification<>(lengthSpec);

        // When & Then
        assertThat(notSpecification.isSatisfiedBy("test")).isFalse(); // length = 4 > 3, so NOT is false
        assertThat(notSpecification.isSatisfiedBy("hi")).isTrue(); // length = 2 < 3, so NOT is true
        assertThat(notSpecification.isSatisfiedBy(null)).isTrue(); // null fails the wrapped spec, so NOT is true
    }

    @Test
    @DisplayName("should support fluent interface operations")
    void shouldSupportFluentInterfaceOperations() {
        // Given
        Specification<String> wrappedSpec = _ -> true;
        NotSpecification<String> notSpecification = new NotSpecification<>(wrappedSpec);
        Specification<String> anotherSpec = _ -> false;

        // When & Then
        assertThat(notSpecification.and(anotherSpec)).isInstanceOf(AndSpecification.class);
        assertThat(notSpecification.or(anotherSpec)).isInstanceOf(OrSpecification.class);
        assertThat(notSpecification.not()).isInstanceOf(NotSpecification.class);
    }

    @Test
    @DisplayName("should handle exception in wrapped specification")
    void shouldHandleExceptionInWrappedSpecification() {
        // Given
        Specification<String> wrappedSpec = _ -> {
            throw new RuntimeException("Wrapped spec error");
        };
        NotSpecification<String> notSpecification = new NotSpecification<>(wrappedSpec);

        // When & Then
        try {
            notSpecification.isSatisfiedBy("test");
            assertThat(false).as("Expected RuntimeException to be thrown").isTrue();
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Wrapped spec error");
        }
    }

    @Test
    @DisplayName("should get and set wrapped specification")
    void shouldGetAndSetWrappedSpecification() {
        // Given
        Specification<String> wrappedSpec = _ -> true;
        NotSpecification<String> notSpecification = new NotSpecification<>(wrappedSpec);

        // When & Then
        assertThat(notSpecification.getWrapped()).isSameAs(wrappedSpec);

        // When
        Specification<String> newWrappedSpec = _ -> false;
        notSpecification.setWrapped(newWrappedSpec);

        // Then
        assertThat(notSpecification.getWrapped()).isSameAs(newWrappedSpec);
    }

    @Test
    @DisplayName("should double negate to original result")
    void shouldDoubleNegateToOriginalResult() {
        // Given
        Specification<String> originalSpec = _ -> true;
        NotSpecification<String> notSpec = new NotSpecification<>(originalSpec);
        NotSpecification<String> doubleNotSpec = new NotSpecification<>(notSpec);

        // When & Then
        assertThat(originalSpec.isSatisfiedBy("test")).isTrue();
        assertThat(notSpec.isSatisfiedBy("test")).isFalse();
        assertThat(doubleNotSpec.isSatisfiedBy("test")).isTrue();
    }

    @Test
    @DisplayName("should work with chained specifications")
    void shouldWorkWithChainedSpecifications() {
        // Given
        Specification<String> lengthSpec = candidate -> candidate != null && candidate.length() > 3;
        Specification<String> startsWithSpec = candidate -> candidate != null && candidate.startsWith("test");

        // When
        NotSpecification<String> notLengthSpec = new NotSpecification<>(lengthSpec);
        Specification<String> combinedSpec = notLengthSpec.and(startsWithSpec);

        // Then
        // Should be true only for strings that are NOT longer than 3 chars AND start with "test"
        // This is impossible since "test" is already 4 chars
        assertThat(combinedSpec.isSatisfiedBy("test")).isFalse();
        assertThat(combinedSpec.isSatisfiedBy("te")).isFalse(); // Not longer than 3, but doesn't start with "test"
        assertThat(combinedSpec.isSatisfiedBy("testing")).isFalse(); // Longer than 3, starts with "test"
        assertThat(combinedSpec.isSatisfiedBy(null)).isFalse(); // Null fails both specs
    }
}
