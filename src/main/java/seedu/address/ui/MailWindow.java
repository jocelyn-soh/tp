package seedu.address.ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller for a mail page
 */
public class MailWindow extends UiPart<Stage> {

    private static final String FXML = "MailWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label mailLinkLabel;

    private String mailtoLink;


    /**
     * Creates a new MailWindow.
     *
     * @param root Stage to use as the root of the MailWindow.
     */
    public MailWindow(Stage root) {
        super(FXML, root);
        setMailLinkLabel("To email this tutorial group: ");
    }

    /**
     * Creates a new HelpWindow.
     */
    public MailWindow() {
        this(new Stage());
    }

    public void setMailtoLink(String mailtoLink) {
        this.mailtoLink = mailtoLink;
    }

    public void setMailLinkLabel(String mailtoLink) {
        mailLinkLabel.setText(mailtoLink);
    }

    /**
     * Shows the help window.
     */
    public void show() {
        getRoot().show();
    }

    /**
     * Focuses the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Copies the mailto link to the clipboard.
     */
    @FXML
    private void openMailtoLink() {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().mail(new URI(this.mailtoLink));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}
