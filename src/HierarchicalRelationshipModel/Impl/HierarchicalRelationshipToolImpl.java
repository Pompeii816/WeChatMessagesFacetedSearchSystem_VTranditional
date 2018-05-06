package HierarchicalRelationshipModel.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import Domain.ConceptLatticeGrid;
import Domain.WeChatMessage;
import HierarchicalRelationshipModel.HierarchicalRelationshipTool;
/*
 * author:Pompeii
 * time:
 * ���ã����ɸ���񣬲��Ա����ʽ��������
 * ���룺
 * �����
 * ��ɶȣ�10%
 * */
public class HierarchicalRelationshipToolImpl implements HierarchicalRelationshipTool{

	@Override
	public ArrayList<ArrayList<ConceptLatticeGrid>> getHierarchicalRelationship(HashMap<Integer,WeChatMessage> messageMap,
			HashMap<String,HashSet<Integer>> facetTermOnDocIDs,
			HashMap<Integer,HashSet<String>> docIDOnFacets) {
		ArrayList<ArrayList<ConceptLatticeGrid>> conceptLatticeGridList = new ArrayList<ArrayList<ConceptLatticeGrid>>();
		int countOfDocs = docIDOnFacets.size();
		//��ÿ���б����һ��null���б����ϲ��ǽڵ������ٵĲ�
		for(int index = countOfDocs; index > 0; index--) {
			ArrayList<ConceptLatticeGrid> tmp = new ArrayList<ConceptLatticeGrid>();
			conceptLatticeGridList.add(tmp);
		}
		
		
		
		HashSet<Integer> setOfFullDocs = (HashSet<Integer>) docIDOnFacets.keySet();
		ConceptLatticeGrid root = new ConceptLatticeGrid();
		Iterator<Entry<String,HashSet<Integer>>> iterOfFacetTermOnDocIDs = facetTermOnDocIDs.entrySet().iterator();
		HashSet<String> querySet = new HashSet<String>();
		while(iterOfFacetTermOnDocIDs.hasNext()) {
			Entry<String,HashSet<Integer>> entryOfFacetTermOnDocIDs = iterOfFacetTermOnDocIDs.next();
			if(entryOfFacetTermOnDocIDs.getValue().containsAll(setOfFullDocs)) {
				querySet.add(entryOfFacetTermOnDocIDs.getKey());
			}
		}
		root.setQuest(querySet);
		root.setResourcesIds(setOfFullDocs);
		
		ArrayList<ConceptLatticeGrid> rootList = new ArrayList<ConceptLatticeGrid>();
		rootList.add(root);
		conceptLatticeGridList.add(rootList);
		
		HashMap<HashSet<String>,HashSet<Integer>> allGrid = new HashMap<HashSet<String>,HashSet<Integer>>();
		for(int index = 0; index < countOfDocs; index++) {
			ArrayList<ConceptLatticeGrid> tmpList = new ArrayList<ConceptLatticeGrid>();
			
		}
		
		//���½�Ϊȥ�����棬��ȷʵ�ֵ�ʱ�������ã���Ҫȥ��
		redundanceCheck();
		getSubList();
		removeNullGrid();
		getFatherGrid();
		return conceptLatticeGridList;
	}
	
	//�����飬������level����ĸ����ϲ�
	private static List<ConceptLatticeGrid> redundanceCheck() {
		
		return null;
	}
	
	//ͬһlevel�Ľڵ��ཻ���õ��Ӹ�����ͬһ��ĸ����
	private static void getSubList() {
		
	}
	
	//ȥ��ÿһ���questΪ�ռ��ĸ����
	private static void removeNullGrid() {
		
	}
	
	//��ȡ�ڵ�ĸ��ڵ�
	private static void getFatherGrid() {
		
	}
}
