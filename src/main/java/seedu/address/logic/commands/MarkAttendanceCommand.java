package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ATTENDANCE;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.group.Group;
import seedu.address.model.person.Person;

/**
 * Marks attendance for a specified person in a group.
 */
public class MarkAttendanceCommand extends Command {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks attendance for a specified person in a group.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_GROUP + "GROUP_NAME "
            + PREFIX_WEEK + "WEEK_NUMBER (week 1 to week 13)"
            + PREFIX_ATTENDANCE + "ABSENT_OR_PRESENT (A for absent P for present)"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_GROUP + "TUT04 "
            + PREFIX_WEEK + "3 "
            + PREFIX_ATTENDANCE + "A ";

    public static final String MESSAGE_SUCCESS = "Attendance marked";
    public static final String MESSAGE_WEEK_NUMBER_INVALID = "Week number is wrong (between 1 and 13)";
    public static final String MESSAGE_ATTENDANCE_INVALID = "Attendance format is wrong (A for absent P for present)";
    public static final String MESSAGE_GROUP_NOT_FOUND = "Group is not found in the person";

    private final Index index;
    private final Group group;
    private final int week;
    private final String attendance;

    public MarkAttendanceCommand(Index index, Group group, int week, String attendance) {
        requireNonNull(index);
        requireNonNull(group);
        requireNonNull(week);
        requireNonNull(attendance);
        
        this.index = index;
        this.group = group;
        this.week = week;
        this.attendance = attendance;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();

        //check if the person index is valid
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        //check if it is valid group
        if (!personToEdit.hasGroup(group)) {
            throw new CommandException(MESSAGE_GROUP_NOT_FOUND);
        }

        Group matchingGroup = null;
        for (Group personGroup : personToEdit.getGroups()) {
            if (personGroup.isSameGroup(group)) {
                matchingGroup = personGroup;
                break;
            }
        }

        // Week number is 1-based, so decrement it to access the correct index in the list
        if (week < 0 || week > 13) {
            throw new CommandException(MESSAGE_WEEK_NUMBER_INVALID);
        }

        // update the attendance of the attendance list
        matchingGroup.markAttendance(week, attendance);

        return new CommandResult(String.format(MESSAGE_SUCCESS));
    }
}