/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adivinador.node;

/**
 *
 * @author Andres
 */
public class QuestionNode extends Node {
	
	private final String question;

	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}

	public QuestionNode(String question) {
		this.question = question;
	}

	
}
