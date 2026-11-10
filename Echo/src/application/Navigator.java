package application;

import java.io.IOException;
import java.io.UncheckedIOException;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Switches the window to another screen. Every controller uses this. */
public final class Navigator {

    private Navigator() {
    }

    /** Switches screens from an event handler (button click, mouse click, ...). */
    public static <T> T show(Event event, String fxmlFile) {
        return show((Node) event.getSource(), fxmlFile);
    }

    /** Switches screens from any node that is currently on screen. Returns the new screen's controller. */
    public static <T> T show(Node nodeOnScreen, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(Navigator.class.getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) nodeOnScreen.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            return loader.getController();
        } catch (IOException e) {
            throw new UncheckedIOException("Could not load " + fxmlFile, e);
        }
    }
}