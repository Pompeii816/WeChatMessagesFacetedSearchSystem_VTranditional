package Domain;

import java.util.HashMap;

/**
 * @author Pompeii
 * 微信消息的实体类
 *
 */
public class WeChatMessage {
	private int ID;										  //该信息的ID，系统根据读取进来的顺序给一个唯一值
	private String SenderID;							  //发送者ID或昵称
	private String SendingTime;							  //发送的时间
	private String MessageText;							  //消息的文本
	private int MessageType;							  //目前只使用文本，该类型留作扩展
	private HashMap<String,Integer> MessageParticiple;    //分词和分词的该分词的数量
	
	public WeChatMessage() {
		
	}
	
	public WeChatMessage(int id,String senderId,
			String sendTime,String MessageText,
			int MessageType,HashMap<String,Integer> MessageParticiple) {
		this.ID = id;
		this.SenderID = senderId;
		this.SendingTime = sendTime;
		this.MessageText = MessageText;
		this.MessageType = MessageType;
		this.MessageParticiple = MessageParticiple;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getSenderID() {
		return SenderID;
	}

	public void setSenderID(String senderID) {
		SenderID = senderID;
	}

	public String getSendingTime() {
		return SendingTime;
	}

	public void setSendingTime(String sendingTime) {
		SendingTime = sendingTime;
	}

	public String getMessageText() {
		return MessageText;
	}

	public void setMessageText(String messageText) {
		MessageText = messageText;
	}

	public int getMessageType() {
		return MessageType;
	}

	public void setMessageType(int messageType) {
		MessageType = messageType;
	}

	public HashMap<String,Integer> getMessageParticiple() {
		return MessageParticiple;
	}

	public void setMessageParticiple(HashMap<String,Integer> messageParticiple) {
		MessageParticiple = messageParticiple;
	}

	@Override
	public String toString() {
		return "WeChatMessage [ID=" + ID + ", SenderID=" + SenderID + ", SendingTime=" + SendingTime + ", MessageText="
				+ MessageText + ", MessageType=" + MessageType + ", MessageParticiple=" + MessageParticiple + "]";
	}
}