package frogger;

import frogger.model.Frog;
import frogger.model.ModelChangeData;
import frogger.model.ObjectTypes;
import frogger.utilClasses.Observer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*; 
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class GameView extends JFrame implements Observer, KeyListener {
    private static final long serialVersionUID = 1L;

    private boolean isGameActive = false;
    private JPanel activePanel;

    private JLabel frog = null;
    private JLabel score = null;

    private List<JLabel> blocks = new ArrayList<JLabel>();

    private GameController controller;

    public GameView(GameController controller) {
        super();

        //TODO fullscreen
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
        device.setFullScreenWindow(this);

        this.controller = controller;

        this.setTitle("Frogger");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        // this.setUndecorated(false);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        //  this.setSize(GameStaticValues.GAME_WINDOW_SIZE);
        // this.setLocation(GameStaticValues.TK.getScreenSize().width / 3, 0);

        this.setVisible(true);

 

        this.addKeyListener(this);

        this.requestFocus();

        activePanel = new JPanel();

        this.add(activePanel);

        activePanel.setLayout(null);
        activePanel.setVisible(true);
        //activePanel.setBorder(new LineBorder(Color.red));

        JLabel label = new JLabel();

        activePanel.add(label);

        label.setText("Frogger");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 32));

        label.setBounds(0, this.getHeight() / 4, this.getWidth(), label.getFont().getSize() * 2);

        label = new JLabel();

        activePanel.add(label);

        label.setText("Press \n\"Space\" to start the game");

        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(new Font("Serif", Font.PLAIN, 24));

        label.setBounds(0, this.getHeight() / 2, this.getWidth(), label.getFont().getSize() * 2);

        controller.addObserverToModel(this);
    }

    public GameController getContr() {
        return controller;
    }

    public void setContr(GameController contr) {
        this.controller = contr;
    }

    @Override
    public void handleEvent(ModelChangeData changeData) {
        Icon icon = null;
        JLabel label = null;

        switch (changeData.getEvent()) {
            case OBJECT_СREATE:

                switch (changeData.getObject().getObjectType()) {
                    case FROG:

                        this.remove(activePanel);

                        activePanel = new JPanel();

                        this.add(activePanel);

                        activePanel.setLayout(null);
                        activePanel.setVisible(true);
                        activePanel.setSize(this.getSize());
                        activePanel.setBackground(Color.CYAN);

                        icon = new ImageIcon("resources/images/frog.png");

                        label = new JLabel(icon);
                        frog = label;
                       // frog.setBorder(new LineBorder(Color.red));

                        score = new JLabel("0");
                        activePanel.add(score);


                        //score.setBorder(new LineBorder(Color.MAGENTA));
                        score.setFont(new Font("Serif", Font.BOLD, 50));
                        score.setHorizontalAlignment(SwingConstants.RIGHT);
                        score.setBounds((int) (activePanel.getWidth() * 0.01),
                                0, (int) (activePanel.getWidth() * 0.97),
                                score.getFont().getSize() * 2);

                        break;
                    case WOOD:

                        icon = new ImageIcon("resources/images/wood.png");

                        label = new JLabel(icon);

                        break;
                    case LEAF:

                        icon = new ImageIcon("resources/images/leaf.png");

                        label = new JLabel(icon);

                        break;
                    case GROUND:

                        icon = new ImageIcon("resources/images/ground.png");

                        label = new JLabel(icon);

                        break;
                    default:
                        break;
                }


                if (changeData.getObject().getObjectType() != ObjectTypes.FROG) {
                    blocks.add(label);
                }
                activePanel.add(label);

                //TODO убрать border
                //label.setBorder(new LineBorder(Color.MAGENTA));
                label.setBounds(changeData.getObject().getObjectRectangle().x, changeData.getObject().getObjectRectangle().y,
                        icon.getIconWidth(), icon.getIconHeight());
                //controller.changeObjectSize(changeData.getObject(), icon.getIconWidth(), icon.getIconHeight());


                break;
            case OBJECT_MOVE:

                if (changeData.getObject().getObjectType() == ObjectTypes.FROG) {

                    frog.setLocation(changeData.getObject().getObjectRectangle().x,
                            changeData.getObject().getObjectRectangle().y);

                    icon = new ImageIcon("resources/images/frog.png");

                    if (!((Frog) changeData.getObject()).isCanJump()) {

                        switch (((Frog) changeData.getObject()).getJumpDirection()) {
                            case LEFT:
                                icon = new ImageIcon("resources/images/frog_left.png");
                                break;
                            case RIGHT:
                                icon = new ImageIcon("resources/images/frog_right.png");
                                break;
                            case UP:
                                icon = new ImageIcon("resources/images/frog_up.png");
                                break;
                            case DOWN:
                                icon = new ImageIcon("resources/images/frog_up.png");
                                break;
                        }
                    }


                    if (icon != null) {
                        frog.setIcon(icon);
                        frog.setSize(icon.getIconWidth(), icon.getIconHeight());
                        //controller.changeObjectSize(changeData.getObject(), icon.getIconWidth(), icon.getIconHeight());

                    }

                } else {

                    blocks.get(changeData.getExtraValues()[0])
                            .setLocation(changeData.getObject().getObjectRectangle().x,
                                    changeData.getObject().getObjectRectangle().y);

                }

                break;
            case OBJECT_DESTROY:

                if (changeData.getObject().getObjectType() == ObjectTypes.FROG) {

                    frog = null;

                } else {

                    activePanel.remove(blocks.get(changeData.getExtraValues()[0]));
                    blocks.remove(changeData.getExtraValues()[0]);

                }

                break;
            case SCORE_CHANGE:

                score.setText("" + changeData.getExtraValues()[0]);

                break;
            case GAME_START:

                blocks = new ArrayList<JLabel>();

                this.isGameActive = true;

                break;

            case GAME_OVER:
                this.isGameActive = false;
                this.remove(activePanel);

                activePanel = new JPanel();

                this.add(activePanel);

                activePanel.setLayout(null);
                activePanel.setVisible(true);
                activePanel.setSize(this.getSize());

                activePanel.setBorder(new LineBorder(Color.red));

                label = new JLabel();

                activePanel.add(label);

                label.setText("Game over");

                label.setHorizontalAlignment(JLabel.CENTER);
                label.setFont(new Font("Serif", Font.BOLD, 24));
                label.setBounds(0, this.getHeight() / 4, this.getWidth(), label.getFont().getSize() * 2);

                label = new JLabel();

                activePanel.add(label);

                label.setText("Your score = " + score.getText());
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setFont(new Font("Serif", Font.PLAIN, 24));

                label.setBounds(0, this.getHeight() / 3, this.getWidth(), label.getFont().getSize() * 2);

                label = new JLabel();

                activePanel.add(label);

                label.setText("Press \"Space\" to restart the game");
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setFont(new Font("Serif", Font.PLAIN, 24));

                label.setBounds(0, this.getHeight() / 2, this.getWidth(), label.getFont().getSize() * 2);

                break;

            case LIFE_LOSS:
                break;
            default:
                break;
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            controller.exitGame();
        }

        if (!isGameActive) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                controller.startGame();
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                controller.jumpLeft();
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                controller.jumpRight();
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                controller.jumpUp();
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                controller.jumpDown();
            }
        }
    }


}
