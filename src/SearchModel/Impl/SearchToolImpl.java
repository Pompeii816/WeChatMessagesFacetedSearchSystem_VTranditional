package SearchModel.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import Domain.WeChatMessage;
import SearchModel.SearchTool;

public class SearchToolImpl implements SearchTool{

	@Override
	public List<WeChatMessage> searchMessageByFacet(List<String> facetTrems,
			HashMap<Integer,WeChatMessage> messageMap,
			HashMap<Integer,HashSet<String>> docIDOnFacets) {
		List<WeChatMessage> resultList = new ArrayList<WeChatMessage>();
		for(Entry<Integer,HashSet<String>> element:docIDOnFacets.entrySet()) {
			if(element.getValue().containsAll(facetTrems)) {
				resultList.add(messageMap.get(element.getKey()));
			}
		}
		return resultList;
	}

}
