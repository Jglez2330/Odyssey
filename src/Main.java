import Socket.Server;
import org.jdom2.JDOMException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, JDOMException {

        Server server = Server.getServerInstance();

        server.listen();



    }
}
