package com.emedina.sharedkernel.command.decorator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.emedina.sharedkernel.command.Command;
import com.emedina.sharedkernel.command.core.CommandBus;

import io.vavr.control.Either;

/**
 * Unit tests for {@link LoggingCommandBus} class.
 *
 * @author Enrique Medina Montenegro
 */
@DisplayName("LoggingCommandBus")
class LoggingCommandBusTest {

    private TestCommandBus decoratedCommandBus;
    private LoggingCommandBus loggingCommandBus;
    private TestCommand testCommand;

    @BeforeEach
    void setUp() {
        decoratedCommandBus = new TestCommandBus();
        loggingCommandBus = new LoggingCommandBus(decoratedCommandBus);
        testCommand = new TestCommand();
    }

    @Test
    @DisplayName("should accept null decorated command bus")
    void shouldAcceptNullDecoratedCommandBus() {
        // Given & When
        LoggingCommandBus loggingCommandBus = new LoggingCommandBus(null);

        // Then
        assertThat(loggingCommandBus).isNotNull();
    }

    @Test
    @DisplayName("should delegate command execution to decorated bus")
    void shouldDelegateCommandExecutionToDecoratedBus() {
        // Given & When
        Either<?, Void> result = loggingCommandBus.execute(testCommand);

        // Then
        assertThat(decoratedCommandBus.wasExecuted()).isTrue();
        assertThat(decoratedCommandBus.getExecutedCommand()).isSameAs(testCommand);
        assertThat(result.isRight()).isTrue();
    }

    @Test
    @DisplayName("should return Either.right(null) on successful execution")
    void shouldReturnEitherRightOnSuccessfulExecution() {
        // Given & When
        Either<?, Void> result = loggingCommandBus.execute(testCommand);

        // Then
        assertThat(result.isRight()).isTrue();
        assertThat(result.get()).isNull();
    }

    @Test
    @DisplayName("should propagate exceptions from decorated bus")
    void shouldPropagateExceptionsFromDecoratedBus() {
        // Given
        RuntimeException expectedException = new RuntimeException("Test exception");
        decoratedCommandBus.setExceptionToThrow(expectedException);

        // When & Then
        assertThatThrownBy(() -> loggingCommandBus.execute(testCommand))
            .isSameAs(expectedException);
    }

    @Test
    @DisplayName("should not interfere with command execution flow")
    void shouldNotInterfereWithCommandExecutionFlow() {
        // Given
        TestCommand command = new TestCommand();

        // When
        Either<?, Void> result = loggingCommandBus.execute(command);

        // Then
        assertThat(decoratedCommandBus.wasExecuted()).isTrue();
        assertThat(decoratedCommandBus.getExecutedCommand()).isSameAs(command);
        assertThat(result.isRight()).isTrue();
    }

    @Test
    @DisplayName("should handle multiple command executions")
    void shouldHandleMultipleCommandExecutions() {
        // Given
        TestCommand command1 = new TestCommand();
        AnotherTestCommand command2 = new AnotherTestCommand();

        // When
        loggingCommandBus.execute(command1);
        loggingCommandBus.execute(command2);

        // Then
        assertThat(decoratedCommandBus.getExecutionCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("should create new instance with different decorated bus")
    void shouldCreateNewInstanceWithDifferentDecoratedBus() {
        // Given
        TestCommandBus anotherBus = new TestCommandBus();

        // When
        LoggingCommandBus anotherLoggingBus = new LoggingCommandBus(anotherBus);
        anotherLoggingBus.execute(testCommand);

        // Then
        assertThat(decoratedCommandBus.wasExecuted()).isFalse();
        assertThat(anotherBus.wasExecuted()).isTrue();
    }

    @Test
    @DisplayName("should execute command even when decorated bus throws in finally block")
    void shouldExecuteCommandEvenWhenDecoratedBusThrowsInFinallyBlock() {
        // Given
        RuntimeException expectedException = new RuntimeException("Test exception");
        decoratedCommandBus.setExceptionToThrow(expectedException);

        // When & Then
        assertThatThrownBy(() -> loggingCommandBus.execute(testCommand))
            .isSameAs(expectedException);
        assertThat(decoratedCommandBus.wasExecuted()).isTrue();
    }

    // Test command implementations
    private static class TestCommand implements Command {
        // Empty implementation for testing
    }

    private static class AnotherTestCommand implements Command {
        // Empty implementation for testing
    }

    // Test CommandBus implementation
    private static class TestCommandBus implements CommandBus {

        private final AtomicBoolean executed = new AtomicBoolean(false);
        private final AtomicReference<Command> executedCommand = new AtomicReference<>();
        private final AtomicReference<RuntimeException> exceptionToThrow = new AtomicReference<>();
        private int executionCount = 0;

        @Override
        public <E, C extends Command> Either<E, Void> execute(C command) {
            executionCount++;
            executed.set(true);
            executedCommand.set(command);

            RuntimeException exception = exceptionToThrow.get();
            if (exception != null) {
                throw exception;
            }

            return Either.right(null);
        }

        public boolean wasExecuted() {
            return executed.get();
        }

        public Command getExecutedCommand() {
            return executedCommand.get();
        }

        public void setExceptionToThrow(RuntimeException exception) {
            exceptionToThrow.set(exception);
        }

        public int getExecutionCount() {
            return executionCount;
        }

    }

}
