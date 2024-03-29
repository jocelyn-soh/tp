package seedu.address.storage;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.group.Group;

/**
 * Jackson-friendly version of {@link Group}.
 */
class JsonAdaptedPersonGroupAttendance {

    private final String groupName;
    private final List<String> attendance;
    /**
     * Constructs a {@code JsonAdaptedGroup} with the given {@code groupName}.
     */
    @JsonCreator
    public JsonAdaptedPersonGroupAttendance(@JsonProperty("groupName") String groupName,
                                            @JsonProperty("attendance") List<String> attendance) {
        this.groupName = groupName;
        this.attendance = attendance;
    }

    /**
     * Converts a given {@code Group} into this class for Jackson use.
     */
    public JsonAdaptedPersonGroupAttendance(Group source) {
        groupName = source.groupName;
        attendance = source.attendance;
    }

    /**
     * Converts this Jackson-friendly adapted group object into the model's {@code Group} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted group.
     */
    public Group toModelType() throws IllegalValueException {
        if (!Group.isValidGroupName(groupName)) {
            throw new IllegalValueException(Group.MESSAGE_CONSTRAINTS);
        }
        return new Group(groupName, attendance);
    }

}
