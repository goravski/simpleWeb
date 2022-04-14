package by.gsu.epamlab.webshop.model;

import java.util.Objects;

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
    public boolean isValid() {
        if (id == 0 || name.equals("") || login.equals("") || role.equals("")
                || status.equals("") || password.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        return  "id = " + id + " ; " +
                "name = " + name + " ; " +
                "login = " + login + " ; " +
                "role = " + role + " ; " +
                "status = " + status ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return getId() == person.getId() && Objects.equals(getName(), person.getName()) && Objects.equals(getLogin(), person.getLogin()) && Objects.equals(getRole(), person.getRole()) && Objects.equals(getPassword(), person.getPassword()) && Objects.equals(getStatus(), person.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getLogin(), getRole(), getPassword(), getStatus());
    }
}
