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
package io.spine.tools.java;

import com.google.protobuf.DescriptorProtos.FileDescriptorProto;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utilities for working with Protobuf when generating Java code.
 *
 * @author Alexander Yevsyukov
 */
public class JavaCode {

    private static final String UNDERSCORE = "_";

    /** Prevents instantiation of this utility class. */
    private JavaCode() {
    }

    /**
     * Transforms the string with a file name with underscores into a camel-case name.
     *
     * <p>The class name is calculated according to
     * <a href="https://developers.google.com/protocol-buffers/docs/reference/java-generated#invocation">Protobuf
     * compiler conventions</a>.
     */
    public static String toCamelCase(String fileName) {
        checkNotNull(fileName);
        final StringBuilder result = new StringBuilder(fileName.length());

        for (final String word : fileName.split(UNDERSCORE)) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)));
                result.append(word.substring(1)
                                  .toLowerCase());
            }
        }

        return result.toString();
    }

    /**
     * Transforms Protobuf-style field name into a corresponding Java-style field name.
     *
     * <p>For example, "seat_assignment_id" becomes "SeatAssignmentId"
     *
     * @param protoFieldName  Protobuf field name
     * @param capitalizeFirst indicates if the first letter should be capitalized
     * @return a field name
     */
    public static String toJavaFieldName(String protoFieldName, boolean capitalizeFirst) {
        checkNotNull(protoFieldName);
        final String[] words = protoFieldName.split(UNDERSCORE);
        final StringBuilder builder = new StringBuilder(words[0]);
        for (int i = 1; i < words.length; i++) {
            final String word = words[i];
            builder.append(Character.toUpperCase(word.charAt(0)))
                   .append(word.substring(1));
        }
        String resultName = builder.toString();
        if (capitalizeFirst) {
            resultName = Character.toUpperCase(resultName.charAt(0)) + resultName.substring(1);
        }
        return resultName;
    }

    /**
     * Obtains the {@link Path} to a folder, that contains
     * a generated file from the file descriptor.
     *
     * @param file the proto file descriptor
     * @return the relative folder path
     */
    public static Path getFolderPath(FileDescriptorProto file) {
        checkNotNull(file);
        final String javaPackage = file.getOptions()
                                       .getJavaPackage();
        final String packageDir = javaPackage.replace('.', File.separatorChar);
        return Paths.get(packageDir);
    }
}
