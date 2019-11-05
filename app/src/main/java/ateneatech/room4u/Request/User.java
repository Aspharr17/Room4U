package ateneatech.room4u.Request;

public class User {
    private String user,telefono;

    public  User(){}

    public User(String user, String telefono) {
        this.user = user;
        this.telefono = telefono;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return telefono;
    }
}
