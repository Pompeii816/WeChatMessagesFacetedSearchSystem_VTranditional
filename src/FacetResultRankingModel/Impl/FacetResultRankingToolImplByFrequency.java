package FacetResultRankingModel.Impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import Domain.SearchStatus;
import Domain.WeChatMessage;
import FacetResultRankingModel.FacetResultRankingTool;

/*
 * author:Pompeii
 * date:0502
 * 作用：分面检索结果排名
 * 输入：聊天信息的HashMap和当前查询状态
 * 输出：每个“结果-排名值”的HashMap，排名在需要调用的时候再排名
 * 完成度：100%
 * 是否测试：否
 * */
public class FacetResultRankingToolImplByFrequency implements FacetResultRankingTool{

	@Override
	public HashMap<WeChatMessage,Float> rankingFacetResult(HashMap<Integer, WeChatMessage> weChatMessage,SearchStatus searchStatus) {
		HashMap<WeChatMessage,Float> resultMap = new HashMap<WeChatMessage,Float>();
		for(WeChatMessage element:searchStatus.getResource()) {
			HashSet<String> facetTerms = searchStatus.getFacetTermsSet();
			HashMap<String,Integer> MessageParticiple = element.getMessageParticiple();
			int numOfParticiple = 0;
			int numOfFacetTerms = 0;
			float result = 0;
			
			Iterator<Entry<String,Integer>> iter = MessageParticiple.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String,Integer> entry = iter.next();
				numOfParticiple = numOfParticiple + entry.getValue();
			}
			
			for(String ele:facetTerms) {
				numOfFacetTerms = numOfFacetTerms + MessageParticiple.get(ele);
			}
			result = (numOfParticiple == 0 ? 0 : numOfFacetTerms / numOfParticiple);
			
			resultMap.put(element, result);
		}
		return resultMap;
	}
}
