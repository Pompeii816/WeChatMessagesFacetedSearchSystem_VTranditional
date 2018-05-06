package DataProcessModel;

import java.util.HashMap;
import Domain.WeChatMessage;

public interface DataPreprocessor {
	public HashMap<Integer,WeChatMessage> getAllWeChatMessages();
}
