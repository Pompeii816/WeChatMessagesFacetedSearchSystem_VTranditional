package Domain;

import java.util.HashMap;

/**
 * @author Pompeii
 * ΢����Ϣ��ʵ����
 *
 */
public class WeChatMessage {
	private int ID;										  //����Ϣ��ID��ϵͳ���ݶ�ȡ������˳���һ��Ψһֵ
	private String SenderID;							  //������ID���ǳ�
	private String SendingTime;							  //���͵�ʱ��
	private String MessageText;							  //��Ϣ���ı�
	private int MessageType;							  //Ŀǰֻʹ���ı���������������չ
	private HashMap<String,Integer> MessageParticiple;    //�ִʺͷִʵĸ÷ִʵ�����
	
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