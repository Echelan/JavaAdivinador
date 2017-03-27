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
public class Possibility extends NodeList<AnswerNode> {
	
	public static final int TYPE_USER = 0;
	public static final int TYPE_GUESS_POSSIBLE = 1;
	public static final int TYPE_GUESS_IMPOSSIBLE = 2;

	private int type;
	private String name;

	public Possibility(int type) {
		super();
		
		this.type = type;
	}

	public Possibility(String name) {
		super();
		
		this.name = name;
	}

	public Possibility(int type, String name) {
		super();
		
		this.type = type;
		this.name = name;
	}

	public Possibility(String name, int type) {
		super();
		
		this.type = type;
		this.name = name;
	}
	
	public void revisePossibility(Possibility base) {
		for (int i = 0; i < this.size(); i++) {
			if (this.get(i) != base.get(i) && base.get(i).isValid()) {
				float delta = 1f;
				if (this.get(i).getAnswer() < base.get(i).getAnswer()) {
					delta = delta * -1f;
				}
				
				if (Math.abs(this.get(i).getAnswer() - base.get(i).getAnswer()) > 0.4f) {
					delta = delta * .1f;
				} else {
					delta = delta * .05f;
				}
				
				float newValue;
				if (this.get(i).isValid()) {
					newValue = this.get(i).getAnswer() + delta;
				} else {
					newValue = base.get(i).getAnswer();
				}
				
				if (newValue < 0.0f) {
					newValue = 0.0f;
				} else if (newValue > 1.0f) {
					newValue = 1.0f;
				}
				
				this.get(i).setAnswer(newValue);
			}
		}
	}
	
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
	
	public void fillAnswers(int target) {
		while (this.size() < target) {
			this.add(new AnswerNode(-1));
		}
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	public boolean isPossible() {
		return (this.type == TYPE_GUESS_POSSIBLE);
	}
	
	public boolean isImpossible() {
		return !isPossible();
	}
	
}
