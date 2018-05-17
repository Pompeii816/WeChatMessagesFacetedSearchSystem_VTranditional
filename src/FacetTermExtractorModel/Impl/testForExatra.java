package FacetTermExtractorModel.Impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import DataProcessModel.DataPreprocessor;
import DataProcessModel.Impl.DataProprecessorImpl;
import Domain.WeChatMessage;
import FacetTermExtractorModel.FacetTermExtractor;
import WordSplitModel.ChineseTextSegmentation;
import WordSplitModel.Impl.RMMWordSegmentationImpl;

public class testForExatra {
	public static void main(String[] args) {
		DataPreprocessor dataPreprocessor = new DataProprecessorImpl();
		HashMap<Integer,WeChatMessage> messageMap = new HashMap<Integer,WeChatMessage>();
		messageMap = dataPreprocessor.getAllWeChatMessages();
		System.out.println(messageMap);
		ChineseTextSegmentation cts = new RMMWordSegmentationImpl();
		List<String> list = cts.getWordSegmentation(messageMap);
		System.out.println(messageMap);
		for(String element:list) {
			System.out.println(element);
		}
		
		FacetTermExtractor facetTermExtractor = new BasedOnFrequencyFacetTermExtractorImpl();
		HashMap<String, Float> resultMap = facetTermExtractor.getFacetTerms(list);
		System.out.println(resultMap);
		HashMap<String, HashSet<Integer>> FacetTermOnDocIDs = 
				facetTermExtractor.getFacetTermOnDocIDs(list, messageMap);
		System.out.println(FacetTermOnDocIDs);
		HashMap<Integer, HashSet<String>> DocIDOnFacets = 
				facetTermExtractor.getDocIDOnFacets(list, messageMap);
		System.out.println(DocIDOnFacets);
	}
}
