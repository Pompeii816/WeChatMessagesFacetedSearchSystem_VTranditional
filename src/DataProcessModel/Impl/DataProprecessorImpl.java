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
 * 完成度：90%，需要将时间更改为标准时间，而非计算机时间
 * 是否测试：已通过测试
 * */
public class DataProprecessorImpl implements DataPreprocessor{
	private final static String GET_DATA = "select talker as talker, content as content, createtime from Message where type = '1'";
	
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
