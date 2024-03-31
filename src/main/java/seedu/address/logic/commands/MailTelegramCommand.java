package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupContainsKeywordsPredicate;
import seedu.address.model.person.Person;

/**
 * Directs users to the HTML website with email links to all the students in the current list.
 */
public class MailTelegramCommand extends Command {

    public static final String COMMAND_WORD = "mailtg";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": generates mailto link to students from "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " LAB10 TUT04";

    public static final String SHOW_MAILTO_LINK = "Showing the email window";

    private final GroupContainsKeywordsPredicate predicate;
    private final Group group;

    /**
     * Constructs a MailCommand with a predicate.
     */
    public MailTelegramCommand(Group group) {
        this.group = group;
        this.predicate = new GroupContainsKeywordsPredicate(Arrays.asList(group.groupName));
    }

    /**
     * Generates a mailto link consisting of emails of students filtered accordingly
     * Shows a pop-up window containing the mailto link
     */
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        ReadOnlyAddressBook addressBook = model.getAddressBook();
        List<Person> personList = addressBook.getPersonList().filtered(predicate);
        List<Group> groupList = addressBook.getGroupList();

        // Extract email addresses of filtered students
        List<String> emailList = personList.stream()
                .map(Person::getEmail)
                .filter(email -> !email.value.isEmpty())
                .map(email -> email.value)
                .collect(Collectors.toList());

        String telegramLink = "";
        for (Group curr : groupList) {
            if (curr.isSameGroup(this.group)) {
                telegramLink = curr.telegramLink;
                break;
            }
        }

        String mailtoLink = createMailtoUrl(String.join(";", emailList),
                String.format("Welcome to Group %s", group.groupName),
            String.format("Greetings,\n\nWelcome to Group %s.\n\n", group.groupName)
                    + String.format("Please join this Telegram group: %s.\n\n", telegramLink)
                    + String.format("Sent from TutorsContactPro."));

        System.out.println(mailtoLink);

        return new CommandResult(SHOW_MAILTO_LINK, mailtoLink);
    }

    /**
     * Generates a mailto link
     * @param recipient The recipient of the email
     * @param subject The subject of the email
     * @param body The body of the email
     * @return A mailto link
     */
    public String createMailtoUrl(String recipient, String subject, String body) {
        try {
            String uri = "mailto:" + recipient
                    + "?subject="
                    + URLEncoder.encode(subject, "UTF-8").replace("+", "%20")
                    + "&body=" + URLEncoder.encode(body, "UTF-8").replace("+", "%20");
            return uri;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create mailto URL", e);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MailTelegramCommand)) {
            return false;
        }

        MailTelegramCommand otherMailCommand = (MailTelegramCommand) other;
        return group.equals(otherMailCommand.group);
    }
}
