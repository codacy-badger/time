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

package io.spine.time.string;

import com.google.protobuf.Duration;
import com.google.protobuf.util.Durations;
import io.spine.string.Stringifier;

import java.io.Serializable;
import java.text.ParseException;

import static io.spine.util.Exceptions.illegalArgumentWithCauseOf;

/**
 * The default stringifier for {@code Duration}s.
 *
 * @author Alexander Yevsyukov
 */
final class DurationStringifier extends Stringifier<Duration> implements Serializable {

    private static final long serialVersionUID = 0L;
    private static final DurationStringifier INSTANCE = new DurationStringifier();

    static DurationStringifier getInstance() {
        return INSTANCE;
    }

    @Override
    protected String toString(Duration duration) {
        final String result = Durations.toString(duration);
        return result;
    }

    @Override
    protected Duration fromString(String str) {
        final Duration result;
        try {
            result = Durations.parse(str);
        } catch (ParseException e) {
            throw illegalArgumentWithCauseOf(e);
        }
        return result;
    }

    @Override
    public String toString() {
        return "TimeStringifiers.forDuration()";
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
