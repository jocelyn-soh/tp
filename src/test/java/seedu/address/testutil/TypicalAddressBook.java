package seedu.address.testutil;

import seedu.address.model.AddressBook;
import seedu.address.model.group.Group;
import seedu.address.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalAddressBook {
    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : TypicalPersons.getTypicalPersons()) {
            ab.addPerson(person);
        }

        for (Group group : TypicalGroups.getTypicalGroups()) {
            ab.addGroup(group);
        }
        return ab;
    }

}
