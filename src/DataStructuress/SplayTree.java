package DataStructuress;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import DataManage.XMLInterpreter;

public class SplayTree extends Tree{
	
	private JsonArray dataBase;
    private JsonObject[] albumNames;
    private SplayNode root;
    
    public SplayTree() {
    	try {
			buildTree();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
    
    public void buildTree() throws FileNotFoundException {
    	this.dataBase = XMLInterpreter.loadDataBase();
    	albumNames = new JsonObject[this.dataBase.size()];
    	for(int i=0; i < albumNames.length; i++) {
    		albumNames[i] = (JsonObject) this.dataBase.get(i);
			insert(albumNames[i]);
		}
    }

    public void insert(JsonObject key) {
        if (root == null) {
            root = new SplayNode(key, null);
            return;
        }

        insert(key, root);
    }

    private void insert(JsonObject key, SplayNode node) {
        if (key.get("Album").getAsString().compareTo(node.getKey().get("Album").getAsString()) < 0) {
            if (node.leftExists()) {
                insert(key, node.getLeft());
            } else {
                node.setLeft(new SplayNode(key, node));
            }
        }

        if (key.get("Album").getAsString().compareTo(node.getKey().get("Album").getAsString()) >0) {
            if (node.rightExists()) {
                insert(key, node.getRight());
            } else {
                node.setRight(new SplayNode(key, node));
            }
        }
    }
    private void splay(SplayNode node) {
        while (node.parentExists()) {
            SplayNode parent = node.getParent();
            if (!parent.parentExists()) {
                if (parent.getLeft() == node) {
                    rotateRight(parent);
                } else {
                    rotateLeft(parent);
                }
            } else {
                SplayNode gparent = parent.getParent();
                if (parent.getLeft() == node && gparent.getLeft() == parent) {
                    rotateRight(gparent);
                    rotateRight(node.getParent());
                } else if (parent.getRight() == node &&
                        gparent.getRight() == parent) {
                    rotateLeft(gparent);
                    rotateLeft(node.getParent());
                } else if (parent.getLeft() == node &&
                        gparent.getRight() == parent) {
                    rotateRight(parent);
                    rotateLeft(node.getParent());
                } else {
                    rotateLeft(parent);
                    rotateRight(node.getParent());
                }
            }
        }
    }    
    private void rotateLeft(SplayNode x) {
        SplayNode y = x.getRight();
        x.setRight(y.getLeft());
        if (y.getLeft() != null) {
            y.getLeft().setParent(x);
        }
        y.setParent(x.getParent());
        if (x.getParent() == null) {
            root = y;
        } else {
            if (x == x.getParent().getLeft()) {
                x.getParent().setLeft(y);
            } else {
                x.getParent().setRight(y);
            }
        }
        y.setLeft(x);
        x.setParent(y);
    }

    private void rotateRight(SplayNode x) {
        SplayNode y = x.getLeft();
        x.setLeft(y.getRight());
        if (y.getRight() != null) {
            y.getRight().setParent(x);
        }
        y.setParent(x.getParent());
        if (x.getParent() == null) {
            root = y;
        } else {
            if (x == x.getParent().getLeft()) {
                x.getParent().setLeft(y);
            } else {
                x.getParent().setRight(y);
            }
        }
        y.setRight(x);
        x.setParent(y);
    }

    
	public void bubbleSort(String type) throws IOException {
		this.dataBase = XMLInterpreter.loadDataBase();
    	albumNames = new JsonObject[this.dataBase.size()];
    	for (int i = 0; i < albumNames.length; i++){
    		albumNames[i] = (JsonObject) this.dataBase.get(i);
    	}
    	int n = albumNames.length;  
    	JsonObject temp = null;  
    	for(int i=0; i < n; i++){  
    		for(int j=1; j < (n-i); j++){  
    			if(albumNames[j-1].get(type).getAsString().compareTo(albumNames[j].get(type).getAsString()) > 0){  
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
    	buildTree();
    }
	
	@Override
	public void sort(String type) throws IOException {
		this.bubbleSort(type);
	}

	@Override
	public JsonObject search(String key) {
        if (root == null) {
            return null;
        }

        SplayNode node = search(key, root);
        System.out.println();
        splay(node);
        return node.getKey();
    }

    private SplayNode search(String key, SplayNode node) {
        if (key.compareTo(node.getKey().get("Album").getAsString()) == 0) {
            return node;
        }

        if (key.compareTo(node.getKey().get("Album").getAsString()) < 0) {
            if (!node.leftExists()) {
                return null;
            }
            return search(key, node.getLeft());
        }

        if (key.compareTo(node.getKey().get("Album").getAsString()) > 0) {
            if (!node.rightExists()) {
                return null;
            }
            return search(key, node.getRight());
        }

        return null;
    }

    public String toString() {
        return root.toString();
    }

	@Override
	public void delete(String key) {
        if (root == null) {
            return;
        }

        search(key);
        delete(key, root);
    }

    private void delete(String key, SplayNode node) {
        if (key.compareTo(node.getKey().get("Album").getAsString())< 0) {
            if (node.leftExists()) {
                delete(key, node.getLeft());
            }
            if (node.getLeft().isDeleted()) {
                node.setLeft(null);
            }
            return;
        }

        if (key.compareTo(node.getKey().get("Album").getAsString()) > 0) {
            if (node.rightExists()) {
                delete(key, node.getRight());
            }
            if (node.getRight().isDeleted()) {
                node.setRight(null);
            }
            return;
        }

        delete(node);
    }

    private void delete(SplayNode node) {
        if (!(node.leftExists() || node.rightExists())) {
            node.setDeleted(true);
            return;
        }

        if (node.leftExists() && !node.rightExists()) {
            node.setKey(node.getLeft().getKey());
            if (node.getLeft().rightExists()) {
                node.setRight(node.getLeft().getRight());
            }
            if (node.getLeft().leftExists()) {
                node.setLeft(node.getLeft().getLeft());
            } else {
                node.setLeft(null);
            }
            return;
        }

        if (node.rightExists() && !node.leftExists()) {
            node.setKey(node.getRight().getKey());
            if (node.getRight().leftExists()) {
                node.setLeft(node.getLeft().getLeft());
            }
            if (node.getRight().rightExists()) {
                node.setRight(node.getLeft().getRight());
            } else {
                node.setRight(null);
            }
            return;
        }

        JsonObject min = findMin(node.getRight());
        node.setKey(min);
    }

    private JsonObject findMin(SplayNode node) {
        if (!node.leftExists()) {
            node.setDeleted(true);
            return node.getKey();
        }

        JsonObject min = findMin(node.getLeft());
        if (node.getLeft().isDeleted()) {
            node.setLeft(null);
        }
        return min;
    }

}
