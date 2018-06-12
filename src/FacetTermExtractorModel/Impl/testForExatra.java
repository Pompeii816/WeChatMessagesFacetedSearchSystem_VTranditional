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
		System.out.println("微信群消息：");
		for(Entry<Integer, WeChatMessage> entry: messageMap.entrySet()) {
			System.out.print(entry.getKey());
			System.out.println(entry.getValue());
		}
		System.out.println();
		System.out.println("中文分词后的结果");
		for(String element:list) {
			System.out.println(element);
		}
		
		FacetTermExtractor facetTermExtractor = new BasedOnFrequencyFacetTermExtractorImpl();
		HashMap<String, Float> resultMap = facetTermExtractor.getFacetTerms(list);
		System.out.println("分面术语以及频率：");
		for(Entry<String, Float> entry:resultMap.entrySet()) {
			System.out.print(entry.getKey());
			System.out.println( entry.getValue());
		}
		System.out.println();
		HashMap<String, HashSet<Integer>> FacetTermOnDocIDs = 
				facetTermExtractor.getFacetTermOnDocIDs(list, messageMap);
		System.out.println("分面术语以及对应的文档ID：");
		for(Entry<String, HashSet<Integer>> entry:FacetTermOnDocIDs.entrySet()) {
			System.out.print(entry.getKey());
			System.out.print(" " + entry.getValue());
		}
		System.out.println();
		HashMap<Integer, HashSet<String>> DocIDOnFacets = 
				facetTermExtractor.getDocIDOnFacets(list, messageMap);
		System.out.println("文档ID以及对应的分面术语：");
		for(Entry<Integer, HashSet<String>> entry:DocIDOnFacets.entrySet()) {
			System.out.print(entry.getKey());
			System.out.print(" " + entry.getValue());
		}
		System.out.println();
	}
}
