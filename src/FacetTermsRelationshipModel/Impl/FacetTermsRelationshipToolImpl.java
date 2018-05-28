package FacetTermsRelationshipModel.Impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import Domain.FacetTerms;
import Domain.WeChatMessage;
import FacetTermsRelationshipModel.FacetTermsRelationshipTool;

/*
 * 通过利用messageMap的WeChatMessage包含的segmentations，
 * 以及segmentations中的术语之间的关系来构建；
 * 具体方法为：构建一个HashSet，该set包含可以成为关系判断的词，
 * 再就是两个逗号之间的术语之间再拥有这个set里面的词汇即可判断两个术语之间拥有层次关系（Is A）
 * */
public class FacetTermsRelationshipToolImpl implements FacetTermsRelationshipTool {

	/*
	 * 传入的参数为分面术语-List<String> segmentations 所有的对象-HashMap<Integer, WeChatMessage>
	 * messageMap
	 */
	@Override
	public ArrayList<FacetTerms> getFacetTermsRelationship(
			List<String> segmentations,
			HashMap<Integer, WeChatMessage> messageMap) {
		ArrayList<FacetTerms> reusltList = new ArrayList<FacetTerms>();
		try {
			Set<String> set = loadFile();
			for(Entry<Integer, WeChatMessage> entry:messageMap.entrySet()) {
				if(entry.getValue().getMessageParticipleList().size() >= 3) {
					boolean flag = true;
					for(int index = 0; index < entry.getValue().getMessageParticipleList().size(); 
							index++) {
						if(segmentations.contains(entry.getValue().getMessageParticipleList().get(index))) {
							for(int i = index + 1; flag && i < entry.getValue().getMessageParticipleList().size();
									i++) {
								if(entry.getValue().getMessageParticipleList().get(i).equals("，") 
										|| entry.getValue().getMessageParticipleList().get(i).equals("。")) {
									flag = false;
								}
								if(set.contains(entry.getValue().getMessageParticipleList().get(i))) {
									for(int j = i + 1; j < entry.getValue().getMessageParticipleList().size(); j++) {
										if(entry.getValue().getMessageParticipleList().get(j).equals("，") 
												|| entry.getValue().getMessageParticipleList().get(j).equals("。")) {
											flag = false;
										}
										if(segmentations.contains(entry.getValue().getMessageParticipleList().get(j))) {
											FacetTerms tmp = 
													new FacetTerms(entry.getValue().getMessageParticipleList().get(j));
											tmp.setFacetTerm(entry.getValue().getMessageParticipleList().get(index));
											reusltList.add(tmp);
											segmentations.remove(entry.getValue().getMessageParticipleList().get(j));
										}
									}
								}
							}
						}
					}
				}
			}
		}catch(IOException ex) {
			
		}
		for (String element : segmentations) {
			FacetTerms tmpFacetTerms = new FacetTerms();
			tmpFacetTerms.setFacetTerm(element);
			reusltList.add(tmpFacetTerms);
		}
		return reusltList;
	}

	// 获取中文字典
	@SuppressWarnings("resource")
	private Set<String> loadFile() throws IOException {
		Set<String> dictionary = new HashSet<String>();
		String filename = "reslationShip.txt";
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String tmp;
		while ((tmp = br.readLine()) != null) {
			String[] token = tmp.split(",");
			String word = token[0];
			dictionary.add(word);
		}
		return dictionary;
	}

}
