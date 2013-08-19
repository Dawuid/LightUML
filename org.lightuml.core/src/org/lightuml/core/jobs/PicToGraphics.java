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

package org.lightuml.core.jobs;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.lightuml.core.IBuildConstants;
import org.lightuml.core.LightUMLCoreException;

/**
 * <p>
 * Converts .pic file to graphics form.
 * </p>
 * 
 * @author antti
 * @hidden
 * 
 */
public class PicToGraphics extends LightUMLJob implements IBuildConstants {
	/**
	 * <p>
	 * The .pic file to convert to graphics.
	 * </p>
	 */
	private IPath picFilePath;

	/**
	 * 
	 * @param time
	 *            Reference time.
	 * @param f
	 *            The .pic file to convert.
	 */
	public PicToGraphics(long time, IPath p) {
		super(time, "Converting to graphics");
		picFilePath = p;
	}

	/**
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IStatus run(IProgressMonitor monitor) {
		try {
			corePlugin.getGraphConverter().picToGraphics(picFilePath, monitor);
			monitor.done();
		} catch (LightUMLCoreException e) {
			return errorStatus(e);
		}

		if (monitor.isCanceled())
			return cancelStatus();
		return Status.OK_STATUS;
	}
}
