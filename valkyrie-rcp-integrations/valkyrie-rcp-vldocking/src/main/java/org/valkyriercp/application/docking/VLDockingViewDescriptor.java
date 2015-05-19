package org.valkyriercp.application.docking;

import com.vldocking.swing.docking.DockingConstants;
import org.valkyriercp.application.View;
import org.valkyriercp.application.support.DefaultViewDescriptor;

import java.util.Collections;
import java.util.Map;

/**
 * @author Rogan Dawes
 */
public class VLDockingViewDescriptor extends DefaultViewDescriptor {

	public static boolean DEFAULT_AUTOHIDEENABLED = false;

	public static DockingConstants.Hide DEFAULT_AUTOHIDEBORDER = DockingConstants.HIDE_LEFT;

	public static boolean DEFAULT_CLOSEENABLED = false;

	public static boolean DEFAULT_FLOATENABLED = false;

	public static boolean DEFAULT_MAXIMIZEENABLED = true;

	private DockingConstants.Hide autoHideBorder = DEFAULT_AUTOHIDEBORDER;

	private boolean autoHideEnabled = DEFAULT_AUTOHIDEENABLED;

	private boolean closeEnabled = DEFAULT_CLOSEENABLED;

	private boolean floatEnabled = DEFAULT_FLOATENABLED;

	private boolean maximizeEnabled = DEFAULT_MAXIMIZEENABLED;

    private float resizeWeight;

	public VLDockingViewDescriptor() {
		// default constructor for spring creation
	}

	public VLDockingViewDescriptor(String id, Class<? extends View> viewClass) {
		this(id, viewClass, Collections.<String, Object> emptyMap());
	}

	public VLDockingViewDescriptor(String id, Class<? extends View> viewClass,
			Map<String, Object> viewProperties) {
		setId(id);
		setViewClass(viewClass);
		setViewProperties(viewProperties);
	}

	/**
	 * @return the autoHideBorder
	 */
	public DockingConstants.Hide getAutoHideBorder() {
		return this.autoHideBorder;
	}

	/**
	 * @param autoHideBorder
	 *            the autoHideBorder to set
	 */
	public void setAutoHideBorder(DockingConstants.Hide autoHideBorder) {
		this.autoHideBorder = autoHideBorder;
	}

	/**
	 * @return the autoHideEnabled
	 */
	public boolean isAutoHideEnabled() {
		return this.autoHideEnabled;
	}

	/**
	 * @param autoHideEnabled
	 *            the autoHideEnabled to set
	 */
	public void setAutoHideEnabled(boolean autoHideEnabled) {
		this.autoHideEnabled = autoHideEnabled;
	}

	/**
	 * @return the closeEnabled
	 */
	public boolean isCloseEnabled() {
		return this.closeEnabled;
	}

	/**
	 * @param closeEnabled
	 *            the closeEnabled to set
	 */
	public void setCloseEnabled(boolean closeEnabled) {
		this.closeEnabled = closeEnabled;
	}

	/**
	 * @return the floatEnabled
	 */
	public boolean isFloatEnabled() {
		return this.floatEnabled;
	}

	/**
	 * @param floatEnabled
	 *            the floatEnabled to set
	 */
	public void setFloatEnabled(boolean floatEnabled) {
		this.floatEnabled = floatEnabled;
	}

	/**
	 * @return the maximizeEnabled
	 */
	public boolean isMaximizeEnabled() {
		return this.maximizeEnabled;
	}

	/**
	 * @param maximizeEnabled
	 *            the maximizeEnabled to set
	 */
	public void setMaximizeEnabled(boolean maximizeEnabled) {
		this.maximizeEnabled = maximizeEnabled;
	}

    public float getResizeWeight() {
        return resizeWeight;
    }

    public void setResizeWeight(float resizeWeight) {
        this.resizeWeight = resizeWeight;
    }
}
