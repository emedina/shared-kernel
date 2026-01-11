package com.emedina.sharedkernel.query.decorator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.emedina.sharedkernel.query.Query;

/**
 * Unit tests for {@link LoggingQueryBus} class with null decorated bus.
 *
 * @author Enrique Medina Montenegro
 */
@DisplayName("LoggingQueryBus with null decorated bus")
class LoggingQueryBusNullTest {

    @Test
    @DisplayName("should accept null decorated query bus")
    void shouldAcceptNullDecoratedQueryBus() {
        // Given & When
        LoggingQueryBus loggingQueryBus = new LoggingQueryBus(null);

        // Then
        assertThat(loggingQueryBus).isNotNull();
    }

    @Test
    @DisplayName("should throw NullPointerException when executing query with null decorated bus")
    void shouldThrowNullPointerExceptionWhenExecutingQueryWithNullDecoratedBus() {
        // Given
        LoggingQueryBus loggingQueryBus = new LoggingQueryBus(null);
        TestQuery query = new TestQuery();

        // When & Then
        assertThatThrownBy(() -> loggingQueryBus.query(query))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should throw NullPointerException when executing null query with null decorated bus")
    void shouldThrowNullPointerExceptionWhenExecutingNullQueryWithNullDecoratedBus() {
        // Given
        LoggingQueryBus loggingQueryBus = new LoggingQueryBus(null);

        // When & Then
        assertThatThrownBy(() -> loggingQueryBus.query(null))
            .isInstanceOf(NullPointerException.class);
    }

    // Test query implementation
    private static class TestQuery implements Query {
        // Empty implementation for testing
    }

}
