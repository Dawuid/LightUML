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


package org.lightuml.ui.actions;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.lightuml.core.LightUMLCorePlugin;
import org.lightuml.ui.LightUMLUIPlugin;

/**
 * <p>
 * Action for restoring default settings.
 * </p>
 * 
 * @author Antti Hakala
 * 
 */
public class RestoreSettings implements IWorkbenchWindowActionDelegate {

    /**
     * <p>
     * Runs the restoreSettings()-method in LightUMLCorePlugin.
     * </p>
     * <p>
     * JobChangeAdapter is used to tell LightUMLUIPlugin to init its preferences
     * when LightUMLCorePlugin is done resetting default properties for graph
     * building. Thus, UI preferences are kept in sync with core properties.
     * JobChangeAdapter is an adapter for IJobChangeListener interface.
     * </p>
     * 
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     * 
     */
    public void run(IAction action) {
        LightUMLCorePlugin.getDefault().restoreSettings(new JobChangeAdapter() {
            public void done(IJobChangeEvent event) {
                LightUMLUIPlugin.getDefault().initPreferences(true);
            }
        });
    }

    /***************************************************************************
     * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
     */
    public void dispose() {
    }

    /**
     * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
     */
    public void init(IWorkbenchWindow window) {
    }

    /**
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
     *      org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {
    }
}
