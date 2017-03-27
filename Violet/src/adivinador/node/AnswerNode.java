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
public class AnswerNode extends Node {
	
	public static final float VALUE_YES = 1f;
	public static final float VALUE_PROB = 0.65f;
	public static final float VALUE_PROB_NO = 0.35f;
	public static final float VALUE_NO = 0f;
	public static final float VALUE_UNANSWERED = -1f;
	public static final float VALUE_IDK = -2f;
	
	public static final String TEXT_YES = "Si";
	public static final String TEXT_PROB = "Parcialmente si";
	public static final String TEXT_IDK = "No lo se";
	public static final String TEXT_PROB_NO = "Parcialmente no";
	public static final String TEXT_NO = "No";
	public static final String TEXT_UNANSWERED = "?";
	
	private float answer;
	
	public static float getValue(int index) {
		float returnValue;
		switch(index) {
			case 0:
				returnValue = VALUE_YES;
				break;
			case 1:
				returnValue = VALUE_PROB;
				break;
			case 2:
				returnValue = VALUE_IDK;
				break;
			case 3:
				returnValue = VALUE_PROB_NO;
				break;
			case 4:
				returnValue = VALUE_NO;
				break;
			default:
				returnValue = VALUE_UNANSWERED;
				break;
		}
		return returnValue;
	}
	
	public static String getText(int index) {
		String returnValue;
		switch(index) {
			case 0:
				returnValue = TEXT_YES;
				break;
			case 1:
				returnValue = TEXT_PROB;
				break;
			case 2:
				returnValue = TEXT_IDK;
				break;
			case 3:
				returnValue = TEXT_PROB_NO;
				break;
			case 4:
				returnValue = TEXT_NO;
				break;
			default:
				returnValue = TEXT_UNANSWERED;
				break;
		}
		return returnValue;
	}

	public AnswerNode(float answer) {
		this.answer = answer;
	}

	/**
	 * @return the answer
	 */
	public float getAnswer() {
		return answer;
	}

	/**
	 * @param answer the answer to set
	 */
	public void setAnswer(float answer) {
		this.answer = answer;
	}
	
	public boolean isAnswered(){
		return (this.answer != VALUE_UNANSWERED);
	}
	
	public boolean isKnown(){
		return (this.answer != VALUE_IDK);
	}
	
	public boolean isValid(){
		return (this.answer >= 0);
	}
	
	public boolean isPositive() {
		return (this.answer > 0.5f) && isKnown() && isAnswered();
	}
	
	public boolean isNegative() {
		return (this.answer < 0.5f) && isKnown() && isAnswered();
	}

}
