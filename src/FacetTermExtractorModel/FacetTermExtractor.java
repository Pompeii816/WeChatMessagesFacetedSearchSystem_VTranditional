package FacetTermExtractorModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import Domain.WeChatMessage;

public interface FacetTermExtractor {
	public HashMap<String,Float> getFacetTerms(List<String> segmentations);
	public HashMap<String,HashSet<Integer>> getFacetTermOnDocIDs(List<String> segmentations,HashMap<Integer,WeChatMessage> messageMap);
	public HashMap<Integer,HashSet<String>> getDocIDOnFacets(List<String> segmentations,HashMap<Integer,WeChatMessage> messageMap);
}
