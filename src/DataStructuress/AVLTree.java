package DataStructuress;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import DataManage.XMLInterpreter;

public class AVLTree {
    private JsonArray dataBase;
    private JsonObject[] artistNames;
    private Node root;

    

    public void buildTree(JsonObject[] array) throws FileNotFoundException {
    	this.dataBase = XMLInterpreter.loadDataBase();
        artistNames = new JsonObject[this.dataBase.size()];
		for(int i=0; i < array.length; i++) {
			artistNames[i] = (JsonObject) this.dataBase.get(i);
			insert(array[i]);
		}
		
		
	}
    
    private void insert(JsonObject data){
    	root = insert(root, data);
    }
    
    private Node insert(Node node, JsonObject data) {
    	if (node == null) {
    		return new Node(data);
    	}
     
    	if (data.get("Artist").getAsString().compareTo(node.getData().get("Artist").getAsString()) < 0) {
    		node.setLeft(insert(node.getLeft(), data));
    	} else {
    		node.setRight(insert(node.getRight(), data));
    	}
     
    	node.setHeight(Math.max(height(node.getLeft()), height(node.getRight())) + 1);
     
    	return checkBalanceAndRotate(data, node); 
    }
     
    private Node checkBalanceAndRotate(JsonObject data, Node node) {
    	int balance = getBalance(node);
     
    	// left-left
    	if (balance > 1 && data.get("Artist").getAsString().compareTo(node.getLeft().getData().get("Artist").getAsString()) < 0) {
    		return rightRotation(node);
    	}
     
    	// right-right
    	if (balance < -1 && data.get("Artist").getAsString().compareTo(node.getRight().getData().get("Artist").getAsString()) > 0) {
    		return leftRotation(node);
    	}
     
    	// left-right
    	if (balance > 1 && data.get("Artist").getAsString().compareTo(node.getLeft().getData().get("Artist").getAsString()) > 0) {
    		node.setLeft(leftRotation(node.getLeft()));
    		return rightRotation(node);
    	}
     
    	// right-left
    	if (balance < -1 && data.get("Artist").getAsString().compareTo(node.getRight().getData().get("Artist").getAsString()) < 0) {
    		node.setRight(rightRotation(node.getRight()));
    		return leftRotation(node);
    	}
     
    	return node;
    }
     
    private Node leftRotation(Node node) {
		
		Node newParentNode = node.getRight();
		Node mid = newParentNode.getLeft();
 
		newParentNode.setLeft(node);
		node.setRight(mid);
 
		node.setHeight(Math.max(height(node.getLeft()), height(node.getRight())) + 1);
		newParentNode.setHeight(Math.max(height(newParentNode.getLeft()), height(newParentNode.getRight())) + 1);
 
		return newParentNode;
	}

	private Node rightRotation(Node node) {
		
		Node newParentNode = node.getLeft();
		Node mid = newParentNode.getRight();
 
		newParentNode.setRight(node);
		node.setLeft(mid);
 
		node.setHeight(Math.max(height(node.getLeft()), height(node.getRight())) + 1);
		newParentNode.setHeight(Math.max(height(newParentNode.getLeft()), height(newParentNode.getRight())) + 1);
 
		return newParentNode;
		
	}
	private int getBalance(Node node) {
    	if (node == null) {
    		return 0;
    	}
    	return height(node.getLeft()) - height(node.getRight());
    }
   
    private int height(Node node) {
    	 
		if (node == null) {
			return -1;
		}

		return node.getHeight();
    }

    public void radixSort() throws IOException {
    	this.dataBase = XMLInterpreter.loadDataBase();
    	artistNames = new JsonObject[this.dataBase.size()];
    	for (int i = 0; i < artistNames.length; i++){
    		artistNames[i] = (JsonObject) this.dataBase.get(i);
    	}
    	
    	ArrayList <JsonObject> returnArr = new ArrayList<JsonObject>(Arrays.asList(artistNames));
    	for (int i = 0; i < 3; i++) {
    		JsonObject [][] sorted = new JsonObject[256][artistNames.length];
    		for (JsonObject currentString : returnArr) {
    			int index = getIndex(currentString.get("Artist").getAsString().charAt(i));
    			addItem(sorted[index], currentString);
    		}

    		returnArr = new ArrayList<JsonObject>();

    		for (int a = 0; a < sorted.length; a++) {
    			if (sorted[a][0] != null) {
    				for (JsonObject cs : sorted[a]) {
    					if (cs != null) {
    						returnArr.add(cs);
    					}
    				}
    			}
    		}
    	}

    	returnArr.toArray(artistNames);
    	for(int i = 0; i<artistNames.length; i++) {
    		System.out.println(artistNames[i]);
    	}
    	JsonArray newArray = new JsonArray();
    	for(int i=0; i<artistNames.length;i++) {
    		newArray.add(artistNames[i]);
    	}
    	XMLInterpreter.saveDataBase(newArray); 
    	buildTree(artistNames);
    }

    private static int getIndex (char letter) {
    	return letter - 'a';
    }

    private static void addItem (JsonObject[] s, JsonObject item) {
    	int index = 0;
    	while (s[index] != null) {
    		index++;
    	}
    	s[index] = item;
    }

    
    
    public Node deleteNode(Node root, JsonObject value){
        if (root == null)
            return root;
 
        if (value.get("Artist").getAsString().compareTo(root.getData().get("Artist").getAsString()) < 0) {
        	root.setLeft(deleteNode(root.getLeft(), value));
        
        }else if (value.get("Artist").getAsString().compareTo(root.getData().get("Artist").getAsString()) > 0) {
            root.setRight(deleteNode(root.getRight(), value));
        
        }else{
            if ((root.getLeft() == null) || (root.getRight() == null)){
            	
                Node temp = null;
                if (temp == root.getLeft()) {
                	
                    temp = root.getRight();
                }else {
                	
                    temp = root.getLeft();
                }
 
                if (temp == null){
                    temp = root;
                    root = null;
                }else {
                    root = temp; 
                }
            }else{
 
                Node temp = minValueNode(root.getRight());
 
                root.setData(temp.getData());
 
                root.setRight(deleteNode(root.getRight(), temp.getData()));
            }
        }
 
        if (root == null)
            return root;
 
        root.setHeight(max(height(root.getLeft()), height(root.getRight())) + 1);
 
        int balance = getBalance(root);
 
        if (balance > 1 && getBalance(root.getLeft()) >= 0) {
        	
            return rightRotation(root);
        }
 
        if (balance > 1 && getBalance(root.getLeft()) < 0){
            root.setLeft(leftRotation(root.getLeft()));
            return rightRotation(root);
        }
 
        if (balance < -1 && getBalance(root.getRight()) <= 0) {
        	
        	return leftRotation(root);
        }
 
        if (balance < -1 && getBalance(root.getRight()) > 0){
            root.setRight(rightRotation(root.getRight()));
            return leftRotation(root);
        }
 
        return root;
    }
    
    private Node minValueNode(Node node){
        Node current = node;
 
        while (current.getLeft() != null)
           current = current.getLeft();
 
        return current;
    }
    
    private int max(int a, int b)
    {
        return (a > b) ? a : b;
    }
}
