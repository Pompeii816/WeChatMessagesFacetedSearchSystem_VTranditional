package Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import Domain.ConceptLatticeGrid;
import Domain.SearchStatus;
import Domain.WeChatMessage;
import FacetTermExtractorModel.FacetTermExtractor;
import FacetTermExtractorModel.Impl.BasedOnFrequencyFacetTermExtractorImpl;
import NavigationModel.HierarchicalRelationshipTool;
import NavigationModel.Impl.HierarchicalRelationshipToolImpl;
import WordSplitModel.ChineseTextSegmentation;
import WordSplitModel.Impl.RMMWordSegmentationImpl;
import DataProcessModel.DataPreprocessor;
import DataProcessModel.Impl.DataProprecessorImpl;

public class client {
	
	private static HashMap<Integer,WeChatMessage> messageMap;   					//��Ŷ���Map�����ݽṹ
	private static ArrayList<String> segmentations;									//��ŷִʺ��ArrayList
	private static HashMap<String,Float> facetTerms;								//��ŷ�������
	private static HashMap<String,HashSet<Integer>> facetTermOnDocIDs;				//���<����-ID>��һ��HashMap
	private static HashMap<Integer,HashSet<String>> docIDOnFacets;					//���<�ĵ�ID-����>��һ��HashMap
	private static ArrayList<ArrayList<ConceptLatticeGrid>> conceptLatticeGrid; 	//��������
	private static SearchStatus searchStatus;
	
	public static void main(String[] args) {
		
		DataPreprocessor dataPreprocessor = new DataProprecessorImpl();
		messageMap = dataPreprocessor.getAllWeChatMessages();
		
		ChineseTextSegmentation textSegmentation = new RMMWordSegmentationImpl();
		segmentations = (ArrayList<String>) textSegmentation.getWordSegmentation(messageMap);
		
		FacetTermExtractor facetTermExtractor = new BasedOnFrequencyFacetTermExtractorImpl();
		facetTerms = facetTermExtractor.getFacetTerms(segmentations);
		facetTermOnDocIDs = facetTermExtractor.getFacetTermOnDocIDs(segmentations,messageMap);
		docIDOnFacets = facetTermExtractor.getDocIDOnFacets(segmentations,messageMap);
		
		HierarchicalRelationshipTool hierarchicalRelationshipToolImpl = new HierarchicalRelationshipToolImpl();
		conceptLatticeGrid = hierarchicalRelationshipToolImpl.getHierarchicalRelationship(messageMap, facetTermOnDocIDs, docIDOnFacets);
		
		ArrayList<WeChatMessage> weChatMessageList = new ArrayList<WeChatMessage>();
		Iterator<Entry<Integer, WeChatMessage>> iter = messageMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<Integer, WeChatMessage> entry = iter.next();
			weChatMessageList.add(entry.getValue());
		}
		System.out.println("����Ǳ��裡");
	}
}
