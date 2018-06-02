package DataManage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import DataStructuress.AVLTree;
import DataStructuress.BTree;
import DataStructuress.SplayTree;
import DataStructuress.Tree;
import Socket.Server;

public class XMLInterpreter {

    private static int page = 0;
    private static int pageCant = 0;
    private static String songPlaying = "";
    private static String artistPlaying = "";
    private static int actualChunk = 0;
    private static String loggedUser = "";
    
    private static Tree bTree = new AVLTree();
    private static Tree splayTree = new SplayTree();
    private static Tree avlTree = new AVLTree();


    private static void addSong(String style, String nameSong,String nameArtist, String nameAlbum, String year, String lyrics, String path,byte[] songBytes) throws IOException {
        JsonArray dataBase = loadDataBase();
        //JsonArray dataBaseCopy = dataBase.deepCopy();
        JsonObject newSong = new JsonObject();
        newSong.addProperty("Style",style);
        newSong.addProperty("Song",nameSong);
        newSong.addProperty("Artist",nameArtist);
        newSong.addProperty("Album",nameAlbum);
        newSong.addProperty("Year",Integer.parseInt(year));
        newSong.addProperty("Lyrics",lyrics);
        newSong.addProperty("SongPath",path);
        dataBase.add(newSong);
        //dataBase.addAll(dataBaseCopy);

        Files.write(Paths.get(path),songBytes);



        saveDataBase(dataBase);
        
        avlTree.buildTree();
        bTree.buildTree();
        splayTree.buildTree();






    }
    public static Document getSongsXML(int index) throws IOException {
        JsonArray dataBase = loadDataBase();
        pageCant = dataBase.size()/4;
        System.out.println(pageCant);

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
        int count = Math.abs(pageCant*index*4);
        int cantObjects = 0;
        for (int i = count; i < dataBase.size(); i ++ ) {
            if (cantObjects == 4){
                break;
            }

            JsonObject cancion = (JsonObject) dataBase.get(i);
            style = cancion.get("Style").getAsString();
            nameSong = cancion.get("Song").getAsString();
            nameArtist = cancion.get("Artist").getAsString();
            nameAlbum = cancion.get("Album").getAsString();
            year = cancion.get("Year").getAsString();
            lyrics = cancion.get("Lyrics").getAsString();
            path = cancion.get("SongPath").getAsString();

            //Element songData = new Element("SongData");
            //String songDataString = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(path)));

            //songData.setText(songDataString.substring(0,songDataString.length()/3));
            //System.out.println(songDataString.length()/3%4);

            Element songData = new Element("SongData");
            Element styleElement = new Element("Style").setText(style);
            Element songNameElement = new Element("SongName").setText(nameSong);
            Element artistElement = new Element("ArtistName").setText(nameArtist);
            Element albumElement = new Element("AlbumName").setText(nameAlbum);
            Element yearElement = new Element("Year").setText(year);
            Element lyricsElement = new Element("Lyrics").setText(lyrics);
            Element pathElement = new Element("Path").setText(path);
            //String songDataString = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(path)));
            //Element songString = new Element("SongString").setText(songDataString.substring(0,songDataString.length()/12));

            songData.addContent(styleElement);
            songData.addContent(songNameElement);
            songData.addContent(artistElement);
            songData.addContent(albumElement);
            songData.addContent(yearElement);
            songData.addContent(lyricsElement);
            songData.addContent(pathElement);
            //songData.addContent(songString);

            //System.out.println(songDataString.substring(0,songDataString.length()/20));




            cantObjects++;
            data.addContent(songData);




        }
        System.out.println(xml.getRootElement().getContent());
        System.out.println(Server.getServerInstance().getClient().getSendBufferSize());
        System.out.println(Server.getServerInstance().getClient().getSendBufferSize());
        Server.getServerInstance().send(xml);
        return xml;

    }



    public static void saveDataBase(JsonArray dataBase) throws IOException {
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

        File file = new File("Music" + "/" + songName + ".mp3");
        if (!file.exists()){
            file.createNewFile();
        }//if (!file2.exists()){
          //  file2.createNewFile();
        //}


        addSong(style,songName,artistName,albumName,year,lyrics,"Music" + "/" + songName + ".mp3", songBytes);
    }
    public static void deleteSong(List listaElementos) throws IOException {
        String name = "";
        String artist = "";
        for (int i = 0; i < listaElementos.size(); i++){
            Element element = (Element) listaElementos.get(i);
            if (element.getName().equals("SongName")){
                name  = element.getContent().get(0).getValue();
            }else if (element.getName().equals("Artist")){
                  artist = element.getContent().get(0).getValue();
            }

        }
        deleteSong(name,artist);

    }

    public static void deleteSong(String name, String artist) throws IOException {
        JsonArray dataBase = loadDataBase();
        JsonArray newDataBase = new JsonArray();

        for (int i = 0; i < dataBase.size();i++){
            JsonObject jsonSong = (JsonObject) dataBase.get(i);
            if (jsonSong.get("Song").getAsString().equals(name) && jsonSong.get("Artist").getAsString().equals(artist)){
                Files.delete(Paths.get(jsonSong.get("SongPath").getAsString()));

            }else{
                newDataBase.add(jsonSong);
            }

        }
        saveDataBase(newDataBase);
        avlTree.buildTree();
        bTree.buildTree();
        splayTree.buildTree();
    }


    public static void register(List listaElementos) throws IOException {
        String user = "";
        String password ="";
        String style ="";
        String name ="";
        String age ="";
        String friends ="";

        for (int i = 0; i< listaElementos.size(); i++){
            Element element = (Element) listaElementos.get(i);
            if (element.getName().contains("User")){

                user = element.getContent().get(0).getValue();

            }else if(element.getName().contains("Password")){

                password = element.getContent().get(0).getValue();
            }else if(element.getName().contains("Style")){

                style = element.getContent().get(0).getValue();
            }else if(element.getName().contains("Name")){

                name = element.getContent().get(0).getValue();
            }else if(element.getName().contains("Age")){

                age = element.getContent().get(0).getValue();
            }else if(element.getName().contains("Friends")){

                friends = element.getContent().get(0).getValue();
            }
        }
        register(user,password,style,name,age,friends);
    }
    public Document searchSong(String type, String value) {
    	JsonObject json = null;
    	if(type.equals("Album")) {
    		json = splayTree.search(value);
    	}else if(type.equals("Artist")) {
    		json = avlTree.search(value);
    	}else if(type.equals("Song")) {
    		json = bTree.search(value);
    	}else {
    		return null;
    	}
    	
    	Document xml = new Document();
        Element data = new Element("Data");
        xml.setRootElement(data);
        String style = json.get("Style").getAsString();
        String nameSong = json.get("Song").getAsString();
        String nameArtist = json.get("Artist").getAsString();
        String nameAlbum = json.get("Album").getAsString();
        String year = json.get("Year").getAsString();
        String lyrics = json.get("Lyrics").getAsString();
        String path = json.get("SongPath").getAsString();
        
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

        
    	return xml;
    	
    }
        


    private static void register(String user, String password,String style,String name, String age, String friends) throws IOException {
        JsonArray usersDataBase = loadUserDataBase();
        //JsonArray usersDataBaseCopy = usersDataBase.deepCopy();
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
            newSong.addProperty("Style", style);
            newSong.addProperty("Name", name);
            newSong.addProperty("Age", age);
            newSong.addProperty("Friends", friends);
            newSong.addProperty("Messages", "");
            loggedUser = user;

            usersDataBase.add(newSong);
            //usersDataBase.addAll(usersDataBaseCopy);

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
                    loggedUser = user;
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

    public static void getSongsXML(List listaElementos) throws IOException {
        int index = 0;
        for (int i = 0; i < listaElementos.size(); i++){
            Element element = (Element) listaElementos.get(i);
            if (element.getName().contains("Index")){
                index = Integer.parseInt(element.getContent().get(0).getValue());
            }
        }
        getSongsXML(index);
    }

    public static void getRequestedSong(List listaElementos) throws IOException {
        String song = "";
        String artist = "";
        for (int i = 0; i < listaElementos.size(); i++) {
            Element element = (Element) listaElementos.get(i);
            if (element.getName().contains("Song")) {
                song = element.getContent().get(0).getValue();
            } else if (element.getName().contains("Artist")) {
                artist = element.getContent().get(0).getValue();
            }
        }
        getRequestedSong(song,artist);
    }


    public static void streamingSong() throws IOException {
        JsonArray dataBase = loadDataBase();

        for (int i = 0; i < dataBase.size();i++){
            JsonObject object = (JsonObject) dataBase.get(i);

            if (object.get("Song").equals(songPlaying) && object.get("Artist").getAsString().equals(artistPlaying)){

                Document xml = new Document();
                Element root = new Element("Data");
                xml.setRootElement(root);
                Element reply = new Element("Reply");
                String path = object.get("SongPath").getAsString();
                String stringSong = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(path)));
                if (stringSong.length() <stringSong.length()/10 * actualChunk )
                {
                    reply.setText("End");
                    songPlaying = "";
                    artistPlaying = "";
                }else {
                    stringSong = stringSong.substring(actualChunk * stringSong.length() , (actualChunk + 1) * stringSong.length() );

                    //stringSong = stringSong.substring(0,stringSong.length());
                    while (stringSong.length() % 4 != 0) {
                        stringSong = stringSong.substring(0, stringSong.length() - 1);
                    }
                    songPlaying = object.get("Song").getAsString();
                    artistPlaying = object.get("Artist").getAsString();
                    reply.setText(stringSong);

                    actualChunk++;
                }
                root.addContent(reply);

                Server.getServerInstance().send(xml);

            }
        }

    }


    private static void getRequestedSong(String song, String artist) throws IOException {
        JsonArray dataBase = loadDataBase();

        for (int i = 0; i < dataBase.size();i++){
            JsonObject object = (JsonObject) dataBase.get(i);

            if (object.get("Song").getAsString().equals(song) && object.get("Artist").getAsString().equals(artist)){
                Document xml = new Document();
                Element root = new Element("Data");
                xml.setRootElement(root);
                Element reply = new Element("Reply");
                String path = object.get("SongPath").getAsString();
                String stringSong = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(path)));
                stringSong = stringSong.substring(actualChunk*stringSong.length(),(actualChunk + 1)* stringSong.length());
                //stringSong = stringSong.substring(0,stringSong.length());
                while (stringSong.length() % 4 != 0){
                    stringSong = stringSong.substring(0,stringSong.length()-1);
                }

                songPlaying = object.get("Song").getAsString();
                artistPlaying = object.get("Artist").getAsString();
                reply.setText(stringSong);
                root.addContent(reply);
                Element length = new Element("Length");
                length.addContent((String.valueOf( Files.readAllBytes(Paths.get(path)).length)));
                root.addContent(length);
                Server.getServerInstance().send(xml);
                break;

            }
        }
    }


    public static void RequestSongInfo(List<Element> listaElementos) throws IOException {
        String song = "";
        String artist = "";
        for (int i = 0; i < listaElementos.size(); i++) {
            Element element = (Element) listaElementos.get(i);
             if (element.getName().contains("Artist")) {
                artist = element.getContent().get(0).getValue();
            }else if (element.getName().contains("Song")) {
                song = element.getContent().get(0).getValue();
            }
        }
        getRequestedSongInfo(song,artist);


    }

    private static void getRequestedSongInfo(String song, String artist) throws IOException {
        JsonArray dataBase = loadDataBase();

        for (int i  = 0; i < dataBase.size(); i++){
            JsonObject object = (JsonObject) dataBase.get(i);

            if (object.get("Song").getAsString().equals(song) && object.get("Artist").getAsString().equals(artist)){
                Document xml = new Document();
                Element data = new Element("Data");
                xml.setRootElement(data);
                Element reply = new Element("Reply");

                Element name = new Element("Name");
                Element artistName = new Element("Artist");
                Element album = new Element("Album");
                Element style = new Element("Style");
                Element year = new Element("Year");
                Element lyrics = new Element("Lyrics");

                name.setText(object.get("Song").getAsString());
                artistName.setText(object.get("Artist").getAsString());
                album.setText(object.get("Album").getAsString());
                style.setText(object.get("Style").getAsString());
                year.setText(object.get("Year").getAsString());
                lyrics.setText(object.get("Lyrics").getAsString());

                reply.addContent(name);
                reply.addContent(artistName);
                reply.addContent(album);
                reply.addContent(style);
                reply.addContent(year);
                reply.addContent(lyrics);

                data.addContent(reply);

                Server.getServerInstance().send(xml);
                break;



            }

        }
    }

    public static void Sort(List<Element> listaElementos) throws IOException {

        for (int i = 0; i < listaElementos.size(); i++){
            Element element = (Element) listaElementos.get(i);
            if (element.getName().equals("Sort")){
                String sort = element.getContent().get(0).getValue();

                if (sort.equals("Cancion")){
                    bTree.sort("Cancion");

                }else if (sort.contains("Artista")){

                    avlTree.sort("Artista");

                }else if (sort.contains("Album")){
                    splayTree.sort("Album");
                }
            }
        }

    }

    public static void editSong(List<Element> listaElementos) throws IOException {
        String nameOriginal = "";
        String artistOriginal = "";
        String name = "";
        String artist = "";
        String album = "";
        String style = "";
        int year = 0;
        String lyrics = "";
        for (int i  = 0; i < listaElementos.size(); i++){
            Element element  = (Element) listaElementos.get(i);
            if (element.getName().contains("OriginalName")){
                nameOriginal = element.getContent().get(0).getValue();
            }else  if (element.getName().contains("OriginalArtist")){
                artistOriginal = element.getContent().get(0).getValue();
            }else  if (element.getName().contains("Name")){
                name = element.getContent().get(0).getValue();
            }else  if (element.getName().contains("Artist")){
                artist = element.getContent().get(0).getValue();
            }else  if (element.getName().contains("Album")){
                album = element.getContent().get(0).getValue();
            }else  if (element.getName().contains("Style")){
                style = element.getContent().get(0).getValue();
            }else  if (element.getName().contains("Year")){
                year = Integer.parseInt(element.getContent().get(0).getValue());
            }else  if (element.getName().contains("Lyrics")){
                lyrics = element.getContent().get(0).getValue();
            }
        }
        editSong(nameOriginal,artistOriginal,name,artist,album,style,year,lyrics);
    }

    private static void editSong(String nameOriginal, String artistOriginal, String name, String artist, String album, String style, int year, String lyrics) throws IOException {
        JsonArray dataBase = loadDataBase();
        JsonArray newDataBase = new JsonArray();

        for (int i = 0; i < dataBase.size(); i++){
            JsonObject object = (JsonObject) dataBase.get(i);

            if (object.get("Song").getAsString().equals(nameOriginal) && object.get("Artist").getAsString().equals(artistOriginal)){
                JsonObject newData = new JsonObject();
                newData.addProperty("Style",style);
                newData.addProperty("Song",name);
                newData.addProperty("Artist",artist);
                newData.addProperty("Album",album);
                newData.addProperty("Year",year);
                newData.addProperty("Lyrics",lyrics);
                newData.addProperty("SongPath",object.get("SongPath").getAsString());
                newDataBase.add(newData);

            }else {
                newDataBase.add(object);
            }
        }
        saveDataBase(newDataBase);
        avlTree.buildTree();
        bTree.buildTree();
        splayTree.buildTree();
    }

    public static void getLyrics(List<Element> listaElementos) throws IOException {
        String name = "";
        String artist = "";
        for (int i = 0; i < listaElementos.size(); i++) {
            Element element = listaElementos.get(i);
            if (element.getName().equals("SongName")) {
                name = element.getContent().get(0).getValue();
            } else if (element.getName().equals("Artist")) {
                artist = element.getContent().get(0).getValue();
            }

        }
        getLyrics(name, artist);
    }

    private static void getLyrics(String name, String artist) throws IOException {
        JsonArray dataBase = loadDataBase();
        String lyrics = "";
        for (int i = 0; i < dataBase.size(); i++) {

            JsonObject object = dataBase.get(i).getAsJsonObject();

            if (object.get("Song").getAsString().equals(name) && object.get("Artist").getAsString().equals(artist)){
                lyrics = object.get("Lyrics").getAsString();
                break;

            }


        }
        Document xml = new Document();
        Element data = new Element("Data");
        xml.setRootElement(data);
        Element reply = new Element("Reply");
        reply.addContent(lyrics);
        data.addContent(reply);
        Server.getServerInstance().send(xml);



    }
    

    public static void recomendar(String song, String album) throws IOException {
        JsonArray dataBaseUsers = loadUserDataBase();
        JsonArray newDataBase = new JsonArray();

        String friends = "";
        for (int i = 0; i < dataBaseUsers.size(); i++) {
            JsonObject usuario = dataBaseUsers.get(i).getAsJsonObject();
            if (usuario.get("User").getAsString().equals(loggedUser)) {
                friends = usuario.get("Friends").getAsString();

                break;
            }
        }
        for (int i = 0; i < dataBaseUsers.size(); i++) {
            JsonObject object = dataBaseUsers.get(i).getAsJsonObject();
            if (friends.contains(object.get("User").getAsString())) {

                JsonObject newObject = new JsonObject();
                newObject.addProperty("User", object.get("User").getAsString());
                newObject.addProperty("Password", object.get("Password").getAsString());
                newObject.addProperty("Style", object.get("Style").getAsString());
                newObject.addProperty("Name", object.get("Name").getAsString());
                newObject.addProperty("Age", object.get("Age").getAsString());
                newObject.addProperty("Friends", object.get("Friends").getAsString());

                newObject.addProperty("Messages", "El usuario: " + loggedUser + ", le ha recomendado: " + song + " por " + album);
                newDataBase.add(newObject);


            } else {
                newDataBase.add(object);
            }
        }
        saveUsersDataBase(newDataBase);

    }

    public static void recomendar(List<Element> listaElementos) throws IOException {
        String song = "";
        String artist = "";


        for (int i = 0; i < listaElementos.size(); i++) {
            Element element = (Element) listaElementos.get(i);
            if (element.getName().equals("SongName")) {
                song = element.getContent().get(0).getValue();
            } else if (element.getName().contains("Artist")) {
                artist = element.getContent().get(0).getValue();
            }


        }
        recomendar(song, artist);

    }

    public static void deleteMessages() throws IOException {
        JsonArray dataBase = loadUserDataBase();
        JsonArray newDataBase = new JsonArray();

        for (int i = 0; i < dataBase.size(); i++) {
            JsonObject object = dataBase.get(i).getAsJsonObject();
            if (object.get("User").getAsString().equals(loggedUser)) {

                JsonObject newObject = new JsonObject();
                newObject.addProperty("User", object.get("User").getAsString());
                newObject.addProperty("Password", object.get("Password").getAsString());
                newObject.addProperty("Style", object.get("Style").getAsString());
                newObject.addProperty("Name", object.get("Name").getAsString());
                newObject.addProperty("Age", object.get("Age").getAsString());
                newObject.addProperty("Friends", object.get("Friends").getAsString());

                newObject.addProperty("Messages", "");
                newDataBase.add(newObject);


            } else {
                newDataBase.add(object);
            }
        }
        saveUsersDataBase(newDataBase);

    }

    public static void getMessages() throws IOException {
        JsonArray dataBase = loadUserDataBase();

        for (int i = 0; i < dataBase.size(); i++){
            JsonObject object = dataBase.get(i).getAsJsonObject();
            if (object.get("User").getAsString().equals(loggedUser)){
                Document xml = new Document();
                Element data = new Element("Data");
                xml.setRootElement(data);
                Element reply = new Element("Reply");
                reply.addContent(object.get("Messages").getAsString());
                data.addContent(reply);
                Server.getServerInstance().send(xml);
                break;


            }
        }
    }
}
