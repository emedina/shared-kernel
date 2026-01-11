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
 * Comprehensive unit tests for {@link AndSpecification} class.
 * 
 * @author Enrique Medina Montenegro
 */
@DisplayName("AndSpecification")
class AndSpecificationTests {

    @Test
    @DisplayName("should create instance with valid specifications")
    void shouldCreateInstanceWithValidSpecifications() {
        // Given
        Specification<String> leftSpec = _ -> true;
        Specification<String> rightSpec = _ -> false;

        // When
        AndSpecification<String> andSpecification = new AndSpecification<>(leftSpec, rightSpec);

        // Then
        assertNotNull(andSpecification);
        assertInstanceOf(AndSpecification.class, andSpecification);
        assertInstanceOf(CompositeSpecification.class, andSpecification);
        assertInstanceOf(Specification.class, andSpecification);
        assertInstanceOf(FluentSpecification.class, andSpecification);
    }

    @Test
    @DisplayName("should handle null specifications in constructor")
    void shouldHandleNullSpecificationsInConstructor() {
        // Given & When
        AndSpecification<String> andSpecification = new AndSpecification<>(null, null);

        // Then
        assertNotNull(andSpecification);
    }

    @ParameterizedTest
    @DisplayName("should evaluate AND logic correctly")
    @CsvSource({
        "true, true, true",
        "true, false, false",
        "false, true, false",
        "false, false, false"
    })
    void shouldEvaluateAndLogicCorrectly(boolean leftResult, boolean rightResult, boolean expectedResult) {
        // Given
        Specification<String> leftSpec = _ -> leftResult;
        Specification<String> rightSpec = _ -> rightResult;
        AndSpecification<String> andSpecification = new AndSpecification<>(leftSpec, rightSpec);

        // When
        boolean result = andSpecification.isSatisfiedBy("test");

        // Then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("should handle null candidate")
    void shouldHandleNullCandidate() {
        // Given
        Specification<String> leftSpec = candidate -> candidate != null;
        Specification<String> rightSpec = _ -> true;
        AndSpecification<String> andSpecification = new AndSpecification<>(leftSpec, rightSpec);

        // When
        boolean result = andSpecification.isSatisfiedBy(null);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("should short-circuit when left specification is false")
    void shouldShortCircuitWhenLeftSpecificationIsFalse() {
        // Given
        Specification<String> leftSpec = _ -> false;
        Specification<String> rightSpec = _ -> {
            throw new RuntimeException("Should not be called");
        };
        AndSpecification<String> andSpecification = new AndSpecification<>(leftSpec, rightSpec);

        // When
        boolean result = andSpecification.isSatisfiedBy("test");

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("should evaluate right specification when left is true")
    void shouldEvaluateRightSpecificationWhenLeftIsTrue() {
        // Given
        boolean[] rightCalled = { false };
        Specification<String> leftSpec = _ -> true;
        Specification<String> rightSpec = _ -> {
            rightCalled[0] = true;
            return true;
        };
        AndSpecification<String> andSpecification = new AndSpecification<>(leftSpec, rightSpec);

        // When
        boolean result = andSpecification.isSatisfiedBy("test");

        // Then
        assertThat(result).isTrue();
        assertThat(rightCalled[0]).isTrue();
    }

    @Test
    @DisplayName("should work with complex specifications")
    void shouldWorkWithComplexSpecifications() {
        // Given
        Specification<String> lengthSpec = candidate -> candidate != null && candidate.length() > 3;
        Specification<String> startsWithSpec = candidate -> candidate != null && candidate.startsWith("test");
        AndSpecification<String> andSpecification = new AndSpecification<>(lengthSpec, startsWithSpec);

        // When & Then
        assertThat(andSpecification.isSatisfiedBy("testing")).isTrue(); // length > 3 AND starts with "test"
        assertThat(andSpecification.isSatisfiedBy("test")).isTrue(); // length = 4 > 3 AND starts with "test" = true
        assertThat(andSpecification.isSatisfiedBy("hello")).isFalse(); // length > 3 but doesn't start with "test"
        assertThat(andSpecification.isSatisfiedBy("hi")).isFalse(); // both conditions fail
        assertThat(andSpecification.isSatisfiedBy(null)).isFalse();
    }

    @Test
    @DisplayName("should support fluent interface operations")
    void shouldSupportFluentInterfaceOperations() {
        // Given
        Specification<String> leftSpec = _ -> true;
        Specification<String> rightSpec = _ -> true;
        AndSpecification<String> andSpecification = new AndSpecification<>(leftSpec, rightSpec);
        Specification<String> anotherSpec = _ -> false;

        // When & Then
        assertThat(andSpecification.and(anotherSpec)).isInstanceOf(AndSpecification.class);
        assertThat(andSpecification.or(anotherSpec)).isInstanceOf(OrSpecification.class);
        assertThat(andSpecification.not()).isInstanceOf(NotSpecification.class);
    }

    @Test
    @DisplayName("should handle exception in left specification")
    void shouldHandleExceptionInLeftSpecification() {
        // Given
        Specification<String> leftSpec = _ -> {
            throw new RuntimeException("Left spec error");
        };
        Specification<String> rightSpec = _ -> true;
        AndSpecification<String> andSpecification = new AndSpecification<>(leftSpec, rightSpec);

        // When & Then
        try {
            andSpecification.isSatisfiedBy("test");
            assertThat(false).as("Expected RuntimeException to be thrown").isTrue();
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Left spec error");
        }
    }

    @Test
    @DisplayName("should handle exception in right specification")
    void shouldHandleExceptionInRightSpecification() {
        // Given
        Specification<String> leftSpec = _ -> true;
        Specification<String> rightSpec = _ -> {
            throw new RuntimeException("Right spec error");
        };
        AndSpecification<String> andSpecification = new AndSpecification<>(leftSpec, rightSpec);

        // When & Then
        try {
            andSpecification.isSatisfiedBy("test");
            assertThat(false).as("Expected RuntimeException to be thrown").isTrue();
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Right spec error");
        }
    }

    @Test
    @DisplayName("should return left specification via getter")
    void shouldReturnLeftSpecificationViaGetter() {
        // Given
        Specification<String> leftSpec = _ -> true;
        Specification<String> rightSpec = _ -> false;
        AndSpecification<String> andSpecification = new AndSpecification<>(leftSpec, rightSpec);

        // When
        Specification<String> retrievedLeftSpec = andSpecification.getLeftSpecification();

        // Then
        assertNotNull(retrievedLeftSpec);
        assertThat(retrievedLeftSpec).isSameAs(leftSpec);
    }

    @Test
    @DisplayName("should return right specification via getter")
    void shouldReturnRightSpecificationViaGetter() {
        // Given
        Specification<String> leftSpec = _ -> true;
        Specification<String> rightSpec = _ -> false;
        AndSpecification<String> andSpecification = new AndSpecification<>(leftSpec, rightSpec);

        // When
        Specification<String> retrievedRightSpec = andSpecification.getRightSpecification();

        // Then
        assertNotNull(retrievedRightSpec);
        assertThat(retrievedRightSpec).isSameAs(rightSpec);
    }

}
