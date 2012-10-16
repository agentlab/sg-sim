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

import name.milesparker.epi.contact.Epidemic3D;

import org.eclipse.amp.agf.IGraphicsAdapter;
import org.eclipse.amp.agf3d.IGraphics3DAdapted;
import org.eclipse.amp.agf3d.IGraphics3DAdapter;

/**
 * Demonstrates how to define custom 3D and color providers.
 * 
 * @author mparker
 * 
 */
public class Epidemic3DTiles extends Epidemic3D implements IGraphics3DAdapted {

    /**
     * @return
     * @see org.eclipse.amp.agf3d.IGraphics3DAdapted#getGraphics3DAdapter()
     */
    public IGraphicsAdapter getGraphicsAdapter() {
        return EpidemicTilesAdapter.singleton;
    }

    /**
     * @return
     * @see org.eclipse.amp.agf3d.IGraphics3DAdapted#getGraphics3DAdapter()
     */
    public IGraphics3DAdapter getGraphics3DAdapter() {
        return EpidemicTiles3DAdapter.singleton;
    }
}
