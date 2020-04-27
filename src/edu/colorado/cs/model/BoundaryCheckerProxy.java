package edu.colorado.cs.model;

//Proxy Pattern Class
public class BoundaryCheckerProxy implements BoundaryChecker {
	private static BoundaryChecker object;
	public boolean boundaryProxy(int h, int w) {
		if (object == null) {
            object = new BoundaryCheckerImpl();
        }
        return object.boundaryProxy(h,w);
	}

}
