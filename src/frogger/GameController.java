package frogger;

import frogger.model.GameModel;
import frogger.model.GameObject;
import frogger.utilClasses.Observer;

public class GameController {
    private GameModel model;

    public GameController(GameModel model) {
        super();
        this.model = model;
    }

    public static void main(String[] args) {
        GameModel model = new GameModel();
        GameController controller = new GameController(model);
        GameView view = new GameView(controller);
        model.start();
    }

    public void addObserverToModel(Observer observer) {
        model.addObserver(observer);
    }

    public void jumpLeft() {
        model.frogJumpLeft();
    }

    public void jumpRight() {
        model.frogJumpRight();
    }

    public void jumpUp() {
        model.frogJumpUp();
    }

    public void jumpDown() {
        model.frogJumpDown();
    }

    public void changeObjectSize(GameObject object, int width, int height) {
        model.changeObjectSize(object, width, height);
    }

    public void startGame() {
        model.setGameActive(true);
    }

}
