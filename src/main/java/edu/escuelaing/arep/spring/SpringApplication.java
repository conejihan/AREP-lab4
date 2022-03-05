package edu.escuelaing.arep.spring;

import edu.escuelaing.arep.httpServer.HttpServer;
import edu.escuelaing.arep.spring.components.PathVariable;
import edu.escuelaing.arep.spring.components.RequestMapping;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class SpringApplication {
    private static final SpringApplication SpringApplication = new SpringApplication();
    private static boolean componentsLoaded = false;
    private final Map<String, Method> componentsRoute = new HashMap<>();

    private SpringApplication() {
        super();
    }

    public static void run(String[] args) throws ClassNotFoundException {
        if (!componentsLoaded) {
            SpringApplication.loadComponents(args);
            componentsLoaded = true;
            SpringApplication.startServer();
        }
    }

    private void startServer() {
        HttpServer httpServer = new HttpServer(componentsRoute);
        try {
            httpServer.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void loadComponents(String[] components) throws ClassNotFoundException {
        for (String component : components) {
            for (Method method : Class.forName(component).getMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    componentsRoute.put(method.getAnnotation(RequestMapping.class).value(), method);
                }
            }
        }
    }

    public static String invoke(Method staticMethod, String... args) throws InvocationTargetException, IllegalAccessException {
        String result;
        String argument = null;
        for (Parameter parameter : staticMethod.getParameters()) {
            if (parameter.isAnnotationPresent(PathVariable.class)) {
                argument = args[0];
            }
        }
        if (argument != null) {
            result = staticMethod.invoke(null, argument).toString();
        } else {
            result = staticMethod.invoke(null).toString();
        }

        return result;
    }
}
