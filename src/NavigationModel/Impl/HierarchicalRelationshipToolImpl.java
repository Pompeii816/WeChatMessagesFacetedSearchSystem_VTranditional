package NavigationModel.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import Domain.ConceptLatticeGrid;
import Domain.WeChatMessage;
import NavigationModel.HierarchicalRelationshipTool;

/*
 * author:Pompeii
 * time:0506
 * 作用：生成概念格，并以表的形式保存起来
 * 输入：
 * 输出：
 * 完成度：100%
 * 是否已测试：否
 * */
public class HierarchicalRelationshipToolImpl implements HierarchicalRelationshipTool {

	@Override
	public ArrayList<ConceptLatticeGrid> getHierarchicalRelationship(
			HashMap<Integer, WeChatMessage> messageMap, HashMap<String, HashSet<Integer>> facetTermOnDocIDs,
			HashMap<Integer, HashSet<String>> docIDOnFacets) {

		return null;
	}
	/*

	@Override
	public ArrayList<ArrayList<ConceptLatticeGrid>> getHierarchicalRelationship(
			HashMap<Integer, WeChatMessage> messageMap, HashMap<String, HashSet<Integer>> facetTermOnDocIDs,
			HashMap<Integer, HashSet<String>> docIDOnFacets) {
		ArrayList<ArrayList<ConceptLatticeGrid>> conceptLatticeGridList = new ArrayList<ArrayList<ConceptLatticeGrid>>();
		ArrayList<ArrayList<ConceptLatticeGrid>> resultConceptLatticeGridList = new ArrayList<ArrayList<ConceptLatticeGrid>>();
		int countOfDocs = docIDOnFacets.size();		//一共有的文本的数量
		// 给每个列表加上一个null的列表，最上层是节点数最多的层，conceptLatticeGridList中包含的List共有countOfDocs个。
		for (int index = 0; index < countOfDocs; index++) {
			ArrayList<ConceptLatticeGrid> tmp = new ArrayList<ConceptLatticeGrid>();
			conceptLatticeGridList.add(tmp);
		}

		// HashSet<Integer> setOfFullDocs = (HashSet<Integer>) docIDOnFacets.keySet();
		Iterator<Entry<String, HashSet<Integer>>> iterOfFacetTermOnDocIDs = facetTermOnDocIDs.entrySet().iterator();
		//将查询集为单个String的grid放到对应的level中去，level与grid.size()的关系为level = countOfDocs - tmpResources.size()
		while(iterOfFacetTermOnDocIDs.hasNext()) {
			Entry<String, HashSet<Integer>> entryOfFacetTermOnDocIDs = iterOfFacetTermOnDocIDs.next();
			ArrayList<ConceptLatticeGrid> tmpList = new ArrayList<ConceptLatticeGrid>();
			ConceptLatticeGrid tmpConceptLatticeGrid = new ConceptLatticeGrid();
			HashSet<String> tmpQuests = new HashSet<String>();
			HashSet<Integer> tmpResources = new HashSet<Integer>();
			tmpQuests.add(entryOfFacetTermOnDocIDs.getKey());
			tmpResources.addAll(tmpResources);
			tmpConceptLatticeGrid.setQuest(tmpQuests);
			tmpConceptLatticeGrid.setResourcesIds(tmpResources);
			tmpList = conceptLatticeGridList.get(countOfDocs - tmpResources.size());   
			tmpList.add(tmpConceptLatticeGrid);
			conceptLatticeGridList.set(countOfDocs - tmpResources.size(), tmpList);
		}
		
		//若最顶层的节点是空的，那就给加一个quest为null，resources为all的节点
		if(null == conceptLatticeGridList.get(0)) {
			ConceptLatticeGrid tmpConceptLatticeGrid = new ConceptLatticeGrid();
			HashSet<Integer> tmpResources = new HashSet<Integer>();
			tmpResources.addAll(docIDOnFacets.keySet());
			tmpConceptLatticeGrid.setQuest(null);
			tmpConceptLatticeGrid.setResourcesIds(tmpResources);
			ArrayList<ConceptLatticeGrid> tmpList = new ArrayList<ConceptLatticeGrid>();
			tmpList.add(tmpConceptLatticeGrid);
			conceptLatticeGridList.add(0, tmpList);
		}
		
		//冗余检查和冗余合并
		for(int index = 0; index < countOfDocs; index++) {
			ArrayList<ConceptLatticeGrid> tmpList = new ArrayList<ConceptLatticeGrid>();
			tmpList = conceptLatticeGridList.get(index);
			tmpList = redundanceCheck(tmpList,index);
			conceptLatticeGridList.set(index, tmpList);
		}
		
		//这一个循环将装进去相交之后的节点。
		//子节点生成，并添加到相应的层次中去
		for (int index = countOfDocs - 1; index >= 0; index--) {
			ArrayList<ConceptLatticeGrid> tmpList = new ArrayList<ConceptLatticeGrid>();
			//int numOfThisLevelList = tmpList.size();			//装本level的节点数，最后如果节点数变化了，就重新在这一层循环一次
			tmpList = conceptLatticeGridList.get(index);
			// 冗余检查，冗余合并，因为上面的层相交的节点会到这一层来。
			tmpList = redundanceCheck(tmpList, index);
			// 将每一层装入到对应的层次中去，但是还没能进行每一层的节点的相交
			for(ConceptLatticeGrid element:tmpList) {
				for(ConceptLatticeGrid element1:tmpList) {
					if(element.equals(element1)) {
						//相同则不做任何事
					}else {
						//不同则相交，如果相交之后的资源集不为空，则加入至相应的level的List中去
						HashSet<String> tmpQuests = new HashSet<String>();
						tmpQuests.addAll(element.getQuest());
						for(String element2:element1.getQuest()) {
							tmpQuests.add(element2);
						}
						
						HashSet<Integer> tmpResources = new HashSet<Integer>();
						for(Integer element3:element1.getResourcesIds()) {
							if(element1.getResourcesIds().contains(element3)) {
								tmpResources.add(element3);
							}
						}				
						
						if(!tmpResources.isEmpty()) {					//资源集不能为空
							ConceptLatticeGrid tmpGrid = new ConceptLatticeGrid();
							tmpGrid.setQuest(tmpQuests);
							tmpGrid.setResourcesIds(tmpResources);							
							ArrayList<ConceptLatticeGrid> levelList = conceptLatticeGridList.get(tmpResources.size()); //对应的level的列表
							levelList.add(tmpGrid);
							conceptLatticeGridList.set(tmpResources.size(), levelList);
						}
					}
				}
			}
			/*
			// 冗余检查，冗余合并
			tmpList = redundanceCheck(tmpList, index);
			conceptLatticeGridList.set(index, tmpList);
			//如果数量变化了，
			if(numOfThisLevelList == tmpList.size()) {
				//数量一致，则什么都不做。
			}else {
				index++; //将index+1，则会重新对这一层进行这一波操作
			}
			
		}
		
		//去除List里面空着的层.
		
		for(ArrayList<ConceptLatticeGrid> element:conceptLatticeGridList) {
			if(null == element) {
				//什么都不做
			}else {
				resultConceptLatticeGridList.add(element);
			}
		}
		
		// 寻找父节点，将List变成Hasse图，每一层都往上找，父节点必定在同一层里面
		for(int index = resultConceptLatticeGridList.size() - 1; index >= 0; index--) {
			ArrayList<ConceptLatticeGrid> tmpConceptLatticeGridList = resultConceptLatticeGridList.get(index);
			for(int j = 0; j < tmpConceptLatticeGridList.size(); j++) {
				ConceptLatticeGrid tmpGrid = tmpConceptLatticeGridList.get(j);
				ArrayList<ConceptLatticeGrid> fatherGrid = new ArrayList<ConceptLatticeGrid>();
				for(int i = index - 1; i >= 0; i--) {
					ArrayList<ConceptLatticeGrid> tmp2 = resultConceptLatticeGridList.get(i);
					ArrayList<ConceptLatticeGrid> childGrid = new ArrayList<ConceptLatticeGrid>();
					
					for(int x = 0; x < tmp2.size(); x++) {
						if(tmp2.get(x).getResourcesIds().containsAll(tmpGrid.getResourcesIds())) {
							childGrid = tmp2.get(x).getChildGrid();
							fatherGrid.add(tmp2.get(x));
							childGrid.add(tmpGrid);
							tmp2.get(x).setChildGrid(childGrid);
						}
					}
					
				}
				tmpGrid.setFatherGrid(fatherGrid);
			}
		}
		return resultConceptLatticeGridList;
	}

	// 冗余检查，并将该level冗余的概念格合并
	private static ArrayList<ConceptLatticeGrid> redundanceCheck(ArrayList<ConceptLatticeGrid> levelList, int level) {
		ArrayList<ConceptLatticeGrid> resultMap = new ArrayList<ConceptLatticeGrid>();
		HashSet<HashSet<Integer>> resourcesSet = new HashSet<HashSet<Integer>>(); // 存储已经遍历了的节点，并且只保存资源不同的查询节点的资源
		
		for (ConceptLatticeGrid element : levelList) {
			if (!resourcesSet.contains(element.getResourcesIds())) {	//如果不包含该资源，直接加入即可
				resourcesSet.add(element.getResourcesIds());			//若element.getResourcesIds()为null，会不会有bug？
				resultMap.add(element);
			} else { 													//如果包含，那就找出所有资源为该资源的grid，将其查询Set合并。
				ConceptLatticeGrid tmpGrid = new ConceptLatticeGrid();
				HashSet<String> questsSet = new HashSet<String>();
				for (ConceptLatticeGrid e : levelList) {
					if (e.getResourcesIds().equals(element.getResourcesIds())) {
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

	// 去掉每一层的quest为空集的概念格，level地方有bug，需要重新更改
	@SuppressWarnings("unused")
	private static ArrayList<ConceptLatticeGrid> removeNullGrid(ArrayList<ConceptLatticeGrid> levelList, int level) {
		ArrayList<ConceptLatticeGrid> resultMap = new ArrayList<ConceptLatticeGrid>();
		if (level == 0) {
			return levelList;
		} else {
			for (ConceptLatticeGrid element : levelList) {
				if (null == element.getQuest()) {

				} else {
					resultMap.add(element);
				}
			}
		}
		return resultMap;
	}
	*/
}
