/**
 * <copyright>
 *
 * Copyright (c) 2009 Metascape, LLC.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Metascape - Initial API and Implementation
 *
 * </copyright>
 *
 */

package name.milesparker.epi;

import name.milesparker.epi.contact.EpidemicGraphicsAdapter;

import org.ascape.model.HostCell;
import org.ascape.model.space.Node;
import org.ascape.util.Conditional;
import org.ascape.util.vis.ColorFeature;
import org.ascape.util.vis.ColorFeatureConcrete;
import org.eclipse.amp.agf.IGraphicsAdapter;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.graphics.Color;

/**
 * 
 * @author mparker
 *
 */
public class EpidemicTilesAdapter extends EpidemicGraphicsAdapter {

    private static final IColorProvider[] INDIVIDUAL_COLOR_PROVIDERS = new IColorProvider[] { IndividualStyle2DColorProvider.getDefault() };

    public static IGraphicsAdapter singleton = new EpidemicTilesAdapter();

    public static final IColorProvider TILE_COLOR_PROVIDER = new IColorProvider() {
        public Color getForeground(Object element) {
            return ColorFeature.GRAY;
        }

        public Color getBackground(Object element) {
            if (!((HostCell) element).getNeighbors(INFECT_LOCATION).isEmpty()) {
                return INFECTED_TILE_COLOR;
            } else if (!((HostCell) element).getNeighbors(EXPOSED_LOCATION).isEmpty()) {
                return EXPOSED_TILE_COLOR;
            }
            return SAFE_TILE_COLOR;
        }
    };

    public static final IColorProvider[] TILE_COLOR_PROVIDERS = new IColorProvider[] { TILE_COLOR_PROVIDER };

    private static final Conditional INFECT_LOCATION = new Conditional() {
        public boolean meetsCondition(Object object) {
            Node occupant = ((HostCell) object).getOccupant();
            return occupant instanceof Individual
            && (((Individual) occupant).getStatus() == StatusEnum.asymptomInfectious || ((Individual) occupant)
                    .getStatus() == StatusEnum.symptomInfectious);
        }
    };
    private static final Conditional EXPOSED_LOCATION = new Conditional() {
        public boolean meetsCondition(Object object) {
            Node occupant = ((HostCell) object).getOccupant();
            return occupant instanceof Individual && ((Individual) occupant).getStatus() == StatusEnum.exposed;
        }
    };

    private static Color INFECTED_TILE_COLOR = ColorFeatureConcrete.createHSB(0, 0.5f, 0.95f);
    private static Color EXPOSED_TILE_COLOR = ColorFeatureConcrete.createHSB(60, 0.5f, 0.95f);
    private static Color SAFE_TILE_COLOR = ColorFeatureConcrete.createHSB(120, 0.5f, 0.95f);


    public IColorProvider[] getColors(Object object) {
        if (object instanceof HostCell) {
            return TILE_COLOR_PROVIDERS;
        }
        if (object instanceof Individual) {
            return INDIVIDUAL_COLOR_PROVIDERS;
        }
        return super.getColors(object);
    };

}
