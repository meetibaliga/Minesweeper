package edu.colorado.cs.controller;

import edu.colorado.cs.Memo;

/*
 * Interface used to implement the Memo tasks
 * COMMAND PATTERN : Interface
 */
public interface MessageHandler {
	public int run(Memo message);
}
