package DataStructuress;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import DataManage.XMLInterpreter;

public class SplayTree {
	
	private JsonArray dataBase;
    private JsonObject[] albumNames;
    private Node root;
    

    
    public void buildTree(JsonObject[] array) throws FileNotFoundException {
    	this.dataBase = XMLInterpreter.loadDataBase();
    	albumNames = new JsonObject[this.dataBase.size()];
    	for(int i=0; i < array.length; i++) {
    		albumNames[i] = (JsonObject) this.dataBase.get(i);
			insert(array[i]);
		}
    }

    private void insert(JsonObject value) {
        // splay key to root
        if (root == null) {
            root = new Node(value);
            return;
        }
        
        root = splay(root);

        int cmp = value.get("Album").getAsString().compareTo(root.getData().get("Album").getAsString());
        
        // Insert new node at root
        if (cmp < 0) {
            Node n = new Node(value);
            n.setLeft(root.getLeft());
            n.setRight(root);
            root.setLeft(null);
            root.setData(n.getData());
        }

        // Insert new node at root
        else if (cmp > 0) {
            Node n = new Node(value);
            n.setRight(root.getRight());
            n.setLeft(root);
            root.setRight(null);
            root.setData(n.getData());
        }

        // It was a duplicate key. Simply replace the value
        else {
            root.setData(value);
        }

    }
    private Node splay(Node n) {
        if (n == null) return null;

        int cmp1 = root.getData().get("Album").getAsString().compareTo(n.getData().get("Album").getAsString());

        if (cmp1 < 0) {
            // key not in tree, so we're done
            if (n.getLeft() == null) {
                return n;
            }
            int cmp2 = root.getData().get("Album").getAsString().compareTo(n.getLeft().getData().get("Album").getAsString());
            if (cmp2 < 0) {
                n.getLeft().setLeft(splay(n.getLeft().getLeft()));
                n.setData(rotateRight(n).getData());
            }
            else if (cmp2 > 0) {
                n.getLeft().setRight(splay(n.getLeft().getRight()));
                if (n.getLeft().getRight() != null)
                    n.setLeft(rotateLeft(n.getLeft()));
            }
            
            if (n.getLeft() == null) {
            	return n;
            }
            else {
            	return rotateRight(n);
            }
        }

        else if (cmp1 > 0) { 
            // key not in tree, so we're done
            if (n.getRight() == null) {
                return n;
            }

            int cmp2 = root.getData().get("Album").getAsString().compareTo(n.getRight().getData().get("Album").getAsString());
            if (cmp2 < 0) {
                n.getRight().setLeft(splay(n.getRight().getLeft()));
                if (n.getRight().getLeft() != null)
                    n.setRight(rotateRight(n.getRight()));
            }
            else if (cmp2 > 0) {
                n.getRight().setRight(splay(n.getRight().getRight()));
                n = rotateLeft(n);
            }
            
            if (n.getRight() == null) {
            	return n;
            }
            else {
            	return rotateLeft(n);
            }
        }

        else {
        	return n;
        }
    }
    
    private Node rotateRight(Node n) {
        Node x = n.getLeft();
        n.setLeft(x.getRight());
        x.setRight(n);
        return x;
    }

    // left rotate
    private Node rotateLeft(Node n) {
        Node x = n.getRight();
        n.setRight(x.getLeft());
        x.setLeft(n);
        return x;
    }
    
	public void bubbleSort() throws IOException {
		this.dataBase = XMLInterpreter.loadDataBase();
    	albumNames = new JsonObject[this.dataBase.size()];
    	for (int i = 0; i < albumNames.length; i++){
    		albumNames[i] = (JsonObject) this.dataBase.get(i);
    	}
    	int n = albumNames.length;  
    	JsonObject temp = null;  
    	for(int i=0; i < n; i++){  
    		for(int j=1; j < (n-i); j++){  
    			if(albumNames[j-1].get("Album").getAsString().compareTo(albumNames[j].get("Album").getAsString()) > 0){  
    				temp = albumNames[j-1];  
    				albumNames[j-1] = albumNames[j];  
    				albumNames[j] = temp;  
    			}  
    			
    		}  
    	}  
    	for(int i = 0; i<albumNames.length; i++) {
    		System.out.println(albumNames[i]);
    	}
    	JsonArray newArray = new JsonArray();
    	for(int i=0; i<albumNames.length;i++) {
    		newArray.add(albumNames[i]);
    	}
    	XMLInterpreter.saveDataBase(newArray); 
    	buildTree(albumNames);
    }
    
}
