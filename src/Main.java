import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import controller.GameController;
import db.DatabaseManager;
import db.Schema;
import model.GameState;
import view.GuiView;
import view.UiTheme;

public class Main {
        public static void main(final String[] theArgs) {
        Schema.initialize();
        DatabaseManager.connect();
        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseManager::disconnect));

        applyTheme();

        SwingUtilities.invokeLater(() -> {
            GuiView view = new GuiView();
            GameController controller = new GameController(new GameState(8, 8), view);

            view.setController(controller);
            controller.newGame(8, 8);
            view.setVisible(true);
            view.showWelcome();
        });
    }

    /**
     * Installs the Nimbus look-and-feel and overrides its core colors with
     * the {@link UiTheme} light palette so standard Swing widgets (menus,
     * text fields, radio buttons, dialogs) match the custom-painted panels.
     */
    private static void applyTheme() {
        try {
            for (final UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            UIManager.put("control", UiTheme.BG);
            UIManager.put("background", UiTheme.BG);
            UIManager.put("nimbusBase", UiTheme.PRIMARY);
            UIManager.put("nimbusBlueGrey", UiTheme.BORDER);
            UIManager.put("nimbusLightBackground", UiTheme.SURFACE);
            UIManager.put("nimbusFocus", UiTheme.PRIMARY);
            UIManager.put("nimbusSelectionBackground", UiTheme.PRIMARY);
            UIManager.put("text", UiTheme.TEXT);
            UIManager.put("menuText", UiTheme.TEXT);
            UIManager.put("infoText", UiTheme.TEXT);
            UIManager.put("controlText", UiTheme.TEXT);
        } catch (final Exception theException) {
            // Fall back to the default look-and-feel; the app still runs.
            System.err.println("Could not apply Nimbus theme: " + theException.getMessage());
        }
    }
}
