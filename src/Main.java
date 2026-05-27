import controller.GameController;
import db.DatabaseManager;
import db.Schema;
import model.GameState;
import view.GuiView;

public class Main {
        public static void main(final String[] theArgs) {
        Schema.initialize();
        DatabaseManager.connect();
        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseManager::disconnect));

        GuiView view = new GuiView();
        GameController controller = new GameController(new GameState(8, 8), view);

        view.setController(controller);
        controller.newGame(8, 8);
        view.setVisible(true);
    }
}
