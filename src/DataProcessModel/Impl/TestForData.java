package DataProcessModel.Impl;

import java.util.HashMap;

import Domain.WeChatMessage;

public class TestForData {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataProprecessorImpl dataProprecessorImpl = new DataProprecessorImpl();
		HashMap<Integer,WeChatMessage> test = new HashMap<Integer,WeChatMessage>();
		test = dataProprecessorImpl.getAllWeChatMessages();
		System.out.println(test);
	}

}
