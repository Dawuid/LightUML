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

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.lightuml.core.IBuildConstants;
import org.lightuml.core.IGraphBuildProperties;
import org.lightuml.core.LightUMLCoreException;
import org.lightuml.core.LightUMLCorePlugin;
import org.lightuml.core.jobs.AddGraphToProject;
import org.lightuml.core.jobs.DotToGraphics;
import org.lightuml.core.jobs.Initialize;
import org.lightuml.core.jobs.JavaToDot;
import org.lightuml.core.jobs.LightUMLJob;
import org.lightuml.core.jobs.PicToGraphics;

public class JobsTestCase extends TestCase implements IGraphBuildProperties,
		IBuildConstants {
	private LightUMLCorePlugin corePlugin;

	private long refTime = System.currentTimeMillis();

	private IProject testProject;

	private IFile picFile;

	private static final IPath UMLGRAPH_PATH = new Path(
			"/home/antti/UMLGraph-snapshot/lib/");

	private static final IPath UMLGRAPH_DOCLET_PATH = UMLGRAPH_PATH.append(
			"UmlGraph").addFileExtension("jar");

	private static final IPath SEQUENCE_PIC_PATH = UMLGRAPH_PATH.append(
			"sequence").addFileExtension("pic");

	protected void setUp() throws CoreException, IOException,
			LightUMLCoreException, InterruptedException {
		// A fixture that allows successful execution of each job separately is
		// needed.
		// This means..
		// AddGraphToProject:
		// (1) A project exists.
		// (2) The graphics file is newer than reference time.
		// DotToGraphics:
		// (3) The Dot file is newer than reference time.
		// JavaToDot:
		// (4) UMLGraph doclet is on the path indicated by "umlgraph-jar-path"
		// property.
		// (5) There exists at least one Java source file in the project.
		// PicToGraphics:
		// (6) A .pic file must exist in the project (given as parameter).
		//
		// In addition GraphViz 'dot' and plotutils 'pic2plot' must be installed
		// and be on
		// the path used by Ant. Though, that can't be helped here.
		// Also, LightUMLCorePlugin must have the appropriate files in its
		// statelocation.zip
		// which is unzipped, including build.xml and graph.ini.

		corePlugin = LightUMLCorePlugin.getDefault();
		scheduleJoinAssert(new Initialize(getRefTime(), true,
				Initialize.CHECK_UMLGRAPH_NONE));
		assertTrue(corePlugin.getStateLocation().append(BUILD_FILE_NAME)
				.toFile().exists());
		assertTrue(corePlugin.getStateLocation().append(
				GRAPH_BUILD_PROPERTIES_FILE_NAME).toFile().exists());
		assertTrue(corePlugin.getStateLocation().append(STATELOC_OUTPUT_DIR)
				.toFile().exists());

		// -- (1) --
		testProject = ResourcesPlugin.getWorkspace().getRoot().getProject(
				"test");
		testProject.create(null);
		testProject.open(null);

		// -- (2) --
		Initialize.nonWSCopy(LightUMLTestPlugin.getDefault().openStream(
				new Path("files").append("test").addFileExtension("png")),
				corePlugin.getStateLocation().append(STATELOC_OUTPUT_DIR)
						.append("test").addFileExtension("png"));

		// -- (3) --
		Initialize.nonWSCopy(LightUMLTestPlugin.getDefault().openStream(
				new Path("files").append("test").addFileExtension("dot")),
				corePlugin.getStateLocation().append(STATELOC_OUTPUT_DIR)
						.append("test").addFileExtension("png"));

		// -- (4) --
		corePlugin.setGraphBuildProperty(P_UMLGRAPH_JAR_PATH,
				UMLGRAPH_DOCLET_PATH.toOSString());

		// -- (5) --
		IFile f = testProject.getFile(new Path("TestClass")
				.addFileExtension("java"));
		f.create(
				LightUMLTestPlugin.getDefault().openStream(
						new Path("files").append("TestClass").addFileExtension(
								"java")), true, null);

		// -- (6) --
		picFile = testProject.getFile(new Path("test").addFileExtension("pic"));
		picFile.create(LightUMLTestPlugin.getDefault().openStream(
				new Path("files").append("test").addFileExtension("pic")),
				true, null);
	}

	// tear down the fixture
	protected void tearDown() throws CoreException, LightUMLCoreException {
		testProject.delete(true, null);
		IPath[] filePaths = corePlugin.getGraphConverter()
				.getGraphicsFilePaths();
		for (int i = 0; i < filePaths.length; i++)
			// there shouldn't be anything..
			filePaths[i].toFile().delete();
		corePlugin.getStateLocation().append(SEQUENCE_PIC_PATH.lastSegment())
				.toFile().delete();
	}

	// schedule a job. join it, i.e. wait until it finishes, and
	// assert true that the result was ok
	private void scheduleJoinAssert(LightUMLJob job)
			throws InterruptedException {
		job.schedule();
		job.join();
		assertTrue(job.getResult().isOK());
	}

	private void assertNewer(IPath pathToFile, long time) {
		assertTrue(time < pathToFile.toFile().lastModified());
	}

	// It has to be noted that only one of the LightUMLJobs may return
	// errorstatus if they belong to the same toolchain (same refTime).
	// => return different reftimes for all jobs
	protected long getRefTime() {
		refTime -= 1;
		return refTime;
	}

	// -- The actual tests --
	public void testAddGraphToProject() throws InterruptedException {
		AddGraphToProject job = new AddGraphToProject(getRefTime(),
				testProject, null);
		scheduleJoinAssert(job);
		// test that a graph was added.
		assertNotNull(testProject.getFile(new Path("test")
				.addFileExtension("png")));
	}

	public void testDotToGraphics() throws InterruptedException,
			LightUMLCoreException {
		long time = System.currentTimeMillis();
		DotToGraphics job = new DotToGraphics(getRefTime());
		scheduleJoinAssert(job);
		// check that a new graphics file was generated
		assertNewer(corePlugin.getGraphConverter().getGraphicsFilePaths()[0], time);
	}

	// Initialize gets actually tested in the setUp already
	public void testInitialize() throws InterruptedException {
		Initialize job = new Initialize(getRefTime(), true,
				Initialize.CHECK_UMLGRAPH_NONE);
		scheduleJoinAssert(job);
		// check that the files were really unzipped to statelocation
		assertTrue(corePlugin.getStateLocation().append(BUILD_FILE_NAME)
				.toFile().exists());
		assertTrue(corePlugin.getStateLocation().append(
				GRAPH_BUILD_PROPERTIES_FILE_NAME).toFile().exists());
		assertTrue(corePlugin.getStateLocation().append(STATELOC_OUTPUT_DIR)
				.toFile().exists());
	}

	public void testJavaToDot() throws InterruptedException {
		long time = System.currentTimeMillis();
		JavaToDot job = new JavaToDot(getRefTime(), testProject.getLocation(),
				null);
		scheduleJoinAssert(job);
		// check that a new dot file was generated
		assertNewer(corePlugin.getDotFilePaths()[0], time);
	}

	public void testPicToGraphics() throws InterruptedException,
			LightUMLCoreException {
		corePlugin.setGraphBuildProperty(P_PIC_MACROS_PATH, SEQUENCE_PIC_PATH
				.toString());
		PicToGraphics job = new PicToGraphics(getRefTime(), picFile
				.getLocation());
		scheduleJoinAssert(job);
		// TODO: check that a new graphics file was generated
	}
}
