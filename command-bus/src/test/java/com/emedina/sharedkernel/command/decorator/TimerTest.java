package com.emedina.sharedkernel.command.decorator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit tests for {@link Timer} class.
 * 
 * @author Enrique Medina Montenegro
 */
@DisplayName("Timer")
class TimerTest {

    @Test
    @DisplayName("should capture start time on creation")
    void shouldCaptureStartTimeOnCreation() {
        // Given & When
        Timer timer = new Timer();

        // Then
        assertThat(timer.elapsed()).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("should measure elapsed time accurately")
    void shouldMeasureElapsedTimeAccurately() throws InterruptedException {
        // Given
        Timer timer = new Timer();

        // When
        Thread.sleep(10); // Sleep for 10ms
        long elapsed = timer.elapsed();

        // Then
        assertThat(elapsed).isGreaterThan(0);
        // Should be at least 10ms in nanoseconds (allowing for some variance)
        assertThat(elapsed).isGreaterThan(TimeUnit.MILLISECONDS.toNanos(5));
    }

    @Test
    @DisplayName("should format elapsed time as string")
    void shouldFormatElapsedTimeAsString() {
        // Given
        Timer timer = new Timer();

        // When
        String formatted = timer.toString();

        // Then
        assertThat(formatted).isNotNull();
        assertThat(formatted).isNotEmpty();
        // Should contain a number and a unit
        assertTrue(formatted.matches(".*\\d+.*[a-zA-Z\u03bc]+.*"),
            "Formatted time should contain digits and unit: " + formatted);
    }

    @ParameterizedTest
    @DisplayName("should format different time units correctly")
    @CsvSource({
        "1000000000, s",     // 1 second in nanoseconds
        "1000000, ms",       // 1 millisecond in nanoseconds  
        "1000, μs",          // 1 microsecond in nanoseconds
        "1, ns"              // 1 nanosecond
    })
    void shouldFormatDifferentTimeUnitsCorrectly(long nanoseconds, String expectedUnit) {
        // Given
        String formatted = Timer.DurationFormatter.format(nanoseconds, TimeUnit.NANOSECONDS);

        // Then
        assertThat(formatted).contains(expectedUnit);
        assertThat(formatted).matches(".*\\d+.*");
    }

    @Test
    @DisplayName("should handle very large durations")
    void shouldHandleVeryLargeDurations() {
        // Given
        long oneDayInNanos = TimeUnit.DAYS.toNanos(1);

        // When
        String formatted = Timer.DurationFormatter.format(oneDayInNanos, TimeUnit.NANOSECONDS);

        // Then
        assertThat(formatted).contains("d"); // Should use days unit
        assertThat(formatted).contains("1");
    }

    @Test
    @DisplayName("should handle zero duration")
    void shouldHandleZeroDuration() {
        // Given & When
        String formatted = Timer.DurationFormatter.format(0, TimeUnit.NANOSECONDS);

        // Then
        assertThat(formatted).isEqualTo("0ns");
    }

    @Test
    @DisplayName("should be consistent across multiple calls")
    void shouldBeConsistentAcrossMultipleCalls() {
        // Given
        Timer timer = new Timer();

        // When
        long elapsed1 = timer.elapsed();
        long elapsed2 = timer.elapsed();

        // Then
        assertThat(elapsed2).isGreaterThanOrEqualTo(elapsed1);
    }
}
