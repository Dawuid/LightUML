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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.lightuml.core.IErrorMessages;
import org.lightuml.core.LightUMLCoreException;

/**
 * <p>
 * Converts .dot file to graphical form. Delegates the actual conversion to
 * graphConverter of LightUMLCorePlugin.
 * </p>
 * 
 * @author Antti Hakala
 */
public class DotToGraphics extends LightUMLJob implements IErrorMessages {
	/**
	 * Constructor
	 * 
	 * @param t
	 *            Reference time. The .dot file used has to be newer than this.
	 */
	public DotToGraphics(long t) {
		super(t, "Converting to graphics");
	}

	/**
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IStatus run(IProgressMonitor monitor) {
		try {
			corePlugin.getGraphConverter().dotToGraphics(corePlugin.getDotFilePaths(), monitor);
			monitor.done();
		} catch (LightUMLCoreException e) {
			return errorStatus(e);
		}
		if(monitor.isCanceled())
			return cancelStatus();
		return Status.OK_STATUS;
	}
}