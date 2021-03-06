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

package io.spine.time;

import com.google.protobuf.Duration;
import com.google.protobuf.Timestamp;
import io.spine.annotation.Experimental;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.protobuf.util.Timestamps.compare;
import static io.spine.time.Timestamps2.isLaterThan;
import static java.lang.Math.abs;

/**
 * A utility class for working with {@link Interval}s.
 *
 * @author Alexander Litus
 */
@Experimental
public final class Intervals {

    /** Prevent instantiation of this utility class. */
    private Intervals() {
    }

    /**
     * Returns an interval between two timestamps.
     *
     * @param start the first point in time
     * @param end   the second point in time
     * @return an interval between {@code start} and {@code end}
     * @throws IllegalArgumentException if the {@code end} is before the {@code start}
     */
    public static Interval between(Timestamp start, Timestamp end) {
        checkArgument(isLaterThan(end, /*than*/ start),
                      "The end must be after the start of the interval.");
        Interval.Builder interval = Interval
                .newBuilder()
                .setStart(start)
                .setEnd(end);
        return interval.build();
    }

    /**
     * Verifies if the timestamp is withing the interval.
     *
     * <p>The interval is closed, which means that the method would return {@code true}
     * if {@code interval.start <= timestamp <= interval.end}.
     *
     * @return {@code true} if the timestamp is withing the interval, {@code false} otherwise
     */
    public static boolean contains(Interval interval, Timestamp timestamp) {
        boolean isAfterOrOnStart = compare(interval.getStart(), timestamp) <= 0;
        boolean isBeforeOrOnEnd = compare(timestamp, interval.getEnd()) <= 0;
        return isAfterOrOnStart && isBeforeOrOnEnd;
    }

    /**
     * Returns a duration of the interval.
     *
     * @param interval the interval to calculate its duration
     * @return the duration between the start and the end of the interval
     */
    public static Duration toDuration(Interval interval) {
        Timestamp start = interval.getStart();
        Timestamp end = interval.getEnd();
        if (start.equals(end)) {
            return Durations2.ZERO;
        }
        long secondsBetween = end.getSeconds() - start.getSeconds();
        int nanosBetween = end.getNanos() - start.getNanos();
        Duration.Builder duration = Duration
                .newBuilder()
                .setSeconds(abs(secondsBetween))
                .setNanos(abs(nanosBetween));
        return duration.build();
    }
}
