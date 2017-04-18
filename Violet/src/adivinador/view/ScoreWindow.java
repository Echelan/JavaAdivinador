/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adivinador.view;

import adivinador.model.Handler;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Andres
 */
public class ScoreWindow extends JFrame {
	
	ArrayList<JLabel> nameDisplay;
	ArrayList<JLabel> scoreDisplay;
	JLabel title;
	Handler ref;
	
	public ScoreWindow() {
		setLayout(null);
		setLocationRelativeTo(null);
		setSize(250,360);
		setTitle("Puntajes");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		
		createInterface();
		
		setVisible(true);
	}
	
	private void createInterface() {
		int size = adivinador.data.NIC.MAX_SCORES;
		nameDisplay = new ArrayList<>();
		scoreDisplay = new ArrayList<>();
		
		title = new JLabel("Lista de Puntajes");
		title.setSize(100,30);
		title.setLocation(getWidth()/2 - (100/2), 5);
		add(title);
		
		for (int i = 0; i < size; i++) {
			String nameValue = adivinador.data.NIC.LIST_SCORES.get(i).getName();
			int scoreValue = adivinador.data.NIC.LIST_SCORES.get(i).getScore();
			
			String placement = "#";
			if (i+1 < 10) {
				placement = placement + "0";
			}
			placement = placement + (i+1)+": ";
			
			JLabel name = new JLabel(placement+nameValue);
			name.setSize(160,30);
			name.setLocation(10, 30 + (30*i));
			nameDisplay.add(name);
			add(name);
			
			JLabel score = new JLabel(scoreValue + " puntos");
			score.setSize(100,30);
			score.setLocation(170, 30 + (30*i));
			scoreDisplay.add(name);
			add(score);
		}
	}
}
