package io.spine.validate;

import com.google.common.collect.ImmutableList;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;
import io.spine.base.FieldPath;
import io.spine.test.validate.msg.InvalidMessage;
import io.spine.test.validate.msg.MessageWithRepeatedRequiredValidatedMessageField;
import io.spine.test.validate.msg.MessageWithRepeatedUnchekedMessageField;
import io.spine.test.validate.msg.MessageWithRepeatedValidatedMessageField;
import io.spine.test.validate.msg.MessegeWithRepeatedRequiredMessageField;

import java.util.List;

/**
 * @author Dmytro Dashenkov
 */
public class MessageFieldValidatorShould extends FieldValidatorShould<Message> {

    private static final FieldDescriptor UNCHECKED_FIELD_DESC =
            MessageWithRepeatedUnchekedMessageField.getDescriptor()
                                                   .getFields()
                                                   .get(0);

    private static final FieldDescriptor VALIDATED_FIELD_DESC =
            MessageWithRepeatedValidatedMessageField.getDescriptor()
                                                    .getFields()
                                                    .get(0);

    private static final FieldDescriptor REQUIRED_FIELD_DESC =
            MessegeWithRepeatedRequiredMessageField.getDescriptor()
                                                   .getFields()
                                                   .get(0);

    private static final FieldDescriptor VALIDATED_REQUIRED_FIELD_DESC =
            MessageWithRepeatedRequiredValidatedMessageField.getDescriptor()
                                                            .getFields()
                                                            .get(0);

    @Override
    protected MessageFieldValidator validatedRequiredRepeatedField(ImmutableList<Message> values) {
        return getValidator(VALIDATED_REQUIRED_FIELD_DESC, values);
    }

    @Override
    protected MessageFieldValidator requiredRepeatedField(ImmutableList<Message> values) {
        return getValidator(REQUIRED_FIELD_DESC, values);
    }

    @Override
    protected MessageFieldValidator validatedRepeatedField(ImmutableList<Message> values) {
        return getValidator(VALIDATED_FIELD_DESC, values);
    }

    @Override
    protected MessageFieldValidator uncheckedRepeatedField(ImmutableList<Message> values) {
        return getValidator(UNCHECKED_FIELD_DESC, values);
    }

    @Override
    protected InvalidMessage newValue() {
        return InvalidMessage.newBuilder()
                             .setS("some non-empty string")
                             .build();
    }

    @Override
    protected InvalidMessage defaultValue() {
        return InvalidMessage.getDefaultInstance();
    }

    private MessageFieldValidator getValidator(FieldDescriptor field,
                                               List<? extends Message> values) {
        return new MessageFieldValidator(field,
                                         values,
                                         FieldPath.getDefaultInstance(),
                                         false);
    }
}
