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
public class ScoreNode extends Node {
	
	private String name;
	private int score;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}

	public ScoreNode(String name, int score) {
		this.name = name;
		this.score = score;
	}

	public boolean compareTo(Node object) {
		boolean result = false;
		if (object instanceof ScoreNode) {
			result = (((ScoreNode) object).score == this.score);
			result = result && (((ScoreNode) object).name.equals(this.name));
		}
		return result;
	}

}
