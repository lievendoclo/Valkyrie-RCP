/*
 * Copyright 2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.valkyriercp.application.docking.view;

import java.awt.Rectangle;

import com.jidesoft.docking.DockContext;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.application.support.DefaultViewDescriptor;

/**
 * Extends the default view descriptor to add the ability to compare
 * the view descriptors, and thus produce alphabetical lists, and
 * support for the generation of a show view command. The ability to 
 * specify dockingframe init parameters is also handled. 
 * 
 * @author Jonny Wray
 *
 */
public class JideViewDescriptor extends DefaultViewDescriptor implements Comparable{

	private boolean isWorkspace = false;
	private int initMode = DockContext.STATE_FRAMEDOCKED;
	private int initSide = DockContext.DOCK_SIDE_EAST;
	private int initIndex = 0;
	private boolean floatOnShow = false;
	private Rectangle floatBounds = new Rectangle(100, 200, 200, 200);
	
	// These have null defaults which implies the default comes
	//	from the framework, since the default can vary depending 
	//	upon the state of the docking manager. Basically if null
	//	the value isn't set on the view
	private Boolean autohidable = null;
	private Boolean floatable = null;
	private Boolean dockable = null;
	private Boolean hidable = null;
	private Boolean showGripper = null;
	private Boolean showTitleBar = null;
	private Boolean sideDockAllowed = null;
	private Boolean slidingAutohide = null;
	private Boolean tabDockAllowed = null;
	private Boolean maximizable = null;
	private Boolean rearrangable = null;
	
	
	public Boolean getRearrangable() {
		return rearrangable;
	}

	public void setRearrangable(Boolean rearrangable) {
		this.rearrangable = rearrangable;
	}

	public Boolean getMaximizable() {
		return maximizable;
	}

	public void setMaximizable(Boolean maximizable) {
		this.maximizable = maximizable;
	}

	public Boolean getSlidingAutohide() {
		return slidingAutohide;
	}

	public void setSlidingAutohide(Boolean slidingAutohide) {
		this.slidingAutohide = slidingAutohide;
	}

	public Boolean getTabDockAllowed() {
		return tabDockAllowed;
	}

	public void setTabDockAllowed(Boolean tabDockAllowed) {
		this.tabDockAllowed = tabDockAllowed;
	}

	public Boolean getAutohidable() {
		return autohidable;
	}

	public void setAutohidable(Boolean autohidable) {
		this.autohidable = autohidable;
	}

	public Boolean getFloatable() {
		return floatable;
	}

	public void setFloatable(Boolean floatable) {
		this.floatable = floatable;
	}

	public Boolean getDockable() {
		return dockable;
	}

	public void setDockable(Boolean dockable) {
		this.dockable = dockable;
	}

	public Boolean getHidable() {
		return hidable;
	}

	public void setHidable(Boolean hidable) {
		this.hidable = hidable;
	}

	public Boolean getShowGripper() {
		return showGripper;
	}

	public void setShowGripper(Boolean showGripper) {
		this.showGripper = showGripper;
	}

	public Boolean getShowTitleBar() {
		return showTitleBar;
	}

	public void setShowTitleBar(Boolean showTitleBar) {
		this.showTitleBar = showTitleBar;
	}

	public Boolean getSideDockAllowed() {
		return sideDockAllowed;
	}

	public void setSideDockAllowed(Boolean sideDockAllowed) {
		this.sideDockAllowed = sideDockAllowed;
	}

	public void setFloatBounds(Rectangle floatBounds){
		this.floatBounds = floatBounds;
	}
	
	public Rectangle getFloatBounds(){
		return floatBounds;
	}
	
	public void setFloatOnShow(boolean floatOnShow){
		this.floatOnShow = floatOnShow;
	}
	
	public boolean isFloatOnShow(){
		return floatOnShow;
	}
	
	/**
	 * @return is the view to be treated as a JIDE workspace
	 */
	public boolean isWorkspace(){
		return isWorkspace;
	}
	
	/**
	 * Specify if the view is to be treated as a JIDE workspace
	 * 
	 * @param isWorkspace true if the view is to be treated as a JIDE workspace
	 */
	public void setIsWorkspace(boolean isWorkspace){
		this.isWorkspace = isWorkspace;
	}
	
	/**
	 * Specify the initMode of the resultant DockableFrame
	 * 
	 * @param initMode the required initMode, as a constant from DockContext
	 */
	public void setInitMode(int initMode){
		this.initMode = initMode;
	}
	
	/**
	 * Specify the initSide of the resultant DockableFrame
	 * 
	 * @param initSide the required initSide, as a constant from DockContext
	 */
	public void setInitSide(int initSide){
		this.initSide = initSide;
	}
	
	/**
	 * Specify the initIndex of the resultant DockableFrame.
	 * 
	 * @param initIndex the required initIndex, usually 0 or 1.
	 */
	public void setInitIndex(int initIndex){
		this.initIndex = initIndex;
	}
	
	public int getInitMode(){
		return initMode;
	}
	
	public int getInitSide(){
		return initSide;
	}
	
	public int getInitIndex(){
		return initIndex;
	}

	/**
	 * Compares the display names of the view descriptors
	 */
	public int compareTo(Object o) {
		ViewDescriptor castObj = (ViewDescriptor)o;
		return this.getDisplayName().compareToIgnoreCase(castObj.getDisplayName());
	}
}
