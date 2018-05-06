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
 * 作用：生成概念格，并以表的形式保存起来
 * 输入：
 * 输出：
 * 完成度：10%
 * */
public class HierarchicalRelationshipToolImpl implements HierarchicalRelationshipTool{

	@Override
	public ArrayList<ArrayList<ConceptLatticeGrid>> getHierarchicalRelationship(HashMap<Integer,WeChatMessage> messageMap,
			HashMap<String,HashSet<Integer>> facetTermOnDocIDs,
			HashMap<Integer,HashSet<String>> docIDOnFacets) {
		ArrayList<ArrayList<ConceptLatticeGrid>> conceptLatticeGridList = new ArrayList<ArrayList<ConceptLatticeGrid>>();
		int countOfDocs = docIDOnFacets.size();
		//给每个列表加上一个null的列表，最上层是节点数最少的层
		for(int index = countOfDocs; index > 0; index--) {
			ArrayList<ConceptLatticeGrid> tmp = new ArrayList<ConceptLatticeGrid>();
			conceptLatticeGridList.add(tmp);
		}
		
		HashSet<Integer> setOfFullDocs = (HashSet<Integer>) docIDOnFacets.keySet();
		Iterator<Entry<String,HashSet<Integer>>> iterOfFacetTermOnDocIDs = facetTermOnDocIDs.entrySet().iterator();
		//从最多的节点那一层开始装
		for(int index = countOfDocs - 1; index >= 0; index--) {
			ArrayList<ConceptLatticeGrid> tmpList = new ArrayList<ConceptLatticeGrid>();
			//将每一层装入到对应的层次中去，但是还没能进行每一层的节点的相交
			while(iterOfFacetTermOnDocIDs.hasNext()) {
				HashSet<String> tmpQuests = new HashSet<String>();
				HashSet<Integer> tmpResources = new HashSet<Integer>();
				ConceptLatticeGrid tmpGrid = new ConceptLatticeGrid();
				Entry<String,HashSet<Integer>> entryOfFacetTermOnDocIDs = iterOfFacetTermOnDocIDs.next();
				if(entryOfFacetTermOnDocIDs.getValue().size() == index) {
					tmpQuests.add(entryOfFacetTermOnDocIDs.getKey());
					tmpResources.addAll(entryOfFacetTermOnDocIDs.getValue());
				}
				tmpGrid.setQuest(tmpQuests);
				tmpGrid.setResourcesIds(tmpResources);
				tmpList = conceptLatticeGridList.get(index);
				tmpList.add(tmpGrid);				//还没有进行冗余检查和父节点查询
			}
			
			tmpList = conceptLatticeGridList.get(index);
			//冗余检查，冗余合并
			//子节点生成，并添加到相应的层次中去
			//寻找父节点，将List变成Hasse图
		}
		
		
		ConceptLatticeGrid root = new ConceptLatticeGrid();
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
		
		//以下仅为去除警告，正确实现的时候并无作用，需要去掉
		redundanceCheck();
		getSubList();
		removeNullGrid();
		getFatherGrid();
		return conceptLatticeGridList;
	}
	
	//冗余检查，并将该level冗余的概念格合并
	private static List<ConceptLatticeGrid> redundanceCheck() {
		
		return null;
	}
	
	//同一level的节点相交，得到子概念格和同一层的概念格
	private static void getSubList() {
		
	}
	
	//去掉每一层的quest为空集的概念格
	private static void removeNullGrid() {
		
	}
	
	//获取节点的父节点
	private static void getFatherGrid() {
		
	}
}
