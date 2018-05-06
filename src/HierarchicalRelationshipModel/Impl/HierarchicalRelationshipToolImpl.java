package HierarchicalRelationshipModel.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

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
		
		//HashSet<Integer> setOfFullDocs = (HashSet<Integer>) docIDOnFacets.keySet();
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
				//只将单元素的查询构建了，还没有做相交的操作
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
			tmpList = redundanceCheck(tmpList,index);
			//去掉每一层中quest为null的节点
			tmpList = removeNullGrid(tmpList,index);			
		}
		
		//子节点生成，并添加到相应的层次中去
		
		//寻找父节点，将List变成Hasse图
		
		//以下仅为去除警告，正确实现的时候并无作用，需要去掉
		return conceptLatticeGridList;
	}
	
	//冗余检查，并将该level冗余的概念格合并
	private static ArrayList<ConceptLatticeGrid> redundanceCheck(ArrayList<ConceptLatticeGrid> levelList,int level) {
		ArrayList<ConceptLatticeGrid> resultMap = new ArrayList<ConceptLatticeGrid>();
		HashSet<HashSet<Integer>> resourcesSet = new HashSet<HashSet<Integer>>();			//存储已经遍历了的节点，并且只保存资源不同的查询节点的资源
		
		for(ConceptLatticeGrid element:levelList) {
			if(!resourcesSet.contains(element.getResourcesIds())) {			//如果不包含该资源，直接加入即可
				resourcesSet.add(element.getResourcesIds());
				resultMap.add(element);
			}else {															//如果包含，那就找出所有资源为该资源的grid，将其查询Set合并。
				ConceptLatticeGrid tmpGrid = new ConceptLatticeGrid();
				HashSet<String> questsSet = new HashSet<String>();
				for(ConceptLatticeGrid e:levelList) {
					if(e.getResourcesIds().equals(element.getResourcesIds())) {
						questsSet.addAll(e.getQuest());
					}
				}
				tmpGrid.setQuest(questsSet);
				tmpGrid.setResourcesIds(element.getResourcesIds());
				resultMap.add(tmpGrid);
			}
		}
		return resultMap;
	}
	
	//去掉每一层的quest为空集的概念格
	private static ArrayList<ConceptLatticeGrid> removeNullGrid(ArrayList<ConceptLatticeGrid> levelList,int level) {
		ArrayList<ConceptLatticeGrid> resultMap = new ArrayList<ConceptLatticeGrid>();
		if(level == 0) {
			return levelList;
		}else {
			for(ConceptLatticeGrid element:levelList) {
				if(element.getQuest().equals(null)) {
					
				}else {
					resultMap.add(element);
				}
			}
		}
		return resultMap;
	}
}
