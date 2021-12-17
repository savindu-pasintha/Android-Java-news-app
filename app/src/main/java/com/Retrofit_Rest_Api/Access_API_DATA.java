package com.Retrofit_Rest_Api;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.Adapter.Adapter_MainActivity_RecyclerTo_ItemsLayout;
import com.Model.Articles;
import com.Model.Headlines;
import com.worldtopnewsappsavindu.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Access_API_DATA extends AppCompatActivity {
        RecyclerView recyclerView;
        SwipeRefreshLayout swipeRefreshLayout;
        EditText etQuery;
        Button btnSearch,btnAboutUs;
        Dialog dialog;
        final String API_KEY = "209ef3dd92b94868bb9f733db7df01c2";
        Adapter_MainActivity_RecyclerTo_ItemsLayout adapter;
        List<Articles> articles = new ArrayList<>();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            swipeRefreshLayout = findViewById(R.id.swipeRefresh);
            recyclerView = findViewById(R.id.recyclerView);

            etQuery = findViewById(R.id.etQuery);
            btnSearch = findViewById(R.id.btnSearch);
            btnAboutUs = findViewById(R.id.aboutUs);
            dialog = new Dialog(getApplicationContext());

            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            final String country = getCountry();


            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    retrieveJson("",country,API_KEY);
                }
            });
            retrieveJson("",country,API_KEY);

            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!etQuery.getText().toString().equals("")){
                        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                retrieveJson(etQuery.getText().toString(),country,API_KEY);
                            }
                        });
                        retrieveJson(etQuery.getText().toString(),country,API_KEY);
                    }else{
                        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                retrieveJson("",country,API_KEY);
                            }
                        });
                        retrieveJson("",country,API_KEY);
                    }
                }
            });

            btnAboutUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog();
                }
            });

        }

        public void retrieveJson(String query ,String country, String apiKey){

            swipeRefreshLayout.setRefreshing(true);
            Call<Headlines> call;
            if (!etQuery.getText().toString().equals("")){
                call= ApiClient.getInstance().getApi().getSpecificData(query,apiKey);
            }else{
                call= ApiClient.getInstance().getApi().getHeadlines(country,apiKey);
            }

            call.enqueue(new Callback<Headlines>() {
                @Override
                public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                    if (response.isSuccessful() && response.body().getArticles() != null){
                        swipeRefreshLayout.setRefreshing(false);
                        articles.clear();
                        articles = response.body().getArticles();
                        adapter = new Adapter_MainActivity_RecyclerTo_ItemsLayout(getApplicationContext(),articles);
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<Headlines> call, Throwable t) {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        public String getCountry(){
            Locale locale = Locale.getDefault();
            String country = locale.getCountry();
            return country.toLowerCase();
        }

        public void showDialog(){
            Button btnClose;

            dialog.setContentView(R.layout.dot_pop_up_button_layout);
            dialog.show();

            btnClose = dialog.findViewById(R.id.close);

            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }


}
