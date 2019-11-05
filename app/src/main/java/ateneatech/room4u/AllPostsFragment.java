package ateneatech.room4u;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import ateneatech.room4u.Request.AdaptadorContactos;
import ateneatech.room4u.Request.Anuncios;
import ateneatech.room4u.Request.Contactos;
import ateneatech.room4u.Request.ciudad;
import cz.msebera.android.httpclient.Header;


public class AllPostsFragment extends Fragment {
    public String cityrecived;
    public String Estaterecived;
    Spinner SpU;
    String Uni;
    AsyncHttpClient client;
    String cero;
    String noadds;

    ListView lvAnuncios, ListaContactos;
    AlertDialog dialog,dialog2, dialog3, dialog4;
    String email,contacto;
    ArrayList<Anuncios> lista;
    ArrayList<String> lvcontactos = new ArrayList<>();

    public String [] Cont = new String[100];

    Button Searchbtn;
    FragmentManager fragmentManager;


    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_all_posts, container, false);


        client = new AsyncHttpClient();
        fragmentManager = getFragmentManager();

        cero = getString(R.string.Searchby);
        noadds = getString(R.string.without_post);

        if(getArguments() != null) {
            email = getArguments().getString("email");
            cityrecived = getArguments().getString("City");
            Estaterecived =getArguments().getString("Estate");


        }
        obtenerContactos(email);
        SpU = v.findViewById(R.id.Sp_un);
        lvAnuncios = v.findViewById(R.id.lvAnuncios);
        Searchbtn = v.findViewById(R.id.newSearch);
        FillSpinnerCity(Estaterecived);
        obtenerAnuncios(Estaterecived,cityrecived);

        //obtenerContactos(email);

        SpU.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals(cero)){

                }
                else  {

                    Uni = parent.getItemAtPosition(position).toString();
                   obtenerAnunciosFiltrados(Estaterecived,Uni,cityrecived);
                    Toast.makeText(getActivity(), Uni, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        lvAnuncios.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dialogo(position);
                dialog.show();

                return true;
            }
        });
        Searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment searchFragment = new SearchFragment();
                Bundle bundle = new Bundle();
                bundle.putString("email", email);
                searchFragment.setArguments(bundle);
                //Start Fragment
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.content_main, searchFragment).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return v;
    }


    //Actually is fillUniversity Spinner
    private void FillSpinnerCity(String estaterecived) {
        //For information, check "FillSpinnerEstates,
        // the only diferrence is the string EstateSelected, it will chose the table deppending of state chosen"
        //php ask for a paramether, that will take you to the table that is given by paramether

        String ciudad = cityrecived.replace("_","%20");
        String url = "https://thorough-precaution.000webhostapp.com/obteneruniversidades.php?tabla=" + estaterecived +"_Universidades&ciudad="+ciudad;

        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    getSpinnerCity(new String(responseBody));

                }
                else{

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    private void getSpinnerCity(String respuesta) {
        //Same, for information, take a look in getSpinnerEstate, hope you cant delete this later and reuse the other one
        //clue, just change paramethers of name of column, or the object that you are lookin in json, in this one is ciudad
        //the other paramether would be the name of the spinner, the java class, and the names of arrays, good luck
        ArrayList<ciudad> lista = new ArrayList<>();
        //Setting the default in position 0
        String ciu = cityrecived.replace("_"," ");
        ciudad first = new ciudad();
        first.setCiudad(cero);
        lista.add(0,first);
        String check;
        try {
            JSONArray jsonArray = new JSONArray(respuesta);

            for (int i = 0; i < jsonArray.length(); i++) {
                ciudad ciudad = new ciudad();
                ciudad uni = new ciudad();
                ciudad.setCiudad(jsonArray.getJSONObject(i).getString("ciudad"));
                uni.setCiudad(jsonArray.getJSONObject(i).getString("universidad"));
                check = ciudad.toString();
                if(check.equals(ciu)) {

                    lista.add(uni);
                }
            }
            ArrayAdapter<ciudad> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, lista);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            SpU.setAdapter(adapter);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void obtenerAnuncios(String estado, String ciudad){
        String est,ciu;
        est =estado.replace("_", "%20");
        ciu = ciudad.replace("_", "%20");
        String url = "https://thorough-precaution.000webhostapp.com/obtener_anuncios.php?estado="+est+"&ciudad="+ciu;
        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    listarAnuncios(new String(responseBody));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) { }
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
            lvAnuncios.setAdapter(adapter);
        }

        catch (Exception e1){
            e1.printStackTrace();
        }
    }
    private void obtenerAnunciosFiltrados(String estado, String universidad, String ciudad){
        String uni, est,ciu;

        ciu = ciudad.replace("_","%20").trim();
        uni =universidad.replace(" ", "%20").trim();
        est =estado.replace("_", "%20");
        String url = "https://thorough-precaution.000webhostapp.com/obtener_anuncios_filtro.php?estado="+est+"&ciudad="+ciu+"&universidad="+uni;
        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    listarAnunciosFiltrados(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    private void listarAnunciosFiltrados(String respuesta){
       lista =new ArrayList<Anuncios>();

        //hacer adapter de vacio para cuando no haya respuesta y no quede con las anteriores
        try {
            JSONArray jsonArray = new JSONArray(respuesta);
            for (int i=0; i<jsonArray.length(); i++){
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
            lvAnuncios.setAdapter(adapter);
        }

        catch (Exception e1){
            e1.printStackTrace();
        }
    }
    public void dialogo3(final int i){
        dialog3 = new AlertDialog.Builder(getContext()).create();
        dialog3.setTitle( getString(R.string.Sharewith)+" "+contacto+"?");
        dialog3.setButton(Dialog.BUTTON_NEUTRAL, getString(R.string.Confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Anuncios anuncios = lista.get(i);
                String idsave = String.valueOf(anuncios.getId());
                String url = "https://thorough-precaution.000webhostapp.com/Compartir.php?usuario="+contacto+"&id="+idsave;
                client.post(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (statusCode == 200){
                            Toast.makeText(getContext(),getString(R.string.Sharedwith)+contacto,Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });

            }
        });
    }
    public void dialogo2(final int i){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.Sharewith));

// add a radio button list
         final int checkedItem = 0;
        contacto = lvcontactos.get(checkedItem);
        builder.setSingleChoiceItems(lvcontactos.toArray(new String[lvcontactos.size()]), checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user checked an item

                contacto = lvcontactos.get(which);

            }
        });

// add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
                dialogo3(i);
                dialog3.show();
            }
        });
        builder.setNegativeButton(getString(R.string.Cancel), null);

// create and show the alert dialog
        dialog2 = builder.create();
        dialog2.show();

    }
    public void dialogo(final int i){
        dialog = new AlertDialog.Builder(getContext()).create();


        dialog.setButton(Dialog.BUTTON_POSITIVE,getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Anuncios anuncios = lista.get(i);
                String idsave = String.valueOf(anuncios.getId());
                String url = "https://thorough-precaution.000webhostapp.com/savepost.php?usuario="+email+"&id="+idsave;
                client.post(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (statusCode == 200){
                            Toast.makeText(getContext(),getString(R.string.saved),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });


            }
        });
        dialog.setButton(Dialog.BUTTON_NEUTRAL,getString(R.string.share), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!lvcontactos.isEmpty()) {
                    dialogo2(i);
                    dialog2.show();
                }
                else{
                    SinContactos(email);
                    dialog4.show();
                }



            }
        });

    }
    private void obtenerContactos(String email){
        String url = "https://thorough-precaution.000webhostapp.com/listarcontactos.php?usuario="+email;


        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    listarContactos(new String(responseBody));

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    private void listarContactos(String respuesta){


        try {

            JSONArray jsonArray = new JSONArray(respuesta);
            String ayuda;
            for (int i = 0; i < jsonArray.length(); i++) {

                AdaptadorContactos adaptadorContactos = new AdaptadorContactos();
                adaptadorContactos.setUsuario(jsonArray.getJSONObject(i).getString("usuario"));
                ayuda = adaptadorContactos.getUsuario();
                lvcontactos.add(ayuda);

               // Toast.makeText(getContext(),ayuda,Toast.LENGTH_SHORT).show();
            }
            for (int i = 0; i<jsonArray.length(); i++){
                Cont[i] = lvcontactos.get(i);


            }

        }

        catch (Exception e1){
            e1.printStackTrace();
            Toast.makeText(getContext(),"error",Toast.LENGTH_SHORT).show();
        }
    }
    private void SinContactos(String email){
        dialog4 = new AlertDialog.Builder(getContext()).create();
        dialog4.setTitle(getString(R.string.NoContacts));
        dialog4.setButton(Dialog.BUTTON_NEGATIVE, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog4.dismiss();
            }
        });
    }
}
