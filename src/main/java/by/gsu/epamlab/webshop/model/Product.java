package by.gsu.epamlab.webshop.model;


public class Product {
    private int idProduct;
    private String productName;
    private Byn price;
    private String describe;

    public Product() {
        productName = "";
        price = new Byn(0);
        describe = "";
    }

    public Product(String name, Byn price, String describe) {
        this.productName = name;
        this.price = price;
        this.describe = describe;

    }

    public Product(int idProduct, String name, Byn price, String describe) {
        this(name, price, describe);
        this.idProduct = idProduct;
    }

    public Product(String strProduct) {
        String[] array = strProduct.split(";");
        if (array.length == 4) {
            idProduct = Integer.parseInt(array[0]);
            productName = array[1];
            price = new Byn((int) Double.parseDouble(array[2]) * 100);
            describe = array[3];
        } else {
            throw new IllegalArgumentException("Wrong number of arguments for constructor");
        }
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

    public boolean isValid() {
        if (idProduct <= 0 || productName.equals("") || price.getValue() <= 0 || describe.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%s;%s", idProduct, productName, price, describe);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return getProductName().equals(product.getProductName()) && getPrice().equals(product.getPrice());
    }
}
