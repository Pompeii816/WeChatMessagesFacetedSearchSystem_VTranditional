package DataProcessModel.Impl;

import java.util.HashMap;
import DataProcessModel.DBUtil;
import DataProcessModel.DataPreprocessor;
import Domain.WeChatMessage;
import java.sql.*;
/*
 * author:Pompeii
 * time:0502
 * 作用：读取数据，并将数据写入WeChatMessage类中去
 * 输入：无
 * 输出：一个“ID-聊天信息对象”的HashMap
 * 完成度：90%
 * 是否测试：否 
 * */
public class DataProprecessorImpl implements DataPreprocessor{
	private final static String GET_DATA = "select * from ";
	
	@Override
	public HashMap<Integer,WeChatMessage> getAllWeChatMessages() {
		HashMap<Integer,WeChatMessage> weChatMessageMap = new HashMap<Integer,WeChatMessage>();
		int index = 1;
		//读取的时候根据时间从远到近排序好，再将每个message赋一个ID，再将这个ID当作返回的hashmap的key值
		try {
			Connection conn = DBUtil.getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(GET_DATA);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				WeChatMessage weChatMessage = new WeChatMessage();
				
				weChatMessage.setID(index);
				weChatMessage.setSenderID(resultSet.getString(1));
				weChatMessage.setSendingTime(resultSet.getString(2));
				weChatMessage.setMessageText(resultSet.getString(3));
				weChatMessage.setMessageType(resultSet.getInt(4));
				
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
