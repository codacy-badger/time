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

import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;
import io.spine.string.Stringifier;
import io.spine.string.Stringifiers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.base.Time.getCurrentTime;
import static io.spine.time.string.TimeStringifiers.forTimestampWebSafe;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Alexander Yevsyukov
 */
@DisplayName("WebSafeTimestampStringifier should")
class WebSafeTimestampStringifierTest extends AbstractStringifierTest<Timestamp> {

    WebSafeTimestampStringifierTest() {
        super(forTimestampWebSafe());
    }

    @Override
    protected Timestamp createObject() {
        return getCurrentTime();
    }

    @Test
    @DisplayName("Throw IllegalArgumentException when parsing unsupported format")
    void parsingError() {
        Stringifier<Timestamp> webSafeStringifier = getStringifier();
        String webSafe = webSafeStringifier.convert(getCurrentTime());
        String corrupt = "XX" + webSafe.substring(2);
        assertThrows(
                IllegalArgumentException.class,
                () -> webSafeStringifier.reverse()
                                        .convert(corrupt)
        );
    }
}
