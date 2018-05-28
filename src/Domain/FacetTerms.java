package Domain;

public class FacetTerms {
	private String facetTerm;
	private FacetTerms fatherFacetTerm;
	
	public FacetTerms(String facetTerm) {
		super();
		this.facetTerm = facetTerm;
	}

	public FacetTerms() {
		super();
	}

	public String getFacetTerm() {
		return facetTerm;
	}

	public void setFacetTerm(String facetTerm) {
		this.facetTerm = facetTerm;
	}

	public FacetTerms getFatherFacetTerm() {
		return fatherFacetTerm;
	}

	public void setFatherFacetTerm(FacetTerms fatherFacetTerm) {
		this.fatherFacetTerm = fatherFacetTerm;
	}

	@Override
	public String toString() {
		return "FacetTerms [facetTerm=" + facetTerm + "]";
	}
	
}
