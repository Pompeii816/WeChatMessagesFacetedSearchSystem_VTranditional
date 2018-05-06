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
 * ���ã����ķִ�
 * ���룺HashMap<ID,Message>��һ��΢����Ϣ����Map
 * ���£�������MySQL��InnoDB�洢�����ȫ��������ʹ��StopWord����˼�룬��ĳЩ�ʻ��ֹ�ˣ���������Ϊ�ִʺ�Ľ������
 * ��ɶȣ�10%
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
