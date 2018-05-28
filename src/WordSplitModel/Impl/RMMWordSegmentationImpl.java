package WordSplitModel.Impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import Domain.WeChatMessage;
import WordSplitModel.ChineseTextSegmentation;

/*
 * author:Pompeii
 * time:0503
 * ���ã����ķִ�
 * ���룺HashMap<ID,Message>��һ��΢����Ϣ����Map
 * ���£�������MySQL��InnoDB�洢�����ȫ��������ʹ��StopWord����˼�룬��ĳЩ�ʻ��ֹ�ˣ���������Ϊ�ִʺ�Ľ������
 * ��ɶȣ�100%
 * ���ԣ�ͨ��
 * */
public class RMMWordSegmentationImpl implements ChineseTextSegmentation {

	private String m_sResult = ""; // �зֺ�Ľ����
	private int m_nPosIndex; // �α�ָ��
	private int m_MaxLen; // ���ȡ�ʳ�
	private int totalMaxlen; // �����ȡ�ʳ�
	private Set<String> dictionary; // �ִ��ֵ�
	private Set<String> stopword; // ���ô�

	public RMMWordSegmentationImpl(int maxLen) {
		this.m_MaxLen = maxLen;
		this.totalMaxlen = maxLen;
		try {
			this.dictionary = loadFile();
			this.stopword = getStopWord();
		} catch (IOException ex) {
			Logger.getLogger(RMMWordSegmentationImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public RMMWordSegmentationImpl() {
		this.m_MaxLen = 5;
		this.totalMaxlen = 5;
		try {
			this.dictionary = loadFile();
			this.stopword = getStopWord();
		} catch (IOException ex) {
			Logger.getLogger(RMMWordSegmentationImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	// ��ȡ�����ֵ�
	@SuppressWarnings("resource")
	private Set<String> loadFile() throws IOException {
		Set<String> dictionary = new HashSet<String>();
		String filename = "dictory.txt";
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String tmp;
		while ((tmp = br.readLine()) != null) {
			String[] token = tmp.split(",");
			String word = token[0];
			dictionary.add(word);
		}
		return dictionary;
	}

	// ��ȡ�����ֵ�
	@SuppressWarnings("resource")
	private Set<String> getStopWord() throws IOException {
		Set<String> dictionary = new HashSet<String>();
		String filename = "stopword.txt";
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String tmp;
		while ((tmp = br.readLine()) != null) {
			String[] token = tmp.split(",");
			String word = token[0];
			dictionary.add(word);
		}
		return dictionary;
	}

	//�з��ַ���
	@SuppressWarnings("unused")
	private List<String> RMMSegment(String source) {
		int len = totalMaxlen;
		this.m_nPosIndex = source.length();
		int frompos = this.m_nPosIndex;
		rmm(source, m_MaxLen, m_nPosIndex);

		// �������˳�����
		String[] token = m_sResult.split("/");
		List<String> result = new ArrayList<String>();

		for (int i = token.length - 1; i > 0; i--) {
			result.add(token[i]);
		}

		m_sResult = "";
		return result;
	}

	//��ȡ�ַ���β�˵����ַ���
	private String getSubString(String source, int m_nPosIndex, int len) {

		int startIndex = m_nPosIndex - len;
		while (startIndex < 0) {
			startIndex += 1;
		}
		String sub = source.substring(startIndex, m_nPosIndex);
		return sub;
	}

	//���ֵ���ƥ��
	private void rmm(String source, int len, int frompos) {
		if (m_nPosIndex < 0)
			return;
		String sub = getSubString(source, m_nPosIndex, len);//��ȡ�ַ���β�˵����ַ���
		if (dictionary.contains(sub)) {//dictionary�ֵ����Ƿ����sub�ַ���
			// ƥ��ɹ�
			//m_nPosIndex:�α�ָ��λ��
			//m_MaxLen:���ȡ�ʳ�
			//totalMaxlen:�����ȡ�ʳ�
			m_sResult += "/" + sub;
			m_nPosIndex = m_nPosIndex - m_MaxLen;
			m_MaxLen = totalMaxlen;
			rmm(source, m_MaxLen, m_nPosIndex);//�ݹ�ƥ��
		} else {
			// ��ƥ��
			if (m_MaxLen > 1) {
				m_MaxLen = m_MaxLen - 1;
				rmm(source, m_MaxLen, m_nPosIndex);
			} else {
				// �ֵ���û��sub��;
				m_sResult += "/" + sub;
				m_nPosIndex -= 1;
				m_MaxLen = totalMaxlen;
				rmm(source, m_MaxLen, m_nPosIndex);
			}
		}
	}

	// �ִ��Ժ��ÿ����Ϣ����Ϸִʣ������޳���stopword�еĴ�
	@Override
	public List<String> getWordSegmentation(HashMap<Integer, 
			WeChatMessage> messageMap) {
		List<String> resultList = new ArrayList<String>();
		String str = "";
		for (Entry<Integer, WeChatMessage> entry : messageMap.entrySet()) {
			str = str + "��" + entry.getValue().getMessageText();
			entry.getValue().setMessageParticipleList(
					RMMSegment(entry.getValue().getMessageText()));
			//���÷ִʺ�������ÿ����Ϣ���зִ�
		}
		List<String> tmpList = RMMSegment(str);
		for (int index = 0; index < tmpList.size(); index++) {
			if (stopword.contains(tmpList.get(index))) {
				//���stopword�ֵ��а����ô������ô�
			} else {
				resultList.add(tmpList.get(index));
			}
		}
		return resultList;
	}
}
