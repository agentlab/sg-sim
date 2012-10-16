package org.eclipse.amp.amf.examples.escape;

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
 * Individual Java Implementation.
 * 
 * Generated by AMF for model: WikiExample.metaabm in project: org.eclipse.amp.amf.examples.escape 
 * <!-- end-user-doc -->
 * @generated
 */
public class Individual extends CellOccupant {

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private int age = 0;
	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private int vision = 5;
	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private double wealth = 0.0;

	private List<IAgentChild> children;

	public List<IAgentChild> getChildren() {
		return children;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Constructs a new Individual.
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Individual() {

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
			Individual clone = (Individual) super.clone();
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
	public WikiExample getWikiExample() {
		return (WikiExample) getScape().getScape();
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
	 * Find Partner Rule. Executed every period.
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void findPartner() {
		Conditional partnerCondition = new Conditional() {

			/**
			 * <!-- begin-user-doc -->
			 * 
			 * <!-- end-user-doc -->
			 * @generated
			 */
			private static final long serialVersionUID = 6846144446402098985L;

			/**
			 * <!-- begin-user-doc -->
			 * 
			 * <!-- end-user-doc -->
			 * @generated
			 */
			public boolean meetsCondition(Object partnerCell) {
				partnerCell = ((org.ascape.model.HostCell) partnerCell)
						.getOccupant();
				if (partnerCell instanceof Individual) {
					Individual partner = (Individual) partnerCell;
					return (getAge() == partner.getAge());
				} else {
					return false;
				}
			}
		};
		Location partnerLocation = ((org.ascape.model.space.Discrete) getWikiExample()
				.getCity().getSpace()).findRandomWithin(
				((org.ascape.model.CellOccupant) this).getHostCell(),
				partnerCondition, false, getVision());
		if (partnerLocation != null) {
			Individual partner = (Individual) ((org.ascape.model.HostCell) partnerLocation)
					.getOccupant();
			if (partner != null) {
				Block partnerNeighbor = (Block) ((org.ascape.model.space.Discrete) partner
						.getWikiExample().getCity().getSpace())
						.findRandomAvailableNeighbor(((org.ascape.model.CellOccupant) partner)
								.getHostCell());
				if (partnerNeighbor != null) {
					if (getHostScape() != ((Agent) partnerNeighbor).getScape()) {
						die();
						getWikiExample().getIndividualScape().add(this);
					}
					moveTo(partnerNeighbor);
				}
			}
		}
	}
	/**
	 * <!-- begin-user-doc -->
	 * Initial State Initialization. Executed once at the beginning of each model run.
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initialState() {
		int initialStateRandomToLimit_MaximumAge_ = randomToLimit(getWikiExample()
				.getMaximumAge());
		double initialStateRandomUnit = getRandom().nextDouble();
		setAge(initialStateRandomToLimit_MaximumAge_);
		setWealth(initialStateRandomUnit);
	}
	/**
	 * <!-- begin-user-doc -->
	 * Initial Movement Initialization. Executed once at the beginning of each model run.
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initialMovement() {
		Block individual = (Block) ((org.ascape.model.space.Discrete) getWikiExample()
				.getCity().getSpace()).findRandomAvailable();
		if (individual != null) {
			if (getHostScape() != ((Agent) individual).getScape()) {
				die();
				getWikiExample().getIndividualScape().add(this);
			}
			moveTo(individual);
		}
	}
	/**
	 * <!-- begin-user-doc -->
	 * Gets the Age property for Individual.
	 * @return 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getAge() {
		return age;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Sets the Age property for Individual.
	 * 
	 * @param _age the new Age value
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAge(int _age) {
		age = _age;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Gets the Vision property for Individual.
	 * @return 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getVision() {
		return vision;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Sets the Vision property for Individual.
	 * 
	 * @param _vision the new Vision value
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVision(int _vision) {
		vision = _vision;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Gets the Wealth property for Individual.
	 * @return 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getWealth() {
		return wealth;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Sets the Wealth property for Individual.
	 * 
	 * @param _wealth the new Wealth value
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWealth(double _wealth) {
		wealth = _wealth;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		if (name == null) {
			return "Individual " + getUID();
		} else {
			return name;
		}
	}
}
