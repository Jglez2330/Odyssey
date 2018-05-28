package Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import DataManage.XMLInterpreter;

// la clase utiliza un singleton para que solo haya un server en cualquier momento
//La clase implementa Runnable para poder correr un Thread para escuchar y responder al cliente
public class Server implements Runnable{

    private static Server serverInstance;
    private ServerSocket serverSocket;
    private Socket client;
    private BufferedReader in;

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
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));



        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Document xml) throws IOException {
        client.getSendBufferSize();
        PrintWriter printWriter = new PrintWriter(this.client.getOutputStream());
        printWriter.println(new XMLOutputter().outputString(xml));
        printWriter.println("\u0001");
        printWriter.flush();

    }

    public void listen() throws IOException, JDOMException {
        String data;

        StringBuilder xml = new StringBuilder();


        while ((data = in.readLine()) != null){
            System.out.println(data);
            if (data.contains("Close")){
                PrintWriter printWriter = new PrintWriter(this.client.getOutputStream());
                printWriter.println("Close");
                printWriter.flush();
                this.client.close();
                break;
            }else {
                xml.append(data);
            }
        }
        this.client.close();



        if (data != null) {


            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(new StringReader(xml.toString()));
            Element root = document.getRootElement();
            List<?> listaElementos = root.getChildren();
            System.out.println(listaElementos);
            Element opcode = (Element) listaElementos.get(0);
            if (Integer.parseInt(opcode.getContent(0).getValue()) == 0) {
                XMLInterpreter.addSong(listaElementos);
            } else if (Integer.parseInt(opcode.getContent(0).getValue()) == 21) {
                XMLInterpreter.register(listaElementos);
            } else if (Integer.parseInt(opcode.getContent(0).getValue()) == 20) {
                XMLInterpreter.access(listaElementos);
            } else if (Integer.parseInt(opcode.getContent(0).getValue()) == 22) {
                XMLInterpreter.retrieveUsers();
            }else if (Integer.parseInt(opcode.getContent(0).getValue()) == 2){
                XMLInterpreter.getSongsXML(0);

            }
            listen();
        }

    }

    public Socket getClient() {
        return client;
    }

    @Override
    public void run() {

    }
}
