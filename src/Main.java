import controller.GameController;
import db.DatabaseManager;
import db.Schema;
import model.GameState;
import view.GameView;
import view.GuiView;

public class Main {

    public static void main(final String[] theArgs) {
        Schema.initialize();
        DatabaseManager db = new DatabaseManager(); // use in QuestionFactory

        GameState state = new GameState(8, 8);
        GameView view = new GuiView();

        GameController controller = new GameController(state, view);
        view.setController(controller);

        ((GuiView) view).setVisible(true);
    }
}