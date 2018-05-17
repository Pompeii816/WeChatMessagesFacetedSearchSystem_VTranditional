package NavigationModel.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import Domain.ConceptLatticeGrid;
import Domain.WeChatMessage;
import NavigationModel.HierarchicalRelationshipTool;
/*
 * 返回的参数是一个概念格的List
 * 动态导航的实现就是根据该List里面的grid的属性集是当前查询的属性集的父集，
 * 就可以当作泛化的，在根据当前查询集的子集来判断是否有可以细化的查询
 * */
public class Navigator implements HierarchicalRelationshipTool {

	@Override
	public ArrayList<ConceptLatticeGrid> getHierarchicalRelationship(
			HashMap<Integer, WeChatMessage> messageMap, HashMap<String, HashSet<Integer>> facetTermOnDocIDs,
			HashMap<Integer, HashSet<String>> docIDOnFacets) {
		
		ArrayList<String> attributesList = new ArrayList<String>();
		ArrayList<Integer> resoucesList = new ArrayList<Integer>();

		for (Entry<String, HashSet<Integer>> entryOfFODIDs : facetTermOnDocIDs.entrySet()) {
			attributesList.add(entryOfFODIDs.getKey());
		}

		for (Entry<Integer, HashSet<String>> entryOfDIDOFs : docIDOnFacets.entrySet()) {
			resoucesList.add(entryOfDIDOFs.getKey());
		}

		ArrayList<ArrayList<String>> attributesSubList = getAttributesSubList(attributesList);
		HashMap<ArrayList<String>, ArrayList<Integer>> processMap = getResourcesSub(attributesSubList, docIDOnFacets);
		HashMap<ArrayList<String>, ArrayList<Integer>> resultSources = reduceResourcesAttributes(processMap);
		resultSources.put(null, resoucesList);

		// HashMap<HashMap<Integer, ArrayList<Integer>>, HashMap<ArrayList<Integer>,
		// ArrayList<String>>> resultMap = getRelationship(resultSources);

		HashMap<Integer, ArrayList<Integer>> storeObjAll = new HashMap<Integer, ArrayList<Integer>>(); // 保存所有的对象的列表<ID-对象集>
		HashMap<Integer, ArrayList<String>> storeAttrAll = new HashMap<Integer, ArrayList<String>>(); // 保存所有属性的列表

		int key = 0;
		for (Entry<ArrayList<String>, ArrayList<Integer>> entry : resultSources.entrySet()) {
			key++;
			storeObjAll.put(key, entry.getValue());
			storeAttrAll.put(key, entry.getKey());
		}

		HashMap<HashMap<Integer, ArrayList<Integer>>, HashMap<ArrayList<Integer>, ArrayList<String>>> result = 
				new HashMap<HashMap<Integer, ArrayList<Integer>>, HashMap<ArrayList<Integer>, ArrayList<String>>>(); // 保存结果集<<节点ID-父节点集>,<对象集list-属性集list>>
		ArrayList<ConceptLatticeGrid> processConceptLatticeGridList = new ArrayList<ConceptLatticeGrid>();
		
		// 寻找父节点
		for (int i = 0; i < storeObjAll.size(); i++) {
			ConceptLatticeGrid tmpConceptLatticeGrid = new ConceptLatticeGrid();
			ArrayList<Integer> tmpFather = new ArrayList<Integer>(); // 父Grid节点列表
			HashMap<Integer, ArrayList<Integer>> numAndFather = 
					new HashMap<Integer, ArrayList<Integer>>(); // 当前节点和父节点的Map
			HashMap<ArrayList<Integer>, ArrayList<String>> concep = 
					new HashMap<ArrayList<Integer>, ArrayList<String>>(); // 对象和属性的Map
			ArrayList<Integer> tmpList = new ArrayList<Integer>(); // 存储ID为index+1的对象list
			tmpList.addAll(storeObjAll.get(i + 1));
			// 遍历storeObjAll，从里面寻找父节点集
			for (int j = 0; j < storeObjAll.size(); j++) {
				if (!(storeObjAll.get(j + 1).equals(tmpList))) { // 避开同一个ID对应的对象集的情况
					if (storeObjAll.get(j + 1).containsAll(tmpList)) { // 如果包含tmpSetp
						if (storeObjAll.get(j + 1).size() - tmpList.size() == 1) { // 且集合的size值只小1，则为该节点的父节点
							tmpFather.add(j + 1);
						} else if (storeObjAll.get(j + 1).size() - tmpList.size() > 1) {// 若size的值差大于1
							int compareNum = 0;
							for (int k = 0; k < storeObjAll.size(); k++) {
								if (!(storeObjAll.get(k + 1).equals(storeObjAll.get(j + 1)))
										&& !(storeObjAll.get(k + 1).equals(tmpList))) { // 避开同一个ID对应的对象集的情况，且避开storeObjAll.get(j
																						// + 1)与
									if (!(storeObjAll.get(k + 1).containsAll(tmpList)
											&& storeObjAll.get(j + 1).containsAll(storeObjAll.get(k + 1)))) {
										compareNum++;
									}
								}

							}
							if (compareNum == (storeObjAll.size() - 2)) {
								tmpFather.add(j + 1);
							}
						} else {
							break;
						}

					}
				}
			}
			numAndFather.put(i + 1, tmpFather);
			tmpConceptLatticeGrid.setID(i + 1);
			tmpConceptLatticeGrid.setFatherGrid(tmpFather);
			tmpConceptLatticeGrid.setQuest(storeAttrAll.get(i + 1));
			tmpConceptLatticeGrid.setResourcesIds(tmpList);
			ArrayList<Integer> setToList = new ArrayList<Integer>();
			for (Integer val : tmpList) {
				setToList.add(val);
			}
			processConceptLatticeGridList.add(tmpConceptLatticeGrid);
			concep.put(setToList, storeAttrAll.get(i + 1)); // 对象集-属性集
			result.put(numAndFather, concep);

		}

		/*
		for(Entry<HashMap<Integer, ArrayList<Integer>>, HashMap<ArrayList<Integer>, ArrayList<String>>> entry:result.entrySet()) {
			for(int index = 1; index <= result.size(); index++) {
				if(entry.getKey().containsKey(index)) {
					ConceptLatticeGrid tmpConceptLatticeGrid = new ConceptLatticeGrid();
					tmpConceptLatticeGrid.setID(index);
					processConceptLatticeGridList.add(tmpConceptLatticeGrid);
				}
			}
		}
		*/
		return processConceptLatticeGridList;
	}

	// 获取属性子集
	/*
	 * 对于集合里面的任何一个元素，有两种可能， 一种是在子集合里，另一种是不在子集合里。 在子集合里的话用1表示，不在的话用0表示，
	 * 那么一个集合的子集合都可以用二进制表示， 假设集合为{1,2,3}，
	 * 那么可以用下列二级制表示：000,001,010,011......共有2^n种表示。
	 */
	private static ArrayList<ArrayList<String>> getAttributesSubList(ArrayList<String> attributesList) {
		ArrayList<ArrayList<String>> resultList = new ArrayList<ArrayList<String>>();
		int max = 1 << attributesList.size();// 子集最大的数量

		for (int i = 0; i < max; i++) {
			int index = 0;
			int k = i;
			ArrayList<String> s = new ArrayList<String>();
			while (k > 0) {
				if ((k & 1) > 0) {
					s.add(attributesList.get(index));
				}
				k >>= 1;
				index++;
			}
			if (!s.isEmpty()) {
				resultList.add(s);
			}
		}
		return resultList;
	}

	// 获取对应的资源子集
	private static HashMap<ArrayList<String>, ArrayList<Integer>> getResourcesSub(
			ArrayList<ArrayList<String>> attributesSubList, HashMap<Integer, HashSet<String>> docIDOnFacets) {
		HashMap<ArrayList<String>, ArrayList<Integer>> resultMap = new HashMap<ArrayList<String>, ArrayList<Integer>>();

		for (int index = 0; index < attributesSubList.size(); index++) {
			ArrayList<String> tmpListOfString = attributesSubList.get(index);
			ArrayList<Integer> tmpListOfInteger = new ArrayList<Integer>();
			for (Entry<Integer, HashSet<String>> entryOfDIDOF : docIDOnFacets.entrySet()) {
				if (entryOfDIDOF.getValue().containsAll(tmpListOfString)) {
					tmpListOfInteger.add(entryOfDIDOF.getKey());
				}
			}
			if (!tmpListOfString.isEmpty() && !tmpListOfInteger.isEmpty()) {
				resultMap.put(tmpListOfString, tmpListOfInteger);
			}
			tmpListOfString = null;
		}
		return resultMap;
	}

	// 冗余去除，资源集相等的情况下，将属性集小的去除
	private static HashMap<ArrayList<String>, ArrayList<Integer>> reduceResourcesAttributes(
			HashMap<ArrayList<String>, ArrayList<Integer>> processMap) {
		HashMap<ArrayList<String>, ArrayList<Integer>> resultMap = new HashMap<ArrayList<String>, ArrayList<Integer>>();
		ArrayList<String> tmpKey = new ArrayList<String>();
		ArrayList<Integer> tmpValue = new ArrayList<Integer>();

		for (Entry<ArrayList<String>, ArrayList<Integer>> entry : processMap.entrySet()) {
			HashMap<ArrayList<String>, ArrayList<Integer>> tmpMap = new HashMap<ArrayList<String>, ArrayList<Integer>>();
			tmpKey.addAll(entry.getKey());
			tmpValue.addAll(entry.getValue());

			if (resultMap.size() == 0) {
				ArrayList<String> tmpKeys = new ArrayList<String>();
				ArrayList<Integer> tmpVals = new ArrayList<Integer>();
				tmpKeys.addAll(tmpKey);
				tmpVals.addAll(tmpValue);
				resultMap.put(tmpKeys, tmpVals);
			} else {
				tmpMap.putAll(resultMap);
				int resLen = tmpMap.size();
				int tmpNum = 0;
				for (Entry<ArrayList<String>, ArrayList<Integer>> entryOfTmpMap : tmpMap.entrySet()) {

					if ((entryOfTmpMap.getKey().equals(tmpKey)) && entryOfTmpMap.getValue().size() < tmpValue.size()) {// 若存在属性集包含于tmpMap内，且资源集大于tmpMap里面的资源集，则将该entry加进去

						ArrayList<String> tmpKeym = new ArrayList<String>();
						ArrayList<Integer> tmpValm = new ArrayList<Integer>();
						tmpKeym.addAll(tmpKey);
						tmpValm.addAll(tmpValue);
						resultMap.put(tmpKeym, tmpValm);

						break;

					}
					if ((entryOfTmpMap.getKey().equals(tmpKey)) && entryOfTmpMap.getValue().size() >= tmpValue.size()) {

						break;
					}
					if (!(entryOfTmpMap.getKey().equals(tmpKey))) { // 若之前一直没有将entry加进去，则遍历完tmpMap之后直接将该entry加进去
						tmpNum = tmpNum + 1;
						if (tmpNum == resLen) {
							ArrayList<String> tmpKeye = new ArrayList<String>();
							ArrayList<Integer> tmpVale = new ArrayList<Integer>();
							tmpKeye.addAll(tmpKey);
							tmpVale.addAll(tmpValue);
							resultMap.put(tmpKeye, tmpVale);
						}
					}
				}

			}
			tmpKey.clear();
			tmpValue.clear();
		}

		return resultMap;
	}

}
