package DataStructuress;

import com.google.gson.JsonObject;

public class Node {
    private Node left;
    private Node right;
    private JsonObject data;
    private int height;
    private int key;
    
    public Node(JsonObject data){
        this.data = data;
        left = null;
        right = null;
    }
    public Node(int key, JsonObject data) {
    	this.data = data;
    	this.key = key;
        left = null;
        right = null;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public JsonObject getData() {
        return data;
    }
    

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

	public int getHeight() {
		return height;
	}

	public void setHeight(int i) {
		height = i;
	}

	public void setData(JsonObject data) {
		this.data = data;
	}
}

