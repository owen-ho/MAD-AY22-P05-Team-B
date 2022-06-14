package sg.edu.np.P05TeamB;

import java.io.Serializable;

public class Product implements Serializable {
    public String asin;
    public String title;
    public String category;
    public Double price;
    public String image;
    public String link;

    public Product() {
    }

    public Product(String asin, String title, String category, Double price, String image, String link){
        this.asin = asin;
        this.title = title;
        this.category = category;
        this.price = price;
        this.image = image;
        this.link = link;
    }
}
