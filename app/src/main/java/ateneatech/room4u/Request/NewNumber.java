package ateneatech.room4u.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class NewNumber extends StringRequest {
    private static final  String ruta = "https://thorough-precaution.000webhostapp.com/newnumber.php";
    private Map<String, String> parametros;
    public NewNumber(String usuario, String numero, Response.Listener<String> listener){
        super(Request.Method.POST, ruta, listener, null);
        parametros = new HashMap<>();
        parametros.put("usuario",usuario+"");
        parametros.put("numero",numero+"");

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parametros;
    }
}
