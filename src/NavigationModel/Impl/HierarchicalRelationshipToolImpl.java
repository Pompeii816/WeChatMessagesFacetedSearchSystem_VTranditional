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
 * ���ã����ɸ���񣬲��Ա����ʽ��������
 * ���룺
 * �����
 * ��ɶȣ�100%
 * �Ƿ��Ѳ��ԣ���
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
		
		//��һ��ѭ����װ��ȥ�ཻ֮��Ľڵ㡣
		//�ӽڵ����ɣ�����ӵ���Ӧ�Ĳ����ȥ
		for (int index = countOfDocs - 1; index >= 0; index--) {
			ArrayList<ConceptLatticeGrid> tmpList = new ArrayList<ConceptLatticeGrid>();
			//int numOfThisLevelList = tmpList.size();			//װ��level�Ľڵ������������ڵ����仯�ˣ�����������һ��ѭ��һ��
			tmpList = conceptLatticeGridList.get(index);
			// �����飬����ϲ�����Ϊ����Ĳ��ཻ�Ľڵ�ᵽ��һ������
			tmpList = redundanceCheck(tmpList, index);
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
			/*
			// �����飬����ϲ�
			tmpList = redundanceCheck(tmpList, index);
			conceptLatticeGridList.set(index, tmpList);
			//��������仯�ˣ�
			if(numOfThisLevelList == tmpList.size()) {
				//����һ�£���ʲô��������
			}else {
				index++; //��index+1��������¶���һ�������һ������
			}
			
		}
		
		//ȥ��List������ŵĲ�.
		
		for(ArrayList<ConceptLatticeGrid> element:conceptLatticeGridList) {
			if(null == element) {
				//ʲô������
			}else {
				resultConceptLatticeGridList.add(element);
			}
		}
		
		// Ѱ�Ҹ��ڵ㣬��List���Hasseͼ��ÿһ�㶼�����ң����ڵ�ض���ͬһ������
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

	// �����飬������level����ĸ����ϲ�
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
