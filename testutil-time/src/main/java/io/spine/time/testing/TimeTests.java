/*
 * Copyright 2018, TeamDev. All rights reserved.
 *
 * Redistribution and use in source and/or binary forms, with or without
 * modification, must retain the above copyright notice and the following
 * disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.spine.time.testing;

import com.google.common.annotations.VisibleForTesting;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;
import io.spine.base.Time;
import io.spine.time.Durations2;

import java.time.LocalTime;

import static com.google.protobuf.util.Durations.fromSeconds;
import static com.google.protobuf.util.Timestamps.add;
import static com.google.protobuf.util.Timestamps.subtract;
import static io.spine.base.Time.getCurrentTime;
import static io.spine.base.Time.systemTime;
import static io.spine.time.Durations2.hours;
import static io.spine.time.Durations2.seconds;
import static io.spine.validate.Validate.checkPositive;

/**
 * Utility class for working with time-related tests.
 *
 * @author Alexander Yevsykov
 */
@VisibleForTesting
public class TimeTests {

    /** Prevent instantiation of this utility class. */
    private TimeTests() {
    }

    /**
     * Returns the {@linkplain Time#getCurrentTime() current time} in seconds.
     *
     * @return a seconds value
     */
    public static long currentTimeSeconds() {
        final long secs = getCurrentTime().getSeconds();
        return secs;
    }

    /**
     * Waits till new day to come, if it's the last day second.
     *
     * <p>This method is useful for tests that obtain current date/time values
     * and need to avoid the day edge for correctness of the test values.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    public static void avoidDayEdge() {
        LocalTime lastDaySecond = LocalTime.MAX.withNano(0);
        do {
            // Wait.
        } while (LocalTime.now().isAfter(lastDaySecond));
    }

    /**
     * The provider of current time, which is always the same.
     *
     * <p>Use this {@code Timestamps.Provider} in time-related tests that are
     * sensitive to bounds of minutes, hours, days, etc.
     */
    public static class FrozenMadHatterParty implements Time.Provider {
        private final Timestamp frozenTime;

        public FrozenMadHatterParty(Timestamp frozenTime) {
            this.frozenTime = frozenTime;
        }

        /** Returns the value passed to the constructor. */
        @Override
        public Timestamp getCurrentTime() {
            return frozenTime;
        }
    }

    /**
     * The time provider that can rewind current time.
     *
     * <p>Created in the future, {@linkplain #THIRTY_YEARS_IN_HOURS 30 years} from
     * the {@link Time#systemTime() current system time}.
     */
    public static class BackToTheFuture implements Time.Provider {

        public static final long THIRTY_YEARS_IN_HOURS = 262800L;

        private Timestamp currentTime;

        public BackToTheFuture() {
            this.currentTime = add(systemTime(), hours(THIRTY_YEARS_IN_HOURS));
        }

        @Override
        public synchronized Timestamp getCurrentTime() {
            return this.currentTime;
        }

        private synchronized void setCurrentTime(Timestamp currentTime) {
            this.currentTime = currentTime;
        }

        /**
         * Rewinds the {@linkplain #getCurrentTime() "current time"} forward
         * by the passed amount of hours.
         */
        @CanIgnoreReturnValue
        public synchronized Timestamp forward(long hoursDelta) {
            checkPositive(hoursDelta);
            final Timestamp newTime = add(this.currentTime, hours(hoursDelta));
            setCurrentTime(newTime);
            return newTime;
        }

        /**
         * Rewinds the {@linkplain #getCurrentTime() "current time"} backward
         * by the passed amount of hours.
         */
        @CanIgnoreReturnValue
        public synchronized Timestamp backward(long hoursDelta) {
            checkPositive(hoursDelta);
            final Timestamp newTime = subtract(this.currentTime, hours(hoursDelta));
            setCurrentTime(newTime);
            return newTime;
        }
    }

    /**
     * Utility class for working with timestamps in the past.
     */
    public static class Past {

        private Past() {
            // Do not allow creating instances of this utility class.
        }

        /**
         * The testing assistance utility, which returns a timestamp of the moment
         * of the passed number of minutes from now.
         *
         * @param value a positive number of minutes
         * @return a timestamp instance
         */
        public static Timestamp minutesAgo(int value) {
            checkPositive(value);
            final Timestamp currentTime = getCurrentTime();
            final Timestamp result = subtract(currentTime, Durations2.fromMinutes(value));
            return result;
        }

        /**
         * Obtains timestamp in the past a number of seconds ago.
         *
         * @param value a positive number of seconds
         * @return the moment `value` seconds ago
         */
        public static Timestamp secondsAgo(long value) {
            checkPositive(value);
            final Timestamp currentTime = getCurrentTime();
            final Timestamp result = subtract(currentTime, seconds(value));
            return result;
        }
    }

    /**
     * Utility class for working with timestamps of the the future.
     */
    public static class Future {

        private Future() {
            // Do not allow creating instances of this utility class.
        }

        /**
         * Obtains timestamp in the future a number of seconds from current time.
         *
         * @param seconds a positive number of seconds
         * @return the moment which is {@code seconds} from now
         */
        public static Timestamp secondsFromNow(long seconds) {
            checkPositive(seconds);
            final Timestamp currentTime = getCurrentTime();
            final Timestamp result = add(currentTime, fromSeconds(seconds));
            return result;
        }

        /**
         * Verifies if the passed timestamp is in the future comparing it
         * with {@linkplain Time#systemTime() system time}.
         */
        public static boolean isFuture(Timestamp timestamp) {
            // Do not use `getCurrentTime()` as we may use custom `TimestampProvider` already.
            // Get time from metal.
            final Timestamp currentSystemTime = systemTime();

            // NOTE: we have the risk of having these two timestamps too close to each other
            // so that the passed timestamp becomes "the past" around the time of this call.
            // To avoid this, select some time in the "distant" future.
            final boolean result = Timestamps.comparator()
                                             .compare(currentSystemTime, timestamp) < 0;
            return result;
        }
    }
}
