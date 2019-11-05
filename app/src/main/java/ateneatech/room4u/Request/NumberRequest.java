package ateneatech.room4u.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class NumberRequest extends StringRequest {
    private static final  String ruta = "https://thorough-precaution.000webhostapp.com/number.php";
    private Map<String, String> parametros;
    public NumberRequest(String usuario, Response.Listener<String> listener){
        super(Request.Method.POST, ruta, listener, null);
        parametros = new HashMap<>();
        parametros.put("usuario",usuario+"");


    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parametros;
    }
}
