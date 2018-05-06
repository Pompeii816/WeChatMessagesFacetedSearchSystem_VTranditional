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
		
		//HashSet<Integer> setOfFullDocs = (HashSet<Integer>) docIDOnFacets.keySet();
		Iterator<Entry<String,HashSet<Integer>>> iterOfFacetTermOnDocIDs = facetTermOnDocIDs.entrySet().iterator();
		//�����Ľڵ���һ�㿪ʼװ
		for(int index = countOfDocs - 1; index >= 0; index--) {
			ArrayList<ConceptLatticeGrid> tmpList = new ArrayList<ConceptLatticeGrid>();
			//��ÿһ��װ�뵽��Ӧ�Ĳ����ȥ�����ǻ�û�ܽ���ÿһ��Ľڵ���ཻ
			while(iterOfFacetTermOnDocIDs.hasNext()) {
				HashSet<String> tmpQuests = new HashSet<String>();
				HashSet<Integer> tmpResources = new HashSet<Integer>();
				ConceptLatticeGrid tmpGrid = new ConceptLatticeGrid();
				Entry<String,HashSet<Integer>> entryOfFacetTermOnDocIDs = iterOfFacetTermOnDocIDs.next();
				//ֻ����Ԫ�صĲ�ѯ�����ˣ���û�����ཻ�Ĳ���
				if(entryOfFacetTermOnDocIDs.getValue().size() == index) {
					tmpQuests.add(entryOfFacetTermOnDocIDs.getKey());
					tmpResources.addAll(entryOfFacetTermOnDocIDs.getValue());
				}
				tmpGrid.setQuest(tmpQuests);
				tmpGrid.setResourcesIds(tmpResources);
				tmpList = conceptLatticeGridList.get(index);
				tmpList.add(tmpGrid);				//��û�н���������͸��ڵ��ѯ
			}
			
			tmpList = conceptLatticeGridList.get(index);
			//�����飬����ϲ�
			tmpList = redundanceCheck(tmpList,index);
			//ȥ��ÿһ����questΪnull�Ľڵ�
			tmpList = removeNullGrid(tmpList,index);			
		}
		
		//�ӽڵ����ɣ�����ӵ���Ӧ�Ĳ����ȥ
		
		//Ѱ�Ҹ��ڵ㣬��List���Hasseͼ
		
		//���½�Ϊȥ�����棬��ȷʵ�ֵ�ʱ�������ã���Ҫȥ��
		return conceptLatticeGridList;
	}
	
	//�����飬������level����ĸ����ϲ�
	private static ArrayList<ConceptLatticeGrid> redundanceCheck(ArrayList<ConceptLatticeGrid> levelList,int level) {
		ArrayList<ConceptLatticeGrid> resultMap = new ArrayList<ConceptLatticeGrid>();
		HashSet<HashSet<Integer>> resourcesSet = new HashSet<HashSet<Integer>>();			//�洢�Ѿ������˵Ľڵ㣬����ֻ������Դ��ͬ�Ĳ�ѯ�ڵ����Դ
		
		for(ConceptLatticeGrid element:levelList) {
			if(!resourcesSet.contains(element.getResourcesIds())) {			//�������������Դ��ֱ�Ӽ��뼴��
				resourcesSet.add(element.getResourcesIds());
				resultMap.add(element);
			}else {															//����������Ǿ��ҳ�������ԴΪ����Դ��grid�������ѯSet�ϲ���
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
	
	//ȥ��ÿһ���questΪ�ռ��ĸ����
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
