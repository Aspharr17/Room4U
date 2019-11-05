package ateneatech.room4u.Request;

public class AdaptadorContactos {
    private int id;
    private String nombre;
    private String usuario;

    public AdaptadorContactos(){}


    public int getId() {
        return id;
    }

    public int setId(int id) {
        this.id = id;
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return
                "nombre: " + nombre + '\n' +
                "usuario: " + usuario + '\n' ;
    }
}
