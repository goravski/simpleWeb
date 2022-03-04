package by.gsu.epamlab.webshop.model;


public class Product {
    private int idProduct;
    private String productName;
    private Byn price;
    private Number quantity;
    private String describe;

    public Product() {
        productName = "";
        price = new Byn(0);
        quantity = 0.0;
        describe = "";
    }

    public Product(String name, Byn price, String describe, Number quantity) {
        this.productName = name;
        this.price = price;
        this.describe = describe;
        this.quantity = quantity;
    }

    public Product(int idProduct, String name, Byn price, String describe, Number quantity) {
        this(name, price, describe, quantity);
        this.idProduct = idProduct;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescribe() {
        return describe;
    }

    public Byn getPrice() {
        return price;
    }

    public Number getQuantity() {
        return quantity;
    }

    public Byn getCost() {
        return price.mul(quantity.doubleValue(), RoundMethod.ROUND, 0);
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%.2f;%s", productName, price, quantity, getCost());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return getProductName().equals(product.getProductName()) && getPrice().equals(product.getPrice());
    }
}
