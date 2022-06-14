package sg.edu.np.P05TeamB;

public class Product {
    private int id;
    private String name;
    private double price;
    private float rating;
    private String websiteName;

    public Product(){}//default constructor

    public Product(Integer _id, String _name, Double _price, Float _rating, String _websiteName){
        this.id = _id;
        this.name = _name;
        this.price = _price;
        this.rating = _rating;
        this.websiteName = _websiteName;
    }

    //methods to access class since class is private
    public void setId(int id){
        this.id = id;
    }
    public int getID() {
        return this.id;
    }
    public void setProductName(String prodName) {
        this.name = prodName;
    }
    public String getProductName() {
        return this.name;
    }
    public void setPrice(double price){
        this.price = price;
    }
    public double getPrice(){
        return this.price;
    }
    public void setRating(Float rating){
        this.rating = rating;
    }
    public float getRating(){
        return  this.rating;
    }
    public void setWebsiteName(String websiteName){
        this.websiteName = websiteName;
    }
    public String getWebsiteName(){
        return websiteName;
    }

}
