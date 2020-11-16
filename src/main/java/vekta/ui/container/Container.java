package vekta.ui.container;

import vekta.ui.Element;

import java.util.ArrayList;

public abstract class Container implements Element {

	private float x, y;
	private int sizeX, sizeY;
	private ArrayList<ContainerMember> elements;

	public Container(int x, int y, int sizeX, int sizeY) {
		this.x = x;
		this.y = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		elements = new ArrayList<>();
	}

	protected float getX() {
		return x;
	}

	protected void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	protected void setY(float y) {
		this.y = y;
	}

	protected int getSizeX() {
		return sizeX;
	}

	protected void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}

	protected int getSizeY() {
		return sizeY;
	}

	protected void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}

	/**
	 * Add UI element to container
	 */
	public void addElement(ContainerMember e) {
		elements.add(e);
	};

	/**
	 * Remove an element from the container
	 * @param e Element to remove
	 */
	public void removeElement(ContainerMember e) {
		elements.remove(e);
	}

	/**
	 * Remove the element at the provided index
	 * @param i index to remove
	 */
	public void removeElementAtIndex(int i) {
		elements.remove(i);
	}

	/**
	 * Check whether this container already has this element
	 * @param e Element to check for
	 * @return Boolean value: whether or not this element exists
	 */
	public boolean contains(ContainerMember e) {
		return elements.contains(e);
	}

	/**
	 * Get the element at index i
	 * @param i
	 * @return
	 */
	public ContainerMember getElement(int i) {
		return elements.get(i);
	}

	/**
	 * Get the number of elements in this container
	 */
	public int getElementCount() {
		return elements.size();
	}

	/**
	 * Render each element in the container
	 */
	public void renderElements() {
		for(ContainerMember e : elements) {
			e.render();
		}
	}
}
