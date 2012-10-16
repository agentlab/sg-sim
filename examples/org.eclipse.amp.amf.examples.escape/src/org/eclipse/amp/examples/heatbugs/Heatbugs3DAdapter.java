package org.eclipse.amp.examples.heatbugs;

import org.eclipse.amp.agf.gef.GenericEditPart;
import org.eclipse.amp.agf3d.IGraphics3DAdapter;
import org.eclipse.amp.agf3d.IShape3DProvider;
import org.eclipse.amp.escape.ascape.adapt.AscapeDefaultGraphics3DAdapter;
import org.eclipse.draw3d.IFigure3D;
import org.eclipse.draw3d.geometry.IVector3f;
import org.eclipse.draw3d.geometry.Vector3fImpl;
import org.eclipse.draw3d.shapes.CylindricFigureShape;
import org.eclipse.draw3d.shapes.Shape;

/**
 * 
 * @author mparker
 * 
 */
public class Heatbugs3DAdapter extends AscapeDefaultGraphics3DAdapter {

    public static final IGraphics3DAdapter singleton = new Heatbugs3DAdapter();

    private static final Vector3fImpl CONE_SIZE = new Vector3fImpl(GenericEditPart.SCALE * .9f,
                                                                   GenericEditPart.SCALE * .9f, GenericEditPart.SCALE * .80f);

    private static final Vector3fImpl CONE_IN = new Vector3fImpl(GenericEditPart.SCALE * .05f,
                                                                 GenericEditPart.SCALE * .05f, 0);

    public static IShape3DProvider CONE_SHAPE_PROVIDER = new IShape3DProvider() {
        public Shape getShape3D(Object object, IFigure3D figure) {
            CylindricFigureShape cylindricFigureShape = new CylindricFigureShape(figure, 30, 2.0f, true);
            cylindricFigureShape.setFill(true);
            return cylindricFigureShape;
        }

        public IVector3f getSize3D(Object object) {
            return CONE_SIZE;
        }

        public IVector3f getRelativeLocation3D(Object object) {
            return CONE_IN;
        }
    };
    public static final IShape3DProvider[] BUG_SHAPE_PROVIDERS = new IShape3DProvider[] { CONE_SHAPE_PROVIDER };

    // public static final IShape3DProvider[] CELL_SHAPE_PROVIDERS = new IShape3DProvider[] { HEAT_CELL_PROVIDER };

    /**
     * @param object
     * @return
     * @see org.eclipse.amp.agf3d.Graphics3DAdapter#getShapes(java.lang.Object)
     */
    public IShape3DProvider[] getShapes(Object object) {
        // if (object instanceof HeatCell) {
        // return CELL_SHAPE_PROVIDERS;
        // }
        if (object instanceof HeatBug) {
            return BUG_SHAPE_PROVIDERS;
        }
        return super.getShapes(object);
    }
}
