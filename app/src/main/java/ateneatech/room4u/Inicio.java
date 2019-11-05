package ateneatech.room4u;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import ateneatech.room4u.Request.LogInRequest;

public class Inicio extends AppCompatActivity  {
    //Initialization Var
    EditText user, pwd;
    TextView SignInTxt;
    Button EnterButton, changeLang;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.login);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));

        //Set Edit Texts and Buttons
        user         =findViewById(R.id.Email);
        pwd          =findViewById(R.id.Password);
        EnterButton  =findViewById(R.id.LogInButton);
        SignInTxt    =findViewById(R.id.SignInText);
        changeLang   =findViewById(R.id.changeMyLang);

        //Change Language
        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show alertdialog to display list of languages
                showChangeLanguageDialog();
            }
        });



        //Not Registered? Goes to Register
        SignInTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignIn.class );
                startActivity(intent);
                finish();

            }
        });

        //Here is the Log In, Good luck!
        EnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the Data from user, Watch out with capital letters
                String User     = user.getText().toString().trim();
                String Pwd      = pwd.getText().toString().trim();
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
                                String name = JsonResponse.getString("nombre");
                                String email = JsonResponse.getString("usuario");
                                String telefono = JsonResponse.getString("telefono");
                                Toast.makeText(Inicio.this,R.string.Welcome,Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),MainMenu.class);
                                intent.putExtra("telefono",telefono);
                                intent.putExtra("nombre",name);
                                intent.putExtra("email",email);
                                startActivity(intent);
                                finish();
                            }
                            //If pass is wrong (success=false), you cant go on, check your data
                            else {
                                Toast.makeText(Inicio.this,R.string.Failed,Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e){
                            e.getMessage();
                        }
                    }
                };
                //Calls Method in LogIn Request (LogInRequest Java)
                //Parameters are given by the user, respuesta boolean confirm the Login
                LogInRequest request = new LogInRequest(User, Pwd, respuesta);
                RequestQueue cola = Volley.newRequestQueue(Inicio.this);
                cola.add(request);

            }
        }); //Here ends all of the mess of login In
    }

    private void showChangeLanguageDialog() {
        //Array of languages to display in dialog
        final String [] listItems = {"Español", "English", "French"};
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Inicio.this);
        mBuilder.setTitle(getString(R.string.ChooseLang));
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which== 0){
                    setLocale("es");
                    recreate();
                }
                if(which == 1){
                    setLocale("en");
                    recreate();
                }
                if(which == 2){
                    setLocale("fr");
                    recreate();
                }

                dialog.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();

    }
    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }

}
