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

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * <p>
 * An interface for converting .dot and .pic files to graphical form
 * </p>
 * <p>
 * Output has to be of format
 * LightUMLCorePlugin.getGraphBuildProperty(P_GRAPHICS_FORMAT).
 * P_GRAPHICS_FORMAT is found in interface IGraphBuildProperties.
 * </p>
 * <p>
 * A class implementing this interface also has to return the location of
 * generated graphics file with getGraphicsFilePath().
 * </p>
 * <p>
 * Progress monitor is provided for canceling support.
 * </p>
 * <p>
 * 	Note: Has to delete the .dot files after converting. 
 * </p>
 * @author Antti Hakala
 */
public interface IGraphConverter {
	/**
	 * <p>
	 * Convert .dot file to some graphical form for viewing.
	 * </p>
	 * 
	 * @param pm
	 *            A progress monitor to use.
	 * @param dotFilePaths paths to .dot files that must be converted.
	 * @throws LightUMLCoreException
	 */
	public void dotToGraphics(IPath[] dotFilePaths, IProgressMonitor pm)
			throws LightUMLCoreException;

	/**
	 * <p>
	 * Convert a .pic file to graphical form.
	 * </p>
	 * 
	 * @param pm
	 *            A progress monitor to use.
	 * @throws LightUMLCoreException
	 */
	public void picToGraphics(IPath picFilePath, IProgressMonitor pm)
			throws LightUMLCoreException;

	/**
	 * <p>
	 * Get path(s) to the converted graphics file(s).
	 * </p>
	 * 
	 * @return the paths to the (coverted) graphics file(s)
	 */
	public IPath[] getGraphicsFilePaths() throws LightUMLCoreException;
}
