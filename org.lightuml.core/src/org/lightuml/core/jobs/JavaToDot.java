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

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.lightuml.core.IBuildConstants;
import org.lightuml.core.IGraphBuildProperties;
import org.lightuml.core.LightUMLCoreException;

/**
 * <p>
 * Converts java source files to a single .dot format file.
 * </p>
 * 
 * @author Antti Hakala
 */
public class JavaToDot extends LightUMLJob implements IBuildConstants,
		IGraphBuildProperties {
	/**
	 * <p>
	 * Path under which to find source files.
	 * </p>
	 */
	private IPath sourcePath;

	/**
	 * <p>
	 * Package name.
	 * </p>
	 */
	private String packageName;

	/**
	 * Constructor.
	 * 
	 * @param time
	 *            The reference time.
	 * @param sp
	 *            Path under which to find source files.
	 * @param pn
	 *            Package name or null.
	 */
	public JavaToDot(long time, IPath sp, String pn) {
		super(time, "Running UmlGraph");
		sourcePath = sp;
		packageName = pn;
	}

	/**
	 * The source path and the scope of the graph generation is delivered via a
	 * property file. Argument -D<path> tends to get broken.
	 * 
	 * @throws IOException
	 * @throws LightUMLCoreException
	 */
	private void deliverAntRunSettings() throws IOException,
			LightUMLCoreException {
		Properties prop = new Properties();
		String scope;

		// define the scope of the graph generation
		if ((packageName == null)
				|| corePlugin.getGraphBuildProperty(P_RECURSE_PACKAGES).equals(
						"true"))
			scope = "**/*.java";
		else
			scope = "*.java";

		prop.setProperty("scope", scope);
		prop.setProperty("source-path", sourcePath.toString());
		IPath p = corePlugin.getStateLocation().append(STATELOC_RUNSETTINGS);
		prop.store(new FileOutputStream(p.toFile()),
				"Ant run settings for java-to-dot");
	}

	/**
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IStatus run(IProgressMonitor pm) {
		try {
			deliverAntRunSettings();
			AntRunner runner = corePlugin.getBuildFileRunner();
			runner.setExecutionTargets(new String[] { "java-to-dot" });
			runner.run(pm);
			pm.done();

		} catch (CoreException e) {
			return errorStatus(e);
		} catch (IOException e) {
			return errorStatus(e);
		} catch (LightUMLCoreException e) {
			return errorStatus(e);
		}
		if(pm.isCanceled())
			return cancelStatus();
		return Status.OK_STATUS;
	}
}