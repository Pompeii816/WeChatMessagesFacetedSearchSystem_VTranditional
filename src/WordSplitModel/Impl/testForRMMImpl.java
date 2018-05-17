package WordSplitModel.Impl;

import java.util.HashMap;
import java.util.List;

import DataProcessModel.DataPreprocessor;
import DataProcessModel.Impl.DataProprecessorImpl;
import Domain.WeChatMessage;
import WordSplitModel.ChineseTextSegmentation;

public class testForRMMImpl {
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
	}
}
