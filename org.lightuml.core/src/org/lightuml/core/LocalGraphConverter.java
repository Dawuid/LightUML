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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

/**
 * <p>
 * Implements IGraphConverter using locally installed GraphViz tools (dot).
 * </p>
 * 
 * @author Antti Hakala
 */
public class LocalGraphConverter implements IGraphConverter, IBuildConstants,
		IGraphBuildProperties {
	/**
	 * <p>
	 * Static instance of LocalGraphConverter.
	 * </p>
	 */
	private static LocalGraphConverter instance = null;

	/**
	 * <p>
	 * Protected constructor (singleton pattern).
	 * </p>
	 */
	private LocalGraphConverter() {
	};

	/**
	 * <p>
	 * Static accessor.
	 * </p>
	 * 
	 * @return LocalGraphConverter
	 */
	static public LocalGraphConverter instance() {
		if (instance == null)
			instance = new LocalGraphConverter();
		return instance;
	}

	private void deliverDotFileName(String name) throws IOException,
			LightUMLCoreException {
		Properties prop = new Properties();

		prop.setProperty("dot-file-name", name);
		IPath p = LightUMLCorePlugin.getDefault().getStateLocation().append(
				STATELOC_RUNSETTINGS);
		prop.store(new FileOutputStream(p.toFile()),
				"Ant run settings for dot-to-graphics");
	}

	/**
	 * <p>
	 * Convert .dot file to graphics format.
	 * </p>
	 * 
	 * @param dotFilePaths
	 *            Paths to .dot files that must be converted.
	 * @param pm
	 *            The progress monitor to be used.
	 * @throws LightUMLCoreException
	 * @see IGraphConverter#dotToGraphics(IProgressMonitor)
	 */
	public void dotToGraphics(IPath[] dotFilePaths, IProgressMonitor pm)
			throws LightUMLCoreException {
		for (int i = 0; i < dotFilePaths.length; i++) {
			try {
				deliverDotFileName(dotFilePaths[i].removeFileExtension()
						.lastSegment().toString());
				AntRunner runner = LightUMLCorePlugin.getDefault()
						.getBuildFileRunner();
				runner.setExecutionTargets(new String[] { "dot-to-graphics" });
				runner.run(pm);
			} catch (CoreException e) {
				throw new LightUMLCoreException(e);
			} catch (IOException e) {
				throw new LightUMLCoreException(e);
			}
		}
	}

	/**
	 * <p>
	 * Path to the graphics file(s) generated.
	 * </p>
	 * 
	 * @see org.lightuml.core.IGraphConverter#getGraphicsFilePath()
	 * @return The path to the graphics file (to be) generated.
	 */
	public IPath[] getGraphicsFilePaths() throws LightUMLCoreException {
		LightUMLCorePlugin cp = LightUMLCorePlugin.getDefault();

		File outputDir = cp.getStateLocation().append(STATELOC_OUTPUT_DIR)
				.toFile();
		String[] graphicsFileNames = outputDir.list();
		IPath[] graphicsFilePaths = new Path[graphicsFileNames.length];
		for (int i = 0; i < graphicsFileNames.length; i++)
			graphicsFilePaths[i] = cp.getStateLocation().append(
					STATELOC_OUTPUT_DIR).append(graphicsFileNames[i]);

		return graphicsFilePaths;
	}

	/**
	 * <p>
	 * Delivers the path to the .pic file to Ant.
	 * </p>
	 * 
	 * @throws IOException
	 */
	private void deliverPicFilePath(IPath picFilePath) throws IOException {
		Properties prop = new Properties();

		prop.setProperty("pic-file-path", picFilePath.toString());
		IPath p = LightUMLCorePlugin.getDefault().getStateLocation().append(
				STATELOC_RUNSETTINGS);
		prop.store(new FileOutputStream(p.toFile()), "Ant run settings");
	}

	/**
	 * <p>
	 * Convert .pic to graphics.
	 * </p>
	 * 
	 * @throws IOException
	 */
	public void picToGraphics(IPath picFilePath, IProgressMonitor pm)
			throws LightUMLCoreException {

		try {
			deliverPicFilePath(picFilePath);
			AntRunner runner = LightUMLCorePlugin.getDefault()
					.getBuildFileRunner();
			runner.setExecutionTargets(new String[] { "pic-to-graphics" });
			runner.run(pm);
		} catch (IOException e) {
			throw new LightUMLCoreException(e);
		} catch (CoreException e) {
			throw new LightUMLCoreException(e);
		}
	}
}
