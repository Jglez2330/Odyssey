package DataStructuress;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.gson.JsonObject;

public abstract class Tree {
	
	public abstract void buildTree() throws FileNotFoundException;
	public abstract JsonObject search(String value);
	public abstract void delete(String value);
	public abstract void sort(String type) throws IOException;
	
}
