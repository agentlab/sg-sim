package name.milesparker.epi.region;

import java.io.File;
import java.io.IOException;

import org.ascape.model.space.SpatialTemporalException;

public class EpidemicPeriods extends Region {

    /**
     * @return
     * @see name.milesparker.epi.region.Region#createCity()
     */
    protected City createCityPrototype() {
        return new City() {
            // We want to run headless in all cases..
            public void createGraphicViews() {
            }
        };
    }

    public void createScape() {
        setRegionalMapWidth(40);
        setRegionalMapHeight(4);
        setCityCount(100);
        setMigrationProbability(0.0005);
        super.createScape();
        try {
            setStopPeriod(3000);
        } catch (SpatialTemporalException e) {
            throw new RuntimeException(e);
        }
    }


    public void createViews() {
        super.createViews();
        org.ascape.view.nonvis.DataOutputView dataView = new org.ascape.view.nonvis.DataOutputView() {
            /**
             * @throws IOException
             * @see org.ascape.view.nonvis.DataOutputView#writePeriodData()
             */
            public void scapeChanged() {
                if (getPeriod() % 20 == 0) {
                    super.scapeChanged();
                }
            }
        };
        try {
            dataView
            .setRunFile(new File(
            "/Volumes/Resources/Developer/repos/eclipse/org.eclipse.amp.amf/examples/org.eclipse.amp.amf.examples.escape/output/Results2.txt"));
            dataView
            .setPeriodFile(new File(
            "/Volumes/Resources/Developer/repos/eclipse/org.eclipse.amp.amf/examples/org.eclipse.amp.amf.examples.escape/output/Periods2.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addView(dataView);
        dataView.getDataSelection().clearSelection();
        for (int i = 0; i < getCityCount(); i++) {
            dataView.getDataSelection().setSelected("Count City " + i + " Individual Symptom Infectious Status", true);
        }
        // SweepControlView view = new SweepControlView();
        // addView(view);
        // SweepDimension sweep = new SweepDimension(this, "MinContactTransmissionProbability", 0.04, 0.08, 0.01);
        // SweepDimension sweep2 = new SweepDimension(this, "MaxContactTransmissionProbability", 0.12, 0.16, 0.01);
        // SweepLink link = new SweepLink();
        // link.addMember(sweep);
        // link.addMember(sweep2);
        // view.getSweepGroup().addMember(link);
        // view.getSweepGroup().setRunsPer(5);
    }
}
