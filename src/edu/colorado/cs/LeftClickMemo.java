package edu.colorado.cs;

/*
 * This message object is created when a left mouse click is used on a square.
 * Used by the left mouse click listener in view.java
 */
public class LeftClickMemo extends Memo {
	int height;
	int width;

	public LeftClickMemo(int height, int width) {
		this.height = height;
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
}
