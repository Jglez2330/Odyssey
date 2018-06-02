package DataStructuress;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.jdom2.Document;

import com.google.gson.JsonObject;

public abstract class Tree {

    public abstract void buildTree() throws FileNotFoundException;
    public abstract void sort() throws IOException;
    public abstract JsonObject search(String value);
    public abstract void delete();

}
