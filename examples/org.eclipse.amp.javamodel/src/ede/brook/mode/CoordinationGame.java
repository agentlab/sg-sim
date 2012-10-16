package ede.brook.mode;

import java.awt.Color;

import org.ascape.model.HostCell;
import org.ascape.model.Scape;
import org.ascape.model.event.ScapeEvent;
import org.ascape.model.space.Array2DVonNeumann;
import org.ascape.util.data.StatCollector;
import org.ascape.util.data.StatCollectorCond;
//import org.ascape.view.vis.ChartView;
//import org.ascape.view.vis.Overhead2DView;

public class CoordinationGame extends Scape {

    // model-scope variables
    protected int nPlayers = 100;

    protected int latticeWidth = 30;

    protected int latticeHeight = 30;

    public int coordinateOnBlue = 1;

    public int coordinateOnRed = 1;

    public int error = 0;

    Scape lattice;

    Scape players;

//    Overhead2DView overheadView;

    // creates scapes and agents
    public void createScape() {
        super.createScape();
        lattice = new Scape(new Array2DVonNeumann());
        lattice.setPrototypeAgent(new HostCell());
        lattice.setExtent(latticeWidth, latticeHeight);

        CoordinationGamePlayer cgplayer = new CoordinationGamePlayer();
        cgplayer.setHostScape(lattice);
        players = new Scape();
        players.setName("Players");
        players.setPrototypeAgent(cgplayer);
        players.setExecutionOrder(Scape.RULE_ORDER);

        add(lattice);
        add(players);

        StatCollector CountReds = new StatCollectorCond("Reds") {
            public boolean meetsCondition(Object object) {
                return (((CoordinationGamePlayer) object).myColor == Color.red);
            }
        };
        StatCollector CountBlues = new StatCollectorCond("Blues") {
            public boolean meetsCondition(Object object) {
                return (((CoordinationGamePlayer) object).myColor == Color.blue);
            }
        };

        players.addStatCollector(CountReds);
        players.addStatCollector(CountBlues);
    }

    public void scapeSetup(ScapeEvent scapeEvent) {
        ((Scape) players).setExtent(nPlayers);
    }

// create views and charts
     public void createGraphicViews() {
/*         super.createGraphicViews();
         ChartView chart = new ChartView();
         players.addView(chart);
         chart.addSeries("Count Reds", Color.red);
         chart.addSeries("Count Blues", Color.blue);
         chart.setDisplayPoints(100);

         overheadView = new Overhead2DView();
         overheadView.setCellSize(15);
         lattice.addView(overheadView);*/
     }

    // get() and set() methods for the model variables
    public int getRedScore() {
        return coordinateOnRed;
    }

    public void setRedScore(int NewcoordinateOnRed) {
        coordinateOnRed = NewcoordinateOnRed;

    }

    public int getBlueScore() {
        return coordinateOnBlue;
    }

    public void setBlueScore(int NewcoordinateOnBlue) {
        coordinateOnBlue = NewcoordinateOnBlue;

    }

    public int getnPlayers() {
        return nPlayers;
    }

    public void setnPlayers(int NewnPlayers) {
        nPlayers = NewnPlayers;

    }
}
