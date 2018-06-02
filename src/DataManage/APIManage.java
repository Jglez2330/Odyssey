package DataManage;

import de.umass.lastfm.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public class APIManage {
    private static String keyAPI ="5b7e0f1f1900de026e5a71ad5a84e41b";
    private static String user ="Glichet";
    private static String secret ="61e75240ea8ca1859100e89bca5d5b0f";


    public String encodeField(String in) throws UnsupportedEncodingException {
        // usually, this would be
        //  return java.net.URLEncoder.encode(in, "UTF-8");
        // but this site *appears* to use a non-standard mapping
        return java.net.URLEncoder.encode(in.replace(' ', '_'), "UTF-8");
    }

    public String getLyricsText(String artist, String song) throws IOException {
        // construct the REST query URL
        String query = "http://lyrics.wikia.com/api.php?func=getSong&artist="
                + encodeField(artist)
                + "&song="
                + encodeField(song)
                + "&fmt=text";
        // open the URL and get a stream to read from
        java.net.URL url = new java.net.URL(query);
        java.io.InputStream is = url.openStream();
        // get the text from the stream as lines
        java.io.BufferedReader reader = new java.io.BufferedReader(new InputStreamReader(is,"UTF-8"));
        StringBuilder buf = new StringBuilder();
        String s;
        while ( (s = reader.readLine()) != null )
            buf.append(s).append('\n');
        // return the lines
        return buf.toString();
    }


}
