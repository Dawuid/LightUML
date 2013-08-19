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

import org.eclipse.core.runtime.jobs.ISchedulingRule;

/**
 * <p>
 * Scheduling rule of LightUMLJobs. Prevents concurrent execution of
 * LightUMLJobs.
 * </p>
 * 
 * @author Antti Hakala
 */
public class LightUMLSchedulingRule implements ISchedulingRule {
    /**
     * <p>
     * Static instance of LightUMLSchedulingRule.
     * </p>
     */
    private static LightUMLSchedulingRule rule = null;

    /**
     * Constructor.
     */
    private LightUMLSchedulingRule() {
        rule = this;
    }

    /**
     * Static accessor (singleton pattern).
     * 
     * @return LightUMLSchedulingRule
     */
    public static LightUMLSchedulingRule getDefault() {
        if (rule == null)
            rule = new LightUMLSchedulingRule();
        return rule;
    }

    /**
     * @see org.eclipse.core.runtime.jobs.ISchedulingRule#contains(org.eclipse.core.runtime.jobs.ISchedulingRule)
     */
    public boolean contains(ISchedulingRule rule) {
        return rule == this;
    }

    /**
     * @see org.eclipse.core.runtime.jobs.ISchedulingRule#isConflicting(org.eclipse.core.runtime.jobs.ISchedulingRule)
     */
    public boolean isConflicting(ISchedulingRule rule) {
        return rule == this;
    }

}