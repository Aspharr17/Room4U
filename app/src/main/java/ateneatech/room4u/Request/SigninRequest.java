package ateneatech.room4u.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SigninRequest extends StringRequest {
    private static final  String ruta = "https://thorough-precaution.000webhostapp.com/registro.php";
    private Map<String, String> parametros;
    public SigninRequest(String nombre, String usuario, String clave, Response.Listener<String> listener){
        super(Method.POST, ruta, listener, null);
        parametros = new HashMap<>();
        parametros.put("nombre",nombre+"");
        parametros.put("usuario",usuario+"");
        parametros.put("clave",clave+"");

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parametros;
    }
}
