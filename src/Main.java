import java.io.IOException;

import org.jdom2.JDOMException;

import Socket.Server;

public class Main {

	public static void main(String[] args) throws IOException, JDOMException {

        Server server = Server.getServerInstance();

        server.listen();
		
    }
}

