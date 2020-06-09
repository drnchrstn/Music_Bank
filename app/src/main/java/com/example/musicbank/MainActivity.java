package com.example.musicbank;

import android.content.Intent;
import android.os.Bundle;

import com.example.musicbank.ApiTransactions.ApiRetrofitHelper;
import com.example.musicbank.DbBaseColumns.DbHelper;
import com.example.musicbank.Objects.SongResultResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DbHelper(MainActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                    Intent intent = new Intent(MainActivity.this, SearchSongActivity.class);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.slideup, R.anim.noanimation);
//
////                ApiRetrofitHelper.getInstance().getSong("with a smile Eraserheads", new Callback<SongResultResponse>() {
////                    @Override
////                    public void onResponse(Call<SongResultResponse> call, Response<SongResultResponse> response) {
////
////                        Log.v("","");
////                    }
////
////                    @Override
////                    public void onFailure(Call<SongResultResponse> call, Throwable t) {
////                        Log.v("","");
////                    }
////                });
//
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

//        ApiRetrofitHelper.getInstance().getSong("with a smile Eraserheads", new Callback<SongResultResponse>() {
//            @Override
//            public void onResponse(Call<SongResultResponse> call, Response<SongResultResponse> response) {
//
//                Log.v("","");
//            }
//
//            @Override
//            public void onFailure(Call<SongResultResponse> call, Throwable t) {
//                Log.v("","");
//            }
//        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
