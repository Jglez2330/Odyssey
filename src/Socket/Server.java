package Socket;

import java.beans.XMLDecoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
// la clase utiliza un singleton para que solo haya un server en cualquier momento
public class Server {

    private static Server serverInstance;
    private ServerSocket serverSocket;
    private Socket client;

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
            this.client = serverSocket.accept();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send() {
    }
    public void listen() throws IOException {
        String data;
        String xml;

        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        while ((data = in.readLine()) != null){
            System.out.println(data);
        }


    }

    public Socket getClient() {
        return client;
    }
}
