package org.metaabm.examples.stupid5;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.amp.agf.IGraphicsAdapted;
import org.eclipse.amp.agf.IGraphicsAdapter;

import org.eclipse.swt.graphics.Color;
import org.eclipse.jface.viewers.IColorProvider;

import org.ascape.model.Agent;
import org.ascape.model.Cell;
import org.ascape.model.CellOccupant;
import org.ascape.model.HostCell;
import org.ascape.model.LocatedAgent;
import org.ascape.model.Scape;
import org.ascape.model.event.ScapeEvent;
import org.ascape.model.rule.Rule;
import org.ascape.model.rule.ExecuteThenUpdate;
import org.ascape.model.space.CollectionSpace;
import org.ascape.model.space.Coordinate;
import org.ascape.model.space.Coordinate2DDiscrete;
import org.ascape.model.space.Graph;
import org.ascape.model.space.Location;
import org.ascape.model.space.Singleton;
import org.ascape.runtime.NonGraphicRunner;
import org.ascape.util.Conditional;
import org.ascape.util.data.DataPoint;
import org.ascape.util.data.DataPointConcrete;
import org.ascape.util.vis.ColorFeature;
import org.ascape.util.vis.ColorFeatureConcrete;
import org.ascape.view.vis.ChartView;
import org.ascape.view.vis.GEFView;
import org.ascape.view.vis.GraphView;

import org.eclipse.amp.escape.runtime.extension.IAgentChild;

/**
 * <!-- begin-user-doc -->
 * Bug Java Implementation.
 * 
 * Generated by AMF for model: StupidModel5.metaabm in project: org.eclipse.amp.amf.examples.escape 
 * <!-- end-user-doc -->
 * @generated
 */
public class Bug extends CellOccupant {

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private double size = 0.0;
	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private double consumption = 0.0;
	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private double maxConsumption = 0.2;

	private List<IAgentChild> children;

	public List<IAgentChild> getChildren() {
		return children;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Constructs a new Bug.
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Bug() {

		children = new ArrayList<IAgentChild>();

	}
	//todo, make this a useful value for evaluating compatibility of different versions of generated classes

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final long serialVersionUID = 89989998L;

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static long nextUniqueID;

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private long uniqueID;

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getUID() {
		if (uniqueID == 0) {
			uniqueID = nextUniqueID++;
		}
		return uniqueID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Clones the agent, ensuring that a unique id is assigned.
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object clone() {
		try {
			Bug clone = (Bug) super.clone();
			clone.uniqueID = 0;
			return clone;
		} catch (Exception e) {
			throw new RuntimeException("Unexpected cloning exception: " + e);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StupidModel5 getStupidModel5() {
		return (StupidModel5) getScape().getScape();
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */

	public void startSimulationAgentChild(int timeStep) {
		if (timeStep == getRoot().getRunner().getEarliestPeriod()) {
			for (IAgentChild tmp : children) {
				tmp.startSimulation(timeStep);
			}
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */

	public void calculateTimeStep(int timeStep) {
		for (IAgentChild tmp : children) {
			tmp.calculate(timeStep);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * Initialize Initialization. Executed once at the beginning of each model run.
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void intializeNonFramework() {
		Habitat randomHabitat = (Habitat) ((org.ascape.model.space.Discrete) getStupidModel5()
				.getGrid2D().getSpace()).findRandomAvailable();
		if (randomHabitat != null) {
			if (getHostScape() != ((Agent) randomHabitat).getScape()) {
				die();
				getStupidModel5().getBugScape().add(this);
			}
			moveTo(randomHabitat);
		}
	}
	/**
	 * <!-- begin-user-doc -->
	 * Random Movement Rule. Executed every period.
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void randomMovement() {
		Habitat nearbySpace = (Habitat) ((org.ascape.model.space.Discrete) getStupidModel5()
				.getGrid2D().getSpace()).findRandomAvailable(
				((org.ascape.model.CellOccupant) this).getHostCell(), null,
				false, getStupidModel5().getBugVision());
		if (nearbySpace != null) {
			if (getHostScape() != ((Agent) nearbySpace).getScape()) {
				die();
				getStupidModel5().getBugScape().add(this);
			}
			moveTo(nearbySpace);
		}
	}
	/**
	 * <!-- begin-user-doc -->
	 * Grow Rule. Executed every period.
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void grow() {
		Habitat habitat = (Habitat) getHostCell();
		double availability = habitat.getFoodAvailability();
		double consumption = availability < getMaxConsumption()
				? availability
				: getMaxConsumption();
		setSize(getSize() + consumption);
		habitat.setFoodAvailability(availability - consumption);

	}
	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void consumeFood() {
	}

	/**
	 * <!-- begin-user-doc -->
	 * Gets the Size property for Bug.
	 * @return 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getSize() {
		return size;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Sets the Size property for Bug.
	 * 
	 * @param _size the new Size value
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSize(double _size) {
		size = _size;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Gets the Consumption property for Bug.
	 * @return 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getConsumption() {
		return consumption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Sets the Consumption property for Bug.
	 * 
	 * @param _consumption the new Consumption value
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConsumption(double _consumption) {
		consumption = _consumption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Gets the Max Consumption property for Bug.
	 * @return 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getMaxConsumption() {
		return maxConsumption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Sets the Max Consumption property for Bug.
	 * 
	 * @param _maxConsumption the new Max Consumption value
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxConsumption(double _maxConsumption) {
		maxConsumption = _maxConsumption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		if (name == null) {
			return "Bug " + getUID();
		} else {
			return name;
		}
	}
}
