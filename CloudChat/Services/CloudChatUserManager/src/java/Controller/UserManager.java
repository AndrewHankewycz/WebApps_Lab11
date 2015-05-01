package Controller;

import Model.User;
import Util.Crypto;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebService;
 

@WebService
//@HandlerChain(file = "serviceHandler.xml")
public class UserManager {
    private final Gson gson = new Gson();
    private static final String CC_DAO_URL = "http://localhost:8080/CloudChatDAO/";
    
    @WebMethod
    public boolean registerUser(String username, String password) {
        boolean registered = false;
        String salt = UUID.randomUUID().toString();
        password = Crypto.sha1(password + salt);

        try {
            String params = formatParameters(new String[]{"action", "username", "password"}, 
                                             new String[]{"register", username, password});
            String res = executePost(CC_DAO_URL, params);
            System.out.println("response was: " + res);
            registered = true;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return registered;
    }
    
    @WebMethod
    public String userLogin(String username, String password) {
        String res = "";

        Logger.getLogger(UserManager.class.getName()).log(Level.INFO, null, "USER LOGIN ENDPOINT");
        
        try {
            String params = formatParameters(new String[]{"action", "username", "password"}, 
                                             new String[]{"login", username, password});
            // make post request to DAO
            res = executePost(CC_DAO_URL, params);
            
            int userId = -1;
            try{
                // parse userId response to an integer
                userId = Integer.parseInt(res);
            }catch(NumberFormatException ex){
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
                // something must have gone wrong with the lookup
            }
            
            // package userId and username in an object for JSON output
            res = gson.toJson(new User(userId, username));
            System.out.println("sending response: " + res);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return res;
    }
    
    @WebMethod
    public boolean userLogout(int userId) {
        boolean loggedOut = true;
        
        try {
            String params = formatParameters(new String[]{"action", "userId"}, 
                                             new String[]{"logout", Integer.toString(userId)});
            String res = executePost(CC_DAO_URL, params);
            System.out.println("response was: " + res);
        } catch (UnsupportedEncodingException ex) {
            loggedOut = false;
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return loggedOut;
    }

    private static String executePost(String targetURL, String urlParameters) {
        String url = "http://localhost:8080/CloudChatDAO";
        String res = "";
        try{
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Language", "en-US");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
        }
        in.close();

        res = response.toString();
        }catch(MalformedURLException ex){
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }catch(IOException ioe){
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ioe);
        }
        return res;
    }
    
    private String formatParameters(String[] keys, String[] values) throws UnsupportedEncodingException{
        // formats parameters for an HTTP POST
        int paramCount = Math.min(keys.length, values.length);
        String params = "";
        
        if(paramCount > 0){
            params += keys[0] + "=" + URLEncoder.encode(values[0], "UTF-8");
        }
        for(int i = 1; i < paramCount; i++){
            params += "&" + keys[i] + "=" + URLEncoder.encode(values[i], "UTF-8");
        }
        
        return params;
    }
}
