package NavigationModel.Impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class Tools {

	// 获取属性-对象的list
	public static List<Map<Integer, List<Integer>>> AttrObj(String source) throws IOException {
		FileReader fileread = new FileReader(new File(source));
		@SuppressWarnings("resource")
		BufferedReader buffread = new BufferedReader(fileread);
		String line = null;
		Integer AttrNum = 0;
		Integer ObjNum = 0;
		List<Map<String, List<String>>> ObjAttrList = new ArrayList<Map<String, List<String>>>();
		while ((line = buffread.readLine()) != null) {
			ObjNum++;
			Map<String, List<String>> map = new HashMap<String, List<String>>();
			List<String> list1 = new ArrayList<String>();
			String[] content = line.split(":");
			String str1 = content[0];
			String str2 = content[1];
			String[] str2tmp = str2.split(",");
			AttrNum = str2tmp.length;
			for (String string : str2tmp) {
				list1.add(string.trim());
			}
			map.put(str1, list1);
			ObjAttrList.add(map);
		}

		List<Map<Integer, List<Integer>>> AttrObjList = new ArrayList<Map<Integer, List<Integer>>>();
		for (int i = 0; i < AttrNum; i++) {
			Integer m = i + 1;
			Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
			List<Integer> list = new ArrayList<Integer>();
			for (int j = 0; j < ObjNum; j++) {
				Integer n = j + 1;
				String str = ObjAttrList.get(j).entrySet().iterator().next().getValue().get(i);
				if (str.equals("1")) {
					list.add(n);
				}
			}
			map.put(m, list);
			AttrObjList.add(map);
		}
		return AttrObjList;
	}

	// 获取属性个数和对象个数的方法
	@SuppressWarnings("resource")
	public static int[] getAttrNum(String source) throws IOException {
		FileReader fileread = new FileReader(new File(source));
		BufferedReader buffread = new BufferedReader(fileread);
		String line = null;
		Integer attrNum = 0;
		Integer objNum = 0;
		while ((line = buffread.readLine()) != null) {
			objNum++;
			String[] content = line.split(":");
			String str = content[1];
			String[] strtmp = str.split(",");
			attrNum = strtmp.length;
		}
		int resNum[] = new int[2];
		resNum[0] = attrNum;
		resNum[1] = objNum;

		return resNum;
	}

	// 给每一个属性赋一个id的值
	public static Integer[] SetN(int attrNum) {
		Set<Integer> set = new HashSet<Integer>();
		for (int i = 1; i <= attrNum; i++) {
			set.add(i);
		}

		Integer[] setN = new Integer[set.size()];
		set.toArray(setN);
		return setN;
	}

	// 写入属性子集
	public static void getSubSet(Integer[] setN) throws IOException {
		FileWriter writer = new FileWriter(new File("src/attrSubSet.txt"));
		int length = setN.length;
		int num = (2 << setN.length - 1) - 1;
		for (int i = 1; i <= num; i++) {
			int now = i;
			for (int j = 0; j <= length - 1; j++) {
				if ((now & 1) == 1) {
					writer.write("" + setN[j] + " ");
				}
				now = now >> 1;
			}
			writer.write("\r\n");
		}
		writer.close();
	}

	// 读取属性子集
	public static List<List<Integer>> attrSubSetList() throws NumberFormatException, IOException {
		FileReader fileread = new FileReader(new File("src/attrSubSet.txt"));
		BufferedReader buffread = new BufferedReader(fileread);
		String line = null;
		List<List<Integer>> list = new ArrayList<List<Integer>>();
		while ((line = buffread.readLine()) != null) {
			List<Integer> list1 = new ArrayList<Integer>();
			String[] content = line.split(" ");
			for (String tmp : content) {
				list1.add(Integer.parseInt(tmp));
			}
			list.add(list1);
		}
		buffread.close();
		return list;
	}

	// 通过属性子集，获取对应的结果资源子集
	/*
	 * 作用：通过属性子集，获取对应的结果资源子集 输入： List<List<Integer>> attrSubSetlist 属性子集的list
	 * List<Map<Integer, List<Integer>>> attrObjList 属性-对象list 输出：
	 * Map<List<Integer>, List<Integer>> result 属性子集-资源子集（对象子集）的一个map
	 */
	public static Map<List<Integer>, List<Integer>> objInterList(List<List<Integer>> attrSubSetlist,
			List<Map<Integer, List<Integer>>> attrObjList) {
		Map<List<Integer>, List<Integer>> result = new HashMap<List<Integer>, List<Integer>>();
		int len = attrSubSetlist.size();
		List<Integer> tmpAttr = new ArrayList<Integer>();
		List<Integer> tmpObj = new ArrayList<Integer>();
		for (int i = 0; i < len; i++) {
			int attrSubSetListSize = attrSubSetlist.get(i).size();
			if (attrSubSetListSize == 1) {
				tmpAttr = attrSubSetlist.get(i);
				int oneAttrVal = attrSubSetlist.get(i).iterator().next();
				tmpObj = attrObjList.get(oneAttrVal - 1).entrySet().iterator().next().getValue();
				result.put(tmpAttr, tmpObj);
			} else {
				tmpAttr = attrSubSetlist.get(i);
				int attrNum = attrSubSetListSize;
				List<Integer> tmpObjLi = new ArrayList<Integer>();
				List<Integer> resObjLi = new ArrayList<Integer>();
				int m1 = attrSubSetlist.get(i).listIterator(0).next();
				resObjLi.addAll(attrObjList.get(m1 - 1).entrySet().iterator().next().getValue());
				for (int j = 1; j < attrNum; j++) {
					int mn = attrSubSetlist.get(i).listIterator(j).next();
					tmpObjLi.clear();
					tmpObjLi.addAll(attrObjList.get(mn - 1).entrySet().iterator().next().getValue());
					resObjLi.retainAll(tmpObjLi);
				}
				List<Integer> resObjLiCopy = new ArrayList<Integer>();
				resObjLiCopy.addAll(resObjLi);
				if (resObjLi.size() > 0) {
					result.put(tmpAttr, resObjLiCopy);

				}
				resObjLi.clear();
			}
		}
		return result;
	}

	// 冗余去除
	public static Map<List<Integer>, List<Integer>> reduceObjAttr(Map<List<Integer>, List<Integer>> processSrc) {
		Map<List<Integer>, List<Integer>> result = new HashMap<List<Integer>, List<Integer>>();
		List<Integer> tmpKey = new ArrayList<Integer>();
		List<Integer> tmpValue = new ArrayList<Integer>();
		for (Entry<List<Integer>, List<Integer>> entry : processSrc.entrySet()) {
			Map<List<Integer>, List<Integer>> tmpMap = new HashMap<List<Integer>, List<Integer>>();
			tmpKey.addAll(entry.getValue());
			tmpValue.addAll(entry.getKey());

			if (result.size() == 0) {
				List<Integer> tmpKeys = new ArrayList<Integer>();
				List<Integer> tmpVals = new ArrayList<Integer>();
				tmpKeys.addAll(tmpKey);
				tmpVals.addAll(tmpValue);
				result.put(tmpKeys, tmpVals);
			} else {
				tmpMap.putAll(result);
				int resLen = tmpMap.size();
				int tmpNum = 0;
				for (Entry<List<Integer>, List<Integer>> resA : tmpMap.entrySet()) {

					if ((resA.getKey().equals(tmpKey)) && resA.getValue().size() < tmpValue.size()) {

						List<Integer> tmpKeym = new ArrayList<Integer>();
						List<Integer> tmpValm = new ArrayList<Integer>();
						tmpKeym.addAll(tmpKey);
						tmpValm.addAll(tmpValue);
						result.put(tmpKeym, tmpValm);

						break;

					}
					if ((resA.getKey().equals(tmpKey)) && resA.getValue().size() >= tmpValue.size()) {

						break;
					}
					if (!(resA.getKey().equals(tmpKey))) {
						tmpNum = tmpNum + 1;
						if (tmpNum == resLen) {
							List<Integer> tmpKeye = new ArrayList<Integer>();
							List<Integer> tmpVale = new ArrayList<Integer>();
							tmpKeye.addAll(tmpKey);
							tmpVale.addAll(tmpValue);
							result.put(tmpKeye, tmpVale);

						}
					}
				}

			}
			tmpKey.clear();
			tmpValue.clear();
		}

		return result;
	}

	// 添加上界和下界
	public static Map<List<Integer>, List<Integer>> addFullConcep(int attrNum, int objNum) {
		Map<List<Integer>, List<Integer>> fullConcept = new HashMap<List<Integer>, List<Integer>>();
		List<Integer> fullAttr = new ArrayList<Integer>();
		List<Integer> fullObj = new ArrayList<Integer>();
		List<Integer> nullLi = new ArrayList<Integer>();
		for (int i = 1; i <= attrNum; i++) {
			fullAttr.add(i);
		}
		for (int j = 1; j <= objNum; j++) {
			fullObj.add(j);
		}
		nullLi.add(0);
		fullConcept.put(fullObj, nullLi);
		fullConcept.put(nullLi, fullAttr);
		return fullConcept;
	}

	// 寻找节点之间的关系
	public static Map<Map<Integer, List<Integer>>, Map<List<Integer>, List<Integer>>> findFather(
			Map<List<Integer>, List<Integer>> src) {
		Map<Map<Integer, List<Integer>>, Map<List<Integer>, List<Integer>>> result = 
				new HashMap<Map<Integer, List<Integer>>, Map<List<Integer>, List<Integer>>>();	// 保存结果集<<节点ID-父节点集>,<对象集list-属性集list>>
		Map<Integer, Set<Integer>> storeObjAll = new HashMap<Integer, Set<Integer>>(); 			// 保存所有的对象的列表<ID-对象集>
		Map<Integer, List<Integer>> storeAttrAll = new HashMap<Integer, List<Integer>>(); 		// 保存所有属性的列表
		Set<Integer> storeObjSet = new HashSet<Integer>(); 										// 保存对象的集合
		int tmpkey = 0; 			// 对象ID
		int tmpValNum = 0; 			// 属性集的ID

		// 遍历整个传进来的对象属性Map
		for (Entry<List<Integer>, List<Integer>> entry : src.entrySet()) {
			List<Integer> tmp = new ArrayList<Integer>();
			List<Integer> tmpval = new ArrayList<Integer>();
			Set<Integer> tmpSet = new HashSet<Integer>();
			tmp = entry.getKey(); 			// 对象集
			tmpval = entry.getValue(); 		// 属性集
			tmpValNum++;
			storeAttrAll.put(tmpValNum, tmpval);
			for (int m = 0; m < tmp.size(); m++) {
				storeObjSet.add(tmp.get(m));
			}
			tmpSet.addAll(storeObjSet);
			tmpkey++;
			storeObjAll.put(tmpkey, tmpSet);
			tmp.clear();
			storeObjSet.clear();
		}

		// 寻找父节点
		for (int i = 0; i < storeObjAll.size(); i++) {
			List<Integer> tmpFather = new ArrayList<Integer>(); 										// 父Grid节点列表
			Map<Integer, List<Integer>> numAndFather = new HashMap<Integer, List<Integer>>();			// 当前节点和父节点的Map
			Map<List<Integer>, List<Integer>> concep = new HashMap<List<Integer>, List<Integer>>(); 	// 对象和属性的Map
			Set<Integer> tmpSetp = new HashSet<Integer>();	//存储ID为index+1的对象集
			tmpSetp.addAll(storeObjAll.get(i + 1));
			//遍历storeObjAll，从里面寻找父节点集
			for (int j = 0; j < storeObjAll.size(); j++) {
				if (!(storeObjAll.get(j + 1).equals(tmpSetp))) {		//避开同一个ID对应的对象集的情况
					if (storeObjAll.get(j + 1).containsAll(tmpSetp)) {	//如果包含tmpSetp
						if (storeObjAll.get(j + 1).size() - tmpSetp.size() == 1) {	//且集合的size值只小1，则为该节点的父节点
							tmpFather.add(j + 1);
						} else if (storeObjAll.get(j + 1).size() - tmpSetp.size() > 1) {//若size的值差大于1
							int compareNum = 0;
							for (int k = 0; k < storeObjAll.size(); k++) {
								if (!(storeObjAll.get(k + 1).equals(storeObjAll.get(j + 1)))
										&& !(storeObjAll.get(k + 1).equals(tmpSetp))) {		//避开同一个ID对应的对象集的情况，且避开storeObjAll.get(j + 1)与
									if (!(storeObjAll.get(k + 1).containsAll(tmpSetp)
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
			List<Integer> setToList = new ArrayList<Integer>();
			for (Integer val : tmpSetp) {
				setToList.add(val);
			}
			concep.put(setToList, storeAttrAll.get(i + 1));	//对象集-属性集
			result.put(numAndFather, concep);

		}
		return result;
	}

}

/*
// 寻找节点之间的关系
public static HashMap<HashMap<Integer, ArrayList<Integer>>, HashMap<ArrayList<Integer>, ArrayList<Integer>>> findFather(
		HashMap<ArrayList<Integer>, ArrayList<Integer>> src) {
	HashMap<HashMap<Integer, ArrayList<Integer>>, HashMap<ArrayList<Integer>, ArrayList<Integer>>> result = new HashMap<HashMap<Integer, ArrayList<Integer>>, HashMap<ArrayList<Integer>, ArrayList<Integer>>>(); // 保存结果集<<节点ID-父节点集>,<对象集list-属性集list>>
	HashMap<Integer, HashSet<Integer>> storeObjAll = new HashMap<Integer, HashSet<Integer>>(); // 保存所有的对象的列表<ID-对象集>
	HashMap<Integer, ArrayList<Integer>> storeAttrAll = new HashMap<Integer, ArrayList<Integer>>(); // 保存所有属性的列表
	HashSet<Integer> storeObjSet = new HashSet<Integer>(); // 保存对象的集合
	int tmpkey = 0; // 对象ID
	int tmpValNum = 0; // 属性集的ID

	// 遍历整个传进来的对象属性Map
	for (Entry<ArrayList<Integer>, ArrayList<Integer>> entry : src.entrySet()) {
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		ArrayList<Integer> tmpval = new ArrayList<Integer>();
		HashSet<Integer> tmpSet = new HashSet<Integer>();
		tmp = entry.getKey(); // 对象集
		tmpval = entry.getValue(); // 属性集
		tmpValNum++;
		storeAttrAll.put(tmpValNum, tmpval);
		for (int m = 0; m < tmp.size(); m++) {
			storeObjSet.add(tmp.get(m));
		}
		tmpSet.addAll(storeObjSet);
		tmpkey++;
		storeObjAll.put(tmpkey, tmpSet);
		tmp.clear();
		storeObjSet.clear();
	}

	// 寻找父节点
	for (int i = 0; i < storeObjAll.size(); i++) {
		ArrayList<Integer> tmpFather = new ArrayList<Integer>(); // 父Grid节点列表
		HashMap<Integer, ArrayList<Integer>> numAndFather = new HashMap<Integer, ArrayList<Integer>>(); // 当前节点和父节点的Map
		HashMap<ArrayList<Integer>, ArrayList<Integer>> concep = new HashMap<ArrayList<Integer>, ArrayList<Integer>>(); // 对象和属性的Map
		Set<Integer> tmpSetp = new HashSet<Integer>(); // 存储ID为index+1的对象集
		tmpSetp.addAll(storeObjAll.get(i + 1));
		// 遍历storeObjAll，从里面寻找父节点集
		for (int j = 0; j < storeObjAll.size(); j++) {
			if (!(storeObjAll.get(j + 1).equals(tmpSetp))) { // 避开同一个ID对应的对象集的情况
				if (storeObjAll.get(j + 1).containsAll(tmpSetp)) { // 如果包含tmpSetp
					if (storeObjAll.get(j + 1).size() - tmpSetp.size() == 1) { // 且集合的size值只小1，则为该节点的父节点
						tmpFather.add(j + 1);
					} else if (storeObjAll.get(j + 1).size() - tmpSetp.size() > 1) {// 若size的值差大于1
						int compareNum = 0;
						for (int k = 0; k < storeObjAll.size(); k++) {
							if (!(storeObjAll.get(k + 1).equals(storeObjAll.get(j + 1)))
									&& !(storeObjAll.get(k + 1).equals(tmpSetp))) { // 避开同一个ID对应的对象集的情况，且避开storeObjAll.get(j
																					// + 1)与
								if (!(storeObjAll.get(k + 1).containsAll(tmpSetp)
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
		ArrayList<Integer> setToList = new ArrayList<Integer>();
		for (Integer val : tmpSetp) {
			setToList.add(val);
		}
		concep.put(setToList, storeAttrAll.get(i + 1)); // 对象集-属性集
		result.put(numAndFather, concep);

	}
	return result;
}

/*
 * private static HashMap<HashMap<Integer, ArrayList<Integer>>,
 * HashMap<ArrayList<Integer>, ArrayList<String>>> getRelationship(
 * HashMap<ArrayList<String>, ArrayList<Integer>> attributes_Resouces){
 * HashMap<HashMap<Integer, ArrayList<Integer>>, HashMap<ArrayList<Integer>,
 * ArrayList<String>>> result = new HashMap<HashMap<Integer,
 * ArrayList<Integer>>, HashMap<ArrayList<Integer>, ArrayList<String>>>(); //
 * 保存结果集<<节点ID-父节点集>,<对象集list-属性集list>> HashMap<Integer, HashSet<Integer>>
 * storeObjAll = new HashMap<Integer, HashSet<Integer>>(); // 保存所有的对象的列表<ID-对象集>
 * HashMap<Integer, ArrayList<Integer>> storeAttrAll = new HashMap<Integer,
 * ArrayList<Integer>>(); // 保存所有属性的列表 HashSet<Integer> storeObjSet = new
 * HashSet<Integer>(); // 保存对象的集合 int tmpkey = 0; // 对象ID int tmpValNum = 0; //
 * 属性集的ID
 * 
 * return result; }
 */

