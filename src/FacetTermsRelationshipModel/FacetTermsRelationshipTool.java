package FacetTermsRelationshipModel;

import java.util.HashMap;
import java.util.List;

import Domain.WeChatMessage;

public interface FacetTermsRelationshipTool {
	public HashMap<String,String> getFacetTermsRelationship(List<String> segmentations,HashMap<Integer,WeChatMessage> messageMap);
}
