package edu.escuelaing.arep.spring.components;

public class Request {

    private final String endpoint;
    private final String value;

    public Request(String path) {
        int indexOfValues = path.indexOf("?");
        if (indexOfValues < 0) {
            this.endpoint = path;
            this.value = null;
        } else {
            String valuePath = path.substring(indexOfValues + 1);
            int indexOfValue = valuePath.indexOf("=");
            this.endpoint = path.substring(0, indexOfValues);
            this.value = valuePath.substring(indexOfValue + 1);
        }
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getValue() {
        return value;
    }
}
