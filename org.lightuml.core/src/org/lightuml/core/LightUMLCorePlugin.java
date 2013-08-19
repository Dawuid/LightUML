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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.lightuml.core.jobs.AddGraphToProject;
import org.lightuml.core.jobs.DotToGraphics;
import org.lightuml.core.jobs.Initialize;
import org.lightuml.core.jobs.JavaToDot;
import org.lightuml.core.jobs.PicToGraphics;
import org.osgi.framework.BundleContext;

/**
 * <p>
 * The "core" of LightUML. A plug-in that directs the execution of UmlGraph and
 * other external tools.
 * </p>
 * 
 * @author Antti Hakala
 * @has 1 - 1 org.lightuml.core.IGraphConverter
 * @navassoc 1 throws * org.lightuml.core.LightUMLCoreException
 */
public class LightUMLCorePlugin extends Plugin implements IBuildConstants,
        IErrorMessages {
    /**
     * <p>
     * The static plugin instance.
     * </p>
     */
    private static LightUMLCorePlugin plugin = null;

    /**
     * <p>
     * The graph converter to use.
     * </p>
     */
    private IGraphConverter graphConverter;

    /**
     * <p>
     * Properties for graph building.
     * </p>
     */
    private Properties graphBuildProperties;

    /**
     * <p>
     * Get the path to buildfile.
     * </p>
     * 
     * @return A path to buildfile.
     */
    public IPath getBuildFilePath() {
        return getStateLocation().append(BUILD_FILE_NAME);
    }

    /**
     * <p>
     * Get the path to graph build properties.
     * </p>
     * 
     * @return A path to properties file.
     */
    public IPath getPropertiesFilePath() {
        return getStateLocation().append(GRAPH_BUILD_PROPERTIES_FILE_NAME);
    }

    /**
     * <p>
     * 	Get paths to all the .dot files in the statelocation output directory.
     * </p>
     * 
     * @throws LightUMLCoreException
     * @return path representing the location where the .dot file is (to be)
     *         generated.
     */
    public IPath[] getDotFilePaths() {
    	IPath outDirPath = getStateLocation().append(STATELOC_OUTPUT_DIR);
    	String[] dotFileNames = outDirPath.toFile().list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if(name.indexOf(".dot") == name.length()-4)
					return true;
				return false;
			}
		});
    	IPath[] dotFilePaths = new Path[dotFileNames.length];
    	for(int i=0; i<dotFileNames.length; i++)
    		dotFilePaths[i] = outDirPath.append(dotFileNames[i]);
    	
        return dotFilePaths;
    }

    /**
     * <p>
     * Constructor.
     * </p>
     */
    public LightUMLCorePlugin() {
        super();
        plugin = this;
        setGraphConverter(LocalGraphConverter.instance());
        graphBuildProperties = new Properties();
    }

    /**
     * <p>
     * Try to load the graph build properties when the plug-in is started.
     * </p>
     * 
     * @see Plugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        try {
            loadProperties();
        } catch (LightUMLCoreException e) {
            // Exception while loading properties -> force an Initialize
            (new Initialize(System.currentTimeMillis(), true,
                    Initialize.CHECK_UMLGRAPH_NONE)).schedule();
        }
    }

    /**
     * <p>
     * The static accessor to get a handle to the singleton. Note: this plug-in
     * class should be created only by the platform.
     * </p>
     * 
     * @return The LightUMLCorePlugin.
     */
    public static LightUMLCorePlugin getDefault() {
        if (plugin == null)
            plugin = new LightUMLCorePlugin();
        return plugin;
    }

    /**
     * <p>
     * Accessor for graphConverter
     * </p>
     * 
     * @return IGraphConverter
     */
    public IGraphConverter getGraphConverter() {
        return graphConverter;
    }

    /**
     * <p>
     * Setter for graphConverter
     * </p>
     * 
     * @param c
     *            The graphconverter.
     */
    public void setGraphConverter(IGraphConverter c) {
        graphConverter = c;
    }

    /**
     * <p>
     * Load properties for graph building.
     * </p>
     * 
     * @throws LightUMLCoreException
     * 
     */
    public synchronized void loadProperties() throws LightUMLCoreException {
        try {
            graphBuildProperties.load(new FileInputStream(
                    getPropertiesFilePath().toFile()));
        } catch (IOException e) {
            throw new LightUMLCoreException(e);
        }
    }

    /**
     * <p>
     * Getter for graphBuildProperties.
     * </p>
     * 
     * @return graphBuildProperties
     * @throws LightUMLCoreException
     */
    public Properties getGraphBuildProperties() throws LightUMLCoreException {
        return graphBuildProperties;
    }

    /**
     * <p>
     * Getter for a graph build property.
     * </p>
     * 
     * @param property
     *            Name of the property.
     * @return value of the property.
     * 
     */
    public String getGraphBuildProperty(String property) {
        return graphBuildProperties.getProperty(property);
    }

    /**
     * <p>
     * Setter for a graph build property.
     * </p>
     * 
     * @param key
     *            Key (name) of the property.
     * @param value
     *            The new value for the porperty.
     * @throws LightUMLCoreException
     */
    public void setGraphBuildProperty(String key, String value)
            throws LightUMLCoreException {
        graphBuildProperties.setProperty(key, value);
        try { // always store so that ant builds are up to date with
            // properties

            graphBuildProperties
                    .store(new FileOutputStream(getPropertiesFilePath()
                            .toFile()), "Graph Build Properties");
        } catch (IOException e) {
            throw new LightUMLCoreException(e);
        }
    }

    /**
     * <p>
     * Getter for the runner that is used to run targets in BUILD_FILE.
     * </p>
     * 
     * @return A new Antrunner for BUILD_FILE.
     */
    public AntRunner getBuildFileRunner() {
        AntRunner runner = new AntRunner();
        runner.setBuildFileLocation(getBuildFilePath().toString());
        // devel time
        runner.addBuildLogger("org.apache.tools.ant.DefaultLogger");
        runner.setMessageOutputLevel(4);
        return runner;
    }

    // ---------- Methods below this line can be called from the UI ----------
    /**
     * <p>
     * This method is used to restore default settings. Called from the UI.
     * </p>
     * <p>
     * A JobChangeListener is needed if the UI wants to update its preferences
     * that depend on graphBuildProperties (that get reset with Initialize).
     * </p>
     * 
     */
    public synchronized void restoreSettings(IJobChangeListener listener) {
        // 1 - init statelocation (& read default properties)
        Initialize initJob = new Initialize(System.currentTimeMillis(), true,
                Initialize.CHECK_UMLGRAPH_NONE);
        if (listener != null)
            initJob.addJobChangeListener(listener);
        initJob.schedule();
    }

    /**
     * <p>
     * Generate a class diagram for a given project. Called from the UI.
     * </p>
     * <ul>
     * <li> LightUMLSchedulingRule prevents LightUMLJobs from running
     * concurrently (they should be run sequentially). </li>
     * <li> All jobs are scheduled at once. This is mainly because otherwise
     * generating two class diagrams "at the same time" could confuse the
     * diagram files (in .dot and in graphics form). </li>
     * <li> Jobs support canceling => responsiveness. </li>
     * <li>Note that Eclipse Java model is used only at UI-level.</li>
     * </ul>
     * 
     * @param project
     *            The project to generate a class diagram for.
     * @param packagePath
     *            A project relative path under which the source files will be
     *            looked for.
     * @param packageName
     *            Name of the package or null if generating a diagram for a
     *            whole project.
     * 
     */
    public synchronized void generateClassDiagram(IProject project,
            IPath packagePath, String packageName) {
        final long refTime = System.currentTimeMillis();
        IPath sourcePath = project.getLocation();
        if (packagePath != null)
            sourcePath = sourcePath.append(packagePath);

        // 1 - initialize if needed
        (new Initialize(refTime, false, Initialize.CHECK_UMLGRAPH_JAR))
                .schedule();

        // 2 - run the UmlGraph doclet to convert .java files to a single .dot
        // file
        (new JavaToDot(refTime, sourcePath, packageName)).schedule();

        // 3 - convert .dot file to a graphics file
        (new DotToGraphics(refTime)).schedule();

        // 4 - add graphics file to the project
        (new AddGraphToProject(refTime, project, packageName)).schedule();
    }

    /**
     * <p>
     * Generate a sequence diagram from a .pic file.
     * </p>
     * 
     * @param picFile
     *            The .pic file to generate from.
     * @hidden
     */
    public synchronized void generateSequenceDiagram(IFile picFile) {
        final long refTime = System.currentTimeMillis();

        // 1 - initialize if needed
        (new Initialize(refTime, false, Initialize.CHECK_UMLGRAPH_PIC_MACROS))
                .schedule();

        // 2 - convert .pic to graphics
        (new PicToGraphics(refTime, picFile.getLocation())).schedule();

        // 3 - add graphics file to the project
        (new AddGraphToProject(refTime, picFile.getProject(), null)).schedule();
    }
}
