/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adivinador.node;

import javax.swing.JButton;

/**
 *
 * @author Andres
 */
public class Button extends Node {
	
	private JButton button;
	private float value;
	private String text;

	public Button(JButton button, String text, float value) {
		this.button = button;
		this.value = value;
		this.text = text;
	}

	public Button(JButton button, float value) {
		this.button = button;
		this.value = value;
	}

	/**
	 * @return the button
	 */
	public JButton getButton() {
		return button;
	}

	/**
	 * @param button the button to set
	 */
	public void setButton(JButton button) {
		this.button = button;
	}

	/**
	 * @return the value
	 */
	public float getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(float value) {
		this.value = value;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	
	
}
