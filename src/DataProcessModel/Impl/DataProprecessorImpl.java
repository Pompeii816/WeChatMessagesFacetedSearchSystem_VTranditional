package DataProcessModel.Impl;

import java.util.HashMap;
import DataProcessModel.DBUtil;
import DataProcessModel.DataPreprocessor;
import Domain.WeChatMessage;
import java.sql.*;

/*
 * author:Pompeii
 * time:0502
 * ���ã���ȡ���ݣ���������д��WeChatMessage����ȥ
 * ���룺��
 * �����һ����ID-������Ϣ���󡱵�HashMap
 * ��ɶȣ�90%����Ҫ��ʱ�����Ϊ��׼ʱ�䣬���Ǽ����ʱ��
 * �Ƿ���ԣ���ͨ������
 * */
public class DataProprecessorImpl implements DataPreprocessor{
	private final static String GET_DATA = "select talker as talker, content as content, createtime from Message where type = '1'";
	
	@Override
	public HashMap<Integer,WeChatMessage> getAllWeChatMessages() {
		HashMap<Integer,WeChatMessage> weChatMessageMap = new HashMap<Integer,WeChatMessage>();
		int index = 1;
		//��ȡ��ʱ�����ʱ���Զ��������ã��ٽ�ÿ��message��һ��ID���ٽ����ID�������ص�hashmap��keyֵ
		try {
			Connection conn = DBUtil.getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(GET_DATA);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				WeChatMessage weChatMessage = new WeChatMessage();
				
				weChatMessage.setID(index);
				weChatMessage.setSenderID(resultSet.getString("talker"));
				weChatMessage.setSendingTime(resultSet.getString("createtime"));
				weChatMessage.setMessageText(resultSet.getString("content"));
				weChatMessage.setMessageType(1);
				
				weChatMessageMap.put(index, weChatMessage);
				index++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return weChatMessageMap;
	}
}
