package NavigationModel.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import Domain.ConceptLatticeGrid;
import Domain.WeChatMessage;
import NavigationModel.HierarchicalRelationshipTool;

/* 
 * author:Pompeii
 * time:0507
 * 作用：
 * 输入：
 * 输出：
 * 完成度：60%
 * */

public class OtherHierachicalRelationshipToolImpl implements HierarchicalRelationshipTool{

	@Override
	public ArrayList<ArrayList<ConceptLatticeGrid>> getHierarchicalRelationship(HashMap<Integer, WeChatMessage> messageMap,
			HashMap<String, HashSet<Integer>> facetTermOnDocIDs, 
			HashMap<Integer, HashSet<String>> docIDOnFacets) {
		ArrayList<ArrayList<ConceptLatticeGrid>> resultListOfGrid = new ArrayList<ArrayList<ConceptLatticeGrid>>();
		HashSet<HashSet<Integer>> docsSet = getSubSet(docIDOnFacets);
		HashMap<HashSet<String>,HashSet<Integer>> gridMap = new HashMap<HashSet<String>,HashSet<Integer>>();
		
		//
		for(int index = 0; index < docIDOnFacets.size(); index++) {
			ArrayList<ConceptLatticeGrid> tmpList = new ArrayList<ConceptLatticeGrid>();
			resultListOfGrid.add(tmpList);
		}
		
		//寻找对应的Set<String>
		for(HashSet<Integer> element:docsSet) {
			HashSet<String> keySet = new HashSet<String>();
			Iterator<Entry<String, HashSet<Integer>>> iterOfFacetTermOnDocIDs = facetTermOnDocIDs.entrySet().iterator();
			while(iterOfFacetTermOnDocIDs.hasNext()) {
				Entry<String, HashSet<Integer>> entryOfFacetTerms = iterOfFacetTermOnDocIDs.next();
				if(entryOfFacetTerms.getValue().containsAll(element)) {
					keySet.add(entryOfFacetTerms.getKey());
				}
			}
			//有以下这一步就将查询为null的值排除了
			if(!keySet.isEmpty()) {
				gridMap.put(keySet, element);
			}
		}
		
		//这里面还存在冗余，应该将每个HashSet<Integer>结果集对应的查询集最大化，不然会有冗余。
		
		//将查询集—查询结果集保存到list里面去
		Iterator<Entry<HashSet<String>,HashSet<Integer>>> iterOfGridMap = gridMap.entrySet().iterator();
		while(iterOfGridMap.hasNext()) {
			Entry<HashSet<String>,HashSet<Integer>> entryOfGridMap = iterOfGridMap.next();
			ConceptLatticeGrid tmpGrid = new ConceptLatticeGrid();
			tmpGrid.setQuest(entryOfGridMap.getKey());
			tmpGrid.setResourcesIds(entryOfGridMap.getValue());
			ArrayList<ConceptLatticeGrid> tmpList = new ArrayList<ConceptLatticeGrid>();
			tmpList = resultListOfGrid.get(entryOfGridMap.getValue().size() - 1);
			tmpList.add(tmpGrid);
			resultListOfGrid.set(entryOfGridMap.getValue().size() - 1, tmpList);
		}
		
		//发现关系
		
		return resultListOfGrid;
	}
	
	//获取子集
	/*
	 * 对于集合里面的任何一个元素，有两种可能，
	 * 一种是在子集合里，另一种是不在子集合里。
	 * 在子集合里的话用1表示，不在的话用0表示，
	 * 那么一个集合的子集合都可以用二进制表示，
	 * 假设集合为{1,2,3}，
	 * 那么可以用下列二级制表示：000,001,010,011......共有2^n种表示。
	 * */
	private static HashSet<HashSet<Integer>> getSubSet(HashMap<Integer, HashSet<String>> docIDOnFacets){
		HashSet<HashSet<Integer>> resultSet = new HashSet<HashSet<Integer>>();
		ArrayList<Integer> tmpList = new ArrayList<Integer>();

		Iterator<Entry<Integer, HashSet<String>>> iterOfIDOnFacets = docIDOnFacets.entrySet().iterator();
		while(iterOfIDOnFacets.hasNext()) {
			Entry<Integer, HashSet<String>> entryOfDOcs = iterOfIDOnFacets.next();
			tmpList.add(entryOfDOcs.getKey());
		}
		int max = 1 << docIDOnFacets.size();
		for (int i = 0; i < max; i++) {
			int index = 0;
			int k = i;
			HashSet<Integer> s = new HashSet<Integer>();
			while (k > 0) {
				if ((k & 1) > 0) {
					s.add(tmpList.get(index));
				}
				k >>= 1;
				index++;
			}
			if(!s.isEmpty()) {
				resultSet.add(s);
			}			
		}
		return resultSet;		
	}
	
	@SuppressWarnings("unused")
	private static Map<Map<Integer, List<Integer>>, Map<List<Integer>, List<Integer>>> findFather(Map<List<Integer>,List<Integer>> src){  
        Map<Map<Integer,List<Integer>>,Map<List<Integer>,List<Integer>>> result = new HashMap<Map<Integer,List<Integer>>,Map<List<Integer>,List<Integer>>>();  
        Map<Integer,Set<Integer>> storeObjAll = new HashMap<Integer,Set<Integer>>();  
        Map<Integer,List<Integer>> storeAttrAll = new HashMap<Integer,List<Integer>>();  
        Set<Integer> storeObjSet = new HashSet<Integer>();  
        int tmpkey = 0;  
        int tmpValNum = 0;
         for(Entry<List<Integer>, List<Integer>> entry: src.entrySet()){  
             List<Integer> tmp = new ArrayList<Integer>();  
             List<Integer> tmpval = new ArrayList<Integer>();  
             Set<Integer> tmpSet = new HashSet<Integer>();  
             tmp = entry.getKey();  
             tmpval = entry.getValue();  
             tmpValNum++;  
             storeAttrAll.put(tmpValNum, tmpval);  
             for(int m=0;m<tmp.size();m++)  
             {  
                storeObjSet.add(tmp.get(m));  
            }  
            tmpSet.addAll(storeObjSet);  
            tmpkey++;  
            storeObjAll.put(tmpkey, tmpSet);  
            tmp.clear();  
            storeObjSet.clear();  
         }  
      
         for(int i=0;i<storeObjAll.size();i++)  
         {  
             List<Integer> tmpFather = new ArrayList<Integer>();  
             Map<Integer,List<Integer>> numAndFather = new HashMap<Integer,List<Integer>>();  
             Map<List<Integer>,List<Integer>> concep = new HashMap<List<Integer>,List<Integer>>();  
             Set<Integer> tmpSetp = new HashSet<Integer>();  
             tmpSetp.addAll(storeObjAll.get(i+1));  
             for(int j=0;j<storeObjAll.size();j++){  
                 if(!(storeObjAll.get(j+1).equals(tmpSetp))){  
                     if(storeObjAll.get(j+1).containsAll(tmpSetp))  
                     {  
                         if(storeObjAll.get(j+1).size()-tmpSetp.size()==1){  
                            tmpFather.add(j+1);  
                         }else if(storeObjAll.get(j+1).size()-tmpSetp.size()>1){  
                             int compareNum = 0;  
                             for(int k=0;k<storeObjAll.size();k++)  
                             {  
                                 if(!(storeObjAll.get(k+1).equals(storeObjAll.get(j+1))) && !(storeObjAll.get(k+1).equals(tmpSetp))){  
                                     if(!(storeObjAll.get(k+1).containsAll(tmpSetp) && storeObjAll.get(j+1).containsAll(storeObjAll.get(k+1)))){  
                                        compareNum++;  
                                     }  
                                 }  
                                      
                             }  
                             if(compareNum == (storeObjAll.size()-2)){  
                                tmpFather.add(j+1);  
                             }  
                         }else{  
                             break;  
                         }  
  
                     }  
                 }  
             }  
            numAndFather.put(i+1, tmpFather);  
            List<Integer> setToList = new ArrayList<Integer>();  
            for(Integer val : tmpSetp){    
                setToList.add(val);    
            }    
            concep.put(setToList, storeAttrAll.get(i+1));  
            result.put(numAndFather, concep);  
              
         }  
        return result;  
    } 
}
