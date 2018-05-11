package SearchModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import Domain.WeChatMessage;

public interface SearchTool {
	public List<WeChatMessage> searchMessageByFacet(List<String> facetTrems,
			HashMap<Integer,WeChatMessage> messageMap,
			HashMap<Integer,HashSet<String>> docIDOnFacets);
}
