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
 * time:0506
 * ���ã����ɸ���񣬲��Ա����ʽ��������
 * ���룺
 * �����
 * ��ɶȣ�60%
 * */
public class HierarchicalRelationshipToolImpl implements HierarchicalRelationshipTool {

	@Override
	public ArrayList<ArrayList<ConceptLatticeGrid>> getHierarchicalRelationship(
			HashMap<Integer, WeChatMessage> messageMap, HashMap<String, HashSet<Integer>> facetTermOnDocIDs,
			HashMap<Integer, HashSet<String>> docIDOnFacets) {
		ArrayList<ArrayList<ConceptLatticeGrid>> conceptLatticeGridList = new ArrayList<ArrayList<ConceptLatticeGrid>>();
		int countOfDocs = docIDOnFacets.size();		//һ���е��ı�������
		// ��ÿ���б����һ��null���б����ϲ��ǽڵ������Ĳ㣬conceptLatticeGridList�а�����List����countOfDocs����
		for (int index = 0; index < countOfDocs; index++) {
			ArrayList<ConceptLatticeGrid> tmp = new ArrayList<ConceptLatticeGrid>();
			conceptLatticeGridList.add(tmp);
		}

		// HashSet<Integer> setOfFullDocs = (HashSet<Integer>) docIDOnFacets.keySet();
		Iterator<Entry<String, HashSet<Integer>>> iterOfFacetTermOnDocIDs = facetTermOnDocIDs.entrySet().iterator();
		//����ѯ��Ϊ����String��grid�ŵ���Ӧ��level��ȥ��level��grid.size()�Ĺ�ϵΪlevel = countOfDocs - tmpResources.size()
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
		
		//�����Ľڵ��ǿյģ��Ǿ͸���һ��questΪnull��resourcesΪall�Ľڵ�
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
		
		//�����������ϲ�
		for(int index = 0; index < countOfDocs; index++) {
			ArrayList<ConceptLatticeGrid> tmpList = new ArrayList<ConceptLatticeGrid>();
			tmpList = conceptLatticeGridList.get(index);
			tmpList = redundanceCheck(tmpList,index);
			conceptLatticeGridList.set(index, tmpList);
		}
		
		
		// �����Ľڵ���һ�㿪ʼװ�����ѭ��ֻװ��ȥ��ÿһ��ĵ�����ѯ��grid��������Ҫɾ����
		for (int index = countOfDocs - 1; index >= 0; index--) {
			ArrayList<ConceptLatticeGrid> tmpList = new ArrayList<ConceptLatticeGrid>();
			// ��ÿһ��װ�뵽��Ӧ�Ĳ����ȥ�����ǻ�û�ܽ���ÿһ��Ľڵ���ཻ
			while (iterOfFacetTermOnDocIDs.hasNext()) {
				Entry<String, HashSet<Integer>> entryOfFacetTermOnDocIDs = iterOfFacetTermOnDocIDs.next();
				// ֻ����Ԫ�صĲ�ѯ�����ˣ���û�����ཻ�Ĳ���
				if ((entryOfFacetTermOnDocIDs.getValue().size() - 1) == index) {	//��ΪcountOfDocs - 1������װ������Դ��ΪcountOfDocs�Ľڵ�
					HashSet<String> tmpQuests = new HashSet<String>();
					HashSet<Integer> tmpResources = new HashSet<Integer>();
					ConceptLatticeGrid tmpGrid = new ConceptLatticeGrid();
					tmpQuests.add(entryOfFacetTermOnDocIDs.getKey());
					tmpResources.addAll(entryOfFacetTermOnDocIDs.getValue());
					tmpGrid.setQuest(tmpQuests);
					tmpGrid.setResourcesIds(tmpResources);
					tmpList.add(tmpGrid); 
					// ��û�н���������͸��ڵ��ѯ
				}
			}
			tmpList = conceptLatticeGridList.get(index);
			// ȥ��ÿһ����questΪnull�Ľڵ㣬��ȥ���սڵ���Ա�����ֿ�ָ�����
			tmpList = removeNullGrid(tmpList, index);
			// �����飬����ϲ�
			tmpList = redundanceCheck(tmpList, index);
			conceptLatticeGridList.set(index, tmpList);
		}
		
		
		//��һ��ѭ����װ��ȥ�ཻ֮��Ľڵ㡣
		//�ӽڵ����ɣ�����ӵ���Ӧ�Ĳ����ȥ
		for (int index = countOfDocs - 1; index >= 0; index--) {
			ArrayList<ConceptLatticeGrid> tmpList = new ArrayList<ConceptLatticeGrid>();
			int numOfThisLevelList = tmpList.size();			//װ��level�Ľڵ������������ڵ����仯�ˣ�����������һ��ѭ��һ��
			tmpList = conceptLatticeGridList.get(index);
			
			// ��ÿһ��װ�뵽��Ӧ�Ĳ����ȥ�����ǻ�û�ܽ���ÿһ��Ľڵ���ཻ
			for(ConceptLatticeGrid element:tmpList) {
				for(ConceptLatticeGrid element1:tmpList) {
					if(element.equals(element1)) {
						//��ͬ�����κ���
					}else {
						//��ͬ���ཻ������ཻ֮�����Դ����Ϊ�գ����������Ӧ��level��List��ȥ
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
						if(!tmpResources.isEmpty()) {					//��Դ������Ϊ��
							ConceptLatticeGrid tmpGrid = new ConceptLatticeGrid();
							tmpGrid.setQuest(tmpQuests);
							tmpGrid.setResourcesIds(tmpResources);
							ArrayList<ConceptLatticeGrid> levelList = conceptLatticeGridList.get(tmpResources.size()); //��Ӧ��level���б�
							levelList.add(tmpGrid);
							conceptLatticeGridList.set(tmpResources.size(), levelList);
						}
					}
				}
			}
			// �����飬����ϲ�
			tmpList = redundanceCheck(tmpList, index);
			conceptLatticeGridList.set(index, tmpList);
			if(numOfThisLevelList == tmpList.size()) {
				//����һ�£���ʲô��������
			}else {
				index++; //��index+1��������¶���һ�������һ������
			}
		}

		// Ѱ�Ҹ��ڵ㣬��List���Hasseͼ
		for(int index = 0; index < countOfDocs; index--) {
			
		}
		// ���½�Ϊȥ�����棬��ȷʵ�ֵ�ʱ�������ã���Ҫȥ��
		return conceptLatticeGridList;
	}

	// �����飬������level����ĸ����ϲ�����Ҫ��飬���ƻ�����bug��
	private static ArrayList<ConceptLatticeGrid> redundanceCheck(ArrayList<ConceptLatticeGrid> levelList, int level) {
		ArrayList<ConceptLatticeGrid> resultMap = new ArrayList<ConceptLatticeGrid>();
		HashSet<HashSet<Integer>> resourcesSet = new HashSet<HashSet<Integer>>(); // �洢�Ѿ������˵Ľڵ㣬����ֻ������Դ��ͬ�Ĳ�ѯ�ڵ����Դ
		
		for (ConceptLatticeGrid element : levelList) {
			if (!resourcesSet.contains(element.getResourcesIds())) {	//�������������Դ��ֱ�Ӽ��뼴��
				resourcesSet.add(element.getResourcesIds());			//��element.getResourcesIds()Ϊnull���᲻����bug��
				resultMap.add(element);
			} else { 													//����������Ǿ��ҳ�������ԴΪ����Դ��grid�������ѯSet�ϲ���
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

	// ȥ��ÿһ���questΪ�ռ��ĸ����level�ط���bug����Ҫ���¸���
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
}
