package com.sdwnmt.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.sdwnmt.demo.Modal.LoginResp;

import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout uname,upass;
    Button btn;
    private Window window;
    private com.sdwnmt.demo.Modal.Response resp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.lognav));
        }

        uname = findViewById(R.id.uname);
        upass = findViewById(R.id.upass);
        btn = findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

    }

    public void performLogin() {
        final UserSes userSes = new UserSes(this);
        String mob = uname.getEditText().getText().toString();
        String adhar = upass.getEditText().getText().toString();
        Toast.makeText(this, mob+" "+adhar, Toast.LENGTH_SHORT).show();
        final Gson gson = new Gson();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LoginResp> call = apiInterface.verifyLogin(mob,adhar);
        call.enqueue(new Callback<LoginResp>() {
            @Override
            public void onResponse(Call<LoginResp> call, Response<LoginResp> response) {
                try {
                    if (!(response.body().getResponse().getSuccess()).equals("1")) {
//                        startActivity(new Intent(LoginActivity.this,User_Home.class));
                        Toast.makeText(LoginActivity.this, "Incorrect Username or password", Toast.LENGTH_SHORT).show();

                    } else {
                        userSes.setResp(gson.toJson(response.body().getResponse()));
                        Intent intent = new Intent(LoginActivity.this, User_Home.class);
                        startActivity(intent);
                        finish();

                    }
                }catch (Exception e){
                    Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResp> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Please Check your Internet Connection..", Toast.LENGTH_SHORT).show();
            }
        });
        uname.getEditText().setText("");
        upass.getEditText().setText("");
    }
//
//    public void setData(){

//    }
}
