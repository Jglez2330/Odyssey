package DataManage;

import Socket.Server;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jdom2.Document;
import org.jdom2.Element;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

public class XMLInterpreter {

    private static void addSong(String style, String nameSong,String nameArtist, String nameAlbum, String year, String lyrics, String path,byte[] songBytes) throws IOException {
        JsonArray dataBase = loadDataBase();
        JsonArray dataBaseCopy = dataBase.deepCopy();
        JsonObject newSong = new JsonObject();
        newSong.addProperty("Style",style);
        newSong.addProperty("Song",nameSong);
        newSong.addProperty("Artist",nameArtist);
        newSong.addProperty("Album",nameAlbum);
        newSong.addProperty("Year",Integer.parseInt(year));
        newSong.addProperty("Lyrics",lyrics);
        newSong.addProperty("SongPath",path);
        dataBase.add(newSong);
        dataBase.addAll(dataBaseCopy);

        Files.write(Paths.get(path),songBytes);



        saveDataBase(dataBase);






    }
    public static Document getSongsXML(int index) throws IOException {
        JsonArray dataBase = loadDataBase();

        Document xml = new Document();
        Element data = new Element("Data");
        xml.setRootElement(data);
        String style;
        String nameSong;
        String nameArtist;
        String nameAlbum;
        String year;
        String lyrics;
        String path;
        int count = 0;
        for (int i = count; i < dataBase.size(); i ++ ) {

            JsonObject cancion = (JsonObject) dataBase.get(i);
            style = cancion.get("Style").getAsString();
            nameSong = cancion.get("Song").getAsString();
            nameArtist = cancion.get("Artist").getAsString();
            nameAlbum = cancion.get("Album").getAsString();
            year = cancion.get("Year").getAsString();
            lyrics = cancion.get("Lyrics").getAsString();
            path = cancion.get("SongPath").getAsString();



            Element songData = new Element("SongData");
            Element styleElement = new Element("Style").setText(style);
            Element songNameElement = new Element("SongName").setText(nameSong);
            Element artistElement = new Element("ArtistName").setText(nameArtist);
            Element albumElement = new Element("AlbumName").setText(nameAlbum);
            Element yearElement = new Element("Year").setText(year);
            Element lyricsElement = new Element("Lyrics").setText(lyrics);
            Element pathElement = new Element("Path").setText(path);
            songData.addContent(styleElement);
            songData.addContent(songNameElement);
            songData.addContent(artistElement);
            songData.addContent(albumElement);
            songData.addContent(yearElement);
            songData.addContent(lyricsElement);
            songData.addContent(pathElement);




            data.addContent(songData);

            break;


        }
        System.out.println(xml.getRootElement().getContent());
        System.out.println(Server.getServerInstance().getClient().getSendBufferSize());
        System.out.println(Server.getServerInstance().getClient().getSendBufferSize());
        Server.getServerInstance().send(xml);
        return xml;

    }



    private static void saveDataBase(JsonArray dataBase) throws IOException {
        File file = new File("src/DataBase.JSON");
        FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
        String dtabase = dataBase.toString();
        fileWriter.write(dtabase);
        fileWriter.flush();
    }




    public static JsonArray loadDataBase() throws FileNotFoundException {
        JsonParser parser  = new JsonParser();
        File file = new File("src/DataBase.JSON");


        JsonArray dataBase = (JsonArray) parser.parse(new FileReader(file));

        return dataBase;
    }



    public static void addSong(List listaElementos) throws IOException {
        String style = "";
        String songName = "";
        String artistName = "";
        String albumName = "";
        String year = "";
        String lyrics = "";
        String song = "";
        byte[] songBytes;
        for(int i = 0; i < listaElementos.size();i ++){
            Element element = (Element) listaElementos.get(i);
            if (element.getName().equals("style")){
                style += element.getContent().get(0).getValue();
            }else if (element.getName().equals("songName")){
                songName += element.getContent().get(0).getValue();
            }else if (element.getName().equals("artistName")){
                artistName += element.getContent().get(0).getValue();
            }else if (element.getName().equals("albumName")){
                albumName += element.getContent().get(0).getValue();
            }else if (element.getName().equals("year")){
                year += element.getContent().get(0).getValue();
            }else if (element.getName().equals("lyrics")){
                lyrics += element.getContent().get(0).getValue();
            }else if (element.getName().equals("song")) {
                song = element.getContent().get(0).getValue();
            }

        }
        songBytes = Base64.getDecoder().decode(song);
        //String path = System.getProperty("user.home") + File.separator
        //File file2 = new File("DataBase.mp3");

        File file = new File("Music" + "/" + songName + ".wav");
        if (!file.exists()){
            file.createNewFile();
        }//if (!file2.exists()){
          //  file2.createNewFile();
        //}


        addSong(style,songName,artistName,albumName,year,lyrics,"Music" + "/" + songName + ".wav", songBytes);
    }
    public static void deleteSong(List listaElementos){

    }


    public static void register(List listaElementos) throws IOException {
        String user = "";
        String password ="";

        for (int i = 0; i< listaElementos.size(); i++){
            Element element = (Element) listaElementos.get(i);
            if (element.getName().contains("User")){

                user = element.getContent().get(0).getValue();

            }else if(element.getName().contains("Password")){

                password = element.getContent().get(0).getValue();
            }
        }
        register(user,password);
    }

    private static void register(String user, String password) throws IOException {
        JsonArray usersDataBase = loadUserDataBase();
        JsonArray usersDataBaseCopy = usersDataBase.deepCopy();
        boolean isNew = true;
        Document xmlresponse;
        for (int i = 0; i < usersDataBase.size(); i++){
            JsonObject element = (JsonObject) usersDataBase.get(i);
            if (element.get("User").getAsString().equals(user)){
                isNew = false;
                System.out.println("Error");
                break;

            }

        }
        if (isNew) {
            JsonObject newSong = new JsonObject();
            newSong.addProperty("User", user);
            newSong.addProperty("Password", password);
            usersDataBase.add(newSong);
            usersDataBase.addAll(usersDataBaseCopy);

            xmlresponse = new Document();
            Element data = new Element("Data");
            xmlresponse.setRootElement(data);
            Element reply = new Element("Reply");
            reply.addContent("Granted");
            data.addContent(reply);
            Server.getServerInstance().send(xmlresponse);

            saveUsersDataBase(usersDataBase);


        }else{

            xmlresponse = new Document();
            Element data = new Element("Data");
            xmlresponse.setRootElement(data);
            Element reply = new Element("Reply");
            data.addContent(reply);
            reply.addContent("Block");

            Server.getServerInstance().send(xmlresponse);


        }


    }

    private static void saveUsersDataBase(JsonArray usersDataBase) throws IOException {
        File file = new File("src/Users.JSON");
        FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
        String dtabase = usersDataBase.toString();
        fileWriter.write(dtabase);
        fileWriter.flush();

    }

    private static JsonArray loadUserDataBase() throws FileNotFoundException {
        JsonParser parser  = new JsonParser();
        File file = new File("src/Users.JSON");


        JsonArray dataBase = (JsonArray) parser.parse(new FileReader(file));

        return dataBase;
    }

    public static void access(List listaElementos) throws IOException {
        String password = "";
        String user = "";

        for (int i = 0; i< listaElementos.size(); i++) {
            Element element = (Element) listaElementos.get(i);


            if (element.getName().contains("Password")) {

                password = element.getContent().get(0).getValue();

            } else if (element.getName().contains("User")) {

                user = element.getContent().get(0).getValue();
            }

        }
        access(user,password);
    }

    private static void access(String user, String password) throws IOException {

        JsonArray usersDataBase = loadUserDataBase();
        Document xmlresponse = null;

        for (int i = 0; i < usersDataBase.size(); i++){

            JsonObject userDataBase = (JsonObject) usersDataBase.get(i);

            if (userDataBase.get("User").getAsString().equals(user)){
                if (userDataBase.get("Password").getAsString().equals(password)){
                    xmlresponse = new Document();
                    Element data = new Element("Data");
                    xmlresponse.setRootElement(data);
                    Element reply = new Element("Reply");
                    reply.addContent("Granted");
                    data.addContent(reply);
                    Server.getServerInstance().send(xmlresponse);
                    break;
                }
            }

        }
        if (xmlresponse == null){
            xmlresponse = new Document();
            Element data = new Element("Data");
            xmlresponse.setRootElement(data);
            Element reply = new Element("Reply");
            reply.addContent("Block");
            data.addContent(reply);
            Server.getServerInstance().send(xmlresponse);

        }

    }
    public static void retrieveUsers() throws IOException {
        JsonArray usersDataBase = loadUserDataBase();
        Document xml = new Document();
        Element data = new Element("Data");
        String userName = "";
        xml.setRootElement(data);
        for (int i = 0; i < usersDataBase.size(); i++){

            JsonObject user = (JsonObject) usersDataBase.get(i);
            userName = user.get("User").getAsString();
            Element element = new Element("User");
            element.addContent(userName);
            data.addContent(element);
            System.out.println(userName);
        }
        Server.getServerInstance().send(xml);
        System.out.println(xml.toString());
    }

}
