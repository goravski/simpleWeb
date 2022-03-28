package by.gsu.epamlab.webshop.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    int id;
    private int personId;
    private List<Order> orderList = new ArrayList<>();

    public Cart(int personId) {
        this.personId = personId;
    }
    public Cart(int id, int personId) {
        this(personId);
        this.id = id;
    }
    public  Cart (int id, Cart oldCart){
        this.id = id;
        personId = oldCart.getPersonId();
        orderList = oldCart.getOrderList();
    }

    public Cart(int id, int personId, Order order) {
        this(id, personId);
        orderList.add(order);
    }

    public Cart() {
    }

    public int getId() {
        return id;
    }

    public int getPersonId() {
        return personId;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public Byn getCost() {
        return orderList.stream().map(Order::getCost).reduce(new Byn(0), Byn::add);
    }

    public  boolean isExist(){
        if (id != 0 && this != null) {
            return true;
        } else {
            return false;
        }
    }
}
