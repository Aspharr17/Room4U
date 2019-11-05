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
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.*;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import ateneatech.room4u.Request.ciudad;
import ateneatech.room4u.Request.estados;
import cz.msebera.android.httpclient.Header;

public class SearchFragment extends Fragment  {
    Spinner SpEstates;
    AsyncHttpClient client;
    AsyncHttpClient client2;
    String EstateSelected;
    Spinner SpCity;
    String CitySelected;
    String searchstate;
    String searchcity;
    String selectfirst;
    String email;
    View v;
    Button Go;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_search, container, false);
        //client to check the url, think so
        client = new AsyncHttpClient();
        client2 = new AsyncHttpClient();
        if(getArguments() != null) {
            email = getArguments().getString("email");

        }
        //Managger to start a new fragment
        final FragmentManager fragmentManager = getFragmentManager();
        //Some String to guide the user
        searchcity = getString(R.string.SearchbyCity);
        searchstate = getString(R.string.SearchbyState);
        selectfirst = getString(R.string.Selectfirst);
        //Spinners
        SpEstates   = v.findViewById(R.id.Sp_State);
        SpCity      = v.findViewById(R.id.Sp_City);
        //Button for search
        Go          = v.findViewById(R.id.GoSearchBtn);
        //Estates Filled automatically, only mexico
        FillSpinnerEstates();
       SpEstates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //Chosee state
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals(searchstate)) {
                    //Adjusting 0 positon, user havent chose state
                    nullSpin();

                } else {
                    EstateSelected = parent.getSelectedItem().toString().replace(" ", "_");
                    FillSpinnerCity(EstateSelected);

                    //Spinner city
                    SpCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(final AdapterView<?> parent, View view, int position, long id) {
                            if (parent.getItemAtPosition(position).toString().equals(searchcity)) {
                                //Just adjusting 0 position, user haven't chose city

                            } else {
                                CitySelected = SpCity.getSelectedItem().toString().replace(" ", "_");
                                //push search button

                                Go.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //Bundle for give the city and state selected by user,
                                        // in allpostFragment shows all adds with this information
                                            AllPostsFragment allPostsFragment = new AllPostsFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("City",CitySelected);
                                            bundle.putString("Estate",EstateSelected);
                                            bundle.putString("email",email);
                                            allPostsFragment.setArguments(bundle);
                                        //Start the new Fragment
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.add(R.id.content_main, allPostsFragment).addToBackStack(null);
                                        fragmentTransaction.commit();
                                        //fragmentManager.beginTransaction().replace(R.id.content_main, new AllPostsFragment()).commit();

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

        }

        );
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
            ArrayAdapter<estados> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, lista);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            SpEstates.setAdapter(adapter);

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
            SpCity.setAdapter(adapter);

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
        SpCity.setAdapter(dataAdapter);
    }



}
