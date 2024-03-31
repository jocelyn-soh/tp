package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalAddressBook.getTypicalAddressBook;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class MailTelegramCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        Group group1 = new Group("TUT01");
        Group group2 = new Group("TUT02");

        MailTelegramCommand findFirstCommand = new MailTelegramCommand(group1);
        MailTelegramCommand findSecondCommand = new MailTelegramCommand(group2);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        MailTelegramCommand findFirstCommandCopy = new MailTelegramCommand(group1);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_withMultiplePredicate_success() {
        Model model = new ModelManager();
        List<Person> personList = Arrays.asList(
                new PersonBuilder().withName("Alice").withEmail("test1@example.com").build(),
                new PersonBuilder().withName("Bob").withEmail("test2@example.com").build()
        );
        model.addPerson(personList.get(0));
        model.addPerson(personList.get(1));

        GroupContainsKeywordsPredicate predicate = new GroupContainsKeywordsPredicate(Arrays.asList("TUT01"));
        Group group = new Group("TUT01");

        model.updateFilteredPersonList(predicate);

        MailTelegramCommand mailCommand = new MailTelegramCommand(group);
        CommandResult commandResult = mailCommand.execute(model);

        // Extract emails from personList filtered by the predicate
        List<String> emails = personList.stream()
                .filter(person -> predicate.test(person))
                .map(person -> person.getEmail().toString())
                .collect(Collectors.toList());

        assertEquals(MailTelegramCommand.SHOW_MAILTO_LINK, commandResult.getFeedbackToUser());
    }
}
