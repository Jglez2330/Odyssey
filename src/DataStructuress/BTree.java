package DataStructuress;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import DataManage.XMLInterpreter;

public class BTree {
	private JsonArray dataBase;
	private JsonObject[] songNames;
	public BNode root;
	private int order;

	public BTree(int order) {
		this. order = order;
		this.root = new BNode(null, order);
	}


	public void buildTree(JsonObject[] array) throws FileNotFoundException {
		this.dataBase = XMLInterpreter.loadDataBase();
		songNames = new JsonObject[this.dataBase.size()];
		for(int i=0; i < array.length; i++) {
			songNames[i] = (JsonObject) this.dataBase.get(i);
			insert(array[i]);
		}

	}

	public void split(BNode x, int i, BNode y){
		BNode z = new BNode(null,order);	                					
		z.setLeaf(y.isLeaf());
		z.setCount(order-1);
		for(int j = 0; j < order - 1; j++){
			z.setData(j, y.getData(j+order));
		}
		if(!y.isLeaf()){
			for(int k = 0; k < order; k++){
				z.setChild(k, y.getChild(k+order));
			}
		}
		y.setCount(order-1);

		for(int j = x.getCount() ; j> i ; j--){

			x.setChild(j+1, x.getChild(j)); 
		}
		x.setChild(i+1,z); 
		for(int j = x.getCount(); j> i; j--){
			x.setData(j + 1, x.getData(j)); 
		}
		x.setData(i, y.getData(order-1));
		y.setData(order-1, null); 
		for(int j = 0; j < order - 1; j++){
			y.setData(j + order, null); 
		}
		x.setCount(x.getCount()+1);  
	}


	public void nonfullInsert(BNode x, JsonObject value){
		int i = x.getCount();
		if(x.isLeaf()){
			while(i >= 1 && value.get("Song").getAsString().compareTo(x.getData(i-1).get("Song").getAsString()) < 0){
				x.setData(i, x.getData(i-1));
				i--;
			}
			x.setData(i, value);
			x.setCount(x.getCount()+1); 
		}
		else{
			int j = 0;
			while(j < x.getCount()  && value.get("Song").getAsString().compareTo(x.getData(j).get("Song").getAsString()) > 0){	
				System.out.println();
				j++;
			}
			if(x.getChild(j) != null) {
				if(x.getChild(j).getCount() == order*2 - 1){
					split(x,j,x.getChild(j));

					if(value.get("Song").getAsString().compareTo(x.getData(j).get("Song").getAsString()) > 0){
						j++;
					}
				}
			}
			nonfullInsert(x.getChild(j),value);
		}
	}

	public void insert(JsonObject value){
		BNode r = root; 
		if(r.getCount() == 2*order - 1){
			BNode s = new BNode(null,order);
			root.setData(s.getData());    
			s.setLeaf(false);
			s.setCount(0);
			s.setChild(0,r);
			split(s,0,r);
			nonfullInsert(s, value); 
		}
		else {
			nonfullInsert(r, value);
		}
	}

	public void quickSort() throws IOException {
		this.dataBase = XMLInterpreter.loadDataBase();
		songNames = new JsonObject[this.dataBase.size()];
		for (int i = 0; i < songNames.length; i++){
			songNames[i] = (JsonObject) this.dataBase.get(i);
		}
		quickSort(0, songNames.length -1);
		for(int i = 0; i<songNames.length; i++) {
			System.out.println(songNames[i]);
    	}
    	JsonArray newArray = new JsonArray();
    	for(int i=0; i<songNames.length;i++) {
    		newArray.add(songNames[i]);
    	}
    	XMLInterpreter.saveDataBase(newArray); 
    	//buildTree(songNames);

    }

    private void quickSort(int lowerIndex, int higherIndex) {

    	int i = lowerIndex;
    	int j = higherIndex;
    	JsonObject pivot = songNames[lowerIndex+(higherIndex-lowerIndex)/2];
    	while (i <= j) {
    		while (songNames[i].get("Song").getAsString().compareTo(pivot.get("Song").getAsString()) < 0) {
    			i++;
    		}
    		while (songNames[j].get("Song").getAsString().compareTo(pivot.get("Song").getAsString()) > 0) {
    			j--;
    		}
    		if (i <= j) {
    			JsonObject temp = songNames[i];
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
}