package com.egkhan.googlesignin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    LinearLayout prof_Section;
    Button signOutBtn;
    SignInButton signInBtn;
    TextView nameTV, emailTV;
    ImageView prof_pic;
    GoogleApiClient googleApiClient;
    public static final int REQ_CODE = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prof_Section = (LinearLayout) findViewById(R.id.prof_section);
        signOutBtn = (Button) findViewById(R.id.logOutBtn);
        signInBtn = (SignInButton) findViewById(R.id.loginBtn);
        nameTV = (TextView) findViewById(R.id.nameTV);
        emailTV = (TextView) findViewById(R.id.emailTV);
        prof_pic = (ImageView) findViewById(R.id.prof_pic);
        signInBtn.setOnClickListener(this);
        signOutBtn.setOnClickListener(this);

        prof_Section.setVisibility(View.GONE);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient= new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.loginBtn:
                signIn();
                break;
            case R.id.logOutBtn:
                signOut();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });
    }

    void handleResult(GoogleSignInResult result) {
        if(result.isSuccess())
        {
            GoogleSignInAccount googleSignInAccount = result.getSignInAccount();
            String name = googleSignInAccount.getDisplayName();
            String email = googleSignInAccount.getEmail();
            String img_url = googleSignInAccount.getPhotoUrl().toString();
            nameTV.setText(name);
            emailTV.setText(email);
            Glide.with(this).load(img_url).into(prof_pic);
            updateUI(true);
        }
        else
            updateUI(false);
    }

    void updateUI(boolean isLogin) {
            if(isLogin)
            {
                prof_Section.setVisibility(View.VISIBLE);
                signInBtn.setVisibility(View.GONE);
            }
            else{
                prof_Section.setVisibility(View.GONE);
                signInBtn.setVisibility(View.VISIBLE);
            }
    }
}
