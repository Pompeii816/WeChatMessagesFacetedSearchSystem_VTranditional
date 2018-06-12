package FacetTermExtractorModel.Impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import DataProcessModel.DataPreprocessor;
import DataProcessModel.Impl.DataProprecessorImpl;
import Domain.WeChatMessage;
import FacetTermExtractorModel.FacetTermExtractor;
import WordSplitModel.ChineseTextSegmentation;
import WordSplitModel.Impl.RMMWordSegmentationImpl;

public class testForExatra {
	public static void main(String[] args) {
		DataPreprocessor dataPreprocessor = new DataProprecessorImpl();
		HashMap<Integer, WeChatMessage> messageMap = new HashMap<Integer,WeChatMessage>();
		messageMap = dataPreprocessor.getAllWeChatMessages();
		//System.out.println(messageMap);
		ChineseTextSegmentation cts = new RMMWordSegmentationImpl();
		List<String> list = cts.getWordSegmentation(messageMap);
		System.out.println("΢��Ⱥ��Ϣ��");
		for(Entry<Integer, WeChatMessage> entry: messageMap.entrySet()) {
			System.out.print(entry.getKey());
			System.out.println(entry.getValue());
		}
		System.out.println();
		System.out.println("���ķִʺ�Ľ��");
		for(String element:list) {
			System.out.println(element);
		}
		
		FacetTermExtractor facetTermExtractor = new BasedOnFrequencyFacetTermExtractorImpl();
		HashMap<String, Float> resultMap = facetTermExtractor.getFacetTerms(list);
		System.out.println("���������Լ�Ƶ�ʣ�");
		for(Entry<String, Float> entry:resultMap.entrySet()) {
			System.out.print(entry.getKey());
			System.out.println( entry.getValue());
		}
		System.out.println();
		HashMap<String, HashSet<Integer>> FacetTermOnDocIDs = 
				facetTermExtractor.getFacetTermOnDocIDs(list, messageMap);
		System.out.println("���������Լ���Ӧ���ĵ�ID��");
		for(Entry<String, HashSet<Integer>> entry:FacetTermOnDocIDs.entrySet()) {
			System.out.print(entry.getKey());
			System.out.print(" " + entry.getValue());
		}
		System.out.println();
		HashMap<Integer, HashSet<String>> DocIDOnFacets = 
				facetTermExtractor.getDocIDOnFacets(list, messageMap);
		System.out.println("�ĵ�ID�Լ���Ӧ�ķ������");
		for(Entry<Integer, HashSet<String>> entry:DocIDOnFacets.entrySet()) {
			System.out.print(entry.getKey());
			System.out.print(" " + entry.getValue());
		}
		System.out.println();
	}
}
