import java.io.IOException;

import org.jdom2.JDOMException;

import DataStructuress.BTree;

public class Main {

	public static void main(String[] args) throws IOException, JDOMException {

        //Server server = Server.getServerInstance();

        //server.listen();
		BTree tree = new BTree(2);
		tree.quickSort();		
        
        


    }
}

