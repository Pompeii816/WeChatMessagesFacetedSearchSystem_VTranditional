package NavigationModel.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import Domain.ConceptLatticeGrid;
import Domain.WeChatMessage;
import NavigationModel.HierarchicalRelationshipTool;
/*
 * ���صĲ�����һ��������List
 * ��̬������ʵ�־��Ǹ��ݸ�List�����grid�����Լ��ǵ�ǰ��ѯ�����Լ��ĸ�����
 * �Ϳ��Ե��������ģ��ڸ��ݵ�ǰ��ѯ�����Ӽ����ж��Ƿ��п���ϸ���Ĳ�ѯ
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

		HashMap<Integer, ArrayList<Integer>> storeObjAll = new HashMap<Integer, ArrayList<Integer>>(); // �������еĶ�����б�<ID-����>
		HashMap<Integer, ArrayList<String>> storeAttrAll = new HashMap<Integer, ArrayList<String>>(); // �����������Ե��б�

		int key = 0;
		for (Entry<ArrayList<String>, ArrayList<Integer>> entry : resultSources.entrySet()) {
			key++;
			storeObjAll.put(key, entry.getValue());
			storeAttrAll.put(key, entry.getKey());
		}

		HashMap<HashMap<Integer, ArrayList<Integer>>, HashMap<ArrayList<Integer>, ArrayList<String>>> result = 
				new HashMap<HashMap<Integer, ArrayList<Integer>>, HashMap<ArrayList<Integer>, ArrayList<String>>>(); // ��������<<�ڵ�ID-���ڵ㼯>,<����list-���Լ�list>>
		ArrayList<ConceptLatticeGrid> processConceptLatticeGridList = new ArrayList<ConceptLatticeGrid>();
		
		// Ѱ�Ҹ��ڵ�
		for (int i = 0; i < storeObjAll.size(); i++) {
			ConceptLatticeGrid tmpConceptLatticeGrid = new ConceptLatticeGrid();
			ArrayList<Integer> tmpFather = new ArrayList<Integer>(); // ��Grid�ڵ��б�
			HashMap<Integer, ArrayList<Integer>> numAndFather = 
					new HashMap<Integer, ArrayList<Integer>>(); // ��ǰ�ڵ�͸��ڵ��Map
			HashMap<ArrayList<Integer>, ArrayList<String>> concep = 
					new HashMap<ArrayList<Integer>, ArrayList<String>>(); // ��������Ե�Map
			ArrayList<Integer> tmpList = new ArrayList<Integer>(); // �洢IDΪindex+1�Ķ���list
			tmpList.addAll(storeObjAll.get(i + 1));
			// ����storeObjAll��������Ѱ�Ҹ��ڵ㼯
			for (int j = 0; j < storeObjAll.size(); j++) {
				if (!(storeObjAll.get(j + 1).equals(tmpList))) { // �ܿ�ͬһ��ID��Ӧ�Ķ��󼯵����
					if (storeObjAll.get(j + 1).containsAll(tmpList)) { // �������tmpSetp
						if (storeObjAll.get(j + 1).size() - tmpList.size() == 1) { // �Ҽ��ϵ�sizeֵֻС1����Ϊ�ýڵ�ĸ��ڵ�
							tmpFather.add(j + 1);
						} else if (storeObjAll.get(j + 1).size() - tmpList.size() > 1) {// ��size��ֵ�����1
							int compareNum = 0;
							for (int k = 0; k < storeObjAll.size(); k++) {
								if (!(storeObjAll.get(k + 1).equals(storeObjAll.get(j + 1)))
										&& !(storeObjAll.get(k + 1).equals(tmpList))) { // �ܿ�ͬһ��ID��Ӧ�Ķ��󼯵�������ұܿ�storeObjAll.get(j
																						// + 1)��
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
			concep.put(setToList, storeAttrAll.get(i + 1)); // ����-���Լ�
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

	// ��ȡ�����Ӽ�
	/*
	 * ���ڼ���������κ�һ��Ԫ�أ������ֿ��ܣ� һ�������Ӽ������һ���ǲ����Ӽ���� ���Ӽ�����Ļ���1��ʾ�����ڵĻ���0��ʾ��
	 * ��ôһ�����ϵ��Ӽ��϶������ö����Ʊ�ʾ�� ���輯��Ϊ{1,2,3}��
	 * ��ô���������ж����Ʊ�ʾ��000,001,010,011......����2^n�ֱ�ʾ��
	 */
	private static ArrayList<ArrayList<String>> getAttributesSubList(ArrayList<String> attributesList) {
		ArrayList<ArrayList<String>> resultList = new ArrayList<ArrayList<String>>();
		int max = 1 << attributesList.size();// �Ӽ���������

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

	// ��ȡ��Ӧ����Դ�Ӽ�
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

	// ����ȥ������Դ����ȵ�����£������Լ�С��ȥ��
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

					if ((entryOfTmpMap.getKey().equals(tmpKey)) && entryOfTmpMap.getValue().size() < tmpValue.size()) {// ���������Լ�������tmpMap�ڣ�����Դ������tmpMap�������Դ�����򽫸�entry�ӽ�ȥ

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
					if (!(entryOfTmpMap.getKey().equals(tmpKey))) { // ��֮ǰһֱû�н�entry�ӽ�ȥ���������tmpMap֮��ֱ�ӽ���entry�ӽ�ȥ
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
