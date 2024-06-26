---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# TutorsContactsPro Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/major/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/major/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/major/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/major/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/major/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/major/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the major book data i.e., all `Person` and `Group` objects (which are contained in a `UniquePersonList` and `UniqueGroupList` object, respectively).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Group` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Group` object per unique group, instead of each `Person` needing their own `Group` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/major/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both major book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.


## Delete a student 

### About

The delete student feature allows TAs to delete an existing student information from the student contact list
using the command `delete INDEX`.

### How it is implemented

The `delete` command mechanism is facilitated by the `DeleteCommand` and the `DeleteCommandParser`.
It allows users to delete a student contact from the student contact list.
It uses the `AddressBook#removePerson(Person key)` which is exposed in the `Model`
interface as `Model#deletePerson(Person personToDelete)`. Then, the `remove(Person person)` is called on the `UniquePersonList`
in `AddressBook` to delete the student contact from the list. <br>

A modification from AB3 delete mechanism is that the `delete` command also involves the facilitation of the `AddressBook#deassignPerson(Person persontToDeassign, Group group)`
which is exposed in the `Model` interface as `Model#deassignPerson(Person person, Group group)`, which result in the call of `Group#deassign(Person person)` to
deassign the deleted student contact from all previously assigned groups.

#### Parsing input

1. The TA inputs the `delete` command.

2. The `TutorsContactsPro` then preliminary process the input and creates a new `DeleteCommandParser`.

3. The `DeleteCommandParser` then calls the `ParserUtil#parseIndex()` to check for the validity of the `INDEX`.
   At this stage, if the `INDEX is invalid or absent`, `ParseException` would be thrown.

4. The `DeleteCommandParser` then creates the `DeleteCommand` based on the processed input.


#### Command execution

5. The `LogicManager` executes the `DeleteCommand`.

6. The `DeleteCommand` calls the `Model#getFilteredPersonList()` to get the unmodifiable view of the filtered person list to get the target
   person to delete based on the provided `INDEX`. <br><br> At this stage, `CommandException` would be thrown if the input `INDEX`
   is invalid (i.e. `INDEX` exceeds the size of the student contact list).

7. The `DeleteCommand` the calls the `Model#deletePerson(Person personToDelete)` to delete the target student contact from the
   student contact list.


#### Displaying of result

8. Finally, the `DeleteCommand` creates a `CommandResult` with a success message and return it to the `LogicManager` to complete the command execution.
    The GUI would also be updated on this change in the student contact list accordingly.

The following sequence diagram shows how the `delete` mechanism works:

![](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The following activity diagram summarizes what happens when a user executes the `delete` command:

![](images/DeleteActivityDiagram.png)


## Filter Students by Group

#### About
The filter students by group feature allows TAs to filter and list students in the address book whose group name matches the specified keywords. 
It helps TAs to quickly find students belonging to specific groups.

#### How it is implemented
The filtering mechanism is facilitated by the `FilterCommand` and the `FilterCommandParser`. 
It enables TAs to filter students based on the groups they belong to. It utilizes the `Model` interface to interact with the student contact list.

#### Parsing input

1. The TA inputs the `filter` command along with the group keywords.

2. The TutorsContactsPro then preliminarily processes the input and creates a new `FilterCommandParser`.

3. The `FilterCommandParser` then checks for the validity of the provided group keywords. If the keywords are invalid or absent, a `ParseException` is thrown.

4. The `FilterCommandParser` creates the `FilterCommand` based on the processed input, with a `GroupContainsKeywordsPredicate` to filter students based on the specified groups.

#### Command execution

5. The `LogicManager` executes the `FilterCommand`.

6. The `FilterCommand` calls the `Model#updateFilteredPersonList()` method to update the filtered person list according to the specified group keywords.

#### Displaying results

7. Finally, the `FilterCommand` creates a CommandResult with the list of students matching the specified groups, and returns it to the LogicManager to complete the command execution. 
The GUI would also be updated accordingly to display the filtered list of students.

The following sequence diagram illustrates how the `filter` mechanism works:
![](images/FilterSequenceDiagram.png)


## Mail Command

### About

The Mail Command feature enables TAs to generate a mailto link containing the email addresses of students filtered based on specified keywords. 
This link can be used to compose emails to these students directly from the TA's default email client.

### How it is Implemented

The Mail Command feature is implemented using the `MailCommand` class and its corresponding parser, `MailCommandParser`.

#### Command Structure

The user inputs the `mail` command followed by optional keywords specifying groups of students they want to include in the email. 
If no keywords are provided, the mailto link will include all students in the current list.

#### Parsing Input

1. The `MailCommandParser` parses the input arguments to extract the specified keywords.

2. If no keywords are provided, an empty `MailCommand` is created, which results in the generation of a mailto link for all students.

3. If keywords are provided, the parser validates them to ensure they conform to the expected format. If any keyword is invalid, a `ParseException` is thrown.

4. The `MailCommand` with the appropriate predicate is then created using the `GroupContainsKeywordsPredicate`, which filters the students based on the specified keywords.

#### Command Execution

5. When the `MailCommand` is executed, it updates the filtered person list in the model based on the provided predicate.

6. It then extracts the email addresses of the filtered students from the model.

7. Using these email addresses, it generates a mailto link, concatenating them with semicolons to form a single string.

#### Displaying Result

8. Finally, the generated mailto link is encapsulated in a `CommandResult` object and returned to the logic manager for further handling.

### Summary

The Mail Command feature provides an efficient way for TAs to compose emails to specific groups of students directly from the application. By leveraging the power of filtering, it allows for targeted communication while maintaining simplicity and ease of use.

The following activity diagram illustrates how the `mail` mechanism works:
![](images/MailActivityDiagram.png)

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current major book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous major book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone major book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial major book state, and the `currentStatePointer` pointing to that single major book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the major book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the major book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted major book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified major book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the major book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous major book state, and restores the major book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the major book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest major book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the major book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all major book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire major book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* A computer science TA (Teaching Assistant) managing students for tutorials
* has numerous of students to manage in a tutorial slot
* has to add, list, delete, sort, search students in the app
* is reasonably comfortable using CLI apps
* can type fast
* can switch between different tutorial classes
* prefer desktop apps over other types


**Value proposition**: manage students faster than a typical GUI driven app


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​     | I want to …​                                | So that I can…​                                               |
|----------|-------------|---------------------------------------------|---------------------------------------------------------------|
| `* * *`  | TA          | add new students to the app                 | keep track of their information                               |
| `* * *`  | TA          | edit student profiles                       | keep their information up to date.                            |
| `* * *`  | TA          | delete students from my class               | track the existing number of students in my tutorial class    |
| `* * *`  | TA          | list all students in my class(es)           | view all of my students’ details at one glance                |
| `* * *`  | TA          | search for specific students using keywords | quickly find relevant information                             |
| `* * *`  | TA          | filter students according to their group    | quickly find relevant information                             |
| `* * *`  | TA          | add a new group                             | keep track of the groups that i teach                         |
| `* * *`  | TA          | edit an existing group                      | keep information of the groups i teach up to date             |
| `* * *`  | TA          | delete an existing group                    | track the existing number of groups that i currently teach    |
| `* * *`  | TA          | generate a mail link                        | conveniently sent an email to the student recipients desired  |
| `* * *`  | TA          | add a telegram link to each group           | keep track of the telegram groups for each group that i teach |
| `* *`    | new TA user | be able to access a help window             | easily seek help for the errors encountered                   |



### Use cases

(For all use cases below, the **System** is the `TutorsContactsPro` and the **Actor** is the `Tutor`, unless specified otherwise)

**Use case: UC01 - Add a student**

**MSS**

1.  TA requests to list students
2.  System shows a list of students
3.  TA requests to add a specific student to the list
4.  System adds the student

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The add command parameters are invalid or incomplete.

    * 3a1. TutorsContactsPro shows an error message.

      Use case resumes at step 2.

* 3b. TutorsContactsPro detects that the student already exists on the list.

    * 3b1. TutorsContactsPro informs the TA that the student already exists on the list.
    * 3b2. TA confirms cancellation of adding the student.
      
      Use case ends.
    


**Use case: UC02 - Edit a student**

**MSS**

1.  TA requests to list students
2.  System shows a list of students
3.  TA requests to edits the particulars of the student
4.  System records the changes

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. TutorsContactsPro shows an error message.

      Use case resumes at step 2.
  
* 3b. The edit command parameters are invalid or incomplete.

    * 3b1. TutorsContactsPro shows an error message.

      Use case resumes at step 2.


**Use case: UC03 - Delete a student**

**MSS**

1.  TA requests to list students
2.  System shows a list of students
3.  TA requests to delete a student
4.  System records the changes

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. TutorsContactsPro shows an error message.

      Use case resumes at step 2.


**Use case: UC04 - List all students**

**MSS**

1.  TA requests to list students
2.  System shows a list of students

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.


**Use case: UC05 - Find a student**

**MSS**

1.  TA requests to list students
2.  System shows a list of students
3.  TA finds student(s) by keyword
4.  System shows a list of students matching the keyword

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given keyword is in an incorrect format (e.g., contains special characters not allowed, exceeds maximum length).
  
  * 3a1. TutorsContactsPro shows an error message.
    Use case resumes at step 2.

* 4a. The list of search results is empty.

  Use case ends.


**Use case: UC06 - Filter students according to their group**

**MSS**

1.  TA requests to list students
2.  System shows a list of students
3.  TA filters student(s) by keyword which is the group name desired
4.  System shows a list of students that belong to the group 

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given keyword is in an incorrect format (e.g., contains special characters not allowed, exceeds maximum length, incorrect group name format).

    * 3a1. TutorsContactsPro shows an error message.
      Use case resumes at step 2.

* 4a. The list of search results is empty.

  Use case ends.

**Use case: UC07 - Add a group**

**MSS**

1. TA requests to add a specific group 

2. System adds the group

    Use case ends.

**Extensions**

* 1a. The addgroup command parameters are invalid or incomplete.

    * 1a1. TutorsContactsPro shows an error message.

      Use case resumes at step 2.

* 1b. TutorsContactsPro detects that the group already exists on the list.

    * 1b1. TutorsContactsPro informs the tutor that the group already exists on the list.
    * 1b2. Tutor confirms cancellation of adding the group.

      Use case ends.


**Use case: UC08 - Edit a group**

**MSS**

1. TA requests to edit the information of the group
2. System records the changes

    Use case ends.

**Extensions**

* 1a. The edit command group name parameter is invalid or incomplete.

    * 1a1. TutorsContactsPro shows an error message.

      Use case resumes at step 1.


**Use case: UC09 - Delete a group**

**MSS**

1. TA requests to delete a student
2. System records the changes

    Use case ends.

**Extensions**

* 1a. The given group name parameter is invalid or incomplete.

    * 1a1. TutorsContactsPro shows an error message.

      Use case resumes at step 1.


**Use case: UC010 - Generate mail link**

**MSS**

1.  TA requests generation of mail link
2.  System shows the mailto link containing emails of specific students recipients

    Use case ends.

**Extensions**

* 1a. The given group name parameter is invalid.

    Use case resumes at step 1.

**Use case: UC011 - Add a telegram link**

**MSS**

1.  TA requests to add a specific telegram link to a particular group
2.  System adds the telegram link to the group

    Use case ends.

**Extensions**

* 1a. The given telegram link is invalid or incomplete.

  Use case resumes at step 1.



### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
3. Should be able to list a maximum of 50 students within a single tutorial slot.
4. Should be able to hold a maximum of 300 students in total without any significant decrease in performance.
5. Any command should be visible within 3 seconds, ensuring a smooth and efficient user experience.
6. The system should have an uptime of at least 99%, allowing tutors to access student information reliably at any time.
7. Student important information (i.e name, email, telegram handle, contact number) should be encrypted both in transit and at rest to prevent unauthorized access.
8. The system should implement secure authentication mechanisms, such as multi-factor authentication, to verify the identity of users.
9. TAs should only have access to student information for classes they are assigned to, ensuring data privacy.
10. The system should be able to scale horizontally to accommodate an increase in the number of users and classes without compromising performance.
11. Regular backups of the system database should be performed, with a robust disaster recovery plan in place to restore data in case of any unexpected failures or outages.


### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **TA**: TA (Teaching Assistant) refers to the person who teaches in a single tutorial/recitation/lab group. 
* **Student**: Student refers to an individual who attends a tutorial class taught by the tutor.
* **Group**: Smaller classes in university which allow discussion of lecture content and assignment. This consists of tutorial, recitation and labs. 
* **CLI (Command-Line Interface)**: A text-based interface used to interact with the software by entering commands into a terminal or console window, typically preferred by users who prefer efficiency and automation.
* **GUI (Graphical User Interface)**: A GUI is a user interface that employs graphical elements such as icons, buttons, and menus for user interaction, providing an intuitive and visually appealing way to navigate and use software.



--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Listing all students

1. Listing all students 

    1. Test case: `list`<br>
       Expected: All students will be returned from the list. The number of students listed is shown in the status message. Timestamp in the status bar is updated.

### Deleting a student

1. Deleting a student while all students are being shown

   1. Prerequisites: List all students using the `list` command. There can be multiple students in the list.

   1. Test case: `delete 1`<br>
      Expected: First student is deleted from the list. Details of the deleted student is shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No student is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Finding student(s) by keyword(s)

1. Finding a student while all students are being shown

    1. Prerequisites: List all students using the `list` command. There can be multiple persons in the list. 

    1. Test case: `find Al Yu`<br>
       Expected: Students who have any part of their names starting with `Al` or `Yu` like `Alex Yeoh` and `Bernice Yu` will be returned from the list. The number of contacts found is shown in the status message. Timestamp in the status bar is updated.

    1. Test case: `find Zoe`, assuming that there is no contact who has any part of his/her name starting with `Zoe`<br>
       Expected: No student is found. Error details shown in the status message. Status bar remains the same.

    1. Other incorrect find commands to try: `find`<br>
       Expected: Similar to previous.

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
