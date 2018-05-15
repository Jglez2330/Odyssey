/*
package DataStructures;

import DataManage.XMLInterpreter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;

//
public class AVLTree {
    private JsonArray dataBase;
    private String[] artistNames;
    private Node<String> root;

    public AVLTree() throws FileNotFoundException {
        this.dataBase = XMLInterpreter.loadDataBase();
        artistNames = new String[this.dataBase.size()];
        for (int i = 0; i < artistNames.length; i++){
            JsonObject cancion = (JsonObject) this.dataBase.get(i);

            artistNames[i] = cancion.get("Artist").getAsString();
        }
        buildTree();

    }

    private void buildTree() {
        this.root = buildTree(root,0);
    }

    private Node<String> buildTree(Node<String> node, int index) {
        if (index < this.artistNames.length) {
            if (root == null){
                root = new Node(this.artistNames[index]);
                return buildTree(node, index + 1);
            }else if (node == null) {
                node = new Node(this.artistNames[index]);
                return buildTree(node, index + 1);
            } else if (node.getData().compareTo(artistNames[index]) > 0) {
                node.setLeft(buildTree(this.root.getLeft(), index));

            } else if (node.getData().compareTo(this.artistNames[index]) < 0) {
                node.setRight(buildTree(this.root.getRight(), index));

            }
        }
        return node;

    }

    @Override
    public String toString() {
        String representation = "";


        return representation;
    }
}
*/
