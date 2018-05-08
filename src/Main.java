import Socket.Server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        Server server = Server.getServerInstance();
        while (server.getClient().isConnected()){
            server.listen();
        }


    }
}
