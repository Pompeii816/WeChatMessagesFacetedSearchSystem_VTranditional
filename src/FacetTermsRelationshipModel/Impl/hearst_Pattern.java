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
				"I have a <np>car</np> such as <np>BMW</np>, <np>Audi</np> or <np>Mercedes</np> and this can drive fast.");

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
		for(String element:patternList) {
			System.out.println(element);
		}
		
		System.out.println("��������");
		for(String element:matcherList) {
			System.out.println(element);
		}
	}
}
