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

package org.lightuml.ui;

import java.util.Iterator;
import java.util.Properties;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.lightuml.core.IGraphBuildProperties;
import org.lightuml.core.LightUMLCoreException;
import org.lightuml.core.LightUMLCorePlugin;

/**
 * <p>
 * Plug-in class of org.lightuml.ui.
 * </p>
 * 
 * @author Antti Hakala
 * 
 * @navassoc - "adds to UI" - org.lightuml.ui.actions.GenerateClassDiagram
 * @navassoc - "adds to UI" - org.lightuml.ui.actions.RestoreSettings
 * @navassoc - "adds to UI" - org.lightuml.ui.actions.GenerateSequenceDiagram
 * @navassoc - "adds to UI" - org.lightuml.ui.preferences.LightUMLPage
 * @navassoc - "adds to UI" - org.lightuml.ui.editor.PicEditor
 * 
 */
public class LightUMLUIPlugin extends AbstractUIPlugin implements
        IUMLGraphConstants, IGraphBuildProperties {

    private static LightUMLUIPlugin plugin;

    /**
     * <p>
     * A property change listener for the preference store of this plug-in.
     * Tells org.lightuml.core plug-in to update its properties if they're
     * changed in the UI (via preference page).
     * </p>
     * 
     * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
     * @hidden
     */
    private class PropertyListener implements IPropertyChangeListener {
        /**
         * @see IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent event) {
            LightUMLCorePlugin cp = LightUMLCorePlugin.getDefault();

            try {
                if (cp.getGraphBuildProperties().getProperty(
                        event.getProperty()) != null)
                    cp.setGraphBuildProperty(event.getProperty(),
                            (String) event.getNewValue().toString());
            } catch (LightUMLCoreException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <p>
     * Overridden from AbstractUIPlugin.
     * </p>
     */
    protected void initializeDefaultPluginPreferences() {
        initPreferences(false);
        getPreferenceStore().addPropertyChangeListener(new PropertyListener());
    }

    /**
     * <p>
     * Initialize preferences. Adds graph build properties of LightUMLCorePlugin
     * into preference store of this plug-in.
     * </p>
     * @param resetPages Reset preference pages?
     */
    public void initPreferences(boolean resetPages) {
        IPreferenceStore store = getPreferenceStore();
        Properties p;
        try {
            p = LightUMLCorePlugin.getDefault().getGraphBuildProperties();
            Iterator i = p.keySet().iterator();
            String propName;
            while (i.hasNext()) {
                propName = (String) i.next();
                store.setDefault(propName, p.getProperty(propName));
                store.setValue(propName, p.getProperty(propName));
            }
        } catch (LightUMLCoreException e) {
            e.printStackTrace();
        }
        if(!resetPages) return;
        // also reset other preferencepage preferences if stored
        // TODO: a cleaner way to reset all the parameters used in preference
        // pages
        for (int i = 0; i < getUMLGraphParams().length; i++) {
            if (store.getBoolean(getUMLGraphParams()[i][2]))
                store.setToDefault(getUMLGraphParams()[i][2]);
        }
        for (int i = 0; i < UMLGRAPH_COLOR_PARAMS.length; i++) {
            if (store.getString(UMLGRAPH_COLOR_PARAMS[i][3]).length() > 0)
                store.setToDefault(UMLGRAPH_COLOR_PARAMS[i][3]);
        }
        if (store.getString(P_UMLGRAPH_PARAM_CMDLINE).length() > 0)
            store.setToDefault(P_UMLGRAPH_PARAM_CMDLINE);
    }
    
    public String[][] getUMLGraphParams() {
        if(LightUMLCorePlugin.getDefault().getGraphBuildProperty(P_UMLGRAPH_DOCLET_NAME).equals("UmlGraph"))
            return UMLGRAPH_PARAMS_OLD;
        // huh, another reminder of lack of power of expression in java
        String[][] params = new String[UMLGRAPH_PARAMS_INFERENCE.length + UMLGRAPH_PARAMS_OTHER.length][3];
        System.arraycopy(UMLGRAPH_PARAMS_OTHER, 0, params, 0, UMLGRAPH_PARAMS_OTHER.length);
        System.arraycopy(UMLGRAPH_PARAMS_INFERENCE, 0, params, UMLGRAPH_PARAMS_OTHER.length, UMLGRAPH_PARAMS_INFERENCE.length);
        return params;
    }
    public String[][] getUMLGraphParamsNoUI() {
        if(LightUMLCorePlugin.getDefault().getGraphBuildProperty(P_UMLGRAPH_DOCLET_NAME).equals("UmlGraph"))
            return UMLGRAPH_PARAMS_NOUI_OLD;
        return UMLGRAPH_PARAMS_NOUI;
    }
    /**
     * Constructor.
     */
    public LightUMLUIPlugin() {
        super();
        plugin = this;
    }

    /**
     * <p>
     * static accessor.
     * </p>
     * 
     * @return LightUMLUIPlugin
     */
    public static LightUMLUIPlugin getDefault() {
        if (plugin == null)
            plugin = new LightUMLUIPlugin();
        return plugin;
    }
}
