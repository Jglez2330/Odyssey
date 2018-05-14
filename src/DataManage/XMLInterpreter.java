package DataManage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

    private static void saveDataBase(JsonArray dataBase) throws IOException {
        File file = new File("src/DataBase.JSON");
        FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
        String dtabase = dataBase.toString();
        fileWriter.write(dtabase);
        fileWriter.flush();
    }

    private static JsonArray loadDataBase() throws FileNotFoundException {
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
}