package com.vehicleclassifier.app;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;

    Rect rect;
    Fragment login, signUp;

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState    <p> If the activity is being re-initialized after
     *                              previously being shut down then this Bundle contains the
     *                              data it most recently supplied in onSaveInstanceState(Bundle).
     *                              Note: Otherwise it is null. </p>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        login = new Login();
        signUp = new SignUp();

        fragment_replacement(login);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setClipToOutline(true);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            /**
             * Invoked when a tab has been selected.
             *
             * @param tab   <p> The tab selected. </p>
             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition())  {

                    case 0:
                        fragment_replacement(login);    //Takes the user to login page (or fragment).
                        break;
                    case 1:
                        fragment_replacement(signUp);   //Takes the user to signUp page.
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        try {

            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            if (mAuth.getCurrentUser() != null) {

                Toast.makeText(getApplicationContext(), "Signed In As " + mAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), CameraScan.class));

            }

        } catch (Exception e)   {
            e.printStackTrace();
        }

    }


    /**
     * Method to replace fragments between {@link Login} and {@link SignUp}.
     *
     * @param fragment  <p> The fragment that has to be replaced. </p>
     */
    private void fragment_replacement(Fragment fragment)    {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.relative, fragment)
                .commit();

    }

}
