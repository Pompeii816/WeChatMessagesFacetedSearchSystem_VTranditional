package WordSplitModel;

import java.util.HashMap;
import java.util.List;
import Domain.WeChatMessage;

public interface ChineseTextSegmentation {
	public List<String> getWordSegmentation(HashMap<Integer,WeChatMessage> messageMap);
}
