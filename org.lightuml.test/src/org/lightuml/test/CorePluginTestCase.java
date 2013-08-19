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

package org.lightuml.test;

import java.io.IOException;
import java.util.Properties;

import junit.framework.TestCase;

import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.lightuml.core.IGraphConverter;
import org.lightuml.core.LightUMLCoreException;
import org.lightuml.core.LightUMLCorePlugin;

public class CorePluginTestCase extends TestCase {
	private LightUMLCorePlugin corePlugin;

	private Properties defaultProperties;

	protected void setUp() throws CoreException, IOException,
			LightUMLCoreException {
		// fixture code
		corePlugin = LightUMLCorePlugin.getDefault();
		corePlugin.restoreSettings(null);
		defaultProperties = corePlugin.getGraphBuildProperties();
	}

	protected void tearDown() throws CoreException {
	}

	public void testGetBuildFilePath() {
		IPath result = corePlugin.getBuildFilePath();
		assertNotNull(result);
	}

	public void testGetPropertiesFilePath() {
		IPath result = corePlugin.getPropertiesFilePath();
		assertNotNull(result);
	}
	// not tested
	//public void testGetDotFilePath() {
	//	IPath result = corePlugin.getDotFilePaths()[0];
	//	assertNotNull(result);
	//}

	public void testGetGraphConverter() {
		IGraphConverter result = corePlugin.getGraphConverter();
		assertNotNull(result);
	}

	public void testSetGraphConverter() {
		IGraphConverter gc = new IGraphConverter() {
			public IPath[] getGraphicsFilePaths() throws LightUMLCoreException {
				return null;
			}
			public void dotToGraphics(IPath[] dotFilePath, IProgressMonitor pm)
					throws LightUMLCoreException {
			}

			public void picToGraphics(IPath picFilePath, IProgressMonitor pm)
					throws LightUMLCoreException {
			}
		};
		corePlugin.setGraphConverter(gc);
		assertSame(gc, corePlugin.getGraphConverter());
	}

	public void testLoadProperties() throws LightUMLCoreException {
		corePlugin.loadProperties();
	}

	public void testGetGraphBuildProperties() throws LightUMLCoreException {
		Properties prop = corePlugin.getGraphBuildProperties();
		assertNotNull(prop);
	}

	public void testGetAndSetGraphBuildProperty() throws LightUMLCoreException {
		String key = "testkey", value = "testvalue";
		corePlugin.setGraphBuildProperty(key, value);
		assertEquals(value, corePlugin.getGraphBuildProperty(key));
	}

	public void testGetBuildFileRunner() {
		AntRunner runner = corePlugin.getBuildFileRunner();
		assertNotNull(runner);
	}

	public void testRestoreSettings() throws LightUMLCoreException {
		corePlugin.setGraphBuildProperty("somekey", "somevalue");
		corePlugin.restoreSettings(null);
		assertEquals(defaultProperties, corePlugin.getGraphBuildProperties());
	}
	// generateClassDiagram and genereateSequenceDiagram
	// should be tested at job level
}
