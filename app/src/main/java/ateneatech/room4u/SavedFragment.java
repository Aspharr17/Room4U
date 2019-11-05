 package ateneatech.room4u;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ateneatech.room4u.Request.Anuncios;
import ateneatech.room4u.Request.NumberRequest;
import cz.msebera.android.httpclient.Header;


 public class SavedFragment extends Fragment {
    View v;
    String email, saved;
     AsyncHttpClient client;
     ArrayList<Anuncios> lista;
     ListView ListaAnuncios;
     AlertDialog dialog,dialog2;
     String size;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.fragment_saved, container, false);
        if(getArguments() != null) {
            email = getArguments().getString("email");

        }
        ListaAnuncios = v.findViewById(R.id.lvsaved);


        client = new AsyncHttpClient();
        obtenerAnuncios(email);
        ListaAnuncios.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dialogo(position);
                dialog.show();

                return true;
            }
        });







        return v;
    }
     public void GetSaved(String user){
         //Get the Data from user, Watch out with capital letters
         //Still Don't really know what this does, if you want, you can check it in SignIn.java


         Response.Listener<String> respuesta = new Response.Listener<String>() {

             @Override
             public void onResponse(String response) {

                 try {
                     //Calls JsonObject in php, for checking if the Sign In was completed

                     JSONObject JsonResponse = new JSONObject(response);
                     boolean ok = JsonResponse.getBoolean("success");

                     //Check php success response, if it is true, Great! IS YOU! you can go on
                     if(ok ==true){
                         //Goes Main menu
                         saved = JsonResponse.getString("guardado");
                         Toast.makeText(getContext(),saved,Toast.LENGTH_LONG).show();


                     }
                     //If pass is wrong (success=false), you cant go on, check your data
                     else {

                     }

                 }
                 catch (JSONException e){
                     e.getMessage();

                 }
             }
         };
         //Calls Method in LogIn Request (LogInRequest Java)
         //Parameters are given by the user, respuesta boolean confirm the Login
         NumberRequest request = new NumberRequest(user, respuesta);
         RequestQueue cola = Volley.newRequestQueue(getActivity());
         cola.add(request);


     }
     private void obtenerAnuncios(String correo){
         //Go url, use the mail to check the adds, if the email appears in one add, that means its yours.
         String url = "https://thorough-precaution.000webhostapp.com/pruebaguardado.php?usuario="+correo;
         client.post(url, new AsyncHttpResponseHandler() {
             @Override
             public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                 if(statusCode == 200){
                     listarAnuncios(new String(responseBody));
                 }
             }

             @Override
             public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

             }
         });
     } //Get Post saved and save in array int [] splitid
     private void listarAnuncios(String respuesta){
         lista =new ArrayList<Anuncios>();

         try {
             JSONArray jsonArray = new JSONArray(respuesta);


             for (int i = 0; i < jsonArray.length(); i++) {
                 Anuncios anuncios = new Anuncios();
                 anuncios.setId(jsonArray.getJSONObject(i).getInt("id"));
                 anuncios.setTitulo(jsonArray.getJSONObject(i).getString("titulo"));
                 anuncios.setCiudad(jsonArray.getJSONObject(i).getString("ciudad"));
                 anuncios.setEstado(jsonArray.getJSONObject(i).getString("estado"));
                 anuncios.setUniversidad(jsonArray.getJSONObject(i).getString("universidad"));
                 anuncios.setPrecio(jsonArray.getJSONObject(i).getString("precio"));
                 anuncios.setDescripcion(jsonArray.getJSONObject(i).getString("descripcion"));
                 anuncios.setCorreo(jsonArray.getJSONObject(i).getString("correo"));
                 anuncios.setTelefono(jsonArray.getJSONObject(i).getString("telefono"));
                 lista.add(anuncios);


             }
             ArrayAdapter<Anuncios> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, lista);
             ListaAnuncios.setAdapter(adapter);




         }

         catch (Exception e1){
             e1.printStackTrace();
         }
     }

     public void dialogo(final int i){
         dialog = new AlertDialog.Builder(getContext()).create();

         dialog.setButton(Dialog.BUTTON_POSITIVE,getString(R.string.delete), new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 Anuncios anuncios = lista.get(i);
                 String iddelete = String.valueOf(anuncios.getId());
                 String url = "https://thorough-precaution.000webhostapp.com/updateguardados.php?usuario="+email+"&id="+iddelete;
                 client.post(url, new AsyncHttpResponseHandler() {
                     @Override
                     public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                         if (statusCode == 200){
                             dialogolisto();
                             dialog2.show();
                         }
                     }
                     @Override
                     public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                     }
                 });
                 obtenerAnuncios(email);
             }
         });


     }
     public void dialogolisto(){
         dialog2 = new AlertDialog.Builder(getContext()).create();
         dialog2.setTitle("Listo");
         dialog2.setButton(Dialog.BUTTON_POSITIVE,"Ok", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 obtenerAnuncios(email);
             }
         });


     }

 }
