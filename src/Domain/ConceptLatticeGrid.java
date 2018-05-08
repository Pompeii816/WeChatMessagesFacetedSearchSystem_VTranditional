package Domain;

import java.util.ArrayList;
import java.util.HashSet;

public class ConceptLatticeGrid {
	private int ID;
	private HashSet<String> quest;
	private HashSet<Integer> resourcesIds; 
	private ArrayList<ConceptLatticeGrid> fatherGrid;
	private ArrayList<ConceptLatticeGrid> childGrid;
	
	public ConceptLatticeGrid(int ID,
			HashSet<String> facetTermsName, HashSet<Integer> resourcesIds,
			ArrayList<ConceptLatticeGrid> childGrid,
			ArrayList<ConceptLatticeGrid> fatherGrid) {
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

	public HashSet<String> getQuest() {
		return quest;
	}

	public void setQuest(HashSet<String> facetTermsName) {
		this.quest = facetTermsName;
	}

	public HashSet<Integer> getResourcesIds() {
		return resourcesIds;
	}

	public void setResourcesIds(HashSet<Integer> resourcesIds) {
		this.resourcesIds = resourcesIds;
	}

	public ArrayList<ConceptLatticeGrid> getChildGrid() {
		return childGrid;
	}

	public void setChildGrid(ArrayList<ConceptLatticeGrid> childGrid) {
		this.childGrid = childGrid;
	}

	public ArrayList<ConceptLatticeGrid> getFatherGrid() {
		return fatherGrid;
	}

	public void setFatherGrid(ArrayList<ConceptLatticeGrid> fatherGrid) {
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
		return "ConceptLatticeGrid [ID=" + ID + ", quest=" + quest + ", resourcesIds=" + resourcesIds + ", fatherGrid="
				+ fatherGrid + ", childGrid=" + childGrid + "]";
	}
	
}
