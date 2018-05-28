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
 * 作用：中文分词
 * 输入：HashMap<ID,Message>的一个微信消息对象Map
 * 创新：借用了MySQL的InnoDB存储引擎的全文索引的使用StopWord表格的思想，将某些词汇禁止了，不让其作为分词后的结果出现
 * 完成度：100%
 * 测试：通过
 * */
public class RMMWordSegmentationImpl implements ChineseTextSegmentation {

	private String m_sResult = ""; // 切分后的结果串
	private int m_nPosIndex; // 游标指针
	private int m_MaxLen; // 最大取词长
	private int totalMaxlen; // 总最大取词长
	private Set<String> dictionary; // 分词字典
	private Set<String> stopword; // 禁用词

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

	// 获取中文字典
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

	// 获取禁用字典
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

	//切分字符串
	@SuppressWarnings("unused")
	private List<String> RMMSegment(String source) {
		int len = totalMaxlen;
		this.m_nPosIndex = source.length();
		int frompos = this.m_nPosIndex;
		rmm(source, m_MaxLen, m_nPosIndex);

		// 将结果按顺序输出
		String[] token = m_sResult.split("/");
		List<String> result = new ArrayList<String>();

		for (int i = token.length - 1; i > 0; i--) {
			result.add(token[i]);
		}

		m_sResult = "";
		return result;
	}

	//获取字符串尾端的子字符串
	private String getSubString(String source, int m_nPosIndex, int len) {

		int startIndex = m_nPosIndex - len;
		while (startIndex < 0) {
			startIndex += 1;
		}
		String sub = source.substring(startIndex, m_nPosIndex);
		return sub;
	}

	//从字典中匹配
	private void rmm(String source, int len, int frompos) {
		if (m_nPosIndex < 0)
			return;
		String sub = getSubString(source, m_nPosIndex, len);//获取字符串尾端的子字符串
		if (dictionary.contains(sub)) {//dictionary字典中是否包含sub字符串
			// 匹配成功
			//m_nPosIndex:游标指针位置
			//m_MaxLen:最大取词长
			//totalMaxlen:总最大取词长
			m_sResult += "/" + sub;
			m_nPosIndex = m_nPosIndex - m_MaxLen;
			m_MaxLen = totalMaxlen;
			rmm(source, m_MaxLen, m_nPosIndex);//递归匹配
		} else {
			// 不匹配
			if (m_MaxLen > 1) {
				m_MaxLen = m_MaxLen - 1;
				rmm(source, m_MaxLen, m_nPosIndex);
			} else {
				// 字典中没有sub词;
				m_sResult += "/" + sub;
				m_nPosIndex -= 1;
				m_MaxLen = totalMaxlen;
				rmm(source, m_MaxLen, m_nPosIndex);
			}
		}
	}

	// 分词以后给每个消息添加上分词，并且剔除在stopword中的词
	@Override
	public List<String> getWordSegmentation(HashMap<Integer, 
			WeChatMessage> messageMap) {
		List<String> resultList = new ArrayList<String>();
		String str = "";
		for (Entry<Integer, WeChatMessage> entry : messageMap.entrySet()) {
			str = str + "，" + entry.getValue().getMessageText();
			entry.getValue().setMessageParticipleList(
					RMMSegment(entry.getValue().getMessageText()));
			//调用分词函数，对每个消息进行分词
		}
		List<String> tmpList = RMMSegment(str);
		for (int index = 0; index < tmpList.size(); index++) {
			if (stopword.contains(tmpList.get(index))) {
				//如果stopword字典中包含该词则丢弃该词
			} else {
				resultList.add(tmpList.get(index));
			}
		}
		return resultList;
	}
}
