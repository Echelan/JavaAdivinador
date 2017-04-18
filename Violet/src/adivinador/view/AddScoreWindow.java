/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adivinador.view;

import adivinador.model.Handler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author Andres
 */
public class AddScoreWindow extends JFrame implements ActionListener {

//	JTextField inputName;
	JFormattedTextField inputName;
	int score;
	JLabel displayScore;
	JLabel displayTime;
	JButton addBtn;
	Handler ref;
	long elapsedTime;
	
	public AddScoreWindow(Handler main, int score, long elapsedTime) {
		setLayout(null);
		setSize(300,200);
		setLocationRelativeTo(null);
		setTitle("Ingresar Puntaje");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		
		this.score = score;
		this.ref = main;
		this.elapsedTime = elapsedTime;
		
		createInterface();
		
		setVisible(true);
	}
	
	private void createInterface() {
		try {
			MaskFormatter userName = new MaskFormatter("UUUUUUU");
//			inputName = new JTextField("Nombre de Usuario");
			userName.setPlaceholderCharacter('A');
			inputName = new JFormattedTextField(userName);
			inputName.setSize(180, 30);
			inputName.setLocation((getWidth()/2) - (180/2), 10);
			add(inputName);

			displayScore = new JLabel("Puntaje: " + score);
			displayScore.setSize(120, 30);
			displayScore.setHorizontalAlignment(SwingConstants.CENTER);
			displayScore.setLocation((getWidth()/2) - (120/2), 40);
			add(displayScore);
			
			double curTime = elapsedTime;
			curTime = Math.floor(curTime * (Math.pow(10,-8)));
			curTime = curTime /10;
			displayScore = new JLabel(curTime+" segs");
			displayScore.setSize(120, 30);
			displayScore.setHorizontalAlignment(SwingConstants.CENTER);
			displayScore.setLocation((getWidth()/2) - (120/2), 70);
			add(displayScore);

			addBtn = new JButton("Agregar");
			addBtn.setSize(150,40);
			addBtn.setLocation((getWidth()/2) - (150/2), 120);
			addBtn.addActionListener(this);
			add(addBtn);
		} catch (Exception e) {
			this.dispose();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
//		if (invalidName(inputName.getText())) {
//			JOptionPane.showMessageDialog(null, "El nombre de usuario no puede tener mas de 7 caracteres, ni caracteres especiales.","Usuario invalido",JOptionPane.ERROR_MESSAGE);
//		} else {
			try{
				inputName.commitEdit();
			} catch (Exception e) {
				
			}
					
			adivinador.data.NIC.LIST_SCORES.add(new adivinador.node.ScoreNode((String)inputName.getValue(), score));
			try {
				adivinador.data.NIC.saveAll();
			} catch (Exception e) {

			}
			this.dispose();
//		}
	}
	
}
