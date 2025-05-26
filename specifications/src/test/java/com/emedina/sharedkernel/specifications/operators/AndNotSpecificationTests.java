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
 * Comprehensive unit tests for {@link AndNotSpecification} class.
 * 
 * @author Enrique Medina Montenegro
 */
@DisplayName("AndNotSpecification")
class AndNotSpecificationTests {

    @Test
    @DisplayName("should create instance with valid specifications")
    void shouldCreateInstanceWithValidSpecifications() {
        // Given
        Specification<String> leftSpec = _ -> true;
        Specification<String> rightSpec = _ -> false;

        // When
        AndNotSpecification<String> andNotSpecification = new AndNotSpecification<>(leftSpec, rightSpec);

        // Then
        assertNotNull(andNotSpecification);
        assertInstanceOf(AndNotSpecification.class, andNotSpecification);
        assertInstanceOf(CompositeSpecification.class, andNotSpecification);
        assertInstanceOf(Specification.class, andNotSpecification);
        assertInstanceOf(FluentSpecification.class, andNotSpecification);
    }

    @Test
    @DisplayName("should handle null specifications in constructor")
    void shouldHandleNullSpecificationsInConstructor() {
        // Given & When
        AndNotSpecification<String> andNotSpecification = new AndNotSpecification<>(null, null);

        // Then
        assertNotNull(andNotSpecification);
    }

    @ParameterizedTest
    @DisplayName("should evaluate AND NOT logic correctly")
    @CsvSource({
        "true, true, false",
        "true, false, true",
        "false, true, false",
        "false, false, false"
    })
    void shouldEvaluateAndNotLogicCorrectly(boolean leftResult, boolean rightResult, boolean expectedResult) {
        // Given
        Specification<String> leftSpec = _ -> leftResult;
        Specification<String> rightSpec = _ -> rightResult;
        AndNotSpecification<String> andNotSpecification = new AndNotSpecification<>(leftSpec, rightSpec);

        // When
        boolean result = andNotSpecification.isSatisfiedBy("test");

        // Then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("should handle null candidate")
    void shouldHandleNullCandidate() {
        // Given
        Specification<String> leftSpec = candidate -> candidate != null;
        Specification<String> rightSpec = candidate -> candidate != null;
        AndNotSpecification<String> andNotSpecification = new AndNotSpecification<>(leftSpec, rightSpec);

        // When
        boolean result = andNotSpecification.isSatisfiedBy(null);

        // Then
        assertThat(result).isFalse(); // Because leftSpec returns false for null
    }

    @Test
    @DisplayName("should short-circuit when left specification is false")
    void shouldShortCircuitWhenLeftSpecificationIsFalse() {
        // Given
        Specification<String> leftSpec = _ -> false;
        Specification<String> rightSpec = _ -> {
            throw new RuntimeException("Should not be called");
        };
        AndNotSpecification<String> andNotSpecification = new AndNotSpecification<>(leftSpec, rightSpec);

        // When
        boolean result = andNotSpecification.isSatisfiedBy("test");

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
        AndNotSpecification<String> andNotSpecification = new AndNotSpecification<>(leftSpec, rightSpec);

        // When
        boolean result = andNotSpecification.isSatisfiedBy("test");

        // Then
        assertThat(result).isFalse(); // true AND NOT true = false
        assertThat(rightCalled[0]).isTrue();
    }

    @Test
    @DisplayName("should work with complex specifications")
    void shouldWorkWithComplexSpecifications() {
        // Given
        Specification<String> lengthSpec = candidate -> candidate != null && candidate.length() > 3;
        Specification<String> startsWithSpec = candidate -> candidate != null && candidate.startsWith("test");
        AndNotSpecification<String> andNotSpecification = new AndNotSpecification<>(lengthSpec, startsWithSpec);

        // When & Then
        assertThat(andNotSpecification.isSatisfiedBy("testing")).isFalse(); // length > 3 AND NOT starts with "test" = false
        assertThat(andNotSpecification.isSatisfiedBy("hello")).isTrue(); // length > 3 AND NOT starts with "test" = true
        assertThat(andNotSpecification.isSatisfiedBy("hi")).isFalse(); // length <= 3, so false
        assertThat(andNotSpecification.isSatisfiedBy(null)).isFalse(); // null fails first spec
    }

    @Test
    @DisplayName("should support fluent interface operations")
    void shouldSupportFluentInterfaceOperations() {
        // Given
        Specification<String> leftSpec = _ -> true;
        Specification<String> rightSpec = _ -> true;
        AndNotSpecification<String> andNotSpecification = new AndNotSpecification<>(leftSpec, rightSpec);
        Specification<String> anotherSpec = _ -> false;

        // When & Then
        assertThat(andNotSpecification.and(anotherSpec)).isInstanceOf(AndSpecification.class);
        assertThat(andNotSpecification.or(anotherSpec)).isInstanceOf(OrSpecification.class);
        assertThat(andNotSpecification.not()).isInstanceOf(NotSpecification.class);
    }

    @Test
    @DisplayName("should handle exception in left specification")
    void shouldHandleExceptionInLeftSpecification() {
        // Given
        Specification<String> leftSpec = _ -> {
            throw new RuntimeException("Left spec error");
        };
        Specification<String> rightSpec = _ -> true;
        AndNotSpecification<String> andNotSpecification = new AndNotSpecification<>(leftSpec, rightSpec);

        // When & Then
        try {
            andNotSpecification.isSatisfiedBy("test");
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
        AndNotSpecification<String> andNotSpecification = new AndNotSpecification<>(leftSpec, rightSpec);

        // When & Then
        try {
            andNotSpecification.isSatisfiedBy("test");
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
        AndNotSpecification<String> andNotSpecification = new AndNotSpecification<>(leftSpec, rightSpec);

        // When & Then
        assertThat(andNotSpecification.getLeftSpecification()).isSameAs(leftSpec);

        // When
        Specification<String> newLeftSpec = _ -> false;
        andNotSpecification.setLeftSpecification(newLeftSpec);

        // Then
        assertThat(andNotSpecification.getLeftSpecification()).isSameAs(newLeftSpec);
    }

    @Test
    @DisplayName("should get and set right specification")
    void shouldGetAndSetRightSpecification() {
        // Given
        Specification<String> leftSpec = _ -> true;
        Specification<String> rightSpec = _ -> false;
        AndNotSpecification<String> andNotSpecification = new AndNotSpecification<>(leftSpec, rightSpec);

        // When & Then
        assertThat(andNotSpecification.getRightSpecification()).isSameAs(rightSpec);

        // When
        Specification<String> newRightSpec = _ -> true;
        andNotSpecification.setRightSpecification(newRightSpec);

        // Then
        assertThat(andNotSpecification.getRightSpecification()).isSameAs(newRightSpec);
    }
}
