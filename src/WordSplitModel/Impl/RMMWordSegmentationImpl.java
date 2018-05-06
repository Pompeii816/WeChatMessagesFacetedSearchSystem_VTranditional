package WordSplitModel.Impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import Domain.WeChatMessage;
import WordSplitModel.ChineseTextSegmentation;
/*
 * author:Pompeii
 * time:0503
 * 作用：中文分词
 * 输入：HashMap<ID,Message>的一个微信消息对象Map
 * 创新：借用了MySQL的InnoDB存储引擎的全文索引的使用StopWord表格的思想，将某些词汇禁止了，不让其作为分词后的结果出现
 * 完成度：10%
 * */
public class RMMWordSegmentationImpl implements ChineseTextSegmentation{

	@Override
	public List<String> getWordSegmentation(HashMap<Integer, WeChatMessage> messageMap) {
		Iterator<Entry<Integer, WeChatMessage>> iter = messageMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<Integer, WeChatMessage> entry = iter.next();
			RMM(entry.getValue().getMessageText());
		}
		return null;
	}
	
	private static List<String> RMM(String messageText){
		return null;
	}
}
