package Request;

/**
 * Created by kp on 2020/2/26.
 */
public class StockDataRequest {

    private String token = "4f1862fc3b5e77c150a2b985b12db0fd";
    private long id;
    private String type = "r";
    private boolean iscr = false;
    private int rtntype = 5;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isIscr() {
        return iscr;
    }

    public void setIscr(boolean iscr) {
        this.iscr = iscr;
    }

    public int getRtntype() {
        return rtntype;
    }

    public void setRtntype(int rtntype) {
        this.rtntype = rtntype;
    }
}
