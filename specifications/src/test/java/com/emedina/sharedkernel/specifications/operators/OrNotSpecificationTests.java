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
 * Comprehensive unit tests for {@link OrNotSpecification} class.
 * 
 * @author Enrique Medina Montenegro
 */
@DisplayName("OrNotSpecification")
class OrNotSpecificationTests {

    @Test
    @DisplayName("should create instance with valid specifications")
    void shouldCreateInstanceWithValidSpecifications() {
        // Given
        Specification<String> leftSpec = _ -> true;
        Specification<String> rightSpec = _ -> false;

        // When
        OrNotSpecification<String> orNotSpecification = new OrNotSpecification<>(leftSpec, rightSpec);

        // Then
        assertNotNull(orNotSpecification);
        assertInstanceOf(OrNotSpecification.class, orNotSpecification);
        assertInstanceOf(CompositeSpecification.class, orNotSpecification);
        assertInstanceOf(Specification.class, orNotSpecification);
        assertInstanceOf(FluentSpecification.class, orNotSpecification);
    }

    @Test
    @DisplayName("should handle null specifications in constructor")
    void shouldHandleNullSpecificationsInConstructor() {
        // Given & When
        OrNotSpecification<String> orNotSpecification = new OrNotSpecification<>(null, null);

        // Then
        assertNotNull(orNotSpecification);
    }

    @ParameterizedTest
    @DisplayName("should evaluate OR NOT logic correctly")
    @CsvSource({
        "true, true, true",
        "true, false, true",
        "false, true, false",
        "false, false, true"
    })
    void shouldEvaluateOrNotLogicCorrectly(boolean leftResult, boolean rightResult, boolean expectedResult) {
        // Given
        Specification<String> leftSpec = _ -> leftResult;
        Specification<String> rightSpec = _ -> rightResult;
        OrNotSpecification<String> orNotSpecification = new OrNotSpecification<>(leftSpec, rightSpec);

        // When
        boolean result = orNotSpecification.isSatisfiedBy("test");

        // Then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("should handle null candidate")
    void shouldHandleNullCandidate() {
        // Given
        Specification<String> leftSpec = candidate -> candidate != null;
        Specification<String> rightSpec = candidate -> candidate != null;
        OrNotSpecification<String> orNotSpecification = new OrNotSpecification<>(leftSpec, rightSpec);

        // When
        boolean result = orNotSpecification.isSatisfiedBy(null);

        // Then
        assertThat(result).isTrue(); // Because leftSpec returns false for null AND rightSpec returns false for null, so OR NOT is true
    }

    @Test
    @DisplayName("should short-circuit when left specification is true")
    void shouldShortCircuitWhenLeftSpecificationIsTrue() {
        // Given
        Specification<String> leftSpec = _ -> true;
        Specification<String> rightSpec = _ -> {
            throw new RuntimeException("Should not be called");
        };
        OrNotSpecification<String> orNotSpecification = new OrNotSpecification<>(leftSpec, rightSpec);

        // When
        boolean result = orNotSpecification.isSatisfiedBy("test");

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
        OrNotSpecification<String> orNotSpecification = new OrNotSpecification<>(leftSpec, rightSpec);

        // When
        boolean result = orNotSpecification.isSatisfiedBy("test");

        // Then
        assertThat(result).isFalse(); // false OR NOT true = false
        assertThat(rightCalled[0]).isTrue();
    }

    @Test
    @DisplayName("should work with complex specifications")
    void shouldWorkWithComplexSpecifications() {
        // Given
        Specification<String> lengthSpec = candidate -> candidate != null && candidate.length() > 3;
        Specification<String> startsWithSpec = candidate -> candidate != null && candidate.startsWith("test");
        OrNotSpecification<String> orNotSpecification = new OrNotSpecification<>(lengthSpec, startsWithSpec);

        // When & Then
        assertThat(orNotSpecification.isSatisfiedBy("testing")).isTrue(); // length > 3 OR NOT starts with "test" = true
        assertThat(orNotSpecification.isSatisfiedBy("hello")).isTrue(); // length > 3 OR NOT starts with "test" = true
        assertThat(orNotSpecification.isSatisfiedBy("hi")).isTrue(); // length <= 3 OR NOT starts with "test" = true
        assertThat(orNotSpecification.isSatisfiedBy("te")).isTrue(); // length <= 3 OR NOT starts with "test" = true
        assertThat(orNotSpecification.isSatisfiedBy(null)).isTrue(); // null fails both specs, so OR NOT is true
    }

    @Test
    @DisplayName("should support fluent interface operations")
    void shouldSupportFluentInterfaceOperations() {
        // Given
        Specification<String> leftSpec = _ -> true;
        Specification<String> rightSpec = _ -> true;
        OrNotSpecification<String> orNotSpecification = new OrNotSpecification<>(leftSpec, rightSpec);
        Specification<String> anotherSpec = _ -> false;

        // When & Then
        assertThat(orNotSpecification.and(anotherSpec)).isInstanceOf(AndSpecification.class);
        assertThat(orNotSpecification.or(anotherSpec)).isInstanceOf(OrSpecification.class);
        assertThat(orNotSpecification.not()).isInstanceOf(NotSpecification.class);
    }

    @Test
    @DisplayName("should handle exception in left specification")
    void shouldHandleExceptionInLeftSpecification() {
        // Given
        Specification<String> leftSpec = _ -> {
            throw new RuntimeException("Left spec error");
        };
        Specification<String> rightSpec = _ -> true;
        OrNotSpecification<String> orNotSpecification = new OrNotSpecification<>(leftSpec, rightSpec);

        // When & Then
        try {
            orNotSpecification.isSatisfiedBy("test");
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
        OrNotSpecification<String> orNotSpecification = new OrNotSpecification<>(leftSpec, rightSpec);

        // When & Then
        try {
            orNotSpecification.isSatisfiedBy("test");
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
        OrNotSpecification<String> orNotSpecification = new OrNotSpecification<>(leftSpec, rightSpec);

        // When & Then
        assertThat(orNotSpecification.getLeftSpecification()).isSameAs(leftSpec);

        // When
        Specification<String> newLeftSpec = _ -> false;
        orNotSpecification.setLeftSpecification(newLeftSpec);

        // Then
        assertThat(orNotSpecification.getLeftSpecification()).isSameAs(newLeftSpec);
    }

    @Test
    @DisplayName("should get and set right specification")
    void shouldGetAndSetRightSpecification() {
        // Given
        Specification<String> leftSpec = _ -> true;
        Specification<String> rightSpec = _ -> false;
        OrNotSpecification<String> orNotSpecification = new OrNotSpecification<>(leftSpec, rightSpec);

        // When & Then
        assertThat(orNotSpecification.getRightSpecification()).isSameAs(rightSpec);

        // When
        Specification<String> newRightSpec = _ -> true;
        orNotSpecification.setRightSpecification(newRightSpec);

        // Then
        assertThat(orNotSpecification.getRightSpecification()).isSameAs(newRightSpec);
    }
}
