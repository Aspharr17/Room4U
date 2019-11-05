package ateneatech.room4u.Request;

public class ciudad {
    private int id;
    private String ciudad;

    public ciudad(){

    }

    public ciudad(int id, String ciudad){
        this.setId(id);
        this.setCiudad(ciudad);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    @Override
    public String toString(){
        return ciudad;
    }
}
