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

        GameState state = new GameState(8, 8);
        GuiView view = new GuiView();

        GameController controller = new GameController(state, view);
        view.setController(controller);

        view.setVisible(true);
    }
}
