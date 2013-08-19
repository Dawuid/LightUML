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

package org.lightuml.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.lightuml.core.IBuildConstants;
import org.lightuml.core.LightUMLCorePlugin;
import org.lightuml.ui.LightUMLUIPlugin;

/**
 * <p>
 *  Preference subpage for UMLGraph doclet. Tab handling borrowed from
 *  net.sf.commonclipse.preferences.TabbedFieldEditorPreferencePage
 * </p>
 * 
 * @author Antti Hakala
 */
public class ClassDiagramsPage extends LightUMLPreferencePage implements IBuildConstants {
    /**
     * <p>
     * Constructor.
     * </p>
     */
    public ClassDiagramsPage() {
        super();
        setDescription("Global parameters for class diagrams:");
    }

    /***
     * Tab folder.
     */
    private TabFolder folder;
    
    
    /***
    * Maximum number of columns for field editors.
    */
    private int maxNumOfColumns;
    
    /***
     * Adds the given field editor to this page.
     * @param editor the field editor
     */
    protected void addField(FieldEditor editor)
    {
        // needed for layout, since there is no way to get fields editor from parent
        this.maxNumOfColumns = Math.max(this.maxNumOfColumns, editor.getNumberOfControls());
        super.addField(editor);
    }
    
    /***
     * Adjust the layout of the field editors so that they are properly aligned.
     */
    protected void adjustGridLayout()
    {
        if (folder != null)
        {
            TabItem[] items = folder.getItems();
            for (int j = 0; j < items.length; j++)
            {
                GridLayout layout = ((GridLayout) ((Composite) items[j].getControl()).getLayout());
                layout.numColumns = this.maxNumOfColumns;
                layout.marginHeight = 5;
                layout.marginWidth = 5;
            }
        }
     
        // need to call super.adjustGridLayout() since fieldEditor.adjustForNumColumns() is protected
        super.adjustGridLayout();
      
        // reset the main container to a single column
        ((GridLayout) super.getFieldEditorParent().getLayout()).numColumns = 1;
    }
    
    /***
     * Returns a parent composite for a field editor.
     * <p>
     * This value must not be cached since a new parent may be created each time this method called. Thus this method
     * must be called each time a field editor is constructed.
     * </p>
     * @return a parent
     */
    protected Composite getFieldEditorParent()
    {
        if (folder == null || folder.getItemCount() == 0)
        {
            return super.getFieldEditorParent();
        }
        return (Composite) folder.getItem(folder.getItemCount() - 1).getControl();
    }
    
    /***
     * Adds a tab to the page.
     * @param text the tab label
     */
    public void addTab(String text)
    {
        if (folder == null)
        {
            // initialize tab folder
            folder = new TabFolder(super.getFieldEditorParent(), SWT.NONE);
            folder.setLayoutData(new GridData(GridData.FILL_BOTH));
        }
     
        TabItem item = new TabItem(folder, SWT.NONE);
        item.setText(text);
     
        Composite currentTab = new Composite(folder, SWT.NULL);
        GridLayout layout = new GridLayout();
        currentTab.setLayout(layout);
        currentTab.setFont(super.getFieldEditorParent().getFont());
        currentTab.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
     
        item.setControl(currentTab);
    }
    
    /**
     * <p>
     * Convert comma-separated rgb-values into an int array (of size 3
     * apparently).
     * </p>
     * 
     * @param rgbString
     *            comma-separated rgb values
     * @return
     */
    private int[] rgbStringToInts(String rgbString) {
        String rgbSeparate[] = rgbString.split(",");
        return new int[] { Integer.parseInt(rgbSeparate[0]),
                Integer.parseInt(rgbSeparate[1]),
                Integer.parseInt(rgbSeparate[2]) };
    }

    
    /**
     * <p>
     * Converts SWT RGB rep. into one understood by UMLGraph / GraphViz. "r,g,b" =>
     * "#rrggbb" (in hex).
     * <p>
     * 
     * @param swtRGB
     *            rgb swt-style
     * @return
     */
    private String convertRGB(String swtRGB) {
        StringBuffer buf = new StringBuffer("#");

        int rgbInts[] = rgbStringToInts(swtRGB);

        for (int i = 0; i < 3; i++) {
            String hexStr = Integer.toHexString(rgbInts[i]);
            if (hexStr.length() < 2)
                buf.append("0");
            buf.append(hexStr);
        }
        return buf.toString();
    }

    // TODO Get the color name somehow from the ColorSelector, and extend
    // ColorFieldEditor so that it will store the color name which is already
    // in the #RRGGBB form. Above two methods would be removed.
    
    /**
     * <p>
     * Compute the parameters given to UMLGraph.
     * </p>
     */
    private void computeUMLGraphParams() {
        StringBuffer buf = new StringBuffer();
        IPreferenceStore store = getPreferenceStore();
        
        String[][] umlgraphParams = LightUMLUIPlugin.getDefault().getUMLGraphParams();

        for (int i = 0; i < umlgraphParams.length; i++) {
            if (store.getBoolean(umlgraphParams[i][2])) {
                buf.append("-");
                buf.append(umlgraphParams[i][0]);
                buf.append(" ");
            }
        }
        for (int i = 0; i < UMLGRAPH_COLOR_PARAMS.length; i++) {
            String swtRGB = store.getString(UMLGRAPH_COLOR_PARAMS[i][3]);
            buf.append("-");
            buf.append(UMLGRAPH_COLOR_PARAMS[i][0]);
            buf.append(" ");
            buf.append(convertRGB(swtRGB));
            buf.append(" ");
        }
        
        buf.append(store.getString(P_UMLGRAPH_PARAM_CMDLINE));
        store.setValue(P_UMLGRAPH_EXTRA_PARAM, buf.toString()); // the one used by core plug-in
    }

    public boolean performOk() {
        boolean result = super.performOk();
        if (result)
            computeUMLGraphParams();
        return result;
    }

    /**
     * <p>
     * Creates the field editors.
     * </p>
     */
    public void createFieldEditors() {
        addTab("General");
        boolean oldUMLGraph = false;
        if(LightUMLCorePlugin.getDefault().getGraphBuildProperty(P_UMLGRAPH_DOCLET_NAME).equals("UmlGraph"))
            oldUMLGraph = true;
        
        String[][] umlgraphParams;
        if(oldUMLGraph) umlgraphParams = LightUMLUIPlugin.getDefault().getUMLGraphParams();
        else umlgraphParams = UMLGRAPH_PARAMS_OTHER;
        
        for (int i = 0; i < umlgraphParams.length; i++)
            addField(new BooleanFieldEditor(umlgraphParams[i][2],
                    umlgraphParams[i][1], getFieldEditorParent()));

        addField(new RadioGroupFieldEditor(P_JAVADOC_ACCESS_LEVEL,
                "Access Level:", 4, ACCESS_LEVELS, getFieldEditorParent()));

        addField(new StringFieldEditor(P_UMLGRAPH_PARAM_CMDLINE,
                "UMLGraph extra commandline parameters (optional):",
                getFieldEditorParent()));
        
        if(!oldUMLGraph) {
            addTab("Inference");
            umlgraphParams = UMLGRAPH_PARAMS_INFERENCE;
            for (int i = 0; i < umlgraphParams.length; i++)
                addField(new BooleanFieldEditor(umlgraphParams[i][2],
                        umlgraphParams[i][1], getFieldEditorParent()));
        }
        
        addTab("Colors");
        IPreferenceStore store = getPreferenceStore();
        for (int i = 0; i < UMLGRAPH_COLOR_PARAMS.length; i++) {
            // set default color values to preference store if none found
            if (store.getString(UMLGRAPH_COLOR_PARAMS[i][3]).length() == 0) {
                store.setDefault(UMLGRAPH_COLOR_PARAMS[i][3],
                        UMLGRAPH_COLOR_PARAMS[i][2]);
                store.setValue(UMLGRAPH_COLOR_PARAMS[i][3],
                        UMLGRAPH_COLOR_PARAMS[i][2]);
            }
            addField(new ColorFieldEditor(UMLGRAPH_COLOR_PARAMS[i][3],
                    UMLGRAPH_COLOR_PARAMS[i][1], getFieldEditorParent()));
        }
    }
}