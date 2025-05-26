package com.emedina.sharedkernel.command.decorator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.emedina.sharedkernel.command.Command;

/**
 * Unit tests for {@link LoggingCommandBus} class with null decorated bus.
 * 
 * @author Enrique Medina Montenegro
 */
@DisplayName("LoggingCommandBus with null decorated bus")
class LoggingCommandBusNullTest {

    @Test
    @DisplayName("should accept null decorated command bus")
    void shouldAcceptNullDecoratedCommandBus() {
        // Given & When
        LoggingCommandBus loggingCommandBus = new LoggingCommandBus(null);

        // Then
        assertThat(loggingCommandBus).isNotNull();
    }

    @Test
    @DisplayName("should throw NullPointerException when executing command with null decorated bus")
    void shouldThrowNullPointerExceptionWhenExecutingCommandWithNullDecoratedBus() {
        // Given
        LoggingCommandBus loggingCommandBus = new LoggingCommandBus(null);
        TestCommand command = new TestCommand();

        // When & Then
        assertThatThrownBy(() -> loggingCommandBus.execute(command))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should throw NullPointerException when executing null command with null decorated bus")
    void shouldThrowNullPointerExceptionWhenExecutingNullCommandWithNullDecoratedBus() {
        // Given
        LoggingCommandBus loggingCommandBus = new LoggingCommandBus(null);

        // When & Then
        assertThatThrownBy(() -> loggingCommandBus.execute(null))
            .isInstanceOf(NullPointerException.class);
    }

    // Test command implementation
    private static class TestCommand implements Command {
        // Empty implementation for testing
    }
}
