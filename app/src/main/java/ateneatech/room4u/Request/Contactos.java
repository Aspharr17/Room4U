package ateneatech.room4u.Request;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import ateneatech.room4u.R;
import cz.msebera.android.httpclient.Header;


public class Contactos extends Fragment {
    View v;
    ListView lvContactos;
    Button Agregar, Eliminar;
    String email, contacto, idcontacto;
    AsyncHttpClient client;
    AlertDialog AgregarDialog,EliminarDialog,dialog2,dialog3;
    ArrayList<AdaptadorContactos> lista;
    ArrayList<String> lvcontactos;
    ArrayList<String> lvid;
    public String [] Cont = new String[100];
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_contactos, container, false);
        Agregar = v.findViewById(R.id.AddContact);
        Eliminar = v.findViewById(R.id.Delet);
        client = new AsyncHttpClient();
        lvContactos = v.findViewById(R.id.lvcontactos);


        if(getArguments() != null) {
            email = getArguments().getString("email");
        }
        obtenerAnuncios();

        Agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Agregar(email);
                AgregarDialog.show();
            }
        });
        Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lista.isEmpty()) {
                    Toast.makeText(getContext(),getString(R.string.WithoutContacts),Toast.LENGTH_SHORT).show();

                }
                else {
                    Eliminar();
                    EliminarDialog.show();

                }
            }
        });

        return v;
    }
    public void Agregar(final String email){
        AgregarDialog = new AlertDialog.Builder(getContext()).create();
        AgregarDialog.setTitle(getString(R.string.Email)+":");
        final EditText editText = new EditText(getContext());
        AgregarDialog.setView(editText);
        AgregarDialog.setButton(Dialog.BUTTON_POSITIVE, getString(R.string.Add), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String contacto = editText.getText().toString();
                String url = "https://thorough-precaution.000webhostapp.com/savecontact.php?usuario="+email+"&contacto="+contacto;
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
                        Toast.makeText(getContext(),"Correo invalido o no existente", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
    private void obtenerAnuncios(){
        //Go url, use the mail to check the adds, if the email appears in one add, that means its yours.

        String url = "https://thorough-precaution.000webhostapp.com/listarcontactos.php?usuario="+email;

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
    }
    private void listarAnuncios(String respuesta){
        lista =new ArrayList<AdaptadorContactos>();
         lvcontactos = new ArrayList<>();
         lvid = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(respuesta);
            String ayuda, idayuda;

            for (int i = 0; i < jsonArray.length(); i++) {
                AdaptadorContactos adaptadorContactos = new AdaptadorContactos();
                adaptadorContactos.setId(jsonArray.getJSONObject(i).getInt("id"));
                adaptadorContactos.setNombre(jsonArray.getJSONObject(i).getString("nombre"));
                adaptadorContactos.setUsuario(jsonArray.getJSONObject(i).getString("usuario"));
                ayuda = adaptadorContactos.getUsuario();
                idayuda = String.valueOf(adaptadorContactos.getId());
                lvcontactos.add(ayuda);
                lvid.add(idayuda);
                lista.add(adaptadorContactos);
            }
            ArrayAdapter<AdaptadorContactos> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, lista);
            lvContactos.setAdapter(adapter);


        }

        catch (Exception e1){
            e1.printStackTrace();
        }

    }
    public void dialogo3(){
        dialog3 = new AlertDialog.Builder(getContext()).create();
        dialog3.setTitle(getString(R.string.DeletTo)+contacto+"?");
        Toast.makeText(getContext(),idcontacto,Toast.LENGTH_SHORT).show();
        dialog3.setButton(Dialog.BUTTON_NEUTRAL, getString(R.string.Confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                String url = "https://thorough-precaution.000webhostapp.com/updatecontactos.php?usuario="+email+"&id="+idcontacto;
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

            }
        });
    }
    public void dialogolisto(){
        dialog2 = new AlertDialog.Builder(getContext()).create();
        dialog2.setTitle(getString(R.string.Done));
        dialog2.setButton(Dialog.BUTTON_POSITIVE,"Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                obtenerAnuncios();
            }
        });



    }


    public void Eliminar(){ //Para usar con set on item long click ocupa como parametro final int i
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.DeletTo));
        // add a radio button list
        final int checkedItem = 0;// cow
            contacto=lvcontactos.get(0);
            idcontacto=lvid.get(0);
        builder.setSingleChoiceItems(lvcontactos.toArray(new String[lvcontactos.size()]), checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user checked an item
                contacto = lvcontactos.get(which);
                idcontacto = lvid.get(which);

            }
        });

// add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
                dialogo3();
                dialog3.show();
            }
        });
        builder.setNegativeButton(getString(R.string.Cancel), null);

// create and show the alert dialog
        EliminarDialog = builder.create();
        EliminarDialog.show();


    }
}
