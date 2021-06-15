package com.example.homeutilityservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.parse.ParseUser;

import static com.example.homeutilityservice.R.*;

public class UserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    String[] category = {"Select Your category", "Electrician", "Carpenter", "Painter"};
    String[] location = {"select Location", "Godadara", "Limbayat", "Dindoli", "Pandasara", "Sachin"};
    Spinner locationSpinner;
    Spinner categorySpinner;
    Button doneButton;
    ConstraintLayout constraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_user);
        constraintLayout = findViewById(id.constraint1);
        Intent intent = getIntent();

        drawerLayout = findViewById(R.id.drawable);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(id.navigationMenu);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().hide();
        // location spinner

        locationSpinner = findViewById(id.locationSpinner);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, location);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(arrayAdapter);
        //category spinner
        categorySpinner = findViewById(id.categorySpinnner);

        ArrayAdapter<String> categoryArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, category);
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryArrayAdapter);
        // done Button
        doneButton = findViewById(id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("location ", locationSpinner.getSelectedItem().toString());
                Log.i("category", categorySpinner.getSelectedItem().toString());

                if (locationSpinner.getSelectedItem() == location[0] || categorySpinner.getSelectedItem().toString() == category[0]) {
                    Toast.makeText(UserActivity.this, "select properly", Toast.LENGTH_LONG).show();
                } else {
                    getSupportActionBar().show();

                    getSupportActionBar().setTitle(locationSpinner.getSelectedItem().toString());
                        locationSpinner.setVisibility(View.INVISIBLE);
                        categorySpinner.setVisibility(View.INVISIBLE);
                        doneButton.setVisibility(View.INVISIBLE);
                    if (categorySpinner.getSelectedItem().toString().equals("Electrician")) {

                        getSupportFragmentManager().beginTransaction().replace(id.fragment_container, new ElectricianFragment()).commit();

                    } else if (categorySpinner.getSelectedItem().toString().equals("Carpenter")) {
                        getSupportFragmentManager().beginTransaction().replace(id.fragment_container, new CarpenterFragment()).commit();
                    } else if (categorySpinner.getSelectedItem().toString().equals("Painter")) {
                        getSupportFragmentManager().beginTransaction().replace(id.fragment_container, new PainterFragment()).commit();
                    }
                }
            }
        });


    }

    //on back press
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    //item selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);


    }

    // navigation selection
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case id.electrician:
                getSupportFragmentManager().beginTransaction().replace(id.fragment_container, new ElectricianFragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case id.carpenter:
                getSupportFragmentManager().beginTransaction().replace(id.fragment_container, new CarpenterFragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case id.painter:
                getSupportFragmentManager().beginTransaction().replace(id.fragment_container, new PainterFragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case id.account:
                getSupportFragmentManager().beginTransaction().replace(id.fragment_container, new AccountFragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case id.order:
                getSupportFragmentManager().beginTransaction().replace(id.fragment_container, new OrderFragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case id.logout1:
                ParseUser.logOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(this, "logout Successfully", Toast.LENGTH_SHORT).show();


        }
        return true;
    }


}
