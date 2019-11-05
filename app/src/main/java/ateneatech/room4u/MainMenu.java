package ateneatech.room4u;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class MainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String name;
    String email;
    String telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //Get information from user fiven by Login
        Intent i = this.getIntent();
        name = i.getStringExtra("nombre");
        email= i.getStringExtra("email");
        telefono= i.getStringExtra("telefono");


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //SET NAME AND EMAIL IN MENU BAR
        View hview = navigationView.getHeaderView(0);
        TextView HeaderName = hview.findViewById(R.id.Header_Name);
        TextView HeaderEmail= hview.findViewById(R.id.Header_email);
        HeaderName.setText(name);
        HeaderEmail.setText(email);


        navigationView.setNavigationItemSelectedListener(this);

        //Start in Search Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        SearchFragment searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        searchFragment.setArguments(bundle);
        //Start Fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content_main, searchFragment).addToBackStack(null);
        fragmentTransaction.commit();



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            Intent intent = new Intent(getApplicationContext(),Inicio.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //Calls Fragment Manager
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_search) {
            //TO Search Fragment
            SearchFragment searchFragment = new SearchFragment();
            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            searchFragment.setArguments(bundle);
            //Start Fragment
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.content_main, searchFragment).addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_saved) {
            //TO Saved Adds Fragments
            SavedFragment savedFragment = new SavedFragment();
            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            savedFragment.setArguments(bundle);
            //Start Fragment
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.content_main, savedFragment).addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_myads) {
            //TO Posted Ads Fragment
            //Make an object with the fragment
            PostedFragment postedFragment = new PostedFragment();
            //Bundle, love u, put the information in the fragment
           Bundle bundle = new Bundle();
           bundle.putString("email", email);
           postedFragment.setArguments(bundle);
           //Start Fragment
           FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
           fragmentTransaction.add(R.id.content_main, postedFragment).addToBackStack(null);
           fragmentTransaction.commit();

            //fragmentManager.beginTransaction().replace(R.id.content_main,new PostedFragment()).commit();


        } else if (id == R.id.nav_profile) {
            //TO Profile Fragment
            ProfileFragment profileFragment = new ProfileFragment();
            Bundle bundle2 = new Bundle();
            bundle2.putString("email",email);
            bundle2.putString("name",name);
            bundle2.putString("telefono",telefono);
            profileFragment.setArguments(bundle2);
            FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
            fragmentTransaction2.add(R.id.content_main,profileFragment).addToBackStack(null);
            fragmentTransaction2.commit();
        }  else if (id == R.id.nav_logout) {
            //Log Out
            Intent intent = new Intent(getApplicationContext(),Inicio.class);
            startActivity(intent);
            finish();

        }   else if (id == R.id.nav_share){
            //To share fragment
            Share share = new Share();
            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            share.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.content_main,share).addToBackStack(null);
            fragmentTransaction.commit();

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }


}
