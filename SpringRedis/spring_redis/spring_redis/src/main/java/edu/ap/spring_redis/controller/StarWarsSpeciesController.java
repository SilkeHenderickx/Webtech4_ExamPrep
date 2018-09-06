package edu.ap.spring_redis.controller;

import edu.ap.spring_redis.model.StarWarsSpecies;
import edu.ap.spring_redis.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

@Controller
@Scope("session")
public class StarWarsSpeciesController {

    private RedisService service;

    @Autowired
    public void setRedisService(RedisService service) {
        this.service = service;
    }

    public StarWarsSpeciesController() {    }

    @RequestMapping("/allspecies")
    @ResponseBody
    public String getAllSpecies(){
        saveAllSpecies();

        List<StarWarsSpecies> s = new ArrayList<>();
        Set<String> keys = service.keys("species:*");
        for(String key : keys) {
            String[] parts = key.split(":");

                s.add(new StarWarsSpecies(parts[1], parts[2], parts[3]));
        }

        StringBuilder b = new StringBuilder();
        b.append("<html><body><table>");
        b.append("<tr><th>Name</th><th>Classification</th><th>Language</th></tr>");

        for(StarWarsSpecies sp : s) {
            b.append("<tr>");
            b.append("<td>");
            b.append(sp.getName());
            b.append("</td>");
            b.append("<td>");
            b.append(sp.getClassification());
            b.append("</td>");
            b.append("<td>");
            b.append(sp.getLanguage());
            b.append("</td>");
            b.append("<td>");
        }
        b.append("</table></body></html>");

        return b.toString();
    }

    @RequestMapping("/speciesbylanguage")
    @ResponseBody
    public String getAllSpecies(@RequestParam("language") String language) {
        saveAllSpecies();

        List<StarWarsSpecies> s = new ArrayList<>();
        Set<String> keys = service.keys("species:*");
        for (String key : keys) {
            String[] parts = key.split(":");

            s.add(new StarWarsSpecies(parts[1], parts[2], parts[3]));
        }
        List<StarWarsSpecies> listByLanguage = s.stream().filter(n -> n.getLanguage().contains(language)).collect(Collectors.toList());

        StringBuilder b = new StringBuilder();
        b.append("<html><body><table>");
        b.append("<tr><th>Name</th><th>Classification</th><th>Language</th></tr>");

        for(StarWarsSpecies sp : listByLanguage) {
            b.append("<tr>");
            b.append("<td>");
            b.append(sp.getName());
            b.append("</td>");
            b.append("<td>");
            b.append(sp.getClassification());
            b.append("</td>");
            b.append("<td>");
            b.append(sp.getLanguage());
            b.append("</td>");
            b.append("</tr>");
        }
        b.append("</table></body></html>");

        return b.toString();
    }


    public void saveAllSpecies(){

        String json = "";
        try {
            json = jsonGetRequest();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject rootObj = reader.readObject();
        JsonArray array = rootObj.getJsonArray("results");

        String pattern = "species:*";
        Set<String> keys = service.keys(pattern);
        if(keys.size() == 0) {
            for(int i = 0; i <= array.size()-1; i++) {


                JsonObject a = array.getJsonObject(i);
                StarWarsSpecies s = new StarWarsSpecies(a.getJsonString("name").toString(), a.getJsonString("classification").toString(), a.getJsonString("language").toString());
                service.setKey(("species:" + s.getName() + ":" + s.getClassification() + ":" + s.getLanguage()), "");
            }
            System.out.println("Species saved");
        }
        else {
            System.out.println("Species already exists.");
        }

    }

    public static String jsonGetRequest() throws Exception {
        String url = "https://swapi.co/api/species/?format=json";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //print in String
        return response.toString();
    }

}
