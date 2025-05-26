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
 * Comprehensive unit tests for {@link OrSpecification} class.
 * 
 * @author Enrique Medina Montenegro
 */
@DisplayName("OrSpecification")
class OrSpecificationTests {

    @Test
    @DisplayName("should create instance with valid specifications")
    void shouldCreateInstanceWithValidSpecifications() {
        // Given
        Specification<String> leftSpec = _ -> true;
        Specification<String> rightSpec = _ -> false;

        // When
        OrSpecification<String> orSpecification = new OrSpecification<>(leftSpec, rightSpec);

        // Then
        assertNotNull(orSpecification);
        assertInstanceOf(OrSpecification.class, orSpecification);
        assertInstanceOf(CompositeSpecification.class, orSpecification);
        assertInstanceOf(Specification.class, orSpecification);
        assertInstanceOf(FluentSpecification.class, orSpecification);
    }

    @Test
    @DisplayName("should handle null specifications in constructor")
    void shouldHandleNullSpecificationsInConstructor() {
        // Given & When
        OrSpecification<String> orSpecification = new OrSpecification<>(null, null);

        // Then
        assertNotNull(orSpecification);
    }

    @ParameterizedTest
    @DisplayName("should evaluate OR logic correctly")
    @CsvSource({
        "true, true, true",
        "true, false, true",
        "false, true, true",
        "false, false, false"
    })
    void shouldEvaluateOrLogicCorrectly(boolean leftResult, boolean rightResult, boolean expectedResult) {
        // Given
        Specification<String> leftSpec = _ -> leftResult;
        Specification<String> rightSpec = _ -> rightResult;
        OrSpecification<String> orSpecification = new OrSpecification<>(leftSpec, rightSpec);

        // When
        boolean result = orSpecification.isSatisfiedBy("test");

        // Then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("should handle null candidate")
    void shouldHandleNullCandidate() {
        // Given
        Specification<String> leftSpec = candidate -> candidate != null;
        Specification<String> rightSpec = _ -> true;
        OrSpecification<String> orSpecification = new OrSpecification<>(leftSpec, rightSpec);

        // When
        boolean result = orSpecification.isSatisfiedBy(null);

        // Then
        assertThat(result).isTrue(); // Because rightSpec returns true regardless of candidate
    }

    @Test
    @DisplayName("should short-circuit when left specification is true")
    void shouldShortCircuitWhenLeftSpecificationIsTrue() {
        // Given
        Specification<String> leftSpec = _ -> true;
        Specification<String> rightSpec = _ -> {
            throw new RuntimeException("Should not be called");
        };
        OrSpecification<String> orSpecification = new OrSpecification<>(leftSpec, rightSpec);

        // When
        boolean result = orSpecification.isSatisfiedBy("test");

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("should evaluate right specification when left is false")
    void shouldEvaluateRightSpecificationWhenLeftIsFalse() {
        // Given
        boolean[] rightCalled = { false };
        Specification<String> leftSpec = _ -> false;
        Specification<String> rightSpec = _ -> {
            rightCalled[0] = true;
            return true;
        };
        OrSpecification<String> orSpecification = new OrSpecification<>(leftSpec, rightSpec);

        // When
        boolean result = orSpecification.isSatisfiedBy("test");

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
        OrSpecification<String> orSpecification = new OrSpecification<>(lengthSpec, startsWithSpec);

        // When & Then
        assertThat(orSpecification.isSatisfiedBy("testing")).isTrue(); // length > 3 AND starts with "test"
        assertThat(orSpecification.isSatisfiedBy("test")).isTrue(); // length = 4 > 3 AND starts with "test" = true
        assertThat(orSpecification.isSatisfiedBy("hello")).isTrue(); // length > 3 but doesn't start with "test"
        assertThat(orSpecification.isSatisfiedBy("hi")).isFalse(); // both conditions fail
        assertThat(orSpecification.isSatisfiedBy(null)).isFalse();
    }

    @Test
    @DisplayName("should support fluent interface operations")
    void shouldSupportFluentInterfaceOperations() {
        // Given
        Specification<String> leftSpec = _ -> true;
        Specification<String> rightSpec = _ -> true;
        OrSpecification<String> orSpecification = new OrSpecification<>(leftSpec, rightSpec);
        Specification<String> anotherSpec = _ -> false;

        // When & Then
        assertThat(orSpecification.and(anotherSpec)).isInstanceOf(AndSpecification.class);
        assertThat(orSpecification.or(anotherSpec)).isInstanceOf(OrSpecification.class);
        assertThat(orSpecification.not()).isInstanceOf(NotSpecification.class);
    }

    @Test
    @DisplayName("should handle exception in left specification")
    void shouldHandleExceptionInLeftSpecification() {
        // Given
        Specification<String> leftSpec = _ -> {
            throw new RuntimeException("Left spec error");
        };
        Specification<String> rightSpec = _ -> true;
        OrSpecification<String> orSpecification = new OrSpecification<>(leftSpec, rightSpec);

        // When & Then
        try {
            orSpecification.isSatisfiedBy("test");
            assertThat(false).as("Expected RuntimeException to be thrown").isTrue();
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Left spec error");
        }
    }

    @Test
    @DisplayName("should handle exception in right specification")
    void shouldHandleExceptionInRightSpecification() {
        // Given
        Specification<String> leftSpec = _ -> false;
        Specification<String> rightSpec = _ -> {
            throw new RuntimeException("Right spec error");
        };
        OrSpecification<String> orSpecification = new OrSpecification<>(leftSpec, rightSpec);

        // When & Then
        try {
            orSpecification.isSatisfiedBy("test");
            assertThat(false).as("Expected RuntimeException to be thrown").isTrue();
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Right spec error");
        }
    }

    @Test
    @DisplayName("should get and set left specification")
    void shouldGetAndSetLeftSpecification() {
        // Given
        Specification<String> leftSpec = _ -> true;
        Specification<String> rightSpec = _ -> false;
        OrSpecification<String> orSpecification = new OrSpecification<>(leftSpec, rightSpec);

        // When & Then
        assertThat(orSpecification.getLeftSpecification()).isSameAs(leftSpec);

        // When
        Specification<String> newLeftSpec = _ -> false;
        orSpecification.setLeftSpecification(newLeftSpec);

        // Then
        assertThat(orSpecification.getLeftSpecification()).isSameAs(newLeftSpec);
    }

    @Test
    @DisplayName("should get and set right specification")
    void shouldGetAndSetRightSpecification() {
        // Given
        Specification<String> leftSpec = _ -> true;
        Specification<String> rightSpec = _ -> false;
        OrSpecification<String> orSpecification = new OrSpecification<>(leftSpec, rightSpec);

        // When & Then
        assertThat(orSpecification.getRightSpecification()).isSameAs(rightSpec);

        // When
        Specification<String> newRightSpec = _ -> true;
        orSpecification.setRightSpecification(newRightSpec);

        // Then
        assertThat(orSpecification.getRightSpecification()).isSameAs(newRightSpec);
    }
}
