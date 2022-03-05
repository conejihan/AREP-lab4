package edu.escuelaing.arep.persistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class PersistenceService {
    private final String firebaseURL = "https://firebasestorage.googleapis.com/v0/b/ejemploxd-2f8bf.appspot.com/o/hello.json?alt=media";


    public String getGreeting(String name) throws IOException {
        String greeting;

            URL url = new URL(firebaseURL);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            greeting = bufferedReader.readLine();

        greeting = greeting.replace("\"", "");
        return greeting + " " + name;
    }
}
