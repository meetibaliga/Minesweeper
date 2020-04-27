package edu.colorado.cs;

/*
 * This message object is created when a right mouse click is used on square.
 * Used by the right mouse click listener in view.java
 */

public class RightClickMemo extends Memo {
	int height;
	int width;

	public RightClickMemo(int height, int width) {
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
