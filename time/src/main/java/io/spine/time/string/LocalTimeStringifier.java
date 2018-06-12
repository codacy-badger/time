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

import io.spine.string.Stringifier;
import io.spine.time.LocalTime;
import io.spine.time.LocalTimes;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.spine.util.Exceptions.illegalArgumentWithCauseOf;

/**
 * The default stringifier for {@link LocalTime} values.
 *
 * @author Alexander Yevsyukov
 */
final class LocalTimeStringifier extends Stringifier<LocalTime> implements Serializable {

    private static final long serialVersionUID = 0L;
    private static final LocalTimeStringifier INSTANCE = new LocalTimeStringifier();

    static LocalTimeStringifier getInstance() {
        return INSTANCE;
    }

    @Override
    protected String toString(LocalTime time) {
        checkNotNull(time);
        String result = LocalTimes.toString(time);
        return result;
    }

    @Override
    protected LocalTime fromString(String str) {
        checkNotNull(str);
        LocalTime time;
        try {
          time = LocalTimes.parse(str);
        } catch (RuntimeException e) {
            throw illegalArgumentWithCauseOf(e);
        }
        return time;
    }

    @Override
    public String toString() {
        return "TimeStringifiers.forLocalTime()";
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
