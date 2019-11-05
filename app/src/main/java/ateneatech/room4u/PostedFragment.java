package ateneatech.room4u;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
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

import ateneatech.room4u.Request.Anuncios;
import ateneatech.room4u.Request.BottomDelete;
import cz.msebera.android.httpclient.Header;

public class PostedFragment extends Fragment  {

    View v;
    AsyncHttpClient client;
    String email;
    ListView ListaAnuncios;
    Button Post;
    AlertDialog dialog,dialog2;
    ArrayList<Anuncios> lista;
    FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_posted,container,false);
        fragmentManager = getFragmentManager();
        //Cliente to check web
        client = new AsyncHttpClient();
        //Get email from main activity to compare in anuncios table
        email = getArguments().getString("email");
        //Find ListView in LayOut
        ListaAnuncios = v.findViewById(R.id.lvmy);
        //Button to Post a new one
        Post = v.findViewById(R.id.NewAddBtn);
        obtenerAnuncios(email);

        ListaAnuncios.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dialogo(position);
                dialog.show();



            return true;
            }
        });
        //Go Post a new Add
        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             postingfragment(fragmentManager);

            }
        });


        return v;
    }
    private void obtenerAnuncios(String correo){
        //Go url, use the mail to check the adds, if the email appears in one add, that means its yours.
        String url = "https://thorough-precaution.000webhostapp.com/obtener_mis_anuncios.php?correo="+correo;
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
        dialog.setTitle("¿Que desea hacer?");
        dialog.setButton(Dialog.BUTTON_POSITIVE,getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Anuncios anuncios = lista.get(i);
                String iddelete = String.valueOf(anuncios.getId());
                String url = "https://thorough-precaution.000webhostapp.com/eliminar.php?id="+iddelete;
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
        dialog.setButton(Dialog.BUTTON_NEUTRAL, getString(R.string.Edit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Anuncios anuncios = lista.get(i);
                int id = anuncios.getId();
                editfragment(id);

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

    private void postingfragment(FragmentManager fragmentManager){
        PostingFragment postingFragment = new PostingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("email",email);
        bundle.putInt("id",0);
        postingFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content_main, postingFragment );
        fragmentTransaction.commit();
    }
    private void editfragment( int id){
        PostingFragment postingFragment = new PostingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("email",email);
        bundle.putInt("id",id);

        postingFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content_main, postingFragment );
        fragmentTransaction.commit();

    }


}