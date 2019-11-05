package ateneatech.room4u.Request;

public class Anuncios {
    private int id;
    private String estado;
    private String ciudad;
    private String universidad;
    private String titulo;
    private String precio;
    private String descripcion;
    private String correo;
    private String telefono;


    public Anuncios() {
    }

    public int getId() {
        return id;
    }

    public int setId(int id) {
        this.id = id;
        return id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getUniversidad() {
        return universidad;
    }

    public void setUniversidad(String universidad) {
        this.universidad = universidad;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return
                titulo + '\n' +
                        ciudad+", "+estado+ '\n'+universidad+ '\n'+
                precio + '\n' +
                descripcion + '\n' +
                "correo: " + correo + '\n' +
                "telefono: " + telefono + '\n';
    }
}

