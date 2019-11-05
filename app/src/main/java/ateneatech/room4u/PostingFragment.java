package ateneatech.room4u;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import ateneatech.room4u.Request.Anuncios;
import ateneatech.room4u.Request.User;
import ateneatech.room4u.Request.ciudad;
import ateneatech.room4u.Request.estados;
import cz.msebera.android.httpclient.Header;


public class PostingFragment extends Fragment {
    View v;
    AsyncHttpClient client, client2, client3;


    EditText Title, Price, Description, number;
    public int id;
    Spinner EstatePostSp, CityPostSp, UniPostSp; //Spinners
    String searchstate, searchcity, selectfirst,cero, selectcity; //Default values for spinners
    String email, Stitle, SPrice, SDescription, Snumber; //Strings to POST
    String EstateSelected, CitySelected, UniSelected; //items selected to POST
    Button post,update;
    ArrayList<Anuncios> lista;
    String ciu;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Set view and client services
        final FragmentManager fragmentManager = getFragmentManager();
        final int pop;
        v = inflater.inflate(R.layout.fragment_posting,container,false);
        client  = new AsyncHttpClient();
        client2 = new AsyncHttpClient();
        client3 = new AsyncHttpClient();
        //Catch bundle from PostedFragment
        if(getArguments() != null) {
            email = getArguments().getString("email");
            id    = getArguments().getInt("id");

        }

        //All things in layout
        Title       = v.findViewById(R.id.setTitle);
        Price       = v.findViewById(R.id.setPrice);
        Description = v.findViewById(R.id.setDes);
        number      = v.findViewById(R.id.setNumber);
        EstatePostSp = v.findViewById(R.id.EstatePostSp);
        CityPostSp  = v.findViewById(R.id.CityPostSp);
        UniPostSp   = v.findViewById(R.id.UniPostSp);
        post        = v.findViewById(R.id.Postbtn);
        //Set default string to spinners
        searchcity  = getString(R.string.SearchbyCity);
        searchstate = getString(R.string.SelectEstatePost);
        selectfirst = getString(R.string.Selectfirst);
        selectcity  = getString(R.string.SelectCityFirst);
        cero        = getString(R.string.Searchby);

        getInformation(email);
        if(id>0){
            obtenerPost(id);
             pop = 1;
             //If is an edit, pop is 1

        }
        else {
            pop = 0;
            //if is neew post, pop = 0
        }



        FillSpinnerEstates();

        EstatePostSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //Chosee state
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals(searchstate)) {
                    //Adjusting 0 positon, user havent chose state
                    nullSpin();

                } else {    //Fill City Spinner
                    EstateSelected = parent.getSelectedItem().toString().replace(" ", "_");
                    FillSpinnerCity(EstateSelected);
                    nullSpinUni();

                    //Spinner city
                    CityPostSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(final AdapterView<?> parent, View view, int position, long id) {
                            if (parent.getItemAtPosition(position).toString().equals(searchcity)) {
                                //Just adjusting 0 position, user haven't chose city

                            } else { //Fill University Spinner
                                CitySelected = CityPostSp.getSelectedItem().toString();
                                FillSpinnerUniversity(EstateSelected,CitySelected);
                                UniPostSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (parent.getItemAtPosition(position).toString().equals(cero)){

                                        }
                                        else  {

                                            UniSelected = UniPostSp.getSelectedItem().toString();
                                            Toast.makeText(getActivity(), UniSelected, Toast.LENGTH_SHORT).show();
                                            UniSelected.replace(" ", "%20");
                                            post.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //Get Data to Post
                                                   String Estado = EstateSelected.replace("_","%20");
                                                   String Ciudad= CitySelected.replace("_","%20");
                                                    Stitle = Title.getText().toString();
                                                    SPrice = Price.getText().toString();
                                                    SDescription = Description.getText().toString();
                                                    Snumber = number.getText().toString();
                                                    //Check if theres is empty box
                                                    if(Stitle.equals("")&&SPrice.equals("")&&SDescription.equals("")){
                                                        Toast.makeText(getContext(),"Faltan Datos",Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        //Put Data to Post
                                                        Anuncios a = new Anuncios();
                                                        a.setEstado(Estado);
                                                        a.setCiudad(Ciudad);
                                                        setA(a);
                                                        if(pop==0){
                                                            AgregarAnuncio(a);
                                                        }
                                                        else{
                                                            EditarAnuncio(a);
                                                        }

                                                        PostedFragment postedFragment = new PostedFragment();
                                                        //Bundle, love u, put the information in the fragment
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("email", email);
                                                        postedFragment.setArguments(bundle);
                                                        //Start Fragment
                                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                        fragmentTransaction.add(R.id.content_main, postedFragment).addToBackStack(null);
                                                        fragmentTransaction.commit();

                                                    }
                                                }
                                            });
                                                                             }
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        return v;
    }



    private void FillSpinnerEstates() {
        //String for php
        String url = "https://thorough-precaution.000webhostapp.com/obtenerestado.php";
        //Goes to the web page
        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    //Go get the data shown in the url (JSON OBJECTS)
                    getSpinnerEstates(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    private void getSpinnerEstates(String respuesta) {
        //We are in php with all objects, let's put all in an array
        ArrayList<estados> lista = new ArrayList<>();
        //First item
        estados first = new estados();
        first.setEstado(searchstate);
        lista.add(0,first);

        try {
            //Json Array to put all object, then put in the arraylist
            JSONArray jsonArray = new JSONArray(respuesta);
            //While there are objects, keep saving in jsonarray
            for (int i = 0; i < jsonArray.length(); i++) {
                //calls ciudad.java and make an object estados, every time, there will be a new object"
                estados estados = new estados();
                //in jsonarray, position i,  take the string in "estado", and put in on object estados
                estados.setEstado(jsonArray.getJSONObject(i).getString("estado"));
                //put the string gotten in the json and add it to arraylist
                lista.add(estados);

            }
            //Just put the arraylist in the box of spinner and show it, not a big deal
            ArrayAdapter<estados> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, lista);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            EstatePostSp.setAdapter(adapter);

        } catch (Exception e) {
            //Catch errors
            e.printStackTrace();
        }
    }
    private void FillSpinnerCity(String estateSelected) {
        //For information, check "FillSpinnerEstates,
        // the only diferrence is the string EstateSelected, it will chose the table deppending of state chosen"
        //php ask for a paramether, that will take you to the table that is given by paramether
        String url2 = "https://thorough-precaution.000webhostapp.com/obtenerciudades.php?tabla=" + estateSelected;
        client2.post(url2, new AsyncHttpResponseHandler() {
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
        //Setting the default position 0
        ciudad first = new ciudad();
        first.setCiudad(searchcity);
        lista.add(0,first);
        try {
            JSONArray jsonArray = new JSONArray(respuesta);

            for (int i = 0; i < jsonArray.length(); i++) {
                ciudad ciudad = new ciudad();
                ciudad.setCiudad(jsonArray.getJSONObject(i).getString("ciudad"));
                lista.add(ciudad);

            }

            ArrayAdapter<ciudad> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, lista);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            CityPostSp.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void FillSpinnerUniversity(String estateSelected, final String citySelected) {
        //For information, check "FillSpinnerEstates,
        // the only diferrence is the string EstateSelected, it will chose the table deppending of state chosen"
        //php ask for a paramether, that will take you to the table that is given by paramether

        ciu = citySelected.replace(" ","%20");
        estateSelected.replace(" ","_");

        String url = "https://thorough-precaution.000webhostapp.com/obteneruniversidades.php?tabla=" + estateSelected +"_Universidades&ciudad="+ciu;
        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    getSpinnerUniversity(new String(responseBody), citySelected);

                }
                else{

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    private void getSpinnerUniversity(String respuesta, String ciu) {

        ArrayList<ciudad> lista = new ArrayList<>();
        //Setting the default in position 0

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
            UniPostSp.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void nullSpin(){
        List<String> nulo = new ArrayList<>();
        nulo.add(0,selectfirst);
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(this.getActivity(),android.R.layout.simple_spinner_dropdown_item,nulo);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        CityPostSp.setAdapter(dataAdapter);
        UniPostSp.setAdapter(dataAdapter);
    }
    private void nullSpinUni(){
        List<String> nulo = new ArrayList<>();
        nulo.add(0,selectcity);
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(this.getActivity(),android.R.layout.simple_spinner_dropdown_item,nulo);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        UniPostSp.setAdapter(dataAdapter);
    }
    private void getInformation(String user){
        String url = "https://thorough-precaution.000webhostapp.com/datosusuario.php?user="+user;
        client3.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    getUserData(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    private void getUserData(String respuesta){
        try{
            JSONArray jsonArray = new JSONArray((respuesta));
            User user = new User();
            user.setTelefono(jsonArray.getJSONObject(0).getString("telefono"));
            String usernumber = user.toString();
            number.setText(usernumber);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void AgregarAnuncio(Anuncios a){
        String pEst = a.getEstado();
        String pCiu = a.getCiudad().replace(" ","%20");
        String pUni = a.getUniversidad().replace(" ","%20");
        String pTit = a.getTitulo().replace(" ","%20");
        String pPrecio = a.getPrecio();
        String pDesc = a.getDescripcion().replace(" ","%20");
        String pTel = a.getTelefono();
        String pCorreo = a.getCorreo();
        String url = "http://thorough-precaution.000webhostapp.com/agregar.php?";
        String parametros = "id="+id+"&est="+pEst+"&ciu="+pCiu+"&uni="+pUni+"&tit="+pTit+"&precio="+pPrecio+"&desc="+pDesc+"&correo="+pCorreo+"&telefono="+pTel;
        client3.post(url+parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    Toast.makeText(getContext(),getString(R.string.saved),Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }
    private void EditarAnuncio(Anuncios a){
        String pEst = a.getEstado();
        String pCiu = a.getCiudad().replace(" ","%20");
        String pUni = a.getUniversidad().replace(" ","%20");
        String pTit = a.getTitulo().replace(" ","%20");
        String pPrecio = a.getPrecio();
        String pDesc = a.getDescripcion().replace(" ","%20");
        String pTel = a.getTelefono();
        String pCorreo = a.getCorreo();
        String url = "http://thorough-precaution.000webhostapp.com/updatepost.php?";
        String parametros = "id="+id+"&est="+pEst+"&ciu="+pCiu+"&uni="+pUni+"&tit="+pTit+"&precio="+pPrecio+"&desc="+pDesc+"&correo="+pCorreo+"&telefono="+pTel;
        client3.post(url+parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    Toast.makeText(getContext(),getString(R.string.saved),Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }
    private void setA(Anuncios a){
        a.setUniversidad(UniSelected);
        a.setTitulo(Stitle);
        a.setPrecio(SPrice);
        a.setDescripcion(SDescription);
        a.setCorreo(email);
        a.setTelefono(Snumber);
    }
    private void obtenerPost(int id){
        //Go url, use the mail to check the adds, if the email appears in one add, that means its yours.
        String url = "https://thorough-precaution.000webhostapp.com/editpost.php?id="+id;
        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    mostrarAnuncio(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    private void mostrarAnuncio(String respuesta){
        lista =new ArrayList<Anuncios>();

        try {
            JSONArray jsonArray = new JSONArray(respuesta);


            for (int i = 0; i < jsonArray.length(); i++) {
                Anuncios anuncios = new Anuncios();
                anuncios.setId(jsonArray.getJSONObject(i).getInt("id"));
                anuncios.setTitulo(jsonArray.getJSONObject(i).getString("titulo"));
                Title.setText(anuncios.getTitulo());
                anuncios.setPrecio(jsonArray.getJSONObject(i).getString("precio"));
                Price.setText(anuncios.getPrecio());
                anuncios.setDescripcion(jsonArray.getJSONObject(i).getString("descripcion"));
                Description.setText(anuncios.getDescripcion());
                anuncios.setTelefono(jsonArray.getJSONObject(i).getString("telefono"));
                number.setText(anuncios.getTelefono());
                anuncios.setCorreo(jsonArray.getJSONObject(i).getString("correo"));


            }






        }

        catch (Exception e1){
            e1.printStackTrace();
        }

    }
}
