package FacetResultRankingModel;

import java.util.HashMap;
import Domain.SearchStatus;
import Domain.WeChatMessage;

public interface FacetResultRankingTool {
	public HashMap<WeChatMessage,Float> rankingFacetResult(HashMap<Integer,WeChatMessage> weChatMessage,SearchStatus searchStatus);
}
