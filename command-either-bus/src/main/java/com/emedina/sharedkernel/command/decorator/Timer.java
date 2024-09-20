package com.emedina.sharedkernel.command.decorator;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;

/**
 * Timer measures elapsed time in nanoseconds. The timer captures the time in nanoseconds during the creation. To print
 * the elapsed time use the {@link #toString()} method.
 *
 * @author Enrique Medina Montenegro
 */
class Timer {

    private long startedAt;

    /**
     * Creates a new timer and captures the start date.
     */
    public Timer() {
        this.startedAt = System.nanoTime();
    }

    /**
     * Returns the elapsed time in nanoseconds.
     *
     * @return elapsed time in nanoseconds
     */
    public long elapsed() {
        return System.nanoTime() - startedAt;
    }

    @Override
    public String toString() {
        return DurationFormatter.format(elapsed(), NANOSECONDS);
    }

    final class DurationFormatter {

        private DurationFormatter() {
        }

        /**
         * Returns the formatted duration with the abbreviate time unit.
         *
         * @param duration   duration
         * @param sourceUnit unit of duration
         * @return formatted duration
         */
        public static String format(final long duration, final TimeUnit sourceUnit) {
            long durationInNanoSeconds = convertToNanoSeconds(duration, sourceUnit);
            return formatNanoSeconds(durationInNanoSeconds);
        }

        private static long convertToNanoSeconds(final long duration, final TimeUnit sourceUnit) {
            return NANOSECONDS.convert(duration, sourceUnit);
        }

        private static String formatNanoSeconds(final long durationInNanoSeconds) {
            TimeUnit targetUnit = chooseUnit(durationInNanoSeconds);
            double value = (double) durationInNanoSeconds / NANOSECONDS.convert(1, targetUnit);

            NumberFormat formatter = NumberFormat.getInstance(Locale.ENGLISH);
            return formatter.format(value) + abbreviate(targetUnit);
        }

        private static TimeUnit chooseUnit(final long nanos) {
            if (DAYS.convert(nanos, NANOSECONDS) > 0) {
                return DAYS;
            }
            if (HOURS.convert(nanos, NANOSECONDS) > 0) {
                return HOURS;
            }
            if (MINUTES.convert(nanos, NANOSECONDS) > 0) {
                return MINUTES;
            }
            if (SECONDS.convert(nanos, NANOSECONDS) > 0) {
                return SECONDS;
            }
            if (MILLISECONDS.convert(nanos, NANOSECONDS) > 0) {
                return MILLISECONDS;
            }
            if (MICROSECONDS.convert(nanos, NANOSECONDS) > 0) {
                return MICROSECONDS;
            }
            return NANOSECONDS;
        }

        private static String abbreviate(final TimeUnit unit) {
            switch (unit) {
                case NANOSECONDS:
                    return "ns";
                case MICROSECONDS:
                    return "\u03bcs"; // μs
                case MILLISECONDS:
                    return "ms";
                case SECONDS:
                    return "s";
                case MINUTES:
                    return "min";
                case HOURS:
                    return "h";
                case DAYS:
                    return "d";
                default:
                    throw new AssertionError();
            }
        }

    }

}