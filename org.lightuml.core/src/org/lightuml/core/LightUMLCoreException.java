/*
 * (C) Copyright 2005 Antti Hakala
 * 
 *  Permission to use, copy, and distribute this software and its documentation 
 *  for any purpose and without fee is hereby granted, provided that the above 
 *  copyright notice appear in all copies and that both that copyright notice and 
 *  this permission notice appear in supporting documentation.
 *  
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES,
 *  INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE. 
 */

package org.lightuml.core;

/**
 * <p>
 * An exception of LightUMLCorePlugin.
 * </p>
 * 
 * @author Antti Hakala
 */
public class LightUMLCoreException extends Exception {
	private static final long serialVersionUID = -7915803826993423881L;

	/**
	 * <p>
	 * Contructor.
	 * </p>
	 * 
	 * @param e
	 *            The cause.
	 */
	public LightUMLCoreException(Throwable e) {
		super(e);
	}

	/**
	 * <p>
	 * Constructor with a message.
	 * </p>
	 * 
	 * @param message
	 *            Exception message.
	 */
	public LightUMLCoreException(String message) {
		super(new Throwable(message));
	}
}
