package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalGroups.LAB10;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.group.Group;
import seedu.address.model.person.Person;

public class MarkAttendanceCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullMarkAttendanceConstructor_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new MarkAttendanceCommand(null,
                LAB10, 1, null));
        assertThrows(NullPointerException.class, () -> new MarkAttendanceCommand(INDEX_FIRST_PERSON,
                null, 1, null));
    }

    @Test
    public void execute_attendanceIsMarked_markSuccessful() throws Exception {
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        firstPerson.getMatchingGroup(new Group("TUT04")).markAttendance(1, "A");

        MarkAttendanceCommand markCommand = new MarkAttendanceCommand(INDEX_FIRST_PERSON,
                new Group("TUT04"), 1, "A");

        assertCommandSuccess(markCommand, model, MarkAttendanceCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        Group tut01 = new Group("TUT01");
        Group lab01 = new Group("LAB01");
        MarkAttendanceCommand markAttendance1 = new MarkAttendanceCommand(INDEX_FIRST_PERSON,
                tut01, 1, "A");
        MarkAttendanceCommand markAttendance2 = new MarkAttendanceCommand(INDEX_FIRST_PERSON,
                lab01, 1, "A");

        // same object -> returns true
        assertTrue(markAttendance1.equals(markAttendance1));

        // same values -> returns true
        MarkAttendanceCommand markAttendance1Copy = new MarkAttendanceCommand(INDEX_FIRST_PERSON,
                tut01, 1, "A");
        assertTrue(markAttendance1.equals(markAttendance1Copy));

        // different types -> returns false
        assertFalse(markAttendance1.equals(1));

        // null -> returns false
        assertFalse(markAttendance1.equals(null));

        // different person -> returns false
        assertFalse(markAttendance1.equals(markAttendance2));
    }

    @Test
    public void toStringMethod() {
        AddGroupCommand addGroupCommand = new AddGroupCommand(LAB10);
        String expected = AddGroupCommand.class.getCanonicalName() + "{toAdd=" + LAB10 + "}";
        assertEquals(expected, addGroupCommand.toString());
    }

}
