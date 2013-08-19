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

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.lightuml.core.IGraphBuildProperties;
import org.lightuml.core.LightUMLCoreException;

/**
 * <p>
 * This job adds the generated graphics file to project.
 * </p>
 * 
 * @author Antti Hakala
 */
public class AddGraphToProject extends LightUMLJob implements
		IGraphBuildProperties {
	/**
	 * <p>
	 * Project to add the graph to.
	 * </p>
	 */
	private IProject project;

	/**
	 * <p>
	 * Package name, or null if no package selected
	 * </p>
	 */
	private String packageName;

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * 
	 * @param p
	 *            Project to add the graph to.
	 * @param t
	 *            Reference time. Graph to add has to be newer than this.
	 * @param pn
	 *            Package name, or null if no package selected.
	 */
	public AddGraphToProject(long t, IProject p, String pn) {
		// Gets a lock to project p
		// A lock to the whole project is ok, since we might need to create a
		// folder.
		super(t, "Adding a graph to project " + p.getName(), p);
		project = p;
		packageName = pn;
	}

	/**
	 * <p>
	 * Internal method for getting the name of the graph file.
	 * <p>
	 * 
	 * @param p Path to the generated graph file.
	 * @return name of the graph file in the project
	 * @throws LightUMLCoreException
	 */
	private IPath getGraphFileName(IPath p) throws LightUMLCoreException {
		IPath graphFileName;
		// TODO: clean up
		String name = p.removeFileExtension().lastSegment().toString();
		if(!name.equals("graph")) // this happens when generating multiple views at once
			return new Path(name).addFileExtension(corePlugin
					.getGraphBuildProperty(P_GRAPHICS_FORMAT));
		
		// check if the graph should be named like the package
		// or project(!)

		if (corePlugin.getGraphBuildProperty(P_USE_PACKAGE_NAME).equals("true")) {
			if (packageName != null)
				graphFileName = new Path(packageName);
			else
				graphFileName = new Path(project.getName());
		} else
			graphFileName = new Path(corePlugin
					.getGraphBuildProperty(P_GRAPH_FILE_NAME));

		// add extension
		graphFileName = graphFileName.addFileExtension(corePlugin
				.getGraphBuildProperty(P_GRAPHICS_FORMAT));

		return graphFileName;
	}

	/**
	 * <p>
	 * Internal method for getting the graph file. If a folder is created, it is
	 * marked as derived.
	 * </p>
	 * 
	 * @param p Path to the generated graph file.
	 * @return the graph file in the project that represents the generated
	 *         diagram
	 * @throws CoreException
	 * @throws LightUMLCoreException
	 */
	private IFile getGraphFile(IPath p) throws CoreException, LightUMLCoreException {
		IPath graphFileName = getGraphFileName(p), projPath = project
				.getFullPath(), outDirPath = new Path(corePlugin
				.getGraphBuildProperty(P_PROJECT_OUTPUT_DIR));
		IFile f;

		// check where to add the graph
		if (projPath.append(outDirPath).equals(projPath))
			f = project.getFile(graphFileName);
		else {
			IFolder folder = project.getFolder(outDirPath);
			if (!folder.exists()) {
				folder.create(true, true, null);
				folder.setDerived(true);
			}
			f = folder.getFile(graphFileName);
		}
		return f;
	}

	/**
	 * <p>
	 * Adds the generated graph file to the project, marking it a derived
	 * resource.
	 * </p>
	 * 
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 * @param monitor
	 *            The progress monitor to be used.
	 * @return Status with OK severity if successful, otherwise ERROR severity.
	 */
	protected IStatus run(IProgressMonitor monitor) {
		try {
			
			IPath[] filePaths = corePlugin.getGraphConverter().getGraphicsFilePaths();
			for(int i=0; i < filePaths.length; i++)
			{
				IFile f = getGraphFile(filePaths[i]);
				FileInputStream fis = new FileInputStream(filePaths[i].toFile());

				if (f.exists())
					f.setContents(fis, true, false, monitor);
				else
					f.create(fis, true, monitor);

				f.setDerived(true);
				filePaths[i].toFile().delete(); // delete from stateloc. after adding to project
			}
			monitor.done();
		} catch (LightUMLCoreException e) {
			return errorStatus(e);
		} catch (CoreException e) {
			return errorStatus(e);
		} catch (FileNotFoundException e) {
			return errorStatus(e);
		}
		if (monitor.isCanceled())
			return cancelStatus();
		return Status.OK_STATUS;
	}
}