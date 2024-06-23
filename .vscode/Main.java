import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.swing.Timer;
public class Main extends JPanel implements ActionListener {

	private static final int WIDTH = 600;
	private static final int HEIGHT = 600;
	private static final int DELAY = 11;
	private static final int DELAY_FAST = 10;

	Font font = new Font("Arial", Font.BOLD, 15);
	// Boolean values for the instructions screen and the game
	private boolean introScreen = true;
	private boolean characterScreen = false;
	private boolean gameScreen = false;
	// Array to store backgrounds
	private int currentBackgroundIndex = 0;
	private String[] backgroundImages = { "assets/background1.png", "assets/background2.png", "assets/background3.png", "assets/background4.png" };

	private int selectedDinoIndex = 0;
	private String[] dinoImages = { "assets/dino1.png", "assets/dino2.png", "assets/dino3.png", "assets/dino4.png" };
	private String[] flippedDinoImages = { "assets/dino1flipped.png", "assets/dino2flipped.png", "assets/dino3flipped.png",
			"assets/dino4flipped.png" };

	private Timer appleTimer, fastTimer, greenAppleTimer, commentTimer, shieldTimer; // Timers

	private Random random; // Creating a random variable for the x position of the apple
	private int appleY, obstacleY, greenY, shieldY = 0; // y position
	private int appleX, obstacleX, greenX, shieldX; // x position

	private int score = 0;
	private int highscore = 0;

	private JButton pauseButton, restartButton, exitButton, guideButton;

	private int dinoX = 215; // x position of dino
	private boolean isFlipped = false; // Keeps track of dino's image

	private boolean isGreenAppleVisible; // Boolean to keep track of if green apple is visible
	private boolean isMovingFast = false; // When the dino intersects with green apple, it moves fast
	private JLabel speedLabel; // Speed label / box on the screen

	private boolean isShieldVisible = false; // Boolean to keep track of the visible of the shield
	private boolean shieldAroundDino = false; // When dino intersects with the shield, there is a shield image around it

	private String currentComment;
	// Array to store comments
	private String[] comment = {
			"Amazing!",
			"Fantastic~",
			"Great!",
			"Way to go!",
			"Perfect~",
			"Bravo!",
			"Awesome!",
			"Excellent!",
			"Impressive~",
			"Good job!",
			"Nice catch!",
			"Wow!",
			"Holy cowza!",
			"Astounding~",
			"Ourstanding!",
			"Nice going~"
	};

	public Main() {

		// Initializing random values so that the objects fall from random x positions
		random = new Random();
		appleX = random.nextInt(WIDTH - 80);
		obstacleX = random.nextInt(WIDTH - 80);
		greenX = random.nextInt(WIDTH - 80);
		shieldX = random.nextInt(WIDTH - 80);

		// Initializing the timers
		appleTimer = new Timer(DELAY, this);
		fastTimer = new Timer(DELAY_FAST, this); // Timer for when score reaches 15

		// Making the comment a string
		currentComment = "";
		// Adding a timer for the comments
		commentTimer = new Timer(1000, new ActionListener() { // Only shows up for 1000 milliseconds (1 second)
			public void actionPerformed(ActionEvent e) {
				currentComment = "";
				repaint();
				commentTimer.stop();
			}
		});

		// Adding a timer for the green apple
		// It disappears for 15000 milliseconds (15 seconds) and then comes back
		greenAppleTimer = new Timer(15000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				greenY = 0;
				greenX = random.nextInt(WIDTH - 80);
				isGreenAppleVisible = true; // Visibility is set to true after 15 seconds have passed
				greenAppleTimer.stop();
				repaint();
			}
		});
		isGreenAppleVisible = false;

		// Adding a timer for the shield
		// It disappears for 20000 milliseconds (20 seconds) and then comes back
		shieldTimer = new Timer(20000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				shieldY = 0;
				shieldX = random.nextInt(WIDTH - 80);
				isShieldVisible = true; // Visibility is set to true after 15 seconds have passed
				shieldAroundDino = false;
				shieldTimer.stop();
				repaint();
			}
		});
		isShieldVisible = false;

		// When the instructions screen is on run this
		if (introScreen) {
			setFocusable(true);
			appleTimer.stop();
			fastTimer.stop();

			addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					int key = e.getKeyCode();

					if (key == KeyEvent.VK_ENTER) {

						if (introScreen) {

							introScreen = false;
							characterScreen = true;
							gameScreen = false;

						} else if (characterScreen) {

							setFocusable(true);
							appleTimer.stop();
							fastTimer.stop();
							greenAppleTimer.stop();
							shieldTimer.stop();

							if (key == KeyEvent.VK_ENTER) {

								if (characterScreen) {
									introScreen = false;
									characterScreen = false;
									gameScreen = true;

									if (introScreen || characterScreen == true) {
										appleTimer.stop();
										fastTimer.stop();
										greenAppleTimer.stop();
										shieldTimer.stop();
									} else if (gameScreen == true) {
										appleTimer.start();
										greenAppleTimer.start();
										shieldTimer.start();
									}
									if (score >= 15) {
										fastTimer.start();
									}

									speedLabel = new JLabel("x1");

									// Pause Button
									pauseButton = new JButton(" Pause ");
									setLayout(null);
									pauseButton.setBorderPainted(false);
									pauseButton.setBounds(478, 12, 110, 30);
									pauseButton.setFont(font);
									pauseButton.setBackground(Color.WHITE);
									add(pauseButton);

									// Action Listener for the pause button
									pauseButton.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e) {
											if (appleTimer.isRunning() || fastTimer.isRunning()) {
												appleTimer.stop();
												fastTimer.stop();
												greenAppleTimer.stop();
												shieldTimer.stop();
												pauseButton.setText(" Resume ");
												add(restartButton);
												restartButton.setVisible(true);
												add(exitButton);
												exitButton.setVisible(true);
												add(guideButton);
												guideButton.setVisible(true);
											} else if (score >= 15) {
												fastTimer.start();
											} else {
												appleTimer.start();
												greenAppleTimer.start();
												shieldTimer.start();
												pauseButton.setText(" Pause ");
												requestFocusInWindow(); // If this isn't added, the dino won't move when resume is clicked
												restartButton.setVisible(false);
												exitButton.setVisible(false);
												guideButton.setVisible(false);
											}
										}
									});

									// Restart Button
									restartButton = new JButton(" Restart ");
									restartButton.setBorderPainted(false);
									restartButton.setBounds(478, 52, 110, 30);
									restartButton.setFont(font);
									restartButton.setBackground(Color.PINK);
									restartButton.setVisible(false); // Set visibility to false initially
									add(restartButton);

									// Action listener for the restart button
									restartButton.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e) {
											appleTimer.stop();
											fastTimer.stop();
											greenAppleTimer.stop();
											shieldTimer.stop();
											isGreenAppleVisible = false;
											isShieldVisible = false;
											shieldAroundDino = false;
											dinoX = 215;
											appleY = 0;
											appleX = random.nextInt(WIDTH - 80);
											obstacleY = 0;
											obstacleX = random.nextInt(WIDTH - 80);
											greenY = 0;
											greenX = random.nextInt(WIDTH - 80);
											shieldY = 0;
											shieldX = random.nextInt(WIDTH - 80);
											score = 0;
											isMovingFast = false;
											speedLabel.setText("x1");
											pauseButton.setText(" Pause ");
											restartButton.setVisible(false);
											exitButton.setVisible(false);
											guideButton.setVisible(false);
											appleTimer.start();
											greenAppleTimer.start();
											shieldTimer.start();
											requestFocusInWindow();
											if (score >= 15) {
												fastTimer.start();
											}
										}
									});

									// Exit Button
									exitButton = new JButton(" Exit ");
									exitButton.setBorderPainted(false);
									exitButton.setBounds(478, 92, 110, 30);
									exitButton.setFont(font);
									exitButton.setBackground(Color.PINK);
									exitButton.setVisible(false); // Set visibility to false initially
									add(exitButton);

									// Action listener for the exit button
									exitButton.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e) {
											System.exit(0);
										}
									});

									// Instructions Button
									guideButton = new JButton(" Guide ");
									guideButton.setBorderPainted(false);
									guideButton.setBounds(478, 132, 110, 30);
									guideButton.setFont(font);
									guideButton.setBackground(Color.PINK);
									guideButton.setVisible(false); // Set visibility to false initially
									add(guideButton);

									// Action listener for the guide button
									guideButton.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e) {
											introScreen = true;
											gameScreen = false; // Set gameScreen to false
											appleTimer.stop();
											fastTimer.stop();
											greenAppleTimer.stop();
											shieldTimer.stop();
											guideButton.setVisible(false);
											restartButton.setVisible(false);
											exitButton.setVisible(false);
											pauseButton.setVisible(false);
											repaint();
										}
									});
									repaint();
								}
							}
						}
					}
				}
			});
		}

		// Run this when the user presses enter and game starts
		if (gameScreen) {
			appleTimer.start();
			if (score >= 15) {
				fastTimer.start();
			}
		}

		// Set focusable and add key listener
		setFocusable(true);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				// Determine the movement increment based on the speed of movement
				int movementIncrement = isMovingFast ? 24 : 12;
				// Set the new dinoX position based on the movement increment
				if (key == KeyEvent.VK_LEFT && appleTimer.isRunning()) {
					dinoX -= movementIncrement; // Move left
					isFlipped = true;
					if (isMovingFast) {
						speedLabel.setText("x2");
					} else if (isMovingFast == false) {
						speedLabel.setText("x1");
					}
				} else if (key == KeyEvent.VK_RIGHT && appleTimer.isRunning()) {
					dinoX += movementIncrement; // Move right
					isFlipped = false;
					if (isMovingFast) {
						speedLabel.setText("x2");
					} else if (isMovingFast == false) {
						speedLabel.setText("x1");
					}
				} else if (key == KeyEvent.VK_LEFT && fastTimer.isRunning()) {
					dinoX -= movementIncrement; // Move left
					isFlipped = true;
					if (isMovingFast) {
						speedLabel.setText("x2");
					} else if (isMovingFast == false) {
						speedLabel.setText("x1");
					}
				} else if (key == KeyEvent.VK_RIGHT && fastTimer.isRunning()) {
					dinoX += movementIncrement; // Move right
					isFlipped = false;
					if (isMovingFast) {
						speedLabel.setText("x2");
					} else if (isMovingFast == false) {
						speedLabel.setText("x1");
					}
				} else if (characterScreen == true) {
					if (key == KeyEvent.VK_RIGHT) {
						selectedDinoIndex = (selectedDinoIndex + 1) % dinoImages.length;
						repaint();
					} else if (key == KeyEvent.VK_LEFT) {
						selectedDinoIndex = (selectedDinoIndex - 1 + dinoImages.length) % dinoImages.length;
						repaint();
					}
				}
				repaint();
			}
		});
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// When the instructions screen is on
		if (introScreen) {

			// Instructions image
			ImageIcon instructions = new ImageIcon("assets/instructions.png");
			Image insImage = instructions.getImage();
			g.drawImage(insImage, 0, 0, getWidth(), getHeight(), null);

		} else if (characterScreen) {

			// Choose a character background image
			ImageIcon instructions = new ImageIcon("assets/chooseDinoBg.png");
			Image insImage = instructions.getImage();
			g.drawImage(insImage, 0, 0, getWidth(), getHeight(), null);

			ImageIcon dinoIcon = new ImageIcon(dinoImages[selectedDinoIndex]);
			Image dinoImage = dinoIcon.getImage();
			g.drawImage(dinoImage, 215, 150, 150, 230, null);

		} else if (gameScreen) { // When the game starts after the player has chose their character

			// Background image
			// Get the file path of the background image based on the current background
			// index
			String backgroundImageFile = backgroundImages[currentBackgroundIndex];
			// Create an ImageIcon object using the background image file
			ImageIcon backgroundImage = new ImageIcon(backgroundImageFile);
			Image bgImg = backgroundImage.getImage();
			g.drawImage(bgImg, 0, 0, getWidth(), getHeight(), null);

			// Dashed line image
			ImageIcon dashed_Line = new ImageIcon("assets/dashedLine.png");
			Image dashedLine = dashed_Line.getImage();
			g.drawImage(dashedLine, 75, 385, 441, 20, null);

			// Apple Image
			ImageIcon apple = new ImageIcon("assets/apple.png");
			Image apple_1 = apple.getImage();
			g.drawImage(apple_1, appleX, appleY, 50, 50, null);

			// Green Apple Image
			if (isGreenAppleVisible) { // Show only when it is supposed to be visible
				ImageIcon green_1 = new ImageIcon("assets/greenApple.png");
				Image greenApple = green_1.getImage();
				g.drawImage(greenApple, greenX, greenY, 60, 60, null);
			}

			// When shield icon is falling, draw the image
			if (isShieldVisible) {
				ImageIcon shieldIcon = new ImageIcon("assets/shield.png");
				Image shield = shieldIcon.getImage();
				g.drawImage(shield, shieldX, shieldY, 45, 45, null);
			}

			// When the dino intersects with shield, draw the shield cover
			if (shieldAroundDino == true) {
				ImageIcon shielOndDino = new ImageIcon("assets/shieldCovers.png");
				Image shieldCovers = shielOndDino.getImage();
				g.drawImage(shieldCovers, dinoX, 290, 150, 230, null);
			}

			// Obstacle Image - meteor
			ImageIcon meteor_obs = new ImageIcon("assets/obstacle.png");
			Image meteor = meteor_obs.getImage();
			g.drawImage(meteor, obstacleX, obstacleY, 65, 65, null);

			// Dino Image
			ImageIcon dinoIcon; // Variable that holds the ImageIcon for the dino
			if (isFlipped) { // Check if the dino should be flipped
				// Create an ImageIcon object for the flipped dino image
				ImageIcon flipped_Dino = new ImageIcon(flippedDinoImages[selectedDinoIndex]);
				dinoIcon = flipped_Dino; // Assign the flipped dinosaur icon to the dinoIcon variable
			} else {
				ImageIcon normal_Dino = new ImageIcon(dinoImages[selectedDinoIndex]);
				dinoIcon = normal_Dino;
			}
			Image dinoImage = dinoIcon.getImage();

			// Check if the dino's x-coordinate is within the boundaries of the screen
			// Don't let dino go off screen
			if (dinoX < 0) {
				dinoX = 0;
			} else if (dinoX + 135 > WIDTH) {
				dinoX = WIDTH - 135;
			}
			g.drawImage(dinoImage, dinoX, 290, 150, 230, null);

			// Score text
			g.setColor(Color.WHITE);
			g.fillRect(10, 10, 100, 30);
			g.setFont(font);
			g.setColor(Color.RED);
			g.drawString("Score: " + score, 24, 30);

			// Highscore text
			g.setColor(Color.WHITE);
			g.fillRect(10, 50, 130, 30);
			g.setFont(font);
			g.setColor(Color.GREEN);
			g.drawString("Highscore: " + highscore, 24, 70);

			// Comments text
			g.setFont(font);
			g.setColor(Color.BLACK);
			g.drawString(currentComment, dinoX + 40, 280);

			// Speed Text
			g.setColor(Color.WHITE);
			g.fillRect(10, 90, 110, 30);
			g.setFont(font);
			g.setColor(Color.BLUE);
			g.drawString("Speed: " + speedLabel.getText(), 24, 110);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (gameScreen == true) {
			// Creating rectangles around the objects
			Rectangle dinoRect = new Rectangle(dinoX + 25, 295, 100, 90);
			Rectangle appleRect = new Rectangle(appleX + 5, appleY, 40, 50);
			Rectangle obstacleRect = new Rectangle(obstacleX + 5, obstacleY + 5, 55, 55);
			Rectangle greenAppleRect = new Rectangle(greenX + 10, greenY + 10, 40, 42);
			Rectangle shieldRect = new Rectangle(shieldX + 5, shieldY, 45, 45);
			Rectangle dashedLineRect = new Rectangle(4, 384, 590, 20);

			// Move the apple down
			appleY += 1;
			obstacleY += 1;
			greenY += 1;
			shieldY += 1;

			// Check if the apple intersects with the dino
			if (dinoRect.intersects(appleRect)) {
				// Increase the score and reset the apple position
				score += 1;
				appleX = random.nextInt(WIDTH - 80);
				appleY = 0;
				// Shuffle the comment array using the Collections.shuffle method
				Collections.shuffle(Arrays.asList(comment));
				// Get the first comment from the shuffled array and assign it to the
				// currentComment variable
				currentComment = comment[0]; //
				commentTimer.start(); // Start the comment timer
				repaint();
			} else if (dinoRect.intersects(obstacleRect)) { // Check when dino intersects with meteor
				if (shieldAroundDino == false) {
					score -= 2;
					obstacleX = random.nextInt(WIDTH - 80);
					obstacleY = 0;
				} else if (shieldAroundDino == true) {
					score -= 0;
				}
			} else if (dinoRect.intersects(greenAppleRect)) {
				greenX = random.nextInt(WIDTH - 80);
				greenY = 0;
			} else if (dinoRect.intersects(shieldRect)) {
				shieldX = random.nextInt(WIDTH - 80);
				shieldY = 0;
				shieldTimer.start();
			}

			// Check if the apple intersects with the dashed line
			if (appleRect.intersects(dashedLineRect)) {
				// Game over
				appleTimer.stop();
				fastTimer.stop();
				greenAppleTimer.stop();
				shieldTimer.stop();
				int choice = JOptionPane.showOptionDialog(this, " Game Over", " Game Over", JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, new Object[] { "Restart" }, "Restart");
				highscore = Math.max(score, highscore); // Update high score only when it's bigger compared to the last one
				if (choice == 0) {
					// Restart the game
					score = 0;
					appleY = 0;
					obstacleY = 0;
					greenY = 0;
					shieldY = 0;
					appleTimer.start();
					fastTimer.stop();
					shieldTimer.start();
					greenAppleTimer.start();
					shieldAroundDino = false;
					dinoX = 215; // Reset the dino's position
					highscore = Math.max(score, highscore); // Update high score
				} else {
					System.exit(0);
				}
			}

			// When meteor intersects with the line
			if (obstacleRect.intersects(dashedLineRect)) {
				obstacleY = 0;
				obstacleX = random.nextInt(WIDTH - 80);
			}

			// Check the value of the score and control the fastTimer accordingly
			if (score >= 15) {
				fastTimer.start();
			} else if (score < 15) {
				fastTimer.stop();
			}

			// When green apple intersects with the line
			if (greenAppleRect.intersects(dashedLineRect) && isGreenAppleVisible) {
				greenY = 0;
				greenX = random.nextInt(WIDTH - 80);
				isGreenAppleVisible = false;
				greenAppleTimer.start();
			}

			// When dino intersects with the green apple
			// Check if the dino intersects with the green apple
			if (dinoRect.intersects(greenAppleRect) && isGreenAppleVisible) {
				greenX = random.nextInt(WIDTH - 80);
				greenY = 0;
				isMovingFast = true;
				// Make the dino move at speed x2 for a delay of 8000 milliseconds (8 seconds)
				Timer speedTimer = new Timer(8000, new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						isMovingFast = false;
						((Timer) e.getSource()).stop();
					}
				});
				speedTimer.setRepeats(false);
				speedTimer.start();
				isGreenAppleVisible = false;
				greenAppleTimer.start();
				greenX = random.nextInt(WIDTH - 80);
				greenY = 0;
				repaint();
			}

			// When shield intersects with the line
			if (shieldRect.intersects(dashedLineRect) && isShieldVisible) {
				shieldY = 0;
				shieldX = random.nextInt(WIDTH - 80);
				isShieldVisible = false;
				shieldTimer.start();
			}

			// When dino intersects with the shield
			// Check if the dino intersects with the shield
			if (dinoRect.intersects(shieldRect) && isShieldVisible) {
				shieldX = random.nextInt(WIDTH - 80);
				shieldY = 0;
				shieldAroundDino = true;
				// Create a Timer with a delay of 8.5 seconds (8500 milliseconds)
				Timer shieldTimer = new Timer(8500, new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						shieldAroundDino = false;
						((Timer) e.getSource()).stop();
						repaint();
					}
				});
				shieldTimer.setRepeats(false);
				shieldTimer.start();
				isShieldVisible = false;
				shieldTimer.start();
				shieldX = random.nextInt(WIDTH - 80);
				shieldY = 0;
				repaint();
			}

			// Check the score value and determine the background accordingly
			if (score == 0 || score >= 40 && score <= 49 || score >= 80 && score <= 89) {
				currentBackgroundIndex = 0;
				repaint();
			} else if (score >= 10 && score <= 19 || score >= 50 && score <= 59 || score >= 90 && score <= 99) {
				currentBackgroundIndex = 1;
				repaint();
			} else if (score >= 20 && score <= 29 || score >= 60 && score <= 69 || score == 100 && score <= 109) {
				currentBackgroundIndex = 2;
				repaint();
			} else if (score >= 30 && score <= 39 || score >= 70 && score <= 79 || score >= 110 && score <= 119) {
				currentBackgroundIndex = 3;
				repaint();
			} else {
				currentBackgroundIndex = 0;
				repaint();
			}

			repaint();
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame(" The Dino Game ");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setContentPane(new Main());
		ImageIcon logo = new ImageIcon("assets/logo.png");
		frame.setIconImage(logo.getImage());
	}
}