/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adivinador.view;

import adivinador.node.Button;
import adivinador.node.AnswerNode;
import adivinador.node.NodeList;
import adivinador.model.Handler;
import java.awt.Canvas;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 *
 * @author Andres
 */
public class MainWindow extends JFrame implements ActionListener {

	public static final int NUM_OPTIONS = 5;
	
	private NodeList<Button> buttons;
	private JTextArea questionDisplay;
	private Handler ref;
	private boolean guessing;
	private JButton openScoreList;
	private TimeDisplay display;
	private boolean newGameCheck;
	
	public MainWindow(Handler main) {
		setLayout(null);
		setSize(400,470);
		setLocationRelativeTo(null);
		setTitle("Adivinador");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		this.ref = main;
		this.guessing = false;
		this.newGameCheck = false;
		
		createInterface();
		
		setVisible(true);
	}
	
	private void createInterface() {
		buttons = new NodeList<>();
		
		for (int i = 0; i < NUM_OPTIONS; i++) {
			JButton button = new JButton(AnswerNode.getText(i));
			button.setSize(150,40);
			button.setLocation((getWidth()/2) - (150/2), 120 + (50*i));
			button.addActionListener(this);
			buttons.add(new Button(button,AnswerNode.getText(i),AnswerNode.getValue(i)));
			add(button);
		}
		
		questionDisplay = new JTextArea();
		questionDisplay.setSize(350,55);
		questionDisplay.setLineWrap(true);
		questionDisplay.setWrapStyleWord(true);
		questionDisplay.setLocation((getWidth()/2) - (350/2), 10);
		questionDisplay.setEditable(false);
		add(questionDisplay);
		
		display = new TimeDisplay();
		display.setSize(160,30);
		display.setLocation((getWidth()/2) - (160/2), 75);
		new Thread(display).start();
		add(display);
		
		openScoreList = new JButton("Ver Puntajes");
		openScoreList.setSize(200,60);
		openScoreList.addActionListener(this);
		openScoreList.setLocation((getWidth()/2) - (200/2), 375);
		add(openScoreList);
	}
	
	public void setDisplayText(String text) {
		questionDisplay.setText(text);
	}
	
	private Button getButton(JButton triggeringButton) {
		Button value = null;
		for (int i = 0; i < buttons.size(); i++) {
			if (triggeringButton == buttons.get(i).getButton()) {
				value = buttons.get(i);
			}
		}
		return value;
	}
	
	private Button getButton(Object triggeringButton) {
		return getButton((JButton) triggeringButton);
	}
	
	private void guessAnswer(Button button) {
		if (button.getValue() == AnswerNode.VALUE_NO) {
			ref.wrongGuess();
		} else {
			ref.correctGuess();
		}
	}
	
	private void questionAnswer(Button button) {
		ref.setAnswer(button.getValue());
		ref.nextStep();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == openScoreList) {
			new ScoreWindow();
		} else {
			if (newGameCheck) {
				if (getButton(e.getSource()).getValue() == AnswerNode.VALUE_YES) {
					setNewGameCheck(false);
					ref.start();
				} else {
					System.exit(0);
				}
			} else if (guessing) {
				guessAnswer(getButton(e.getSource()));
			} else {
				questionAnswer(getButton(e.getSource()));
			}
		}
	}

	/**
	 * @return the guessing
	 */
	public boolean isGuessing() {
		return guessing;
	}

	/**
	 * @param guessing the guessing to set
	 */
	public void setGuessing(boolean guessing) {
		this.guessing = guessing;
		changeButtonStates(guessing);
	}
	
	private void changeButtonStates(boolean YesNoQuestion) {
		if (YesNoQuestion) {
			for (int i = 1; i < buttons.size()-1; i++) {
				buttons.get(i).getButton().setEnabled(false);
			}
		} else {
			for (int i = 0; i < buttons.size(); i++) {
				buttons.get(i).getButton().setEnabled(true);
			}
		}
		
	}
	
	/**
	 * @return the newGameCheck
	 */
	public boolean isNewGameCheck() {
		return newGameCheck;
	}

	/**
	 * @param newGameCheck the newGameCheck to set
	 */
	public void setNewGameCheck(boolean newGameCheck) {
		this.newGameCheck = newGameCheck;
		changeButtonStates(newGameCheck);
	}
	
	private class TimeDisplay extends Canvas implements Runnable {

		
		@Override
		public void paint(Graphics g) {
			g.setFont(new Font("Arial",Font.BOLD,30));
			double curTime = ref.getElapsedTime();
			curTime = Math.floor(curTime * (Math.pow(10,-8)));
			curTime = curTime /10;
			g.drawString(curTime+"s", 1, getHeight()-1);
		}

		@Override
		public void run() {
			while(true) {
				repaint();
				try{
					Thread.sleep(100);
				}catch (Exception ex) {
					
				}
			}
		}
		
	}
	
}
