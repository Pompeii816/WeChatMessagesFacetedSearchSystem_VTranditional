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
 * ͨ������messageMap��WeChatMessage������segmentations��
 * �Լ�segmentations�е�����֮��Ĺ�ϵ��������
 * ���巽��Ϊ������һ��HashSet����set�������Գ�Ϊ��ϵ�жϵĴʣ�
 * �پ�����������֮�������֮����ӵ�����set����Ĵʻ㼴���ж���������֮��ӵ�в�ι�ϵ��Is A��
 * */
public class FacetTermsRelationshipToolImpl implements FacetTermsRelationshipTool {

	/*
	 * ����Ĳ���Ϊ��������-List<String> segmentations ���еĶ���-HashMap<Integer, WeChatMessage>
	 * messageMap
	 */
	@Override
	public ArrayList<FacetTerms> getFacetTermsRelationship(List<String> segmentations,
			HashMap<Integer, WeChatMessage> messageMap) {
		ArrayList<FacetTerms> reusltList = new ArrayList<FacetTerms>();
		for (String element : segmentations) {
			FacetTerms tmpFacetTerms = new FacetTerms();
			tmpFacetTerms.setFacetTerm(element);
			reusltList.add(tmpFacetTerms);
		}
		try {
			Set<String> set = loadFile();
			for(Entry<Integer, WeChatMessage> entry:messageMap.entrySet()) {
				ArrayList<String> tmpList = new ArrayList<String>();
				for(String element:set) {
					if(entry.getValue().getMessageParticipleList().contains(element)) {
						for(String str:segmentations) {
							if(entry.getValue().getMessageParticipleList().contains(str)) {
								tmpList.add(str);
							}
						}
						if(tmpList.size() > 1) {
							for(String ele:tmpList) {
								if(entry.getValue().getMessageParticipleList().indexOf(element) 
										> entry.getValue().getMessageParticipleList().indexOf(ele)) {
									
								}
							}
						}
					}
				}
			}
		}catch(IOException ex) {
			
		}
		return reusltList;
	}

	// ��ȡ�����ֵ�
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
