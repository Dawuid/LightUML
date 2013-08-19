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

import java.util.Collection;
import java.util.Vector;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.lightuml.core.LightUMLCorePlugin;

/**
 * <p>
 * The abstract parent class of LightUML jobs.
 * </p>
 * 
 * @author Antti Hakala
 * @navassoc - uses - org.lightuml.core.jobs.LightUMLSchedulingRule
 */
public abstract class LightUMLJob extends Job {
	/**
	 * <p>
	 * Reference time (time this toolchain was created).
	 * </p>
	 */
	private long refTime;

	/**
	 * <p>
	 * String representing the id of this family of jobs.
	 * </p>
	 */
	private String familyId;

	/**
	 * <p>
	 * static instance of LightUMLCorePlugin (for convenience)
	 * </p>
	 */
	protected static LightUMLCorePlugin corePlugin = null;
	
	/**
	 * <p>
	 * A register for error and cancel statuses that have been returned
	 * by LightUMLJobs. Used to see if a job in a specific toolchain should
	 * run, or if an earlier job in the chain has already been canceled or
	 * has returned an error status.
	 * </p> 
	 */
	private static Collection errorAndCancelStatusRegister = null;
	
	/**
	 * <p>
	 * Internal init method.
	 * </p>
	 * 
	 * @param time
	 *            reference time of this job
	 * @param rule
	 *            Scheduling rule for this job.
	 */
	private void init(long time, ISchedulingRule rule) {
		refTime = time;
		familyId = new StringBuffer("org.lightuml.core").append(refTime).toString();
		setRule(rule);
		if (corePlugin == null)
			corePlugin = LightUMLCorePlugin.getDefault();
		if (errorAndCancelStatusRegister == null)
			errorAndCancelStatusRegister = java.util.Collections
					.synchronizedCollection(new Vector());
	}

	/**
	 * <p>
	 * Return an error status for this job. Only one error (first one)
	 * should occur from a toolchain.
	 * </p>
	 * <p>
	 * Uses reference times of jobs as toolchain identifiers. If jobs have the
	 * same reference time, they belong to the same toolchain. Cancels the other
	 * jobs that belong to same toolchain.
	 * </p>
	 * 
	 * @param e
	 *            Exception that caused the error.
	 * @return IStatus that can be returned in the run() of LightUMLJob (if an
	 *         exception occured).
	 */
	protected IStatus errorStatus(Exception e) {
		errorAndCancelStatusRegister.add(familyId);
		// return the error status
		return new Status(Status.ERROR, "org.lightuml.core", Status.OK, e
				.getMessage(), e);
	}
	/**
	 * <p>
	 * 	Return a cancel status and add familyId to register. See above.
	 * </p>
	 * @return cancel status
	 */
	protected IStatus cancelStatus() {
		errorAndCancelStatusRegister.add(familyId);
		return Status.CANCEL_STATUS;
	}
	/**
	 * <p>
	 * 	Run only if family not canceled or erred.
	 * </p>
	 */
	public boolean shouldRun() {
		if(errorAndCancelStatusRegister.contains(familyId))
			return false;
		return true;
	}
	
	/**
	 * <p>
	 * constructor
	 * </p>
	 * 
	 * @param str
	 *            Name passed to parent class Job.
	 */
	public LightUMLJob(long time, String str) {
		super(str);
		init(time, LightUMLSchedulingRule.getDefault());
	}

	/**
	 * <p>
	 * Alternative constructor with an additional scheduling rule.
	 * </p>
	 * 
	 * @param time
	 *            The reference time.
	 * @param str
	 *            Name passed to parent class Job.
	 * @param rule
	 *            Additional scheduling rule for this job.
	 */
	public LightUMLJob(long time, String str, ISchedulingRule rule) {
		super(str);
		init(time, MultiRule.combine(rule, LightUMLSchedulingRule.getDefault()));
	}

	/**
	 * @see org.eclipse.core.runtime.jobs.Job#belongsTo(java.lang.Object)
	 */
	public boolean belongsTo(Object family) {
		return (family.equals(familyId));
	}
}