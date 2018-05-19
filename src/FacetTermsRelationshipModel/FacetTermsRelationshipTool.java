package FacetTermsRelationshipModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Domain.FacetTerms;
import Domain.WeChatMessage;

public interface FacetTermsRelationshipTool {
	public ArrayList<FacetTerms> getFacetTermsRelationship(List<String> segmentations,HashMap<Integer,WeChatMessage> messageMap);
}
