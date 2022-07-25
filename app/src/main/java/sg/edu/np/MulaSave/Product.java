package sg.edu.np.MulaSave;

import java.io.Serializable;

public class Product implements Serializable {
    private String asin;
    private String title;
    private String category;
    private Double price;
    private String image;
    private String link;
    private Float rating;
    private String website;
    private String desc;
    private String condition;
    private String meetup;
    private String sellerUid;

    public Product() {//default constructors
    }

    public Product(String _asin, String _title, String _category, Double _price, String _image, String _link, Float _rating, String _website, String _desc, String _condition, String _meetup, String _sellerUid){
        this.asin = _asin;
        this.title = _title;
        this.category = _category;
        this.price = _price;
        this.image = _image;
        this.link = _link;
        this.rating = _rating;
        this.website = _website;
        this.desc = _desc;
        this.condition = _condition;
        this.meetup = _meetup;
        this.sellerUid = _sellerUid;
    }


    public void setAsin(String _asin){
        this.asin = _asin;
    }
    public String getAsin() {
        return this.asin;
    }
    public void setTitle(String _title) {
        this.title = _title;
    }
    public String getTitle() {
        return this.title;
    }
    public void setCategory(String _category){
        this.category = _category;
    }
    public String getCategory(){
        return this.category;
    }
    public void setPrice(double _price){
        this.price = _price;
    }
    public Double getPrice(){ return this.price; }
    public void setImageUrl(String _imageUrl){
        this.image = _imageUrl;
    }
    public String getImageUrl(){
        return this.image;
    }
    public void setLink(String _link){
        this.link = _link;
    }
    public String getLink(){
        return this.link;
    }
    public void setRating(Float _rating){ this.rating = _rating; }
    public Float getRating(){ return this.rating; }
    public void setWebsiteName(String _website){
        this.website = _website;
    }
    public String getWebsite(){
        return this.website;
    }
    public void setDesc(String _desc){
        this.desc = _desc;
    }
    public String getDesc(){
        return this.desc;
    }
    public void setCondition(String _condition){
        this.condition = _condition;
    }
    public String getCondition(){
        return this.condition;
    }
    public void setMeetup(String _meetup){
        this.meetup = _meetup;
    }
    public String getMeetup(){
        return this.meetup;
    }
    public void setSellerUid(String _sellerUid){
        this.sellerUid = _sellerUid;
    }
    public String getSellerUid(){
        return this.sellerUid;
    }
}
