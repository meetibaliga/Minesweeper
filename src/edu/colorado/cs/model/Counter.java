package edu.colorado.cs.model;

/*
 * Class to maintain state of number of mines.
 */
public class Counter {

	private int numOfMines;

	public Counter(int numbOfMines) {
		this.numOfMines = numbOfMines;
	}

	public void decreaseMine() {
		numOfMines--;
	}

	public void increaseMine() {
		numOfMines++;
	}

	public int getCurrentNumOfMine() {
		return this.numOfMines;
	}
}
