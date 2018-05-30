import java.io.IOException;

import Socket.Server;
import org.jdom2.JDOMException;

import DataStructuress.AVLTree;

public class Main {

	public static void main(String[] args) throws IOException, JDOMException {

        Server server = Server.getServerInstance();

        server.listen();
		//AVLTree tree = new AVLTree();
		//tree.radixSort();
        
        
        


    }
}

