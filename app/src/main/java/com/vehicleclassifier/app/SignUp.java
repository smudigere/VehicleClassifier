package com.vehicleclassifier.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignUp extends Fragment implements
        View.OnClickListener{


    final static String TAG = "Firebase AUTH";

    private TextInputEditText email, password;
    private FirebaseAuth mAuth;
    private SharedPreferences prefs;

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater  <p> The LayoutInflater object that can be used to inflate any views in the fragment. </p>
     * @param container     <p> If non-null, this is the parent view that the fragment's UI should
     *                      be attached to. The fragment should not add the view itself,
     *                      but this can be used to generate the LayoutParams of the view. </p>
     * @param savedInstanceState    <p> If non-null, this fragment is being re-constructed from
     *                              a previous saved state as given here. </p>
     * @return  <p> Return the View for the fragment's UI, or null.  </p>
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();

        email = (TextInputEditText) view.findViewById(R.id.email);
        password = (TextInputEditText) view.findViewById(R.id.password);

        Button signIn = (Button) view.findViewById(R.id.login_button);
        signIn.setText(R.string.SignUp);
        signIn.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId())   {

            case R.id.login_button:

                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    Toast.makeText(getActivity(), "Signed Up As " + user.getEmail(), Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(getContext(), CameraScan.class));

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(getActivity(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                break;
        }
    }
}
