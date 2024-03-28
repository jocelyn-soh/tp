package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;

public class MailWindow extends UiPart<Stage> {

    private static final String FXML = "MailWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label mailLinkLabel;

    /**
     * Creates a new MailWindow.
     *
     * @param root Stage to use as the root of the MailWindow.
     */
    public MailWindow(Stage root, String mailtoLink) {
        super(FXML, root);
        mailLinkLabel.setText(mailtoLink);
    }

    public void show() {
        getRoot().show();
    }

    /**
     * Copies the mailto link to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(mailLinkLabel.getText());
        clipboard.setContent(content);
    }
}
