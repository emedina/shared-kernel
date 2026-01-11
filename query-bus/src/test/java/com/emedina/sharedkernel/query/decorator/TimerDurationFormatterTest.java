package com.emedina.sharedkernel.query.decorator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit tests for {@link Timer.DurationFormatter} class.
 *
 * @author Enrique Medina Montenegro
 */
@DisplayName("Timer.DurationFormatter")
class TimerDurationFormatterTest {

    @Test
    @DisplayName("should format zero duration correctly")
    void shouldFormatZeroDurationCorrectly() {
        // Given & When
        String formatted = Timer.DurationFormatter.format(0, TimeUnit.NANOSECONDS);

        // Then
        assertThat(formatted).isEqualTo("0ns");
    }

    @ParameterizedTest
    @DisplayName("should format nanoseconds correctly")
    @CsvSource({
        "1, 1ns",
        "500, 500ns",
        "999, 999ns"
    })
    void shouldFormatNanosecondsCorrectly(long nanos, String expected) {
        // Given & When
        String formatted = Timer.DurationFormatter.format(nanos, TimeUnit.NANOSECONDS);

        // Then
        assertThat(formatted).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("should format microseconds correctly")
    @CsvSource({
        "1000, 1μs",
        "1500, 1.5μs",
        "5500, 5.5μs",
        "999999, 999.999μs"
    })
    void shouldFormatMicrosecondsCorrectly(long nanos, String expected) {
        // Given & When
        String formatted = Timer.DurationFormatter.format(nanos, TimeUnit.NANOSECONDS);

        // Then
        assertThat(formatted).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("should format milliseconds correctly")
    @CsvSource({
        "1000000, 1ms",
        "1500000, 1.5ms",
        "5500000, 5.5ms",
        "999000000, 999ms"
    })
    void shouldFormatMillisecondsCorrectly(long nanos, String expected) {
        // Given & When
        String formatted = Timer.DurationFormatter.format(nanos, TimeUnit.NANOSECONDS);

        // Then
        assertThat(formatted).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("should format seconds correctly")
    @CsvSource({
        "1000000000, 1s",
        "1500000000, 1.5s",
        "5500000000, 5.5s",
        "59999999999, 60s"
    })
    void shouldFormatSecondsCorrectly(long nanos, String expected) {
        // Given & When
        String formatted = Timer.DurationFormatter.format(nanos, TimeUnit.NANOSECONDS);

        // Then
        assertThat(formatted).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("should format minutes correctly")
    @CsvSource({
        "60000000000, 1min",
        "90000000000, 1.5min",
        "120000000000, 2min",
        "3599999999999, 60min"
    })
    void shouldFormatMinutesCorrectly(long nanos, String expected) {
        // Given & When
        String formatted = Timer.DurationFormatter.format(nanos, TimeUnit.NANOSECONDS);

        // Then
        assertThat(formatted).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("should format hours correctly")
    @CsvSource({
        "3600000000000, 1h",
        "5400000000000, 1.5h",
        "7200000000000, 2h",
        "86399999999999, 24h"
    })
    void shouldFormatHoursCorrectly(long nanos, String expected) {
        // Given & When
        String formatted = Timer.DurationFormatter.format(nanos, TimeUnit.NANOSECONDS);

        // Then
        assertThat(formatted).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("should format days correctly")
    @CsvSource({
        "86400000000000, 1d",
        "129600000000000, 1.5d",
        "172800000000000, 2d",
        "604799999999999, 7d"
    })
    void shouldFormatDaysCorrectly(long nanos, String expected) {
        // Given & When
        String formatted = Timer.DurationFormatter.format(nanos, TimeUnit.NANOSECONDS);

        // Then
        assertThat(formatted).isEqualTo(expected);
    }

    @Test
    @DisplayName("should handle very large durations")
    void shouldHandleVeryLargeDurations() {
        // Given
        long oneYearInNanos = TimeUnit.DAYS.toNanos(365);

        // When
        String formatted = Timer.DurationFormatter.format(oneYearInNanos, TimeUnit.NANOSECONDS);

        // Then
        assertThat(formatted).contains("d"); // Should use days unit
        assertThat(formatted).contains("365");
    }

    @Test
    @DisplayName("should handle different source time units")
    void shouldHandleDifferentSourceTimeUnits() {
        // Given & When
        String formattedFromNanos = Timer.DurationFormatter.format(1000000, TimeUnit.NANOSECONDS);
        String formattedFromMicros = Timer.DurationFormatter.format(1000, TimeUnit.MICROSECONDS);
        String formattedFromMillis = Timer.DurationFormatter.format(1, TimeUnit.MILLISECONDS);

        // Then
        assertThat(formattedFromNanos).isEqualTo("1ms");
        assertThat(formattedFromMicros).isEqualTo("1ms");
        assertThat(formattedFromMillis).isEqualTo("1ms");
    }

    @Test
    @DisplayName("should handle negative durations")
    void shouldHandleNegativeDurations() {
        // Given & When
        String formatted = Timer.DurationFormatter.format(-1000000, TimeUnit.NANOSECONDS);

        // Then
        assertThat(formatted).isEqualTo("-1,000,000ns");
    }

    @Test
    @DisplayName("should handle edge case of maximum long value")
    void shouldHandleEdgeCaseOfMaximumLongValue() {
        // Given & When
        String formatted = Timer.DurationFormatter.format(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        // Then
        assertThat(formatted).isNotEmpty();
        assertThat(formatted).contains("d"); // Should use days unit
    }

    @Test
    @DisplayName("should handle edge case of minimum long value")
    void shouldHandleEdgeCaseOfMinimumLongValue() {
        // Given & When
        String formatted = Timer.DurationFormatter.format(Long.MIN_VALUE, TimeUnit.NANOSECONDS);

        // Then
        assertThat(formatted).isNotEmpty();
        assertThat(formatted).startsWith("-");
    }

}
