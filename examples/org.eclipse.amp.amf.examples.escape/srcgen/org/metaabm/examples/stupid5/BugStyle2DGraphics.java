package org.metaabm.examples.stupid5;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import org.ascape.model.Agent;
import org.ascape.model.CellOccupant;
import org.ascape.model.HostCell;

import org.ascape.util.vis.DrawFeature;

/**
 * <!-- begin-user-doc -->
 * Bug Style 2D Java Implementation.
 * 
 * Generated by AMF for model: StupidModel5.metaabm in project: org.eclipse.amp.amf.examples.escape 
 * <!-- end-user-doc -->
 * @generated
 */
public class BugStyle2DGraphics extends DrawFeature {

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	enum Shapes {
		rectangle, oval, marker
	};

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	Shapes shape = Shapes.rectangle;

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void draw(Graphics g, Object object, int width, int height) {

		HostCell rootActGroupCell = (HostCell) object;
		if (rootActGroupCell.getOccupant() instanceof Bug) {
			Bug rootActGroup = (Bug) rootActGroupCell.getOccupant();
			Bug bugRule = rootActGroup;
			double bugSizeRatio = bugRule.getSize() / 10.0;

			double bugSizeUnit = java.lang.Math.min(bugSizeRatio, 1.0)

			;

			double invertBugSize = 1 - bugSizeUnit;

			Color bugRuleColorRGB_BugRuleMinimum_BugSizeRatioLiteral1pt0_Literal1pt0Literal1pt0_ = new Color(
					Display.getCurrent(), (int) (1.0 * 255),
					(int) (invertBugSize * 255), (int) (invertBugSize * 255));

			g.setForegroundColor(bugRuleColorRGB_BugRuleMinimum_BugSizeRatioLiteral1pt0_Literal1pt0Literal1pt0_);
			g.setBackgroundColor(bugRuleColorRGB_BugRuleMinimum_BugSizeRatioLiteral1pt0_Literal1pt0Literal1pt0_);
			shape = shape.oval;
			if (shape == Shapes.oval) {
				g.fillOval(0, 0, width, height);
			} else if (shape == Shapes.marker) {
				int d = (int) (width * .25 - 1);
				int d2 = (int) (width * .5 - 1);
				g.fillOval(d, d, d2, d2);
			} else if (shape == Shapes.rectangle) {
				g.fillRectangle(0, 0, width, height);
			}
		}
	}
}
