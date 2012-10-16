package org.eclipse.amp.examples.heatbugs;

import org.eclipse.amp.agf3d.IGraphics3DAdapted;
import org.eclipse.amp.agf3d.IGraphics3DAdapter;

public class Heatbugs3DCustom extends Heatbugs3D implements IGraphics3DAdapted {

    /**
     * @return
     * @see org.eclipse.amp.agf3d.IGraphics3DAdapted#getGraphics3DAdapter()
     */
    public IGraphics3DAdapter getGraphics3DAdapter() {
        return Heatbugs3DAdapter.singleton;
    }
}
