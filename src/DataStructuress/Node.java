package DataStructuress;

public class Node<T> {
    private Node<T> left;
    private Node<T> right;
    private T data;
    private int height;

    public Node(T data){
        this.data = data;
        left = null;
        right = null;
    }

    public void setLeft(Node<T> left) {
        this.left = left;
    }

    public void setRight(Node<T> right) {
        this.right = right;
    }

    public T getData() {
        return data;
    }

    public Node<T> getLeft() {
        return left;
    }

    public Node<T> getRight() {
        return right;
    }

	public int getHeight() {
		return height;
	}

	public void setHeight(int i) {
		height = i;
	}
}

