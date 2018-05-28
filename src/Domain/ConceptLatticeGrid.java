package Domain;

import java.util.ArrayList;

public class ConceptLatticeGrid {
	private int ID;
	private ArrayList<String> quest;
	private ArrayList<Integer> resourcesIds; 
	private ArrayList<Integer> fatherGrid;
	private ArrayList<Integer> childGrid;
	
	public ConceptLatticeGrid(int ID,
			ArrayList<String> facetTermsName, ArrayList<Integer> resourcesIds,
			ArrayList<Integer> childGrid,
			ArrayList<Integer> fatherGrid) {
		super();
		this.ID = ID;
		this.quest = facetTermsName;
		this.resourcesIds = resourcesIds;
		this.childGrid = childGrid;
		this.fatherGrid = fatherGrid;
	}

	public ConceptLatticeGrid() {
		super();
	}

	public ArrayList<String> getQuest() {
		return quest;
	}

	public void setQuest(ArrayList<String> facetTermsName) {
		this.quest = facetTermsName;
	}

	public ArrayList<Integer> getResourcesIds() {
		return resourcesIds;
	}

	public void setResourcesIds(ArrayList<Integer> resourcesIds) {
		this.resourcesIds = resourcesIds;
	}

	public ArrayList<Integer> getChildGrid() {
		return childGrid;
	}

	public void setChildGrid(ArrayList<Integer> childGrid) {
		this.childGrid = childGrid;
	}

	public ArrayList<Integer> getFatherGrid() {
		return fatherGrid;
	}

	public void setFatherGrid(ArrayList<Integer> fatherGrid) {
		this.fatherGrid = fatherGrid;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	@Override
	public String toString() {
		return quest + "";
	}
}
