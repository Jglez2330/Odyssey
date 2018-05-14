package Socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;


import DataManage.XMLInterpreter;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

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

    public void listen() throws IOException, JDOMException {
        String data;
//        String xml = "";

        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        StringBuilder xml = new StringBuilder();


        while ((data = in.readLine()) != null){
            //System.out.println(data);
            xml.append(data);
        }
        this.client.close();
        xml.append("\n");
        String s = System.getProperty("user.home");
        s += "/" + "Desktop" + "/";
        File file = new File(s+"Canciones.XML");
        //System.out.println(xml);
        file.setWritable(true);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(xml.toString());
        fileWriter.flush();
        //System.out.println(xml);
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(new StringReader(xml.toString()));
        Element root = document.getRootElement();
        List listaElementos = root.getChildren();
        System.out.println(listaElementos);
        Element opcode = (Element) listaElementos.get(0);
        if (Integer.parseInt(opcode.getContent(0).getValue()) == 0){
            XMLInterpreter.addSong(listaElementos);
        }
      /*  for (int i = 0; i < listaElementos.size(); i++){
            Element element = (Element) listaElementos.get(i);
            if (element.getName().toLowerCase().equals("song")){
                String stmp = element.getContent().get(0).getValue();
                byte[] cancion = Base64.getDecoder().decode(stmp);


                Files.write(Paths.get(s+"Canciones.mp3"),cancion);

                System.out.println(cancion);

                XMLInterpreter.addSong("Rock","Save Yourself","Kaleo","A/B","2016","Test", "Desktop");


            }
        }*/


    }

    public Socket getClient() {
        return client;
    }
}
