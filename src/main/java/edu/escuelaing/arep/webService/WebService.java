package edu.escuelaing.arep.webService;

import edu.escuelaing.arep.persistence.PersistenceService;
import edu.escuelaing.arep.spring.components.PathVariable;
import edu.escuelaing.arep.spring.components.RequestMapping;

import java.io.IOException;

public class WebService {

    private static final PersistenceService persistenceService = new PersistenceService();


    @RequestMapping(value = "/hello")
    public static String index(@PathVariable String value) throws IOException {
        return persistenceService.getGreeting(value) + " Saludos desde el Laboratorio 3";
    }
}
