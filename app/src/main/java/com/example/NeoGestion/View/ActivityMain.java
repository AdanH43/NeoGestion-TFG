package com.example.NeoGestion.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.NeoGestion.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityMain extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MenuItem previousMenuItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            replaceFragment(new SimpleFragment());
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(false);

                if (item.getItemId() == R.id.nav_home) {
                    replaceFragment(new SimpleFragment());

                    item.setChecked(true);
                }
                 else if (item.getItemId() == R.id.nav_usuarios) {
                    replaceFragment(new MainTrabajadores());

                    item.setChecked(true);
                } else if (item.getItemId() == R.id.nav_productos) {
                    item.setChecked(true);

                    replaceFragment(new ProductosFragment());
                } else if (item.getItemId() == R.id.nav_chat) {
                     item.setChecked(true);

                     replaceFragment(new ChatFragment());
                } else if (item.getItemId() == R.id.nav_cerrar) {
                    mAuth.signOut();
                    finish();
                }
                if (previousMenuItem != null) {
                    previousMenuItem.setChecked(false);
                }
                previousMenuItem = item;
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        updateNavHeader();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        replaceFragment(new SimpleFragment());
    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void updateNavHeader() {

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_name);
        TextView userEmailTextView = headerView.findViewById(R.id.user_email);
        ImageView userImageView = headerView.findViewById(R.id.user_image);


        String userName = mAuth.getCurrentUser().getEmail();
        String userEmail = mAuth.getCurrentUser().getDisplayName();
        userNameTextView.setText(userEmail);
        userEmailTextView.setText(userName);
        userImageView.setImageResource(R.drawable.baseline_person_24);
    }
}
