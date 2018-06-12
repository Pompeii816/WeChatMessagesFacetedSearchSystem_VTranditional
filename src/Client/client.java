package Client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import Domain.ConceptLatticeGrid;
import Domain.WeChatMessage;
import FacetResultRankingModel.FacetResultRankingTool;
import FacetResultRankingModel.Impl.FacetResultRankingToolImplByFrequency;
import FacetTermExtractorModel.FacetTermExtractor;
import FacetTermExtractorModel.Impl.BasedOnFrequencyFacetTermExtractorImpl;
import NavigationModel.HierarchicalRelationshipTool;
import NavigationModel.Impl.Navigator;
import SearchModel.SearchTool;
import SearchModel.Impl.SearchToolImpl;
import WordSplitModel.ChineseTextSegmentation;
import WordSplitModel.Impl.RMMWordSegmentationImpl;
import DataProcessModel.DataPreprocessor;
import DataProcessModel.Impl.DataProprecessorImpl;

public class client extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static HashMap<Integer,WeChatMessage> messageMap;   					//存放对象Map的数据结构
	private static ArrayList<String> segmentations;									//存放分词后的ArrayList
	private static HashMap<String,Float> facetTerms;								//存放分面术语
	private static HashMap<String,HashSet<Integer>> facetTermOnDocIDs;				//存放<术语-ID>的一个HashMap
	private static HashMap<Integer,HashSet<String>> docIDOnFacets;					//存放<文档ID-术语>的一个HashMap
	private static ArrayList<ConceptLatticeGrid> conceptLatticeGrid; 	//保存概念格
	private static SearchTool search;
	private static FacetResultRankingTool facetResultRankingTool;
	private JButton SearchButton;
    private JTree FacetTermsTree;
    private JTree NavigationTree;
    private JPanel NavigationPanel;
    private JPanel SearchPanel;
    private JPanel FacetedSearchSystem;
    private JPanel FacetTermsPanel;
    //private JPanel ResultPanel;
    private JTextArea FacetedSearchResult;
    private JTextArea NavigationArea;
    private JTextArea FacetTermsArea;
	
    public client(ArrayList<ConceptLatticeGrid> conceptLatticeGrid,HashMap<String,Float> facetTerms) throws HeadlessException {
		NavigationPanel = new JPanel();
		SearchPanel = new JPanel();
		FacetedSearchSystem = new JPanel();
		FacetTermsPanel = new JPanel();
		//ResultPanel = new JPanel();
		
		NavigationTree = new JTree(createNavigator(conceptLatticeGrid));
		FacetTermsTree = new JTree(createNodes());
		
		//ResultTable = new JTable(5,2);
		
		SearchButton = new JButton();
		SearchButton.setText("Search");
		
		SearchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> searList = new ArrayList<>();
				String[] strs = NavigationArea.getText().split("\n");
				for(String element:strs) {
					String[] s = element.split(",");
					for(String str:s) {
						searList.add(str);
					}
				}
				List<WeChatMessage> messageList = searchMessage(searList,search);
				messageList = facetResultRankingTool.rankingFacetResult(messageList, searList);
				FacetedSearchResult.setText("");
				for(WeChatMessage element:messageList) {
					Long timestamp = Long.parseLong(element.getSendingTime());  
				    String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp)); 
					FacetedSearchResult.append("信息发送者： " + element.getSenderID() + "\n");
					FacetedSearchResult.append("信息发送时间： " + date + "\n");
					FacetedSearchResult.append("信息内容： " + "\n" + element.getMessageText() + "\n");
				}
				FacetTermsArea.setText("");
				for(ConceptLatticeGrid element:conceptLatticeGrid) {
					if(element.getQuest().containsAll(searList) && !element.getQuest().equals(searList)) {
						for(String el:element.getQuest()) {
							if(element.getQuest().indexOf(el) == (element.getQuest().size() - 1)) {
								FacetTermsArea.append(el + "\n");
							}else {
								FacetTermsArea.append(el + ",");
							}
						}
					}
				}
				for(ConceptLatticeGrid element:conceptLatticeGrid) {
					if(searList.containsAll(element.getQuest()) && !element.getQuest().equals(searList)) {
						for(String el:element.getQuest()) {
							if(element.getQuest().indexOf(el) == (element.getQuest().size() - 1)) {
								FacetTermsArea.append(el + "\n");
							}else {
								FacetTermsArea.append(el + ",");
							}
						}
					}
				}
			}
			
		});
		//TermsList = new JTextField(12);
		
		FacetedSearchResult = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(FacetedSearchResult);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(200, 275));
		
		NavigationArea = new JTextArea();
		JScrollPane ScrollPaneOfNavigationArea = new JScrollPane(NavigationArea);
		ScrollPaneOfNavigationArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		ScrollPaneOfNavigationArea.setPreferredSize(new Dimension(200, 275));
		
		FacetTermsArea = new JTextArea();
		JScrollPane scrollPaneOfFacetTerms = new JScrollPane(FacetTermsArea);
		scrollPaneOfFacetTerms.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneOfFacetTerms.setPreferredSize(new Dimension(200, 275));
		
		JScrollPane scrollPaneOfFacetTermsTree = new JScrollPane(FacetTermsTree);
		scrollPaneOfFacetTermsTree.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneOfFacetTermsTree.setPreferredSize(new Dimension(200, 190));
		
		JScrollPane scrollPaneOfNavigationTree = new JScrollPane(NavigationTree);
		scrollPaneOfNavigationTree.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//scrollPaneOfNavigationTree.setPreferredSize(new Dimension(200, 190));
		
		NavigationTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				//NavigationArea.append("222" + "\n");
			}
		});
		
		FacetTermsTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				//NavigationArea.append("222" + "\n");
			}
		});
		
		this.setLayout(new GridLayout(1,3));
		NavigationPanel.setLayout(new GridLayout(1,1));
		FacetTermsPanel.setLayout(new BorderLayout());
		FacetedSearchSystem.setLayout(new BorderLayout());
		SearchPanel.setLayout(new FlowLayout());
		//ResultPanel.setLayout(new BorderLayout());
		
		FacetTermsPanel.add(scrollPaneOfFacetTermsTree,"North");
		FacetTermsPanel.add(scrollPaneOfFacetTerms, "South");
		NavigationPanel.add(scrollPaneOfNavigationTree);
		//SearchPanel.add(TermsList);
		SearchPanel.add(SearchButton);
		FacetedSearchSystem.add(SearchPanel,"North");
		FacetedSearchSystem.add(ScrollPaneOfNavigationArea, "Center");
		FacetedSearchSystem.add(scrollPane,"South");
		
		this.add(FacetTermsPanel);
		this.add(FacetedSearchSystem);
		this.add(NavigationPanel);
		
		this.setTitle("基于微信群消息分面检索系统");// 窗体标签
		this.setSize(800, 500);// 窗体大小
		this.setLocationRelativeTo(null);// 在屏幕中间显示(居中显示)
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 退出关闭JFrame
		this.setVisible(true);// 显示窗体
	}
    
	private TreeNode createNodes() {
		DefaultMutableTreeNode root;
		DefaultMutableTreeNode grandparent;
		DefaultMutableTreeNode parent;
		DefaultMutableTreeNode child;
		
		root = new DefaultMutableTreeNode("分面术语");
		
		grandparent = new DefaultMutableTreeNode("NBA");
		root.add(grandparent);
		parent = new DefaultMutableTreeNode("东部");
		grandparent.add(parent);
		child = new DefaultMutableTreeNode("骑士");
		parent.add(child);
		child = new DefaultMutableTreeNode("绿军");
		parent.add(child);
		parent = new DefaultMutableTreeNode("西部");
		grandparent.add(parent);
		child = new DefaultMutableTreeNode("勇士");
		parent.add(child);
		child = new DefaultMutableTreeNode("火箭");
		parent.add(child);
		parent = new DefaultMutableTreeNode("常规赛");
		grandparent.add(parent);
		parent = new DefaultMutableTreeNode("季后赛");
		grandparent.add(parent);
		
		grandparent = new DefaultMutableTreeNode("世界杯");
		root.add(grandparent);
		
		return root;
	}
	
	private List<WeChatMessage> searchMessage(
			ArrayList<String> facetValues,
			SearchTool search){
		List<WeChatMessage> result = new ArrayList<>();
		result = search.searchMessageByFacet(facetValues, messageMap, docIDOnFacets);
		return result;
	}
	
	private TreeNode createNavigator(ArrayList<ConceptLatticeGrid> conceptLatticeGrid) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("#");
		//ArrayList<DefaultMutableTreeNode> list = new ArrayList<>();
		
		for(int index = 1; index <= 10; index++) {
			for(ConceptLatticeGrid element:conceptLatticeGrid) {
				if(element.getQuest() != null) {
					if(element.getQuest().size() == index) {
						if(element.getQuest().contains("#")) {
							
						}else {
							DefaultMutableTreeNode tmpNode = new DefaultMutableTreeNode(element);
							root.add(tmpNode);
						}
						
					}
				}
			}
		}
		
		return root;
	}
	
	public static void main(String[] args) {
		
		DataPreprocessor dataPreprocessor = new DataProprecessorImpl();
		messageMap = dataPreprocessor.getAllWeChatMessages();
		
		ChineseTextSegmentation textSegmentation = new RMMWordSegmentationImpl();
		segmentations = (ArrayList<String>) textSegmentation.getWordSegmentation(messageMap);
		
		FacetTermExtractor facetTermExtractor = new BasedOnFrequencyFacetTermExtractorImpl();
		facetTerms = facetTermExtractor.getFacetTerms(segmentations);
		for(String element:facetTerms.keySet()) {
			System.out.println(element);
		}
		//System.out.println(facetTerms);
		facetTermOnDocIDs = facetTermExtractor.getFacetTermOnDocIDs(segmentations,messageMap);
		docIDOnFacets = facetTermExtractor.getDocIDOnFacets(segmentations,messageMap);
		
		HierarchicalRelationshipTool hierarchicalRelationshipToolImpl = new Navigator();
		conceptLatticeGrid = hierarchicalRelationshipToolImpl.getHierarchicalRelationship(messageMap, facetTermOnDocIDs, docIDOnFacets);
		
		HashMap<ConceptLatticeGrid,ArrayList<ConceptLatticeGrid>> childConceptLatticeGridMap = 
				new HashMap<ConceptLatticeGrid,ArrayList<ConceptLatticeGrid>>();
		
		for(ConceptLatticeGrid element:conceptLatticeGrid) {
			ArrayList<Integer> list = element.getFatherGrid();
			ArrayList<ConceptLatticeGrid> tmpList = new ArrayList<ConceptLatticeGrid>();
			for(Integer I:list) {
				tmpList.add(conceptLatticeGrid.get(I));
			}
			childConceptLatticeGridMap.put(element, tmpList);
		}
		
		facetResultRankingTool = new FacetResultRankingToolImplByFrequency();
		/*
		System.out.println(childConceptLatticeGridMap.size());
		for(Entry<ConceptLatticeGrid,ArrayList<ConceptLatticeGrid>> entry:childConceptLatticeGridMap.entrySet()) {
			System.out.println(entry.getKey().getID());
			for(ConceptLatticeGrid element:entry.getValue()) {
				System.out.print(element.getID() + " ");
			}
			System.out.println();
		}
		*/		
		
		search = new SearchToolImpl();
		
		@SuppressWarnings("unused")
		client c = new client(conceptLatticeGrid, facetTerms);
		
	}
}
