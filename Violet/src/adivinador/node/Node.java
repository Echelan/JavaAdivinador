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
public abstract class Node {
	
	protected Node link;

	/**
	 * @return the link
	 */
	public Node getLink() {
		return link;
	}

	/**
	 * @param link the link to set
	 */
	public void setLink(Node link) {
		this.link = link;
	}
	
}
