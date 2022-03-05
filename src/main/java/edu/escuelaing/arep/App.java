package edu.escuelaing.arep;

import edu.escuelaing.arep.spring.SpringApplication;

public class App {
    public static void main(String[] args) throws ClassNotFoundException {
        if (args.length == 0) {
            String[] path = {"edu.eci.arep.demoService.WebService"};
            SpringApplication.run(path);
        } else {
            SpringApplication.run(args);
        }
    }
}
