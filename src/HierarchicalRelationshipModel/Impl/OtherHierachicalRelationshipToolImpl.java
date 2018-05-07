package HierarchicalRelationshipModel.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import Domain.ConceptLatticeGrid;
import Domain.WeChatMessage;
import HierarchicalRelationshipModel.HierarchicalRelationshipTool;

/* 
 * author:Pompeii
 * time:
 * ���ã�
 * ���룺
 * �����
 * */

public class OtherHierachicalRelationshipToolImpl implements HierarchicalRelationshipTool{

	@Override
	public ArrayList<ArrayList<ConceptLatticeGrid>> getHierarchicalRelationship(HashMap<Integer, WeChatMessage> messageMap,
			HashMap<String, HashSet<Integer>> facetTermOnDocIDs, 
			HashMap<Integer, HashSet<String>> docIDOnFacets) {
		HashSet<HashSet<Integer>> docsSet = getDocsSet(docIDOnFacets);
		HashMap<HashSet<String>,HashSet<Integer>> gridMap = new HashMap<HashSet<String>,HashSet<Integer>>();
		//Ѱ�Ҷ�Ӧ��Set<String>
		for(HashSet<Integer> element:docsSet) {
			HashSet<String> keySet = new HashSet<String>();
			Iterator<Entry<String, HashSet<Integer>>> iterOfFacetTermOnDocIDs = facetTermOnDocIDs.entrySet().iterator();
			while(iterOfFacetTermOnDocIDs.hasNext()) {
				Entry<String, HashSet<Integer>> entryOfFacetTerms = iterOfFacetTermOnDocIDs.next();
				if(entryOfFacetTerms.getValue().containsAll(element)) {
					keySet.add(entryOfFacetTerms.getKey());
				}
			}
			//��������һ���ͽ���ѯΪnull��ֵ�ų���
			if(!keySet.isEmpty()) {
				gridMap.put(keySet, element);
			}
		}
		//���ֹ�ϵ
		// TODO Auto-generated method stub
		return null;
	}
	
	private static HashSet<HashSet<Integer>> getDocsSet(HashMap<Integer, HashSet<String>> docIDOnFacets){
		HashSet<HashSet<Integer>> resultSet = new HashSet<HashSet<Integer>>();
		return resultSet;
	}
}
