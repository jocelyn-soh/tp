package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.group.Group;

/**
 * Jackson-friendly version of {@link Group}.
 */
class JsonAdaptedGroup {

    private final String groupName;
    private final String telegramLink;
    public final List<String> attendance;

    /**
     * Constructs a {@code JsonAdaptedGroup} with the given {@code groupName}.
     */
    @JsonCreator
    public JsonAdaptedGroup(@JsonProperty("groupName") String groupName, @JsonProperty("telegramLink") String link,
        @JsonProperty("attendance") List<String> attendance) {
        this.groupName = groupName;
        this.telegramLink = link;
        this.attendance = attendance;
    }

    /**
     * Constructs a {@code JsonAdaptedGroup} with the given {@code groupName}.
     */
    public JsonAdaptedGroup(String groupName) {
        this.groupName = groupName;
        this.telegramLink = "";
        List<String> emptyList = new ArrayList<>();
        this.attendance = emptyList;
    }

    /**
     * Converts a given {@code Group} into this class for Jackson use.
     */
    public JsonAdaptedGroup(Group source) {
        groupName = source.groupName;
        telegramLink = source.telegramLink;
        List<String> emptyList = new ArrayList<>();
        this.attendance = emptyList;
    }

    public String getGroupName() {
        return groupName;
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
        return new Group(groupName);
    }

}
