package DataStructuress;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import DataManage.XMLInterpreter;

public class AVLTree {
    private JsonArray dataBase;
    private String[] artistNames;
    private String[] songNames;
    private String[] albumNames;
    private Node<String> root;

    
    public AVLTree() throws FileNotFoundException {
        this.dataBase = XMLInterpreter.loadDataBase();
        artistNames = new String[this.dataBase.size()];
        for (int i = 0; i < artistNames.length; i++){
            JsonObject cancion = (JsonObject) this.dataBase.get(i);
            artistNames[i] = cancion.get("Artist").getAsString();
        }
        buildTree(artistNames);

    }

    private void buildTree(String[] array) {
		for(int i=0; i < array.length; i++) {
			insert(array[i]);
		}
		
	}
    
    private void insert(String data){
    	root = insert(root, data);
    }
    
    private Node<String> insert(Node<String> node, String data) {
    	if (node == null) {
    		return new Node<String>(data);
    	}
     
    	if (data.compareTo(node.getData()) < 0) {
    		node.setLeft(insert(node.getLeft(), data));
    	} else {
    		node.setRight(insert(node.getRight(), data));
    	}
     
    	node.setHeight(Math.max(height(node.getLeft()), height(node.getRight())) + 1);
     
    	return checkBalanceAndRotate(data, node); // check balance and make rotations if neccessary
    }
     
    private Node<String> checkBalanceAndRotate(String data, Node<String> node) {
    	int balance = getBalance(node); // balance = leftNode.height - rightNode.height
     
    	// left-left
    	if (balance > 1 && data.compareTo(node.getLeft().getData()) < 0) {
    		return rightRotation(node);
    	}
     
    	// right-right
    	if (balance < -1 && data.compareTo(node.getRight().getData()) > 0) {
    		return leftRotation(node);
    	}
     
    	// left-right
    	if (balance > 1 && data.compareTo(node.getLeft().getData()) > 0) {
    		node.setLeft(leftRotation(node.getLeft()));
    		return rightRotation(node);
    	}
     
    	// right-left
    	if (balance < -1 && data.compareTo(node.getRight().getData()) < 0) {
    		node.setRight(rightRotation(node.getRight()));
    		return leftRotation(node);
    	}
     
    	return node;
    }
     
    private Node<String> leftRotation(Node<String> node) {
		
		Node<String> newParentNode = node.getRight();
		Node<String> mid = newParentNode.getLeft();
 
		newParentNode.setLeft(node);
		node.setRight(mid);
 
		node.setHeight(Math.max(height(node.getLeft()), height(node.getRight())) + 1);
		newParentNode.setHeight(Math.max(height(newParentNode.getLeft()), height(newParentNode.getRight())) + 1);
 
		return newParentNode;
	}

	private Node<String> rightRotation(Node<String> node) {
		
		Node<String> newParentNode = node.getLeft();
		Node<String> mid = newParentNode.getRight();
 
		newParentNode.setRight(node);
		node.setLeft(mid);
 
		node.setHeight(Math.max(height(node.getLeft()), height(node.getRight())) + 1);
		newParentNode.setHeight(Math.max(height(newParentNode.getLeft()), height(newParentNode.getRight())) + 1);
 
		return newParentNode;
		
	}
	private int getBalance(Node<String> node) {
    	if (node == null) {
    		return 0;
    	}
    	return height(node.getLeft()) - height(node.getRight());
    }
   
    private int height(Node<String> node) {
    	 
		if (node == null) {
			return -1;
		}

		return node.getHeight();
    }


    public void quickSort() {

    	songNames = new String[this.dataBase.size()];
    	for (int i = 0; i < songNames.length; i++){
    		JsonObject cancion = (JsonObject) this.dataBase.get(i);
    		songNames[i] = cancion.get("Song").getAsString();
    	}
    	quickSort(0, songNames.length -1);
    	for(int i = 0; i<songNames.length; i++) {
    		System.out.println(songNames[i]);
    	}
    	buildTree(songNames);

    }

    private void quickSort(int lowerIndex, int higherIndex) {

    	int i = lowerIndex;
    	int j = higherIndex;
    	String pivot = songNames[lowerIndex+(higherIndex-lowerIndex)/2];
    	while (i <= j) {
    		while (songNames[i].compareTo(pivot) < 0) {
    			i++;
    		}
    		while (songNames[j].compareTo(pivot) > 0) {
    			j--;
    		}
    		if (i <= j) {
    			String temp = songNames[i];
    			songNames[i] = songNames[j];
    			songNames[j] = temp;
    			i++;
    			j--;
    		}
    	}
    	if (lowerIndex < j)
    		quickSort(lowerIndex, j);
    	else if (i < higherIndex)
    		quickSort(i, higherIndex);
    }
    

    public void radixSort() {
    	artistNames = new String[this.dataBase.size()];
    	for (int i = 0; i < artistNames.length; i++){
    		JsonObject cancion = (JsonObject) this.dataBase.get(i);
    		artistNames[i] = cancion.get("Song").getAsString();
    	}
    	
    	ArrayList <String> returnArr = new ArrayList<String>(Arrays.asList(artistNames));
    	for (int pass = artistNames.length - 1; pass >= 0; pass--) {
    		String [][] sorted = new String[256][artistNames.length];
    		for (String currentString : returnArr) {
    			int index = getIndex(currentString.charAt(pass));
    			addItem(sorted[index], currentString);
    		}

    		returnArr = new ArrayList<String>();

    		for (int a = 0; a < sorted.length; a++) {
    			if (sorted[a][0] != null) {
    				for (String cs : sorted[a]) {
    					if (cs != null) returnArr.add(cs);
    				}
    			}
    		}
    	}

    	returnArr.toArray(artistNames);
    	for(int i = 0; i<artistNames.length; i++) {
    		System.out.println(artistNames[i]);
    	}
    	buildTree(artistNames);
    }

    private static int getIndex (char letter) {
    	return letter - 'a';
    }

    private static void addItem (String [] s, String item) {
    	int index = 0;
    	while (s[index] != null) {
    		index++;
    	}
    	s[index] = item;
    }

    public void bubbleSort() {
    	albumNames = new String[this.dataBase.size()];
    	for (int i = 0; i < albumNames.length; i++){
    		JsonObject cancion = (JsonObject) this.dataBase.get(i);
    		albumNames[i] = cancion.get("Album").getAsString();
    	}
    	int n = albumNames.length;  
    	String temp = "";  
    	for(int i=0; i < n; i++){  
    		for(int j=1; j < (n-i); j++){  
    			if(albumNames[j-1].compareTo(albumNames[j]) > 0){  
    				temp = albumNames[j-1];  
    				albumNames[j-1] = albumNames[j];  
    				albumNames[j] = temp;  
    			}  
    			
    		}  
    	}  
    	for(int i = 0; i<albumNames.length; i++) {
    		System.out.println(albumNames[i]);
    	}
    	buildTree(songNames);
    }
    @Override
    public String toString() {
        String representation = "";

        return representation;
    }
}

