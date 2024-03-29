package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GROUP_LAB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GROUP_TUTORIAL;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MAJOR_BOB;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.group.Group;
import seedu.address.model.group.exceptions.DuplicateGroupException;
import seedu.address.model.group.exceptions.GroupNotFoundException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.testutil.PersonBuilder;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withMajor(VALID_MAJOR_BOB).withGroups(VALID_GROUP_LAB)
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        List<Group> newGroups = Arrays.asList(new Group(VALID_GROUP_TUTORIAL), new Group(VALID_GROUP_LAB));
        AddressBookStub newData = new AddressBookStub(newPersons, newGroups);

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withMajor(VALID_MAJOR_BOB).withGroups(VALID_GROUP_LAB)
                .build();
        assertTrue(addressBook.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getPersonList().remove(0));
    }

    @Test
    public void resetData_withDuplicateGroups_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        List<Person> newPersons = Arrays.asList(ALICE);
        List<Group> newGroups = Arrays.asList(new Group(VALID_GROUP_TUTORIAL), new Group(VALID_GROUP_TUTORIAL));
        AddressBookStub newData = new AddressBookStub(newPersons, newGroups);

        assertThrows(DuplicateGroupException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasGroup_nullGroup_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasGroup(null));
    }

    @Test
    public void hasGroup_groupNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasGroup(new Group(VALID_GROUP_LAB)));
    }

    @Test
    public void hasGroup_groupInAddressBook_returnsTrue() {
        addressBook.addGroup(new Group(VALID_GROUP_LAB));
        assertTrue(addressBook.hasGroup(new Group(VALID_GROUP_LAB)));
    }

    @Test
    public void hasGroup_groupWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addGroup(new Group(VALID_GROUP_LAB));
        assertTrue(addressBook.hasGroup(new Group(VALID_GROUP_LAB)));
    }

    @Test
    public void getGroupList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getGroupList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName() + "{persons=" + addressBook.getPersonList()
                + ", groups=" + addressBook.getGroupList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    @Test
    public void hashCode_equalAddressBooks_sameHashCode() {
        // Create two identical address books
        AddressBook addressBook1 = new AddressBook();
        AddressBook addressBook2 = new AddressBook();

        // Add a person to the first address book
        Person person1 = new PersonBuilder().withName("Alice").withGroups("TUT04").build();
        addressBook1.addPerson(person1);

        // Add a person to the second address book
        Person person2 = new PersonBuilder().withName("Alice").withGroups("TUT04").build();
        addressBook2.addPerson(person2);

        // Check that the hash codes are equal
        assertEquals(addressBook1.hashCode(), addressBook2.hashCode());
    }

    @Test
    public void hashCode_differentAddressBooks_differentHashCodes() {
        // Create two address books with different contents
        AddressBook addressBook1 = new AddressBook();
        AddressBook addressBook2 = new AddressBook();

        // Add a person to the first address book
        Person person1 = new PersonBuilder().withName("Alice").withGroups("TUT05").build();
        addressBook1.addPerson(person1);

        // Add a person to the second address book
        Person person2 = new PersonBuilder().withName("Bob").withGroups("LAB08").build();
        addressBook2.addPerson(person2);

        // Check that the hash codes are different
        assertEquals(addressBook1.hashCode() != addressBook2.hashCode(), true);
    }

    @Test
    public void removeGroup_existingGroup_success() {
        // Create an address book with a group
        AddressBook addressBook = new AddressBook();
        Group groupToRemove = new Group("LAB08");
        addressBook.addGroup(groupToRemove);

        // Remove the group
        addressBook.removeGroup(groupToRemove);

        // Check that the group is no longer in the address book
        assertFalse(addressBook.getGroupList().contains(groupToRemove));
    }

    @Test
    public void removeGroup_nonExistingGroup_noChange() {
        // Create an address book with a group
        AddressBook addressBook = new AddressBook();
        Group existingGroup = new Group("LAB05");
        addressBook.addGroup(existingGroup);

        // Create a group that does not exist in the address book
        Group nonExistingGroup = new Group("LAB06");

        // Throws GroupNotFoundException when the non-existing group is removed
        assertThrows(GroupNotFoundException.class, () -> addressBook.removeGroup(nonExistingGroup));
    }

    @Test
    public void setGroup_existingGroup_success() {
        // Create an address book with an existing group
        AddressBook addressBook = new AddressBook();
        Group existingGroup = new Group("TUT04");
        addressBook.addGroup(existingGroup);

        // Create a new group
        Group newGroup = new Group("TUT01");

        // Replace the existing group with the new group
        addressBook.setGroup(existingGroup, newGroup);

        // Check that the new group is in the address book
        assertFalse(addressBook.getGroupList().contains(existingGroup));
        assertTrue(addressBook.getGroupList().contains(newGroup));
    }

    @Test
    public void setGroup_nonExistingGroup_noChange() {
        // Create an address book with an existing group
        AddressBook addressBook = new AddressBook();
        Group existingGroup = new Group("TUT04");
        addressBook.addGroup(existingGroup);

        // Create a new group
        Group nonExistingGroup = new Group("TUT01");

        // Throws GroupNotFoundException when the non-existing group is replaced
        assertThrows(GroupNotFoundException.class, () -> addressBook.setGroup(nonExistingGroup, existingGroup));
    }

    @Test
    public void equals_sameInstance_returnsTrue() {
        AddressBook addressBook = new AddressBook();
        assertTrue(addressBook.equals(addressBook));
    }

    @Test
    public void equals_null_returnsFalse() {
        AddressBook addressBook = new AddressBook();
        assertFalse(addressBook.equals(null));
    }

    @Test
    public void equals_differentClass_returnsFalse() {
        AddressBook addressBook = new AddressBook();
        assertFalse(addressBook.equals("Not an AddressBook instance"));
    }

    @Test
    public void equals_differentPersons_returnsFalse() {
        AddressBook addressBook1 = new AddressBook();
        AddressBook addressBook2 = new AddressBook();

        // Add a person to the first address book
        Person person1 = new PersonBuilder().withName("Alice").withGroups("TUT05").build();
        addressBook1.addPerson(person1);

        // Add a person to the second address book
        Person person2 = new PersonBuilder().withName("Bob").withGroups("LAB08").build();
        addressBook2.addPerson(person2);
        assertFalse(addressBook1.equals(addressBook2));
    }

    @Test
    public void equals_differentGroups_returnsFalse() {
        AddressBook addressBook1 = new AddressBook();
        AddressBook addressBook2 = new AddressBook();
        addressBook1.addGroup(new Group("TUT01"));
        addressBook2.addGroup(new Group("LAB01"));
        assertFalse(addressBook1.equals(addressBook2));
    }

    @Test
    public void equals_sameContent_returnsTrue() {
        AddressBook addressBook1 = new AddressBook();
        AddressBook addressBook2 = new AddressBook();
        List<Person> persons = new ArrayList<>();
        List<Group> groups = new ArrayList<>();
        Person person = new PersonBuilder().withName("Alice").withGroups("TUT05").build();
        addressBook1.addPerson(person);
        groups.add(new Group("TUT05"));
        addressBook1.setPersons(persons);
        addressBook2.setPersons(persons);
        addressBook1.setGroups(groups);
        addressBook2.setGroups(groups);
        assertTrue(addressBook1.equals(addressBook2));
    }

    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<Group> groups = FXCollections.observableArrayList();

        AddressBookStub(Collection<Person> persons, Collection<Group> groups) {
            this.persons.setAll(persons);
            this.groups.setAll(groups);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<Group> getGroupList() {
            return groups;
        }

    }

}
