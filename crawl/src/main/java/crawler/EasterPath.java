package crawler;

/**
 * Created by kp on 2020/2/21.
 */
public enum EasterPath {

    HOMEPATH("https://www.eastmoney.com/"),

    STOCKDATAPATH("http://pdfm.eastmoney.com/EM_UBG_PDTI_Fast/api/js");
    private String path;

    EasterPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
