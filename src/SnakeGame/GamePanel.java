package SnakeGame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements ActionListener{
	
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY =100;
	
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	
	int bodyParts = 6;
	int appleEaten;
	int appleX;
	int appleY;
	
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
		
		
	}
	
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
		
	}
	
	public void draw(Graphics g) {
		
		if(running) {
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			for(int i = 0; i<bodyParts; i++) {
				if(i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(45,180,0));
					g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Serif",Font.BOLD,40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + appleEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + appleEaten))/2, g.getFont().getSize() / 1);
		}
		else {
			gameOver(g);
		}
	}
	
	public void newApple(){
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	
	public void move() {
		
		for(int i = bodyParts; i>0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
			
		}
		
		switch(direction) {
		
		case'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
		
	}
	
	public void checkApple() {
		
		if((x[0] == appleX) && (y[0]== appleY)) {
			bodyParts ++;
			appleEaten ++;
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			toolkit.beep();
			newApple();
		}
		
		
	}
	
	public void checkCollisions () {
		//Checks if head collides with body
		for(int i= bodyParts; i > 0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		//Check if head head touches left border
		if(x[0] < 0) {
			running = false;
		}
		//Check if head touches right border
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		//Check if head touches top border
		if(y[0] < 0) {
			running = false;
		}
		//Check if head touches bottom border
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		if(!running) {
			timer.stop();
		}
	}
	
	public void gameOver(Graphics g) {
		//Score
		g.setColor(Color.red);
		g.setFont(new Font("Serif",Font.BOLD,40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Final Score: " + appleEaten, (SCREEN_WIDTH - metrics1.stringWidth("Final Score: " + appleEaten))/2, g.getFont().getSize()*10);
		//Game Over text
		g.setColor(Color.red);
		g.setFont(new Font("Serif",Font.BOLD,75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT / 2);
		
		if (appleEaten > 10) {
			g.setColor(Color.red);
			g.setFont(new Font("Serif",Font.BOLD,40));
			FontMetrics metrics3 = getFontMetrics(g.getFont());
			g.drawString("Nice Try", (SCREEN_WIDTH - metrics3.stringWidth("Nice Try"))/2, g.getFont().getSize()*12);
		} else {
			g.setColor(Color.red);
			g.setFont(new Font("Serif",Font.BOLD,40));
			FontMetrics metrics4 = getFontMetrics(g.getFont());
			g.drawString("Bad... :(", (SCREEN_WIDTH - metrics4.stringWidth("Bad... :("))/2, g.getFont().getSize()*12);
		}
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
				
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
				
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
				
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}

}
