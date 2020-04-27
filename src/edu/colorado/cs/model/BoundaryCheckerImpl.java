package edu.colorado.cs.model;

//Proxy Pattern Implementation
public class BoundaryCheckerImpl implements BoundaryChecker {
	public boolean boundaryProxy(int h, int w) {
		if (h < 0 || 12 <= h || w < 0 || 12 <= w)
			return false;
		return true;
	}

}
