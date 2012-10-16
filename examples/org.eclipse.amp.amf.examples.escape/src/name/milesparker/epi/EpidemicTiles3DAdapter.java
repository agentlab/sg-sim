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

import name.milesparker.epi.contact.Individual;

import org.eclipse.amp.agf3d.IGraphics3DAdapter;
import org.eclipse.amp.agf3d.IShape3DProvider;
import org.eclipse.amp.agf3d.Shapes3D;
import org.eclipse.amp.escape.ascape.adapt.AscapeDefaultGraphics3DAdapter;

/**
 * 
 * @author mparker
 *
 */
public class EpidemicTiles3DAdapter extends AscapeDefaultGraphics3DAdapter {

    public static final IGraphics3DAdapter singleton = new EpidemicTiles3DAdapter();

    private static final IShape3DProvider[] HUMAN_SHAPE_PROVIDERS = new IShape3DProvider[] { Shapes3D.HUMAN_SHAPE_PROVIDER };

    /**
     * @param object
     * @return
     * @see org.eclipse.amp.agf3d.Graphics3DAdapter#getShapes(java.lang.Object)
     */
    public IShape3DProvider[] getShapes(Object object) {
        if (object instanceof Individual) {
            return HUMAN_SHAPE_PROVIDERS;
        }
        return super.getShapes(object);
    }
}
