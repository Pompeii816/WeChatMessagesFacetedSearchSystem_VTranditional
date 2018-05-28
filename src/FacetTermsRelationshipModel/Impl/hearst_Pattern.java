package FacetTermsRelationshipModel.Impl;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * ����ȡ�������������<np></np>��һ��ǩ���ٽ��в�ι�ϵ��ȡ
 * */
public class hearst_Pattern {
	public static void main(String[] args) {
		Pattern p = Pattern.compile("<np>(\\w+)</np> such as|\\G(?:,| or| and)? <np>(\\w+)</np>");
		Matcher m = p.matcher(
				"I have a <np>car</np> such as <np>BMW</np>, "
				+ "<np>Audi</np> or <np>Mercedes</np> "
				+ "and this can drive fast.");

		ArrayList<String> patternList = new ArrayList<String>();
		ArrayList<String> matcherList = new ArrayList<String>();

		while (m.find()) {
			if (m.group(1) != null) {
				patternList.add(m.group(1));
				System.out.println(m.group(1));
			}
			if (m.group(2) != null) {
				matcherList.add(m.group(2));
				System.out.println(m.group(2));
			}
		}

		System.out.println("��������");
		for (String element : patternList) {
			System.out.println(element);
		}

		System.out.println("��������");
		for (String element : matcherList) {
			System.out.println(element);
		}

		String testTxt = "8���ļ�";
		// ע��[\u4E00-\u9FA5]�����б���ַ���ǧ�򲻿�ʡ�ԣ������ִ�Сд
		Pattern pat = Pattern.compile("^(\\d+)[\u4E00-\u9FA5]{3}$");
		Matcher mat = pat.matcher(testTxt);
		if (mat.matches()) {
			System.out.println(mat.group(1));
		}

		// ƥ��˫���ż�����
		String pstr = "\"([^\"]+)\"";
		Pattern p1 = Pattern.compile(pstr);
		Matcher m1 = p1.matcher("\"goodjob\"");
		System.out.println(m1.find() ? m1.group(1) : "nothing");
		// ��������
		m1 = p1.matcher("\"goodjob������������\"");
		System.out.println(m1.find() ? m1.group(1) : "nothing");
	}
}
