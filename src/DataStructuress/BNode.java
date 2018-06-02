package DataStructuress;

import com.google.gson.JsonObject;

public class BNode {

	private BNode[] child;
	private JsonObject[] data;
	private int order;
	private int count;
	private boolean leaf = true;

	public BNode(int order){
		this.order = 2*order;
		this.data = new JsonObject[this.order];
		this.child = new BNode[this.order+1];

	}


	public JsonObject[] getData() {
		return data;
	}

	public JsonObject getData(int i) {
		return data[i];
	}



	public BNode getChild(int i) {
		return child[i];
	}


	public void setChild(int i, BNode child) {
		this.child[i] = child;
	}


	public void setData(JsonObject[] data) {
		this.data = data;
	}

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}


	public void setData(int i, JsonObject data) {
		this.data[i] = data;
	}

}

