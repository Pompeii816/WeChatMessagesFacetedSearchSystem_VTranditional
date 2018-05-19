package FacetResultRankingModel;

import java.util.ArrayList;
import java.util.List;

import Domain.WeChatMessage;

public interface FacetResultRankingTool {
	public ArrayList<WeChatMessage> rankingFacetResult(List<WeChatMessage> messageList,List<String> searchList);
}
