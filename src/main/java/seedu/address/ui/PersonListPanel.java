package seedu.address.ui;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.group.Group;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML
    private ListView<Person> personListView;
    @FXML
    private TabPane tabPane; // Inject the TabPane from FXML

    private ObservableList<Person> personList;
    private ObservableList<Group> groupList; // Observable list of groups


    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(ObservableList<Person> personList, ObservableList<Group> groupList) {
        super(FXML);
        this.personList = personList;
        this.groupList = groupList;
        initializeTabs(); // Initialize tabs after FXML is loaded

        // Add listener to personList
        personList.addListener((ListChangeListener<Person>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    updateTabs(); // Update tabs if new persons were added
                    break; // Only update tabs once for each change
                }
            }
        });
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonCard(person, getIndex() + 1).getRoot());
            }
        }
    }

    /**
     * Creates a tab to display all persons.
     */
    private void createAllTab() {
        Tab allTab = new Tab("All");
        ListView<Person> allListView = new ListView<>();
        allListView.setItems(personList);
        allListView.setCellFactory(listView -> new PersonListViewCell());
        allTab.setContent(allListView);
        tabPane.getTabs().add(allTab);
    }

    /**
     * Creates a tab for each group and display all persons in that group.
     * @param groups
     */
    private void createEachGroupTab(Set<Group> groups) {
        for (Group group : groups) {
            Tab tab = new Tab(group.groupName);
            ListView<Person> groupListView = new ListView<>();
            tab.setContent(groupListView);

            // Filter persons based on the group and set them in the ListView
            groupListView.setItems(personList.filtered(person -> person.getGroups().contains(group)));
            groupListView.setCellFactory(listView -> new PersonListViewCell());
            tabPane.getTabs().add(tab);
        }
    }
    private void initializeTabs() {
        if (tabPane == null) {
            throw new AssertionError("TabPane is not injected.");
        }

        // Clear existing tabs
        tabPane.getTabs().clear();

        // Creates a tab for displaying all persons
        createAllTab();

        // Get the set of all unique groups from the person list
        Set<Group> groups = new HashSet<>();
        for (Person person : personList) {
            groups.addAll(person.getGroups());
        }

        createEachGroupTab(groups);
    }

    private void updateTabs() {
        // Clear existing tabs
        tabPane.getTabs().clear();

        // Reinitialize tabs with updated group list
        initializeTabs();
    }
}
