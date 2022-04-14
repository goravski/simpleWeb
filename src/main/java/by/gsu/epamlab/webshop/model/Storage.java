package by.gsu.epamlab.webshop.model;

public class Storage {
    private int idStorage;
    private int quantity;
    private int productId;

    public Storage() {
    }

    public Storage(int productId, int quantity) {
        this.quantity = quantity;
        this.productId = productId;

    }

    public Storage(int idStorage, int product, int quantity) {
        this(product, quantity);
        this.idStorage = idStorage;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getProductId() {
        return productId;
    }

    public int getIdStorage() {
        return idStorage;
    }

    public boolean isValid() {
        if (productId <= 0 || quantity <0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        return String.format("id=%s;idProd=%s,quantity=%s", idStorage,productId,quantity);
    }
}
