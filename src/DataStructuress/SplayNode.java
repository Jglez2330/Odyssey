package DataStructuress;

import com.google.gson.JsonObject;

public class SplayNode {

    private SplayNode left;
    private SplayNode right;
    private SplayNode parent;

    private JsonObject key;
    private boolean isDeleted = false;

    public SplayNode(JsonObject key, SplayNode parent) {
        this.key = key;
        this.parent = parent;
    }

    public boolean leftExists() {
        return left != null;
    }

    public boolean rightExists() {
        return right != null;
    }

    public boolean parentExists() {
        return parent != null;
    }

    public JsonObject getKey() {
        return key;
    }

    public void setKey(JsonObject key) {
        this.key = key;
    }

    public SplayNode getLeft() {
        return left;
    }

    public void setLeft(SplayNode left) {
        this.left = left;
    }

    public SplayNode getRight() {
        return right;
    }

    public void setRight(SplayNode right) {
        this.right = right;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public SplayNode getParent() {
        return parent;
    }

    public void setParent(SplayNode parent) {
        this.parent = parent;
    }

}
