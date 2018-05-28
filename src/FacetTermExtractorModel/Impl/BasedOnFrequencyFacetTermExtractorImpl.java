package FacetTermExtractorModel.Impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import Domain.WeChatMessage;
import FacetTermExtractorModel.FacetTermExtractor;

/*
 * author：Pompeii
 * time:0504
 * 作用：获取分面术语，以及分面术语的词频HashMap<String, Float>
 * 输入：List<String> segmentations
 * 输出：
 * 完成度：100%
 * 是否已测试：已通过测试
 * */

public class BasedOnFrequencyFacetTermExtractorImpl implements FacetTermExtractor{

	private static HashMap<String, Float> getTerms(List<String> segmentations) {
		
		HashMap<String, Float> tmpMap = new HashMap<String, Float>();
		int allSegmentationsCount = segmentations.size();
		
		for(String element:segmentations) {
			if(tmpMap.containsKey(element)) {
				float tmp = tmpMap.get(element) + 1;
				tmpMap.replace(element, tmp);
			}else {
				tmpMap.put(element, (float) 1);
			}
		}
		
		Iterator<Entry<String,Float>> iter = tmpMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String,Float> entry = iter.next();
			if(allSegmentationsCount != 0) {
				tmpMap.replace(entry.getKey(), entry.getValue() / allSegmentationsCount);
			}else {
				tmpMap.replace(entry.getKey(), (float) 0);
			}
		}
		
		HashMap<String,Float> resultMap = new HashMap<String,Float>();
		
		for(int i = 0; i < segmentations.size() / 5 && i < 10; i++) {
			float maxValue = Float.MIN_VALUE;
			String str = null;
			Iterator<Entry<String,Float>> iterOfTmp = tmpMap.entrySet().iterator();
			while (iterOfTmp.hasNext()) {
				Entry<String,Float> entry = iterOfTmp.next();
				if(entry.getValue() > maxValue) {
					maxValue = entry.getValue();
					str = entry.getKey();
				}
			}
			tmpMap.remove(str);
			resultMap.put(str, maxValue);
		}
		
		return resultMap;
	}
	
	//保留频率是为了之后的分面检索的排序方便
	@Override
	public HashMap<String, Float> getFacetTerms(List<String> segmentations) {
		return getTerms(segmentations);
	}
	
	//该方法写的时候状态比较差，估计会预留bug.
	@Override
	public HashMap<String, HashSet<Integer>> getFacetTermOnDocIDs(
			List<String> segmentations,	HashMap<Integer,WeChatMessage> messageMap) {
		HashMap<String, Float> tmpMap = getTerms(segmentations);
		HashMap<String, HashSet<Integer>> resultMap = new HashMap<String, HashSet<Integer>>();
		
		for(Entry<String, Float> entryOfTmpMap:tmpMap.entrySet()) {
			List<String> tmpList = null;
			HashSet<Integer> tmpSet = new HashSet<Integer>();
			for(Entry<Integer,WeChatMessage> entry:messageMap.entrySet()) {
				tmpList = entry.getValue().getMessageParticipleList();
				if(tmpList.contains(entryOfTmpMap.getKey())) {
					tmpSet.add(entry.getKey());
				}
			}
			resultMap.put(entryOfTmpMap.getKey(), tmpSet);
		}
		
		/*
		Iterator<Entry<String,Float>> iterOfTmp = tmpMap.entrySet().iterator();
		while (iterOfTmp.hasNext()) {
			Entry<String,Float> entryOfTmp = iterOfTmp.next();
			Iterator<Entry<Integer, WeChatMessage>> iterOfMessageMap = 
					messageMap.entrySet().iterator();
			while(iterOfMessageMap.hasNext()) {
				Entry<Integer,WeChatMessage> entryOfMassage = 
						iterOfMessageMap.next();
				if(entryOfMassage.getValue().getMessageParticiple()
						.containsKey(entryOfTmp.getKey())) {
					if(resultMap.containsKey(entryOfTmp.getKey())) {
						HashSet<Integer> tmp1 = resultMap.get(entryOfTmp.getKey());
						tmp1.add(entryOfMassage.getKey());
						resultMap.replace(entryOfTmp.getKey(), resultMap.get(entryOfTmp.getKey()), tmp1);
					}else {
						HashSet<Integer> tmp2 = new HashSet<Integer>();
						tmp2.add(entryOfMassage.getKey());
						resultMap.put(entryOfTmp.getKey(), tmp2);
					}
				}
			}
		}
		*/
		
		return resultMap;
	}

	//写的时候状态很差，需要检查，肯定会预留bug.
	@Override
	public HashMap<Integer, HashSet<String>> getDocIDOnFacets(
			List<String> segmentations,HashMap<Integer,WeChatMessage> messageMap) {
		HashMap<String, Float> tmpMap = getTerms(segmentations);
		HashMap<Integer, HashSet<String>> resultMap = new HashMap<Integer, HashSet<String>>();
		
		
		for(Entry<Integer,WeChatMessage> entryOfMessageMap:messageMap.entrySet()) {
			HashSet<String> tmpSet = new HashSet<String>();
			List<String> tmpList = null;
			tmpList = entryOfMessageMap.getValue().getMessageParticipleList();
			for(Entry<String, Float> entryOfTmpMap:tmpMap.entrySet()) {
				if(tmpList.contains(entryOfTmpMap.getKey())) {
					tmpSet.add(entryOfTmpMap.getKey());
				}
			}
			
			if(tmpSet.size() > 0) {
				resultMap.put(entryOfMessageMap.getKey(), tmpSet);
			}else {
				
			}
		}
		
		/*
		Iterator<Entry<Integer,WeChatMessage>> iterOfMessage = messageMap.entrySet().iterator();
		while(iterOfMessage.hasNext()) {
			Entry<Integer,WeChatMessage> entryOfMessage = iterOfMessage.next();
			Iterator<Entry<String,Float>> iterOfTmpMap = tmpMap.entrySet().iterator();
			
			while(iterOfTmpMap.hasNext()) {
				Entry<String,Float> entryOfTmpMap = iterOfTmpMap.next();
				
				if(entryOfMessage.getValue().getMessageParticiple().
						containsKey(entryOfTmpMap.getKey())) {
					if(resultMap.containsKey(entryOfMessage.getValue().getID())) {
						HashSet<String> tmpSet1 = resultMap.get(entryOfMessage.getValue().getID());
						tmpSet1.add(entryOfTmpMap.getKey());
						resultMap.replace(entryOfMessage.getKey(), resultMap.get(entryOfMessage.getValue().getID()),tmpSet1);
					}else {
						HashSet<String> tmp2 = new HashSet<String>();
						tmp2.add(entryOfTmpMap.getKey());
						resultMap.put(entryOfMessage.getKey(), tmp2);
					}
				}
			}
		}
		*/
		return resultMap;
	}
}
