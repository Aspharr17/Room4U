package ateneatech.room4u;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import ateneatech.room4u.Request.SigninRequest;

public class SignIn extends AppCompatActivity {
    //Initialization Variables
        EditText User, Pass, CPass, Name, name;
        Button Registerbtn;
        TextView LogedTxt, terms;
        CheckBox acept;
        AlertDialog dialog;
    private static final String TAG = "Room4u";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        //Set Text, Buttons, DB
        Name    = findViewById(R.id.NameTxt);
        User    = findViewById(R.id.Ruser);
        Pass    = findViewById(R.id.Rpass);
        CPass   = findViewById(R.id.CRpass);
        acept   = findViewById(R.id.Termscheck);
        terms   = findViewById(R.id.termsdialog);
        LogedTxt=findViewById(R.id.LogInTxt);
        Registerbtn = findViewById(R.id.Rbutton);
        mAuth = FirebaseAuth.getInstance();


        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(SignIn.this,"Terminos",Toast.LENGTH_SHORT).show();
                dialog = new AlertDialog.Builder(SignIn.this).create();
                dialog.setTitle(getString(R.string.terminos));
                dialog.setMessage(getString(R.string.texto));
                dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        //Already Registered?, Goes Back Main to Log In
        LogedTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Inicio.class);
                startActivity(intent);
                finish();
            }
        });

        //Sign In, All is done here, good luck checking it
        Registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get Information Provided by user
                String name = Name.getText().toString().trim();
                String EmailUser = User.getText().toString().trim();
                String PasswordUser = Pass.getText().toString().trim();
                String CPassword = CPass.getText().toString().trim();

                //Check Information is not Empty
                if (name.equals("") || EmailUser.equals("") || PasswordUser.equals("")) {
                    Toast.makeText(SignIn.this, R.string.EmptyData, Toast.LENGTH_SHORT).show();
                }
                else if (!PasswordUser.equals((CPassword))){
                    Toast.makeText(SignIn.this, R.string.Dontmactch, Toast.LENGTH_SHORT).show();
                }
                else if (PasswordUser.length()<6){
                    Toast.makeText(SignIn.this, "Contraseña debe ser mayor de 6 digitos",Toast.LENGTH_SHORT).show();
                }
                else if (acept.isChecked()==false){
                    Toast.makeText(SignIn.this, "Acepta los términos",Toast.LENGTH_SHORT).show();

                }
                else {
                   /* createAccount();
                    register(name, EmailUser, PasswordUser);*/
                 signin(name, EmailUser, PasswordUser);

                }


                }




        }); //Here ends the mess of Sign In

    }
    private void signin(String name, String EmailUser, String PasswordUser){
        //Dont really know whats this does, i think it calls the Response.Listener from SigninRequest.java
        Response.Listener<String> respuesta = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    //Calls JsonObjetc in php, for checking if the Sign In was completed

                    JSONObject jsonRespuesta = new JSONObject(response);
                    boolean ok = jsonRespuesta.getBoolean("success");

                    //If php gives a success = true, return to Log In, the Registration is COMPLETED
                    if (ok == true) {
                        //Register Complete
                        Intent intent = new Intent(getApplicationContext(), Inicio.class);
                        startActivity(intent);
                        Toast.makeText(SignIn.this, R.string.Registered, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        //The user (email) is already registered in DB
                        Toast.makeText(SignIn.this, R.string.RegisteredUser, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.getMessage();


                }
            }
        };

        //Calls Method in Signin Request (SignInRequest Java
        //Parameters are given by the user, respuesta boolean confirm the regist
        SigninRequest request = new SigninRequest(name, EmailUser, PasswordUser, respuesta);
        RequestQueue cola = Volley.newRequestQueue(SignIn.this);
        cola.add(request);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }
    protected  void createAccount(){
        final String email = User.getText().toString();
        final String password = Pass.getText().toString();
        final String name = Name.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();

                            user.updateProfile(profileUpdates);
                           // updateUI(user);
                            signin(name, email, password);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignIn.this, "Autenticacion Fallida",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent homeActivity = new Intent(SignIn.this, Inicio.class);
            startActivity(homeActivity);
        }
    }
    private void register(final String username, String email, String password){
    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()){
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                String userid = firebaseUser.getUid();
                reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                HashMap<String, String > hashMap = new HashMap<>();
                hashMap.put("id", userid);
                hashMap.put("username", username);

                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SignIn.this,"Registro Completo",Toast.LENGTH_SHORT).show();
                        }
                }
            });
        }
    }
    });



    }




}