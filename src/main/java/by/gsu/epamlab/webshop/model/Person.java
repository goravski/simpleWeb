package by.gsu.epamlab.webshop.model;

public class Person {
    private int id;
    private String name;
    private String login;
    private String role;
    private String password;
    private String status;

    public Person() {
    }

    public Person(int id, String name, String login, String role, String status) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.role = role;
        this.status = status;
    }

    public Person(int id, String name, String login, String pass, String role, String status) {
        this(id, name,login, role, status);
        this.password = pass;

    }

    public Person(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
        role = "user";
        status = "Open";
    }

    public String getPassword() {
        return password;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return  "id = " + id + " ; " +
                "name = " + name + " ; " +
                "login = " + login + " ; " +
                "role = " + role + " ; " +
                "status = " + status ;
    }

}
