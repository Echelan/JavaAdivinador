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
public class NodeList<T extends Node> extends Node {

	private T listStart;
	private int size;
	
	public NodeList() {
		this.size = 0;
	}
	
	public T getLastNode() {
		T p = listStart;
		if (p != null) {
			while (p.getLink() != null) {
				p = (T) p.getLink();
			}
		}
		return p;
	}
	
	public void add(T node) {
		this.size++;
		if (this.size > 1) {
			this.getLastNode().setLink(node);
		} else {
			listStart = node;
		}
	}
	
	public void remove(int index) {
		if (index < this.size) {
			this.size--;
			if (index == 0) {
				listStart = (T) listStart.link;
			} else {
				T p = listStart;
				for (int i = 0; i < index-1; i++) {
					p = (T) p.getLink();
				}
				p.setLink(p.getLink().getLink());
			}
		}
	}
	
	public T get(int index) {
		T p = null;
		if (index < size) {
			p = listStart;
			for (int i = 0; i < index; i++) {
				p = (T) p.getLink();
			}
		}
		return p;
	}
	
	public boolean isEmpty() {
		return (this.size == 0);
	}
	
	public int size() {
		return this.size;
	}
	
	public void clear() {
		listStart = null;
		this.size = 0;
	}
	
}
