package Domain;

import java.util.HashSet;

/**
 * @author Pompeii
 * 当前查询状态的实体类
 *
 */
public class SearchStatus {
	private HashSet<String> FacetTermsSet;		//当前构成查询的类别（分面术语）的集合
	private HashSet<WeChatMessage> Resource;	//该查询对用的结果集

	public SearchStatus(HashSet<String> facetTermsSet,
			HashSet<WeChatMessage> resource) {
		this.FacetTermsSet = facetTermsSet;
		this.Resource = resource;
	}

	public SearchStatus() {
		super();
	}

	public HashSet<String> getFacetTermsSet() {
		return FacetTermsSet;
	}

	public void setFacetTermsSet(HashSet<String> facetTermsSet) {
		FacetTermsSet = facetTermsSet;
	}

	public HashSet<WeChatMessage> getResource() {
		return Resource;
	}

	public void setResource(HashSet<WeChatMessage> resource) {
		this.Resource = resource;
	}

	@Override
	public String toString() {
		return "SearchStatus [FacetTermsSet=" + FacetTermsSet + ", resource=" + Resource + "]";
	}	
}