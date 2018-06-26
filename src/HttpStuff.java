import com.sun.jndi.toolkit.url.Uri;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HttpStuff {
    public static void main(String[] args) throws Exception {

        URL url = new URL("http://smieszne-koty.herokuapp.com/oauth/token?grant_type=password&email=etnaya@wp.pl&password=DUJ-8h9-LDb-PW3");
        //URL url = new URL("http://smieszne-koty.herokuapp.com/api/kittens?access_token=a7fa0fcb9169e6309cf05c0433246d9c2079b1472b9dfd22d16480ee166d52f9");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        //connection.setRequestMethod("GET");
        connection.setReadTimeout(30000);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = input.readLine()) != null)
            response.append(inputLine);

        input.close();
        System.out.println(response);

        JSONObject autoryzacja = new JSONObject(response.toString());
        //System.out.println("Token = " + autoryzacja.getString("access_token"));

        String token = autoryzacja.getString("access_token");
        url = new URL("http://smieszne-koty.herokuapp.com/api/kittens" + "?access_token=" + token);

        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setReadTimeout(30000);

        input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        response = new StringBuilder();

        while ((inputLine = input.readLine()) != null)
            response.append(inputLine);

        input.close();
        System.out.println(response);

        //var
        JSONArray kotki = new JSONArray(response.toString());

        for (int i = 0; i < kotki.length(); i++) {
            JSONObject kotek = kotki.getJSONObject(i);
            System.out.println(kotek.getString("name"));
        }


    }


    public String getToken(String email, String password) throws IOException {
        URL url = new URL("http://smieszne-koty.herokuapp.com/oauth/token?grant_type=password&email=" + email + "&password=" + password);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setReadTimeout(30000);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = input.readLine()) != null)
            response.append(inputLine);

        input.close();
        //System.out.println(response);

        JSONObject autoryzacja = new JSONObject(response.toString());
        String token = autoryzacja.getString("access_token");

        return token;
    }

    public List<Map<String, String>> getKittens(String token, String page) throws IOException {
        URL url = new URL("http://smieszne-koty.herokuapp.com/api/kittens" + "?access_token=" + token + page);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setReadTimeout(30000);

        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = input.readLine()) != null)
            response.append(inputLine);

        input.close();
        //System.out.println(response);

        JSONArray kotki = new JSONArray(response.toString());

        List<Map<String, String>> myList = new ArrayList<>();
        Map<String, String> myMap;

        for (int i = 0; i < kotki.length(); i++) {
            JSONObject kotek = kotki.getJSONObject(i);

            myMap = new LinkedHashMap();
            myMap.put("name", kotek.getString("name"));
            myMap.put("url", kotek.getString("url"));
            myMap.put("votes", String.valueOf(kotek.getInt("vote_count")));
            myList.add(myMap);
        }
        return myList;
    }

}
