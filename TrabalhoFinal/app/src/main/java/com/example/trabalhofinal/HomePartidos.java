package com.example.trabalhofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapters.PartidoAdapter;
import api.ApiService;
import api.RetrofitClient;
import models.Partido;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePartidos extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PartidoAdapter partidoAdapter;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_partidos);

        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Dados();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem i) {
                return selectedItemNavigationView(i);
            }
        });
    }

    private void Dados() {
        ApiService service = RetrofitClient.criarApiService();
        Call<ResponseBody> call = service.getPartidos();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.d("ApiResult", "Raw API Response: " + responseData);

                        List<Partido> parts = parseJson(responseData);

                        setupRecyclerView(parts);
                    } catch (IOException error) {
                        error.printStackTrace();
                    }
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                handleConnectionError(throwable);
            }
        });
    }

    private List<Partido> parseJson(String jsonString) {
        List<Partido> parts = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(jsonString);
            JSONArray dados = json.getJSONArray("dados");

            for (int i = 0; i < dados.length(); i++) {
                JSONObject partJson = dados.getJSONObject(i);

                int id = partJson.getInt("id");
                String sigla = partJson.getString("sigla");
                String nome = partJson.getString("nome");
                String uri = partJson.getString("uri");

                Partido partido = new Partido(id, nome, sigla);
                parts.add(partido);
            }
        } catch (JSONException error) {
            error.printStackTrace();
        }

        return parts;
    }

    private void setupRecyclerView(List<Partido> parts) {
        partidoAdapter = new PartidoAdapter(parts, new PartidoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Partido part) {
                Intent intent = new Intent(HomePartidos.this, com.example.trabalhofinal.Partido.class);
                intent.putExtra("ID_PARTIDO", part.getId());
                Log.d("RecyclerView", "ID do Partido: " + part.getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(partidoAdapter);
    }

    private void handleApiError(Response<?> response) {
        try {
            Log.e("API", "Erro: " + response.code());
            String errorBody = "Resposta de erro indisponível";

            if (response.errorBody() != null) {
                errorBody = response.errorBody().string();
                Log.e("API", "Resposta de erro: " + errorBody);
            }

        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    private void handleConnectionError(Throwable t) {
        Log.e("API", "Erro de conexão", t);
    }

    private boolean selectedItemNavigationView(MenuItem item) {
        int i = item.getItemId();

        if (i == R.id.sair) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (i == R.id.config) {
            startActivity(new Intent(this, Config.class));
        } else if (i == R.id.deputados) {
            startActivity(new Intent(this, HomeDeputados.class));
        } else if (i == R.id.partidos) {
            startActivity(new Intent(this, HomePartidos.class));
        }

        return false;
    }

}