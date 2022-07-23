package sg.edu.np.MulaSave;

public class Notification {
    private String userid;
    private String text;
    private String productid;
    private boolean isproduct;

    public Notification (String userid, String text, String productid, boolean isproduct){
        this.userid = userid;
        this.text = text;
        this.productid = productid;
        this.isproduct = isproduct;
    }
    public Notification(){
    }

    public String getUserid(){
        return userid;
    }

    public void setUserid(String userid){
        this.userid = userid;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getProductid(){
        return productid;
    }

    public boolean isIsproduct()
    {
        return isproduct;
    }

}
