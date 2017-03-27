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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author Andres
 */
public class LearnWindow extends JFrame implements ActionListener  {

	public static final int NUM_OPTIONS = 5;
	
	JTextField inputName;
	JTextField inputQuestion;
	JLabel displayAnswer;
	private NodeList<Button> buttons;
	Handler ref;

	public LearnWindow(Handler main) {
		setLayout(null);
		setLocationRelativeTo(null);
		setSize(300,400);
		setTitle("Agregar Respuesta");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		
		this.ref = main;
		createInterface();
		
		setVisible(true);
	}
	
	public void createInterface() {
		
		inputName = new JTextField("Nombre del Juego");
		inputName.setSize(180, 30);
		inputName.setLocation((getWidth()/2) - (180/2), 20);
		add(inputName);
		
		inputQuestion = new JTextField("Nueva pregunta");
		inputQuestion.setSize(180, 30);
		inputQuestion.setLocation((getWidth()/2) - (180/2), 50);
		add(inputQuestion);
		
		displayAnswer = new JLabel("Respuesta a la pregunta:");
		displayAnswer.setSize(180, 30);
		displayAnswer.setLocation((getWidth()/2) - (180/2), 90);
		add(displayAnswer);
		
		buttons = new NodeList<>();
		for (int i = 0; i < NUM_OPTIONS; i++) {
			String text = AnswerNode.getText(i);
			float multiplier = 1;
			if (i == 2) {
				multiplier = 1.5f;
				text = "Solo agregar respuesta";
			}
			JButton button = new JButton(text);
			button.setSize((int)(150*multiplier),40);
			button.setLocation((getWidth()/2) - ((int)(150*multiplier)/2), 120 + (50*i));
			button.addActionListener(this);
			buttons.add(new Button(button,AnswerNode.getValue(i)));
			add(button);
		}
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
	
	public boolean canProceed() {
		return (!inputName.getText().isEmpty()) && (!inputQuestion.getText().isEmpty()) && (inputName.getText().compareTo("Nombre del Juego") != 0);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (canProceed()) {
			if ( getButton(e.getSource()).getValue() == AnswerNode.VALUE_IDK ) {
				ref.learn(getButton(e.getSource()).getValue(),inputName.getText());
			} else {
				ref.learn(inputQuestion.getText(),getButton(e.getSource()).getValue(),inputName.getText());
			}
			this.dispose();
		}
	}
	
}
