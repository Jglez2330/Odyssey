import java.io.IOException;

import DataManage.APIManage;
import Socket.Server;
import org.jdom2.JDOMException;

import DataStructuress.AVLTree;

public class Main {

	public static void main(String[] args) throws IOException, JDOMException {

        Server server = Server.getServerInstance();

        server.listen();
        //APIManage apiManage = new APIManage();
        //System.out.println(apiManage.getLyricsText("do_i_wanna_know".replace("_", " "),"arctic_monkeys".replace("_", " ")));
		//AVLTree tree = new AVLTree();
		//tree.radixSort();
        
        
        


    }
}

