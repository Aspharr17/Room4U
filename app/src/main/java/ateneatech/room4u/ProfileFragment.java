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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import ateneatech.room4u.Request.Contactos;
import ateneatech.room4u.Request.NewNumber;
import ateneatech.room4u.Request.NumberRequest;


public class ProfileFragment extends Fragment {
    View v;
    TextView Nametxt,Emailtxt,Telefonotxt;

    String bname,bemail, btelefono;
    Button Contacts;
    ImageButton EditBtn;
    AlertDialog dialog;
    EditText Edialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile,container,false);
        //Views in Layout
        final FragmentManager fragmentManager = getFragmentManager();
        Nametxt = v.findViewById(R.id.NameView);
        Emailtxt = v.findViewById(R.id.EmailView);
        Telefonotxt = v.findViewById(R.id.NumeroView);
        EditBtn = v.findViewById(R.id.EditBtn);
        Contacts = v.findViewById(R.id.ToContacts);
        bemail = getArguments().getString("email");
        bname = getArguments().getString("name");
        Nametxt.setText(bname);
        Emailtxt.setText(bemail);
        Login(bemail);
        //Dialog
        dialog = new AlertDialog.Builder(getContext()).create();
        Edialog = new EditText(getContext());
        Edialog.setInputType(InputType.TYPE_CLASS_PHONE);
        dialog.setTitle(R.string.Number);
        dialog.setView(Edialog);
        dialog.setButton(Dialog.BUTTON_POSITIVE,getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nnumber;
                nnumber = Edialog.getText().toString();
                NewNumber(bemail,nnumber);
                Telefonotxt.setText(nnumber);
                Toast.makeText(getContext(),getString(R.string.saved),Toast.LENGTH_SHORT).show();

            }
        });

        EditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             dialog.show();
            }
        });
        Contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contactos contactos = new Contactos();
                Bundle bundle = new Bundle();
                bundle.putString("email",bemail);
                contactos.setArguments(bundle);
                //Start the new Fragment
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.content_main, contactos).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });



        return v;
    }
    public void Login(String user){
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
                        btelefono = JsonResponse.getString("telefono");
                        Telefonotxt.setText(btelefono);

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
    public void NewNumber(String user, String numero){
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
                        btelefono = JsonResponse.getString("telefono");
                        Telefonotxt.setText(btelefono);
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
        NewNumber newNumber = new NewNumber(user, numero, respuesta);
        RequestQueue cola = Volley.newRequestQueue(getActivity());
        cola.add(newNumber);


    }



}
