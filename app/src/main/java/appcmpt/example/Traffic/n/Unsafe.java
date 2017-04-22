package appcmpt.example.Traffic.n;

/**
 * Created by hp on 4/13/2017.
 */

public class Unsafe {
    String username;
    String vehino;
    String date;
    String latlong;

    public Unsafe(String username, String vehino, String date, String latlong) {
        this.username = username;
        this.vehino = vehino;
        this.date = date;
        this.latlong = latlong;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVehino() {
        return vehino;
    }

    public void setVehino(String vehino) {
        this.vehino = vehino;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLatlong() {
        return latlong;
    }

    public void setLatlong(String latlong) {
        this.latlong = latlong;
    }
}
