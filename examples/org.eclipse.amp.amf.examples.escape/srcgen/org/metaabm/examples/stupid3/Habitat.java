package org.metaabm.examples.stupid3;

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
 * Habitat Java Implementation.
 * 
 * Generated by AMF for model: StupidModel3.metaabm in project: org.eclipse.amp.amf.examples.escape 
 * <!-- end-user-doc -->
 * @generated
 */
public class Habitat extends HostCell {

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private double foodAvailability = 0.0;
	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private double maximumFoodProductionRate = 0.01;

	private List<IAgentChild> children;

	public List<IAgentChild> getChildren() {
		return children;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Constructs a new Habitat.
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Habitat() {

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
			Habitat clone = (Habitat) super.clone();
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
	public StupidModel3 getStupidModel3() {
		return (StupidModel3) getScape().getScape();
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
	 * determindFoodProduction Rule. Executed every period.
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void determindFoodProduction() {
		double calculateFoodProductionRate = randomInRange(0,
				getMaximumFoodProductionRate());
		double newFoodProductionValue = getFoodAvailability()
				+ calculateFoodProductionRate;
		setFoodAvailability(newFoodProductionValue);
	}
	/**
	 * <!-- begin-user-doc -->
	 * Gets the FoodAvailability property for Habitat.
	 * @return 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getFoodAvailability() {
		return foodAvailability;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Sets the FoodAvailability property for Habitat.
	 * 
	 * @param _foodAvailability the new FoodAvailability value
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFoodAvailability(double _foodAvailability) {
		foodAvailability = _foodAvailability;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Gets the MaximumFoodProductionRate property for Habitat.
	 * @return 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getMaximumFoodProductionRate() {
		return maximumFoodProductionRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Sets the MaximumFoodProductionRate property for Habitat.
	 * 
	 * @param _maximumFoodProductionRate the new MaximumFoodProductionRate value
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaximumFoodProductionRate(double _maximumFoodProductionRate) {
		maximumFoodProductionRate = _maximumFoodProductionRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		if (name == null) {
			return "Habitat " + getUID();
		} else {
			return name;
		}
	}
}
