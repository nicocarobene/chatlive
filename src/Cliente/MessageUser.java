package Cliente;

import java.io.Serializable;
import java.util.ArrayList;

public class MessageUser implements Serializable {
    private String name, message, ip;
    private ArrayList<String>ips;

    public MessageUser(String name, String message, String ip) {
        this.name = name;
        this.message = message;
        this.ip = ip;
    }

    public ArrayList<String> getIps() {
        return ips;
    }

    public void setIps(ArrayList<String> ips) {
        this.ips = ips;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
