package com.example.trabalhofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapters.DeputadoAdapter;
import api.ApiService;
import api.RetrofitClient;
import models.Deputado;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeDeputados extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DeputadoAdapter deputadoAdapter;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_deputados);

        recyclerView = findViewById(R.id.recyclerView);
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
        Call<ResponseBody> call = service.getDeputados();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.d("ApiResult", "Raw API Response: " + responseData);

                        List<Deputado> deps = parseJson(responseData);

                        setupRecyclerView(deps);
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

    private List<Deputado> parseJson(String jsonString) {
        List<Deputado> deps = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(jsonString);
            JSONArray dados = json.getJSONArray("dados");

            for (int i = 0; i < dados.length(); i++) {
                JSONObject depJson = dados.getJSONObject(i);

                int id = depJson.getInt("id");
                String siglaPartido = depJson.getString("siglaPartido");
                String nome = depJson.getString("nome");
                String email = depJson.getString("email");
                String siglaUf = depJson.getString("siglaUf");

                Deputado deputado = new Deputado(id, nome, email, siglaPartido, siglaUf);
                deps.add(deputado);
            }
        } catch (JSONException error) {
            error.printStackTrace();
        }

        return deps;
    }

    private void setupRecyclerView(List<Deputado> deps) {
        deputadoAdapter = new DeputadoAdapter(deps, new DeputadoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Deputado dep) {
                Intent intent = new Intent(HomeDeputados.this, com.example.trabalhofinal.Deputado.class);
                intent.putExtra("ID_DEPUTADO", dep.getId());
                Log.d("RecyclerView", "ID do Deputado: " + dep.getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(deputadoAdapter);
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