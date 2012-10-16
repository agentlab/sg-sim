package name.milesparker.epi.plague;

import org.ascape.util.vis.ColorFeature;
import org.ascape.model.CellOccupant;
import org.ascape.model.HostCell;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.ColorConstants;

import org.eclipse.jface.viewers.IColorProvider;

/**
 * <!-- begin-user-doc -->
 * Flea Style 2D Java Implementation.
 * 
 * Generated by AMF for model: EpidemicPlague.metaabm in project: org.eclipse.amp.amf.examples.escape 
 * <!-- end-user-doc -->
 * @generated
 */
public class FleaStyle2DColorProvider implements IColorProvider {

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static IColorProvider singleton;

	/**
	 * <!-- begin-user-doc -->
	 * Provides a color for Flea based on wether we want to fill or not.
	 * 
	 * @param object the agent
	 * @return	the foreground color for the element, or <code>null</code> 
	 *   to use the default foreground color
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Color getColor(Object object, boolean fill) {
		Color color = null;
		Color fillColor = null;
		Color outlineColor = null;

		Flea actions = (Flea) object;
		Flea fleaRule = actions;
		outlineColor = ColorConstants.gray;
		if (fill) {
			return fillColor;
		} else {
			return outlineColor;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * Provides a foreground color for the given Flea.
	 * 
	 * @param object the agent
	 * @return	the foreground color for the element, or <code>null</code> 
	 *   to use the default foreground color
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Color getForeground(Object object) {
		Color color = getColor(object, false);
		if (color != null) {
			return color;
		}
		//try to see if we have a fill color, if we do, we'll paint the whole agent that color.
		color = getColor(object, true);
		if (color != null) {
			return color;
		}
		return ColorFeature.BLACK;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Provides a background color for the given Flea.
	 * 
	 * @param object the agent
	 * @return	the background color for the element, or <code>null</code> 
	 *   to use the default background color
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Color getBackground(Object object) {
		Color color = getColor(object, true);
		if (color != null) {
			return color;
		}
		//if no fill color, just return white
		return ColorFeature.WHITE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static IColorProvider getDefault() {
		if (singleton == null) {
			singleton = new FleaStyle2DColorProvider();
		}
		return singleton;
	}
}
