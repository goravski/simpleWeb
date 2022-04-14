package by.gsu.epamlab.webshop.model;

import java.time.LocalDate;

public class Order {
    private int id;
    private Product product;
    private int orderQuantity;
    private int cartId;
    LocalDate date;

    public Order() {
    }

    public Order(int orderQuantity, Product product, LocalDate date) {
        this.orderQuantity = orderQuantity;
        this.product = product;
        this.date = date;
    }

    public Order(int orderQuantity, Product product, int cartId, LocalDate date) {
        this(orderQuantity, product, date);
        this.cartId = cartId;
    }

    public Order(int cartId, Order oldOrder) {
        this.cartId = cartId;
        product = oldOrder.getProduct();
        orderQuantity = oldOrder.getOrderQuantity();
        date = oldOrder.getDate();
    }

    public int getId() {
        return id;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getCartId() {
        return cartId;
    }

    public Byn getCost() {
        return product.getPrice().mul(orderQuantity, RoundMethod.ROUND, 0);
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isValid() {
        if (!product.isValid() || orderQuantity <= 0 ) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        return String.format("OrderId=%s;product=%s;quantity=%s;date=$s", id, product,orderQuantity,date);
    }
}
