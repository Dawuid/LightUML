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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.lightuml.core.IBuildConstants;
import org.lightuml.core.IErrorMessages;
import org.lightuml.core.IGraphBuildProperties;
import org.lightuml.core.LightUMLCoreException;

/**
 * 
 * <p>
 * Initializes the plug-in statelocation by unzipping the statelocation.zip into
 * the plug-in state location. This is done only if forced or needed.
 * </p>
 * 
 * @author Antti Hakala
 */
public class Initialize extends LightUMLJob implements IBuildConstants,
        IGraphBuildProperties, IErrorMessages {
    final static int BUFFER_SIZE = 2048;

    private boolean forceInitialize;

    private int checkUMLGraph;

    public final static int CHECK_UMLGRAPH_NONE = 0, CHECK_UMLGRAPH_JAR = 1,
            CHECK_UMLGRAPH_PIC_MACROS = 2;

    /**
     * <p>
     * Constructor.
     * </p>
     * 
     * @param time
     *            The reference time.
     * @param forceInitializeFlag
     *            Force initialization?
     * @param checkUMLGraphFlag
     *            Check if UMLGraph is found?
     */
    public Initialize(long time, boolean forceInitializeFlag,
            int checkUMLGraphFlag) {
        super(time, "Initializing LightUML");
        forceInitialize = forceInitializeFlag;
        checkUMLGraph = checkUMLGraphFlag;
    }

    /**
     * <p>
     * Used for non-workspace copying.
     * </p>
     * 
     * @param is
     *            inputstream to copy from
     * @param p
     *            path to the destination
     * @throws IOException
     */
    public static void nonWSCopy(InputStream is, IPath p) throws IOException {
        byte data[] = new byte[1024];
        File f = p.toFile();
        f.createNewFile();
        FileOutputStream fos = new FileOutputStream(f);
        int count;
        while ((count = is.read(data)) != -1)
            fos.write(data, 0, count);
        is.close();
        fos.close();
    }

    /**
     * <p>
     * Initializes statelocation. Overwrites properties / preferences for graph
     * generation.
     * </p>
     * <ul>
     * <li> Create statelocation output directory where generated graphs will be
     * stored. </li>
     * <li>copy "build.xml" to statelocation</li>
     * <li>copy "graph.ini" to statelocation</li>
     * </ul>
     * <p>
     *  TODO: refactor: copy everything in the dir to statelocation?
     * </p>
     * @throws IOException
     */
    private void initializeStateLocation() throws IOException {
        corePlugin.getStateLocation().append(STATELOC_OUTPUT_DIR).toFile()
                .mkdir();
        nonWSCopy(corePlugin.openStream(TO_STATELOCATION_DIR
                .append(BUILD_FILE_NAME)), corePlugin.getStateLocation()
                .append(BUILD_FILE_NAME));
        nonWSCopy(corePlugin.openStream(TO_STATELOCATION_DIR
                .append(GRAPH_BUILD_PROPERTIES_FILE_NAME)), corePlugin
                .getStateLocation().append(GRAPH_BUILD_PROPERTIES_FILE_NAME));
    }

    /**
     * <p>
     * Check if initialize is needed. Checks if the necessary files are found
     * and if the last initialize was with the same version of plug-in, i.e.
     * files are up to date.
     * </p>
     * 
     * @return true if initialize is needed, false otherwise.
     * @throws LightUMLCoreException
     */
    private boolean needInitialize() throws LightUMLCoreException {
        String initVersion;
        if (!corePlugin.getBuildFilePath().toFile().exists()
                || !corePlugin.getPropertiesFilePath().toFile().exists()
                || ((initVersion = corePlugin
                        .getGraphBuildProperty("bundle-version")) == null)
                || !initVersion.equals(corePlugin.getBundle().getHeaders().get(
                        "Bundle-Version")))
            return true;
        return false;
    }

    /**
     * <p>
     * Check if UMLGraph is found.
     * </p>
     * 
     * @throws LightUMLCoreException
     * 
     */
    private void checkUMLGraph() throws LightUMLCoreException {
        if ((checkUMLGraph & CHECK_UMLGRAPH_JAR) != 0) {
            String pathToDoclet = corePlugin
                    .getGraphBuildProperty(P_UMLGRAPH_JAR_PATH);
            if ((pathToDoclet == null) || !new File(pathToDoclet).exists())
                throw new LightUMLCoreException(ERRMSG_NO_UMLGRAPH_JAR);
        }
        if ((checkUMLGraph & CHECK_UMLGRAPH_PIC_MACROS) != 0) {
            String pathToMacros = corePlugin
                    .getGraphBuildProperty(P_PIC_MACROS_PATH);
            if ((pathToMacros == null) || !new File(pathToMacros).exists())
                throw new LightUMLCoreException(ERRMSG_NO_PIC_MACROS);
        }
    }

    /**
     * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
     */
    protected IStatus run(IProgressMonitor monitor) {
        try {
            if (forceInitialize || needInitialize()) {
                // Unzip the files in the state location.
                initializeStateLocation();
                // Load default properties.
                corePlugin.loadProperties();
                // Store the bundle version to properties.
                corePlugin.setGraphBuildProperty("bundle-version", corePlugin
                        .getBundle().getHeaders().get("Bundle-Version")
                        .toString());
            }

            checkUMLGraph();

        } catch (LightUMLCoreException e) {
            return errorStatus(e);
        } catch (IOException e) {
            return errorStatus(e);
        }
        if (monitor.isCanceled())
            return cancelStatus();
        return Status.OK_STATUS;
    }
}