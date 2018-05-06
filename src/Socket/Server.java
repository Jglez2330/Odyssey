package Socket;

import java.io.IOException;
import java.net.ServerSocket;
// la clase utiliza un singleton para que solo haya un server en cualquier momento
public class Server {

    private static Server serverInstance;
    private ServerSocket serverSocket;

    /**
     * Metodo para obtener la instancia de server, es un singleton
     * @return Server
     * @param
     * @exception null
     */
    public static Server getServerInstance(){
        if (serverInstance == null){
            serverInstance = new Server();
        }
        return serverInstance;
    }

    private Server(){
        try {
            // Se inicia el server en el puerto 3000
            this.serverSocket = new ServerSocket(3000);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(){

    }


}
