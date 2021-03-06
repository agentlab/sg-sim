package name.milesparker.epi.plague;

/**
 * <!-- begin-user-doc -->
 * Status Java Implementation.
 * 
 * Generated by AMF for model: EpidemicPlague.metaabm in project: org.eclipse.amp.amf.examples.escape 
 * <!-- end-user-doc -->
 * @generated
 */
public enum StatusEnum {
	susceptible("Susceptible"), exposed("Exposed"), asymptomInfectious(
			"Asymptom Infectious"), symptomInfectious("Symptom Infectious"), recovered(
			"Recovered"), dead("Dead");

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
	private StatusEnum(String name) {
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
