/*
 * Copyright 2017, TeamDev Ltd. All rights reserved.
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

package io.spine.util;

import io.spine.annotation.Internal;
import io.spine.base.Error;

import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Throwables.getRootCause;
import static com.google.common.base.Throwables.getStackTraceAsString;
import static java.lang.String.format;

/**
 * Utility class for working with exceptions for cases that are not
 * covered by {@link com.google.common.base.Throwables Throwables} class from Guava.
 *
 * @author Alexander Yevsyukov
 */
public final class Exceptions {

    /** Prevent instantiation of this utility class. */
    private Exceptions() {
    }

    /**
     * Always throws {@code UnsupportedOperationException} initialized with the passed string.
     *
     * <p>Use this method in combination with static import for brevity of code for
     * unsupported operations.
     * The return type is given to keep Java type system happy when called in methods with
     * return type as shown below:
     *
     * <pre>
     *  {@code
     *   import static io.spine.util.Exceptions.unsupported;
     *   ...
     *   T doSomething() {
     *      throw unsupported("Cannot do this");
     *   }
     * }</pre>
     *
     * @param message a message for exception
     * @return nothing ever
     * @throws UnsupportedOperationException always
     */
    public static UnsupportedOperationException unsupported(String message) {
        checkNotNull(message);
        throw new UnsupportedOperationException(message);
    }

    /**
     * Always throws {@code UnsupportedOperationException} initialized with the formatted string.
     *
     * <p>Use this method in combination with static import for brevity of code for
     * unsupported operations.
     * The return type is given to keep Java type system happy when called in methods with
     * return type as shown below:
     *
     * <pre>
     *  {@code
     *
     *   import static io.spine.util.Exceptions.unsupported;
     *   ...
     *   T doSomething() {
     *      throw unsupported("This operation is not supported because of %s and %s", arg1, arg2);
     *   }
     * }</pre>
     *
     * @param format the format string
     * @param args   formatting parameters
     * @return nothing ever
     * @throws UnsupportedOperationException always
     */
    public static UnsupportedOperationException unsupported(String format, Object... args) {
        checkNotNull(format);
        checkNotNull(args);
        final String msg = formatMessage(format, args);
        return unsupported(msg);
    }

    /**
     * Always throws {@code UnsupportedOperationException}.
     *
     * <p>Use this method in combination with static import for brevity of code for
     * unsupported operations.
     * The return type is given to keep Java type system happy when called in methods with
     * return type as shown below:
     *
     * <pre>
     *   import static static io.spine.util.Exceptions.unsupported;
     *   ...
     *   T doSomething() {
     *      throw unsupported();
     *   }
     * </pre>
     *
     * @return nothing ever
     * @throws UnsupportedOperationException always
     */
    @SuppressWarnings("NewExceptionWithoutArguments") // No message necessary for this case.
    public static UnsupportedOperationException unsupported() {
        throw new UnsupportedOperationException();
    }

    /**
     * Sets a throwable's cause as the cause of a {@link IllegalStateException} and throws it.
     *
     * @param throwable to wrap
     * @return nothing ever, always throws an exception, the return type is for convenience
     * @throws IllegalStateException always
     */
    public static IllegalStateException illegalStateWithCauseOf(Throwable throwable) {
        checkNotNull(throwable);
        final Throwable rootCause = getRootCause(throwable);
        throw new IllegalStateException(rootCause);
    }

    /**
     * Sets a throwable's cause as the cause of a {@link IllegalArgumentException} and throws it.
     *
     * @param throwable to wrap
     * @return nothing ever, always throws an exception, the return type is for convenience
     * @throws IllegalArgumentException always
     */
    public static IllegalArgumentException illegalArgumentWithCauseOf(Throwable throwable) {
        checkNotNull(throwable);
        final Throwable rootCause = getRootCause(throwable);
        throw new IllegalArgumentException(rootCause);
    }

    private static String formatMessage(String format, Object[] args) {
        checkNotNull(format);
        checkNotNull(args);
        return format(Locale.ROOT, format, args);
    }

    /**
     * Throws {@code IllegalArgumentException} with the formatted string.
     *
     * @param format the format string
     * @param args   formatting parameters
     * @return nothing ever, always throws an exception. The return type is given for convenience.
     * @throws IllegalArgumentException always
     */
    public static IllegalArgumentException newIllegalArgumentException(String format,
                                                                       Object... args) {
        final String errMsg = formatMessage(format, args);
        throw new IllegalArgumentException(errMsg);
    }

    /**
     * Throws {@code IllegalArgumentException} with the formatted string and the cause.
     *
     * @param cause the cause of the exception
     * @param format the format string
     * @param args formatting parameters
     * @return nothing ever, always throws an exception. The return type is given for convenience.
     * @throws IllegalArgumentException always
     */
    public static IllegalArgumentException newIllegalArgumentException(Throwable cause,
                                                                       String format,
                                                                       Object... args) {
        checkNotNull(cause);
        final String errMsg = formatMessage(format, args);
        throw new IllegalArgumentException(errMsg, cause);
    }

    /**
     * Throws {@code IllegalStateException} with the formatted string.
     *
     * @param format the format string
     * @param args formatting parameters
     * @return nothing ever, always throws an exception. The return type is given for convenience.
     * @throws IllegalStateException always
     */
    public static IllegalStateException newIllegalStateException(String format,
                                                                 Object... args) {
        final String errMsg = formatMessage(format, args);
        throw new IllegalStateException(errMsg);
    }

    /**
     * Throws {@code IllegalStateException} with the formatted string and the cause.
     *
     * @param cause the cause of the exception
     * @param format the format string
     * @param args formatting parameters
     * @return nothing ever, always throws an exception. The return type is given for convenience.
     * @throws IllegalStateException always
     */
    public static IllegalStateException newIllegalStateException(Throwable cause,
                                                                 String format,
                                                                 Object... args) {
        checkNotNull(cause);
        final String errMsg = formatMessage(format, args);
        throw new IllegalStateException(errMsg, cause);
    }

    /**
     * Creates a Protobuf error from the given {@link Throwable}.
     *
     * @param throwable the {@code Throwable} to convert
     * @return new instance of {@link Error}
     */
    public static Error toError(Throwable throwable) {
        checkNotNull(throwable);
        final Throwable cause = getRootCause(throwable);
        final Error error = Error.newBuilder()
                                 .setType(cause.getClass().getCanonicalName())
                                 .setMessage(cause.getMessage())
                                 .setStacktrace(getStackTraceAsString(cause))
                                 .build();
        return error;
    }

    /**
     * Creates a Protobuf error from the given {@link Throwable} with the given error code.
     *
     * <p>The error code may represent a number in an enum or a native error number (ERRNO).
     *
     * @param throwable the {@code Throwable} to convert
     * @param errorCode the error code to include in the resulting {@link Error}
     * @return new instance of {@link Error}
     * @see #toError(Throwable) as the recommended overload
     */
    @Internal
    public static Error toError(Throwable throwable, int errorCode) {
        final Error error = toError(throwable).toBuilder()
                                              .setCode(errorCode)
                                              .build();
        return error;
    }
}
