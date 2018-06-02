package DataStructuress;

import java.io.FileNotFoundException;
import java.io.IOException;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import DataManage.XMLInterpreter;

public class SplayTree extends Tree{

    private JsonArray dataBase;
    private JsonObject[] albumNames;
    private Node root;



    public void buildTree() throws FileNotFoundException {
        this.dataBase = XMLInterpreter.loadDataBase();
        albumNames = new JsonObject[this.dataBase.size()];
        for(int i=0; i < albumNames.length; i++) {
            albumNames[i] = (JsonObject) this.dataBase.get(i);
            insert(albumNames[i]);
        }
    }

    private void insert(JsonObject value) {
        if (root == null) {
            root = new Node(value);
            return;
        }

        root = splay(root, value);

        int cmp = value.get("Album").getAsString().compareTo(root.getData().get("Album").getAsString());

        if (cmp < 0) {
            Node n = new Node(value);
            n.setLeft(root.getLeft());
            n.setRight(root);
            root.setLeft(null);
            root.setData(n.getData());
        }

        else if (cmp > 0) {
            Node n = new Node(value);
            n.setRight(root.getRight());
            n.setLeft(root);
            root.setRight(null);
            root.setData(n.getData());
        }

        else {
            root.setData(value);
        }

    }
    private Node splay(Node n, JsonObject value) {
        if (n == null) return null;

        int cmp1 = value.get("Album").getAsString().compareTo(n.getData().get("Album").getAsString());

        if (cmp1 < 0) {
            if (n.getLeft() == null) {
                return n;
            }
            int cmp2 = value.get("Album").getAsString().compareTo(n.getLeft().getData().get("Album").getAsString());
            if (cmp2 < 0) {
                n.getLeft().setLeft(splay(n.getLeft().getLeft(),value));
                n.setData(rotateRight(n).getData());
            }
            else if (cmp2 > 0) {
                n.getLeft().setRight(splay(n.getLeft().getRight(),value));
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
            if (n.getRight() == null) {
                return n;
            }

            int cmp2 = value.get("Album").getAsString().compareTo(n.getRight().getData().get("Album").getAsString());
            if (cmp2 < 0) {
                n.getRight().setLeft(splay(n.getRight().getLeft(),value));
                if (n.getRight().getLeft() != null)
                    n.setRight(rotateRight(n.getRight()));
            }
            else if (cmp2 > 0) {
                n.getRight().setRight(splay(n.getRight().getRight(),value));
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
    private Node splay(Node n, String value) {
        if (n == null) {
            return null;
        }

        int cmp1 = value.compareTo(n.getData().get("Album").getAsString());

        if (cmp1 < 0) {
            if (n.getLeft() == null) {
                return n;
            }
            int cmp2 = value.compareTo(n.getLeft().getData().get("Album").getAsString());
            if (cmp2 < 0) {
                n.getLeft().setLeft(splay(n.getLeft().getLeft(),value));
                n.setData(rotateRight(n).getData());
            }
            else if (cmp2 > 0) {
                n.getLeft().setRight(splay(n.getLeft().getRight(),value));
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
            if (n.getRight() == null) {
                return n;
            }

            int cmp2 = value.compareTo(n.getRight().getData().get("Album").getAsString());
            if (cmp2 < 0) {
                n.getRight().setLeft(splay(n.getRight().getLeft(),value));
                if (n.getRight().getLeft() != null)
                    n.setRight(rotateRight(n.getRight()));
            }
            else if (cmp2 > 0) {
                n.getRight().setRight(splay(n.getRight().getRight(),value));
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
        buildTree();
    }

    public void sort() throws IOException {
        this.bubbleSort();
    }

    @Override
    public JsonObject search(String value) {
        root = splay(root, value);
        int cmp = value.compareTo(root.getData().get("Album").getAsString());
        if (cmp == 0) {
            return root.getData();
        }else {
            return null;
        }
    }

    @Override
    public void delete() {
        // TODO Auto-generated method stub

    }

}
