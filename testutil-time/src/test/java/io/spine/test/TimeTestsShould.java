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

package io.spine.test;

import com.google.common.testing.NullPointerTester;
import com.google.protobuf.Timestamp;
import io.spine.base.Time;
import org.junit.Assert;
import org.junit.Test;

import static com.google.protobuf.util.Timestamps.subtract;
import static io.spine.base.Time.getCurrentTime;
import static io.spine.time.Durations2.fromMinutes;
import static org.junit.Assert.assertEquals;

/**
 * @author Alexander Yevsyukov
 */
public class TimeTestsShould {

    @Test
    public void have_private_utility_ctor() {
        Tests.assertHasPrivateParameterlessCtor(TimeTests.class);
        Tests.assertHasPrivateParameterlessCtor(TimeTests.Future.class);
        Tests.assertHasPrivateParameterlessCtor(TimeTests.Past.class);
    }

    @Test
    public void pass_null_tolerance_check() {
        new NullPointerTester()
                .setDefault(Timestamp.class, getCurrentTime())
                .testAllPublicStaticMethods(TimeTests.class);
    }

    @Test
    public void have_frozen_time_provider() {
        final Timestamp fiveMinutesAgo = subtract(getCurrentTime(),
                                                  fromMinutes(5));

        final Time.Provider provider =
                new TimeTests.FrozenMadHatterParty(fiveMinutesAgo);

        assertEquals(fiveMinutesAgo, provider.getCurrentTime());
    }

    @Test
    public void return_current_time_in_seconds() {
        Assert.assertNotEquals(0, TimeTests.currentTimeSeconds());
    }
}
