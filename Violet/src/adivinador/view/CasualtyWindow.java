/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adivinador.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 *
 * @author Andres
 */
public class CasualtyWindow extends JFrame implements ActionListener {

	JTextArea display;
	int id1;
	int id2;
	JButton acceptBtn;
	JButton rejectBtn;
	
	public CasualtyWindow(int id1, int id2) {
		setLayout(null);
		setLocationRelativeTo(null);
		setSize(300,300);
		setTitle("Resultados Parecidos");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		
		this.id1 = id1;
		this.id2 = id2;
		
		createInterface();
		
		setVisible(true);
	}
	
	private String getNameID(int id) {
		return adivinador.data.NIC.LIST_ANSWERS.get(id).getName();
	}
	
	private void createInterface() {
		display = new JTextArea(getNameID(id1) + " y " + getNameID(id2) + " parecen tener nombres similares. Son el mismo juego?");
		display.setSize(280,80);
		display.setLineWrap(true);
		display.setWrapStyleWord(true);
		display.setLocation((getWidth()/2) - (280/2), 10);
		display.setEditable(false);
		add(display);
		
		acceptBtn = new JButton("Si");
		acceptBtn.setSize(150,40);
		acceptBtn.setLocation((getWidth()/2) - (150/2), 100);
		acceptBtn.addActionListener(this);
		add(acceptBtn);

		rejectBtn = new JButton("No");
		rejectBtn.setSize(150,40);
		rejectBtn.setLocation((getWidth()/2) - (150/2), 150);
		rejectBtn.addActionListener(this);
		add(rejectBtn);
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == acceptBtn) {
			int size = adivinador.data.NIC.LIST_ANSWERS.size();
			if (size > id2 && size > id1) {
				adivinador.data.NIC.LIST_ANSWERS.get(id1).revisePossibility(adivinador.data.NIC.LIST_ANSWERS.get(id2));
				adivinador.data.NIC.LIST_ANSWERS.remove(id2);
				adivinador.data.NIC.saveAll();
			}
		}
		this.dispose();
	}
	
}
