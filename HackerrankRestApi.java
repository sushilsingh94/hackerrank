import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;


import com.google.gson.*;
import java.net.*;
import java.util.*;


Call the rest API and parse the response and search from the response based on the input query criteria below.
Search should be case sensitive.



Input 
["username","EQUALS", "vinayk"] 3
["username","IN", "Kolkata, Mumbai"] 3
["address.city","EQUALS", "Mumbai"] 3

Output:
reutrn list of Ids for the macthing user

Sample 1
Input : ["username","EQUALS", "vinayk"] 3
Output: [1]

Sample 2
Input : ["username","IN", "Kolkata, Mumbai"] 3
Output: [1, 2]

class Result {

private static final String userApi = "https://raw.githubusercontent.com/arcjsonapi/ApiSampleData/master/api/users";
    /*
     * Complete the 'apiResponseParser' function below.
     *
     * The function is expected to return an INTEGER_ARRAY.
     * The function accepts following parameters:
     *  1. STRING_ARRAY inputList
     *  2. INTEGER size
     */

    public static List<Integer> apiResponseParser(List<String> inputList, int size) {
        List<Integer> list = new ArrayList<>();
        String apiResponse = getAPIResponse();
        
        
        JsonArray jsonArray = new Gson().fromJson(apiResponse, JsonArray.class);
        
        
        int count = 0;
        for(final JsonElement element : jsonArray){
            
            String[] inputName = inputList.get(0).split("[.]");
            String type = inputList.get(1);
            String[] values = inputList.get(2).split(","); 
            
            if("EQUALS".equals(type)){
                checkIfHasElementValue(inputName, element, values[0], list);
                
            }else if ("IN".equals(type)){
                for(int i=0; i<values.length; i++){
                    //System.out.println(values[i] + ", i:"+i);
                    checkIfHasElementValue(inputName, element, values[i], list);
                }
            }
            
        }
        
        if(list.isEmpty()){
            list.add(-1);
        }
        return list;
    }
    
    private static void checkIfHasElementValue(String[] inputName, JsonElement element, String values, List<Integer> list){
        if(inputName.length == 1){
            String val = element.getAsJsonObject().get(inputName[0]).getAsString();
            if(val != null && val.equals(values)){
                list.add(element.getAsJsonObject().get("id").getAsInt());
            }
        }else if(inputName.length == 2){
            String val = element.getAsJsonObject().get(inputName[0]).getAsJsonObject().get(inputName[1]).getAsString();
            if(val != null && val.equals(values)){
                list.add(element.getAsJsonObject().get("id").getAsInt());
            }
        }
    }
    
    private static String getAPIResponse(){
        StringBuilder sb = new StringBuilder();
        try{
            URL url = new URL(userApi);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.addRequestProperty("Content-Type", "application/json");
            
            int statusCode = con.getResponseCode();
            if(statusCode < 200 || statusCode >= 300){
                throw new IOException("Error in reading data, StatusCode:"+statusCode);
                
            }
            
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line="";
            
            while((line = br.readLine()) != null){
                sb.append(line);
            }
            br.close();
            con.disconnect();
        }catch(Exception e){
            e.printStackTrace();
        }
        return sb.toString();
        
    }

}

class Input{
    private String name;
    private Type type;
    private List<String> value;
}

enum Type{
    EQUALS, IN
}

class User{
    private int id;
    private String name;
    private String username;
    private String email;
    private String website;
    private Address address;
    private Company company;
}

class Address{
    private String street;
    private String suite;
    private String city;
    private String zipcode;
    private Geo geo;
}

class Geo{
    private String lat;
    private String lng;
}
class Company{
    private String name;
    private String basename;
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int inputListCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<String> inputList = IntStream.range(0, inputListCount).mapToObj(i -> {
            try {
                return bufferedReader.readLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        })
            .collect(toList());

        int size = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> result = Result.apiResponseParser(inputList, size);

        bufferedWriter.write(
            result.stream()
                .map(Object::toString)
                .collect(joining("\n"))
            + "\n"
        );

        bufferedReader.close();
        bufferedWriter.close();
    }
}




Sample Response:
[
  {
    "id": 1,
    "name": "Vinay Kumar",
    "username": "vinayk",
    "email": "vinayk@abcu.com",
    "address": {
      "street": "random1",
      "suite": "APR",
      "city": "Mumbai",
      "zipcode": "192008-13874",
      "geo": {
        "lat": "-17.3159",
        "lng": "91.1496"
      }
    },
    "website": "seuinfra.org",
    "company": {
      "name": "sec infra",
      "basename": "seu infra tech"
    }
  },
  {
    "id": 2,
    "name": "Anandita Basu",
    "username": "PrernaB",
    "email": "Anandita.b@abc1f.cpm",
    "address": {
      "street": "Hawroh Bridge",
      "suite": "ATY",
      "city": "Kolkata",
      "zipcode": "700001",
      "geo": {
        "lat": "-67.3159",
        "lng": "91.8006"
      }
    },
    "website": "techInfar.org",
    "company": {
      "name": "tech infar world",
      "basename": "seu infra tech"
    }
  }
  ]
