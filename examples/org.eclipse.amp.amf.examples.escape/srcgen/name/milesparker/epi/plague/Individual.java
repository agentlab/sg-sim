package name.milesparker.epi.plague;

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
 * Generated by AMF for model: EpidemicPlague.metaabm in project: org.eclipse.amp.amf.examples.escape 
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
	private StatusEnum status = StatusEnum.susceptible;
	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private int exposureEndPeriod = 0;
	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private int asymptomaticEndPeriod = 0;
	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private int outcomePeriod = 0;
	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private double contactTransmissionProbability = 0.0;

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
	public Epidemic getEpidemic() {
		return (Epidemic) getScape().getScape();
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
	 * Initialize Location Initialization. Executed once at the beginning of each model run.
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializeLocation() {
		Object cityLocation = (Object) ((org.ascape.model.space.Discrete) getEpidemic()
				.getCity().getSpace()).findRandomAvailable();
		if (cityLocation != null) {
			if (getHostScape() != ((Agent) cityLocation).getScape()) {
				die();
				getEpidemic().getIndividualScape().add(this);
			}
			moveTo(((HostCell) cityLocation));
		}
	}
	/**
	 * <!-- begin-user-doc -->
	 * Movement Rule. Executed every period.
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void movement() {
		double movementDraw = getRandom().nextDouble();
		if ((getStatus() != StatusEnum.dead
				&& movementDraw < getEpidemic().getMovementProbability()
				&& getStatus() != StatusEnum.symptomInfectious && movementDraw < getEpidemic()
				.getMovementProbability())) {
			Object neighboringLocation = (Object) ((org.ascape.model.space.Discrete) getEpidemic()
					.getCity().getSpace())
					.findRandomAvailableNeighbor(((org.ascape.model.CellOccupant) this)
							.getHostCell());
			if (neighboringLocation != null) {
				if (getHostScape() != ((Agent) neighboringLocation).getScape()) {
					die();
					getEpidemic().getIndividualScape().add(this);
				}
				moveTo(((HostCell) neighboringLocation));
			}
		}
	}
	/**
	 * <!-- begin-user-doc -->
	 * Initialize State Initialization. Executed once at the beginning of each model run.
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializeState() {
		double infectionDraw = getRandom().nextDouble();
		double individualTransmissionRate = randomInRange(getEpidemic()
				.getMinContactTransmissionProbability(), getEpidemic()
				.getMaxContactTransmissionProbability());
		if (infectionDraw < getEpidemic().getInitialInfectionProbability()) {
			setStatus(StatusEnum.exposed);
		}
		setContactTransmissionProbability(individualTransmissionRate);
	}
	/**
	 * <!-- begin-user-doc -->
	 * Transmission Rule. Executed every period.
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void transmission() {
		if ((getStatus() == StatusEnum.symptomInfectious || getStatus() == StatusEnum.asymptomInfectious)) {
			Conditional potentialExposureCondition = new Conditional() {

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
				public boolean meetsCondition(Object potentialExposureCell) {
					if (potentialExposureCell instanceof Individual) {
						return true;
					} else {
						return false;
					}
				}
			};
			Individual potentialExposure = (Individual) ((org.ascape.model.space.Discrete) getEpidemic()
					.getCity().getSpace()).findRandomNeighbor(this,
					potentialExposureCondition);
			if (potentialExposure != null) {
				if (potentialExposure.getStatus() == StatusEnum.susceptible) {
					double transmissionDraw = getRandom().nextDouble();
					if (transmissionDraw < potentialExposure
							.getContactTransmissionProbability()) {
						potentialExposure.setStatus(StatusEnum.exposed);
					}
				}
			}
		}
	}
	/**
	 * <!-- begin-user-doc -->
	 * Progression Rule. Executed every period.
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void progression() {
		int currentPeriod = getScape().getPeriod();
		if ((getExposureEndPeriod() != getAsymptomaticEndPeriod() && currentPeriod == getExposureEndPeriod())) {
			setStatus(StatusEnum.asymptomInfectious);
		}
		if (currentPeriod == getOutcomePeriod()) {
			double mortalityDraw = getRandom().nextDouble();
			if (mortalityDraw < getEpidemic().getCaseMortalityRate()) {
				setStatus(StatusEnum.dead);
			}
			if (!(mortalityDraw < getEpidemic().getCaseMortalityRate())) {
				setStatus(StatusEnum.recovered);
			}
		}
		if ((currentPeriod == getExposureEndPeriod() && getExposureEndPeriod() == getAsymptomaticEndPeriod())) {
			setStatus(StatusEnum.symptomInfectious);
		}
		if ((currentPeriod == getAsymptomaticEndPeriod() && getExposureEndPeriod() != getAsymptomaticEndPeriod())) {
			setStatus(StatusEnum.symptomInfectious);
		}
	}
	/**
	 * <!-- begin-user-doc -->
	 * Watch for changes in Status.
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void exposure() {
		if (getStatus() == StatusEnum.exposed) {
			int exposureNow = getScape().getPeriod();
			int exposureLength = randomInRange(getEpidemic()
					.getMinPeriodExposure(), getEpidemic()
					.getMaxPeriodExposure());
			int endExposure = exposureNow + exposureLength;
			setExposureEndPeriod(endExposure);
			int asymptomaticLength = randomInRange(getEpidemic()
					.getMinPeriodAsymptomInfection(), getEpidemic()
					.getMaxPeriodAsymptomInfection());
			int endAsymptomatic = endExposure + asymptomaticLength;
			setAsymptomaticEndPeriod(endAsymptomatic);
			int symptomLength = randomInRange(getEpidemic()
					.getMinPeriodSymptomInfection(), getEpidemic()
					.getMaxPeriodSymptomInfection());
			int endInfection = endAsymptomatic + symptomLength;
			setOutcomePeriod(endInfection);
		}
	}
	/**
	 * <!-- begin-user-doc -->
	 * Gets the Status property for Individual.
	 * @return 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StatusEnum getStatus() {
		return status;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Sets the Status property for Individual.
	 * 
	 * @param _status the new Status value
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStatus(StatusEnum _status) {
		status = _status;
		exposure();
	}

	/**
	 * <!-- begin-user-doc -->
	 * Gets the Exposure End Period property for Individual.
	 * @return 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getExposureEndPeriod() {
		return exposureEndPeriod;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Sets the Exposure End Period property for Individual.
	 * 
	 * @param _exposureEndPeriod the new Exposure End Period value
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExposureEndPeriod(int _exposureEndPeriod) {
		exposureEndPeriod = _exposureEndPeriod;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Gets the Asymptomatic End Period property for Individual.
	 * @return 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getAsymptomaticEndPeriod() {
		return asymptomaticEndPeriod;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Sets the Asymptomatic End Period property for Individual.
	 * 
	 * @param _asymptomaticEndPeriod the new Asymptomatic End Period value
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAsymptomaticEndPeriod(int _asymptomaticEndPeriod) {
		asymptomaticEndPeriod = _asymptomaticEndPeriod;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Gets the Outcome Period property for Individual.
	 * @return 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getOutcomePeriod() {
		return outcomePeriod;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Sets the Outcome Period property for Individual.
	 * 
	 * @param _outcomePeriod the new Outcome Period value
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOutcomePeriod(int _outcomePeriod) {
		outcomePeriod = _outcomePeriod;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Gets the Contact Transmission Probability property for Individual.
	 * @return 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getContactTransmissionProbability() {
		return contactTransmissionProbability;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Sets the Contact Transmission Probability property for Individual.
	 * 
	 * @param _contactTransmissionProbability the new Contact Transmission Probability value
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContactTransmissionProbability(
			double _contactTransmissionProbability) {
		contactTransmissionProbability = _contactTransmissionProbability;
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
