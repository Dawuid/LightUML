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

/**
 * <p>
 * Property names used in the graph build properties file.
 * </p>
 * 
 * @author Antti Hakala
 * @hidden
 */
public interface IGraphBuildProperties {

    // property names (used in build.xml also)
    public String P_GRAPHICS_FORMAT = "graphics-format";

    public String P_GRAPH_FILE_NAME = "graph-file-name";

    public String P_PROJECT_OUTPUT_DIR = "project-output-dir";

    public String P_EXTRA_LOOKUP_PATH = "extra-lookup-path";

    public String P_DOT_EXTRA_PARAM = "dot-extra-param";

    public String P_PIC2PLOT_EXTRA_PARAM = "pic2plot-extra-param";

    public String P_UMLGRAPH_EXTRA_PARAM = "umlgraph-extra-param";

    public String P_USE_PACKAGE_NAME = "use-package-name";

    public String P_RECURSE_PACKAGES = "recurse-packages";

    public String P_JAVADOC_ACCESS_LEVEL = "javadoc-access-level";

    public String P_UMLGRAPH_JAR_PATH = "umlgraph-jar-path";

    public String P_UMLGRAPH_DOCLET_NAME = "umlgraph-doclet-name";

    public String P_PIC_MACROS_PATH = "pic-macros-path";

    public String P_JAVADOC_EXECUTABLE_PATH = "javadoc-executable-path";

    public String[][] OUTPUT_FORMATS = {
            { "dia (Dia format)", "dia" },
            { "fig (XFIG Graphics)", "fig" },
            { "gif (Graphics Interchange Format)", "gif" },
            { "hpgl (HP pen plotters)", "hpgl" },
            { "pcl (Laserjet printers)", "pcl" },
            { "png (Portable Network Graphics)", "png" },
            { "ps (PostScript)", "ps" },
            { "svg (Structured Vector Graphics)", "svg" } };

    public String[][] ACCESS_LEVELS = { { "private", "private" },
            { "protected", "protected" }, { "package", "package" },
            { "public", "public" } };
}
