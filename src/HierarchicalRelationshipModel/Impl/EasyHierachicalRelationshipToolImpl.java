package HierarchicalRelationshipModel.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import Domain.ConceptLatticeGrid;
import Domain.WeChatMessage;
import HierarchicalRelationshipModel.HierarchicalRelationshipTool;

/* 
 * author:Pompeii
 * time:
 * 作用：Free版本的分面检索，直接将选取的分面值进行检索，不存在导航
 * 输入：
 * 输出：
 * */

public class EasyHierachicalRelationshipToolImpl implements HierarchicalRelationshipTool{

	@Override
	public ArrayList<ArrayList<ConceptLatticeGrid>> getHierarchicalRelationship(HashMap<Integer, WeChatMessage> messageMap,
			HashMap<String, HashSet<Integer>> facetTermOnDocIDs, HashMap<Integer, HashSet<String>> docIDOnFacets) {
		// TODO Auto-generated method stub
		return null;
	}

}
