package HierarchicalRelationshipModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import Domain.ConceptLatticeGrid;
import Domain.WeChatMessage;

public interface HierarchicalRelationshipTool {
	public ArrayList<ArrayList<ConceptLatticeGrid>> getHierarchicalRelationship(HashMap<Integer,WeChatMessage> messageMap,
			HashMap<String,HashSet<Integer>> facetTermOnDocIDs,
			HashMap<Integer,HashSet<String>> docIDOnFacets);
}
