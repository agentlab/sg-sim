package name.milesparker.epi.region;

/**
 * <!-- begin-user-doc -->
 * Community Infection Status Java Implementation.
 * 
 * Generated by AMF for model: EpidemicRegional.metaabm in project: org.eclipse.amp.amf.examples.escape 
 * <!-- end-user-doc -->
 * @generated
 */
public enum CommunityInfectionStatusEnum {
	originCity("Origin City"), infectionsObserved("Infections Observed"), noInfections(
			"No Infections");

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private CommunityInfectionStatusEnum(String name) {
		this.name = name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		return name;
	}
}
