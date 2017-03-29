/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adivinador.view;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author Andres
 */
public class GuideWindow  extends JFrame implements ActionListener {
	
	JButton prevSlide;
	JButton nextSlide;
	
	JLabel slideDisplay;
	JLabel slideText;
	
	int currentSlide;
	final int maxSlide = 6;
	
	public GuideWindow() {
		setLayout(null);
		setSize(700,700);
		setLocationRelativeTo(null);
		setTitle("Guia");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		
		createInterface();
		
		setVisible(true);
	}
	
	private void createInterface() {
		currentSlide = 1;
		
		slideDisplay = new JLabel();
		slideDisplay.setSize(600, 600);
		slideDisplay.setLocation(getWidth()/2 - 700/2, 20);
		add(slideDisplay);
		refreshSlide();
		
		slideText = new JLabel(currentSlide + "/" + maxSlide);
		slideText.setSize(200, 50);
		slideText.setHorizontalAlignment(SwingConstants.CENTER);
		slideText.setLocation(getWidth()/2 - 200/2, getHeight()-90);
		add(slideText);
		
		nextSlide = new JButton("Siguiente");
		nextSlide.setSize(200, 50);
		nextSlide.setLocation(getWidth() - 250, getHeight() - 90);
		nextSlide.addActionListener(this);
		add(nextSlide);
		
		prevSlide = new JButton("Anterior");
		prevSlide.setSize(200, 50);
		prevSlide.setEnabled(false);
		prevSlide.setLocation(50, getHeight() - 90);
		prevSlide.addActionListener(this);
		add(prevSlide);
		
	}
	
	private void refreshSlide() {
		try {
			Image image = ImageIO.read(new File("guide/"+currentSlide+".png"));
			slideDisplay.setIcon(new ImageIcon(image));
			slideText.setText(currentSlide + "/" + maxSlide);
		} catch(Exception e) {
		}
	}

	private void goNext() {
		if (currentSlide < maxSlide) {
			currentSlide++;
			refreshSlide();
			if (currentSlide == maxSlide) {
				nextSlide.setEnabled(false);
			}
			prevSlide.setEnabled(true);
		}
	}

	private void goPrev() {
		if (currentSlide > 1) {
			currentSlide--;
			refreshSlide();
			if (currentSlide == 1) {
				prevSlide.setEnabled(false);
			}
			nextSlide.setEnabled(true);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == nextSlide) {
			goNext();
		} else if (e.getSource() == prevSlide) {
			goPrev();
		}
	}
	
}
