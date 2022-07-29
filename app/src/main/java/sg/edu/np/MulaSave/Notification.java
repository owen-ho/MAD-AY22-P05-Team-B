package sg.edu.np.MulaSave;

public class Notification {
    // Private Variables
    private String userid; // userid is the variable to differentiate between different users
    private String text; //content of the notification
    private String productid;//id of product that is relevant
    private boolean isproduct;

    // Constructor for Class
    public Notification (String userid, String text, String productid, boolean isproduct){
        this.userid = userid;
        this.text = text;
        this.productid = productid;
        this.isproduct = isproduct;
    }
    public Notification(){
    }
    //Getter and Setting for Class
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
