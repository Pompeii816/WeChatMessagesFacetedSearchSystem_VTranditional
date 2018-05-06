package Domain;

import java.util.HashSet;

/**
 * @author Pompeii
 * ��ǰ��ѯ״̬��ʵ����
 *
 */
public class SearchStatus {
	private HashSet<String> FacetTermsSet;		//��ǰ���ɲ�ѯ����𣨷�������ļ���
	private HashSet<WeChatMessage> Resource;	//�ò�ѯ���õĽ����

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