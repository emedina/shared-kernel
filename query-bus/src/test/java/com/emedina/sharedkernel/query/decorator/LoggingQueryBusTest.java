package com.emedina.sharedkernel.query.decorator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.emedina.sharedkernel.query.Query;
import com.emedina.sharedkernel.query.core.QueryBus;

/**
 * Unit tests for {@link LoggingQueryBus} class.
 *
 * @author Enrique Medina Montenegro
 */
@DisplayName("LoggingQueryBus")
class LoggingQueryBusTest {

    private TestQueryBus decoratedQueryBus;
    private LoggingQueryBus loggingQueryBus;
    private TestQuery testQuery;

    @BeforeEach
    void setUp() {
        decoratedQueryBus = new TestQueryBus();
        loggingQueryBus = new LoggingQueryBus(decoratedQueryBus);
        testQuery = new TestQuery();
    }

    @Test
    @DisplayName("should accept null decorated query bus")
    void shouldAcceptNullDecoratedQueryBus() {
        // Given & When
        LoggingQueryBus loggingQueryBus = new LoggingQueryBus(null);

        // Then
        assertThat(loggingQueryBus).isNotNull();
    }

    @Test
    @DisplayName("should delegate query execution to decorated bus")
    void shouldDelegateQueryExecutionToDecoratedBus() {
        // Given
        String expectedResult = "test-result";
        decoratedQueryBus.setResult(expectedResult);

        // When
        String result = loggingQueryBus.query(testQuery);

        // Then
        assertThat(decoratedQueryBus.wasExecuted()).isTrue();
        assertThat(decoratedQueryBus.getExecutedQuery()).isSameAs(testQuery);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("should return result from decorated bus")
    void shouldReturnResultFromDecoratedBus() {
        // Given
        String expectedResult = "query-result";
        decoratedQueryBus.setResult(expectedResult);

        // When
        String result = loggingQueryBus.query(testQuery);

        // Then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("should propagate exceptions from decorated bus")
    void shouldPropagateExceptionsFromDecoratedBus() {
        // Given
        RuntimeException expectedException = new RuntimeException("Test exception");
        decoratedQueryBus.setExceptionToThrow(expectedException);

        // When & Then
        assertThatThrownBy(() -> loggingQueryBus.query(testQuery))
            .isSameAs(expectedException);
    }

    @Test
    @DisplayName("should not interfere with query execution flow")
    void shouldNotInterfereWithQueryExecutionFlow() {
        // Given
        TestQuery query = new TestQuery();
        String expectedResult = "result";
        decoratedQueryBus.setResult(expectedResult);

        // When
        String result = loggingQueryBus.query(query);

        // Then
        assertThat(decoratedQueryBus.wasExecuted()).isTrue();
        assertThat(decoratedQueryBus.getExecutedQuery()).isSameAs(query);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("should handle multiple query executions")
    void shouldHandleMultipleQueryExecutions() {
        // Given
        TestQuery query1 = new TestQuery();
        AnotherTestQuery query2 = new AnotherTestQuery();
        decoratedQueryBus.setResult("result1");

        // When
        loggingQueryBus.query(query1);
        loggingQueryBus.query(query2);

        // Then
        assertThat(decoratedQueryBus.getExecutionCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("should create new instance with different decorated bus")
    void shouldCreateNewInstanceWithDifferentDecoratedBus() {
        // Given
        TestQueryBus anotherBus = new TestQueryBus();
        anotherBus.setResult("another-result");

        // When
        LoggingQueryBus anotherLoggingBus = new LoggingQueryBus(anotherBus);
        anotherLoggingBus.query(testQuery);

        // Then
        assertThat(decoratedQueryBus.wasExecuted()).isFalse();
        assertThat(anotherBus.wasExecuted()).isTrue();
    }

    @Test
    @DisplayName("should execute query even when decorated bus throws in finally block")
    void shouldExecuteQueryEvenWhenDecoratedBusThrowsInFinallyBlock() {
        // Given
        RuntimeException expectedException = new RuntimeException("Test exception");
        decoratedQueryBus.setExceptionToThrow(expectedException);

        // When & Then
        assertThatThrownBy(() -> loggingQueryBus.query(testQuery))
            .isSameAs(expectedException);
        assertThat(decoratedQueryBus.wasExecuted()).isTrue();
    }

    @Test
    @DisplayName("should handle null results from decorated bus")
    void shouldHandleNullResultsFromDecoratedBus() {
        // Given
        decoratedQueryBus.setResult(null);

        // When
        Object result = loggingQueryBus.query(testQuery);

        // Then
        assertThat(result).isNull();
        assertThat(decoratedQueryBus.wasExecuted()).isTrue();
    }

    // Test query implementations
    private static class TestQuery implements Query {
        // Empty implementation for testing
    }

    private static class AnotherTestQuery implements Query {
        // Empty implementation for testing
    }

    // Test QueryBus implementation
    private static class TestQueryBus implements QueryBus {

        private final AtomicBoolean executed = new AtomicBoolean(false);
        private final AtomicReference<Query> executedQuery = new AtomicReference<>();
        private final AtomicReference<RuntimeException> exceptionToThrow = new AtomicReference<>();
        private final AtomicReference<Object> result = new AtomicReference<>();
        private int executionCount = 0;

        @Override
        public <R, Q extends Query> R query(Q query) {
            executionCount++;
            executed.set(true);
            executedQuery.set(query);

            RuntimeException exception = exceptionToThrow.get();
            if (exception != null) {
                throw exception;
            }

            @SuppressWarnings("unchecked")
            R typedResult = (R) result.get();
            return typedResult;
        }

        public boolean wasExecuted() {
            return executed.get();
        }

        public Query getExecutedQuery() {
            return executedQuery.get();
        }

        public void setExceptionToThrow(RuntimeException exception) {
            exceptionToThrow.set(exception);
        }

        public void setResult(Object result) {
            this.result.set(result);
        }

        public int getExecutionCount() {
            return executionCount;
        }

    }

}
