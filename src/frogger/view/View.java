package frogger.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;

import frogger.controller.Controller;
import frogger.model.Model;
import frogger.model.ModelChangeData;
import frogger.utilClasses.Constants;
import frogger.utilClasses.IObservable;
import frogger.utilClasses.IObserver;

public class View extends JFrame implements IObserver<Model>, KeyListener {
	private static final long serialVersionUID = 1L;

	private boolean isGameActive = false;
	private JPanel activePanel = null;

	private JLabel doodlerLabel = null;
	private JLabel scoreLabel = null;

	private List<JLabel> objectLabels = new ArrayList<JLabel>();

	private Controller controller;

	public View(Controller controller) {
		super();

		this.controller = controller;
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);

		this.setSize(Constants.GAME_WINDOW_SIZE);
		this.setLocation(Constants.TK.getScreenSize().width / 3, 0);

		this.setVisible(true);

		controller.subscribeObserver(this);

		this.addKeyListener(this);

		this.requestFocus();

		activePanel = new JPanel();

		this.add(activePanel);

		activePanel.setLayout(null);
		activePanel.setVisible(true);
		activePanel.setBorder(new LineBorder(Color.red));

		JLabel label = new JLabel();

		activePanel.add(label);

		label.setText("Doodle jump");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(new Font("Serif", Font.BOLD, 32));

		label.setBounds(0, this.getHeight() / 4, this.getWidth(), label.getFont().getSize() * 2);

		label = new JLabel();

		activePanel.add(label);

		label.setText("Press \"Space\" to start the game");

		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(new Font("Serif", Font.PLAIN, 24));

		label.setBounds(0, this.getHeight() / 2, this.getWidth(), label.getFont().getSize() * 2);

	}

	public Controller getContr() {
		return controller;
	}

	public void setContr(Controller contr) {
		this.controller = contr;
	}

	@Override
	public void subscribe(IObservable<Model> observable) {
		Model model = ((Model) observable);
		if (observable == null)
			throw new NullPointerException();
		if (model.getSubscribers().contains(this))
			return;
		model.getSubscribers().add(this);
	}

	@Override
	public void unsubscribe(IObservable<Model> observable) {
		Model model = ((Model) observable);
		if (observable == null)
			throw new NullPointerException();
		if (!model.getSubscribers().contains(this))
			return;
		model.getSubscribers().remove(this);
	}

	@Override
	public void observableChanged(Object object) {
		ModelChangeData changeData = (ModelChangeData) object;
		Icon icon = null;
		JLabel label = null;

		switch (changeData.getChangeType()) {
		case OBJECT_СREATE:

			switch (changeData.getObjectType()) {
			case DOODLER:

				this.remove(activePanel);

				activePanel = new JPanel();

				this.add(activePanel);

				activePanel.setLayout(null);
				activePanel.setVisible(true);
				activePanel.setSize(this.getSize());

				icon = new ImageIcon("resourses/images/doodleR.png");
				doodlerLabel = new JLabel(icon);
				activePanel.add(doodlerLabel);

				doodlerLabel.setBounds(changeData.getAffectedValues()[0], changeData.getAffectedValues()[1],
						icon.getIconWidth(), icon.getIconHeight());
				controller.changeObjectSize(changeData.getObjectType(), icon.getIconWidth(), icon.getIconHeight());

				scoreLabel = new JLabel("0");
				activePanel.add(scoreLabel);

				scoreLabel.setFont(new Font("Serif", Font.BOLD, 20));
				scoreLabel.setBounds(scoreLabel.getFont().getSize() / 2, 0, activePanel.getWidth(),
						scoreLabel.getFont().getSize() * 2);

				break;
			case PLATFORM:

				switch (changeData.getAffectedValues()[2]) {
				case (0):
					icon = new ImageIcon("resourses/images/p-normal.png");
					break;
				case (1):
					icon = new ImageIcon("resourses/images/p-horiz.png");
					break;
				case (2):
					icon = new ImageIcon("resourses/images/p-vertical.png");
					break;
				case (3):
					icon = new ImageIcon("resourses/images/p-broken-1.png");
					break;
				case (4):
					icon = new ImageIcon("resourses/images/p-ghost.png");
					break;
				}

				JLabel platformLabel = new JLabel(icon);
				objectLabels.add(platformLabel);
				activePanel.add(platformLabel);

				platformLabel.setBounds(changeData.getAffectedValues()[0], changeData.getAffectedValues()[1],
						icon.getIconWidth(), icon.getIconHeight());
				controller.changeObjectSize(changeData.getObjectType(), icon.getIconWidth(), icon.getIconHeight(),
						objectLabels.size() - 1);

				break;
			case ENEMY:

				switch (Constants.RND.nextInt(5)) {
				case (0):
					icon = new ImageIcon("resourses/images/enemies/monster1.png");
					break;
				case (1):
					icon = new ImageIcon("resourses/images/enemies/monster2.png");
					break;
				case (2):
					icon = new ImageIcon("resourses/images/enemies/monster3.png");
					break;
				case (3):
					icon = new ImageIcon("resourses/images/enemies/monster4.png");
					break;
				case (4):
					icon = new ImageIcon("resourses/images/enemies/monster5.png");
					break;
				}

				JLabel enemyLabel = new JLabel(icon);

				objectLabels.add(enemyLabel);
				activePanel.add(enemyLabel);

				enemyLabel.setBounds(changeData.getAffectedValues()[0], 0, icon.getIconWidth(), icon.getIconHeight());
				controller.changeObjectSize(changeData.getObjectType(), icon.getIconWidth(), icon.getIconHeight(),
						objectLabels.size() - 1);

				break;
			case BULLET:
				icon = new ImageIcon("resourses/images/bullet.png");

				JLabel bulletLabel = new JLabel(icon);

				objectLabels.add(bulletLabel);
				activePanel.add(bulletLabel);

				bulletLabel.setBounds(changeData.getAffectedValues()[0], changeData.getAffectedValues()[1],
						icon.getIconWidth(), icon.getIconHeight());
				controller.changeObjectSize(changeData.getObjectType(), icon.getIconWidth(), icon.getIconHeight(),
						objectLabels.size() - 1);

				break;
			case BONUS:

				switch (changeData.getAffectedValues()[0]) {
				case (0):
					icon = new ImageIcon("resourses/images/bonuses/spring.png");
					break;
				case (1):
					icon = new ImageIcon("resourses/images/bonuses/shield.png");
					break;
				case (2):
					icon = new ImageIcon("resourses/images/bonuses/jetpack.png");
					break;
				case (3):
					icon = new ImageIcon("resourses/images/bonuses/spring_shoes.png");
					break;
				}

				JLabel bonusLabel = new JLabel(icon);

				objectLabels.add(bonusLabel);
				activePanel.add(bonusLabel);

				JLabel ownerPlatformLabel = objectLabels.get(changeData.getAffectedValues()[1]);

				int bonusXPosition = (int) ownerPlatformLabel.getLocation().getX()
						+ Constants.RND.nextInt(ownerPlatformLabel.getWidth() - icon.getIconWidth());
				int bonusYPosition = (int) ((ownerPlatformLabel.getLocation().getY() - icon.getIconHeight()));

				bonusLabel.setBounds(bonusXPosition, bonusYPosition, icon.getIconWidth(), icon.getIconHeight());

				bonusLabel.setSize(icon.getIconWidth(), icon.getIconHeight());

				controller.changeObjectLocation(changeData.getObjectType(), bonusXPosition, bonusYPosition,
						objectLabels.size() - 1);
				controller.changeObjectSize(changeData.getObjectType(), icon.getIconWidth(), icon.getIconHeight(),
						objectLabels.size() - 1);

				break;
			default:
				break;

			}

			break;
		case OBJECT_MOVE:

			switch (changeData.getObjectType()) {
			case DOODLER:

				doodlerLabel.setLocation(changeData.getAffectedValues()[0], changeData.getAffectedValues()[1]);

				if (changeData.getAffectedValues().length >= 4 && changeData.getAffectedValues()[3] == 1) {
					icon = new ImageIcon("resourses/images/doodleS.png");

					if (changeData.getAffectedValues().length >= 5) {
						switch (changeData.getAffectedValues()[4]) {
						case (0):
							break;
						case (1):
							icon = new ImageIcon("resourses/images/bonuses/shieldS.png");
							break;
						case (2):
							break;
						case (3):
							icon = new ImageIcon("resourses/images/bonuses/shoesS.png");
							break;
						}
					}

				} else if (changeData.getAffectedValues()[2] < 0) {
					icon = new ImageIcon("resourses/images/doodleL.png");

					if (changeData.getAffectedValues().length >= 5) {
						switch (changeData.getAffectedValues()[4]) {
						case (0):
							break;
						case (1):
							icon = new ImageIcon("resourses/images/bonuses/shieldL.png");
							break;
						case (2):
							icon = new ImageIcon("resourses/images/bonuses/jetpackL.png");
							break;
						case (3):
							icon = new ImageIcon("resourses/images/bonuses/shoesL.png");
							break;
						}
					}

				} else if (changeData.getAffectedValues()[2] > 0
						|| (changeData.getAffectedValues().length >= 4 && changeData.getAffectedValues()[3] == 0)) {
					icon = new ImageIcon("resourses/images/doodleR.png");

					if (changeData.getAffectedValues().length >= 5) {
						switch (changeData.getAffectedValues()[4]) {
						case (0):
							break;
						case (1):
							icon = new ImageIcon("resourses/images/bonuses/shieldR.png");
							break;
						case (2):
							icon = new ImageIcon("resourses/images/bonuses/jetpackR.png");
							break;
						case (3):
							icon = new ImageIcon("resourses/images/bonuses/shoesR.png");
							break;
						}
					}

				}

				if (icon != null) {
					doodlerLabel.setIcon(icon);
					doodlerLabel.setSize(icon.getIconWidth(), icon.getIconHeight());
					controller.changeObjectSize(changeData.getObjectType(), icon.getIconWidth(), icon.getIconHeight());

				}

				break;
			case PLATFORM:

				objectLabels.get(changeData.getAffectedValues()[2]).setLocation(changeData.getAffectedValues()[0],
						changeData.getAffectedValues()[1]);

				if (changeData.getAffectedValues().length == 4) {
					if (changeData.getAffectedValues()[3] == 1) {
						icon = new ImageIcon("resourses/images/p-broken-2.png");

						objectLabels.get(changeData.getAffectedValues()[2]).setIcon(icon);

						objectLabels.get(changeData.getAffectedValues()[2]).setSize(icon.getIconWidth(),
								icon.getIconHeight());
						controller.changeObjectSize(changeData.getObjectType(), icon.getIconWidth(),
								icon.getIconHeight(), changeData.getAffectedValues()[2]);
					}
				}

				break;
			case ENEMY:

				objectLabels.get(changeData.getAffectedValues()[2]).setLocation(changeData.getAffectedValues()[0],
						changeData.getAffectedValues()[1]);

				break;
			case BULLET:

				objectLabels.get(changeData.getAffectedValues()[2]).setLocation(changeData.getAffectedValues()[0],
						changeData.getAffectedValues()[1]);

				break;
			case BONUS:

				objectLabels.get(changeData.getAffectedValues()[2]).setLocation(changeData.getAffectedValues()[0],
						changeData.getAffectedValues()[1]);

				if (changeData.getAffectedValues().length >= 4 && changeData.getAffectedValues()[3] == 1) {
					objectLabels.get(changeData.getAffectedValues()[2]).setLocation(doodlerLabel.getLocation().x,
							doodlerLabel.getLocation().y);

					controller.changeObjectLocation(changeData.getObjectType(), doodlerLabel.getLocation().x,
							doodlerLabel.getLocation().y, changeData.getAffectedValues()[2]);
				}

				break;
			default:
				break;

			}

			break;
		case OBJECT_DESTROY:

			switch (changeData.getObjectType()) {
			case DOODLER:

				doodlerLabel = null;

				break;
			case PLATFORM:

				activePanel.remove(objectLabels.get(changeData.getAffectedValues()[0]));
				objectLabels.remove(changeData.getAffectedValues()[0]);

				break;
			case ENEMY:

				activePanel.remove(objectLabels.get(changeData.getAffectedValues()[0]));
				objectLabels.remove(changeData.getAffectedValues()[0]);

				break;
			case BULLET:

				activePanel.remove(objectLabels.get(changeData.getAffectedValues()[0]));
				objectLabels.remove(changeData.getAffectedValues()[0]);

				break;
			case BONUS:

				activePanel.remove(objectLabels.get(changeData.getAffectedValues()[0]));
				objectLabels.remove(changeData.getAffectedValues()[0]);

				break;
			default:
				break;

			}

			break;
		case BONUS_ACTIVATION:

			objectLabels.get(changeData.getAffectedValues()[0]).setBounds(doodlerLabel.getLocation().x,
					doodlerLabel.getLocation().y, 0, 0);

			controller.changeObjectLocation(changeData.getObjectType(), doodlerLabel.getLocation().x,
					doodlerLabel.getLocation().y, changeData.getAffectedValues()[0]);
			controller.changeObjectSize(changeData.getObjectType(), 0, 0, changeData.getAffectedValues()[0]);

			break;

		case SCORE_CHANGE:
			scoreLabel.setText("" + changeData.getAffectedValues()[0]);
			break;
		case GAME_START:

			objectLabels = new ArrayList<JLabel>();

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

			label.setText("Your score = " + scoreLabel.getText());
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

		default:
			break;
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (!isGameActive) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				controller.startGame();
			}
		} else {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				controller.changeDx(-Constants.DOODLER_HORIZONTAL_SPEED);
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				controller.changeDx(Constants.DOODLER_HORIZONTAL_SPEED);
			} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_SPACE) {
				controller.doodlerShoot();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			controller.changeDx(0);
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			controller.changeDx(0);
		} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_SPACE) {
			controller.doodlerStopShoot();
		}
	}
}