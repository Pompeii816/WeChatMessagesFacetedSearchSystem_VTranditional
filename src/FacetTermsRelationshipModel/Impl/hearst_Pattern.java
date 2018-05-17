package FacetTermsRelationshipModel.Impl;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
 * 给抽取出来的术语加上<np></np>这一标签，再进行层次关系抽取
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
		
		System.out.println("父类术语");
		for(String element:patternList) {
			System.out.println(element);
		}
		
		System.out.println("子类术语");
		for(String element:matcherList) {
			System.out.println(element);
		}
	}
}
