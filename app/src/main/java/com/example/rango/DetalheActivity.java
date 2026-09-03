package com.example.rango;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rango.model.Lugar;

public class DetalheActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalhe);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configurando a toolbar
        Toolbar toolbar = findViewById(R.id.toolbarDetalhe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Detalhes");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        Lugar dados = (Lugar) getIntent().getSerializableExtra("obj");

        TextView txtNome = findViewById(R.id.txtDetalheNome);
        TextView txtCategoria = findViewById(R.id.txtDetalheCategoria);
        TextView txtPreco = findViewById(R.id.txtDetalhePreco);
        TextView txtObservacao = findViewById(R.id.txtDetalheObservacao);
        TextView txtVotos = findViewById(R.id.txtDetalheVotos);

        txtNome.setText(dados.getNome());
        txtCategoria.setText(dados.getCategoria());
        txtPreco.setText("R$ " + dados.getPrecoMedio());
        txtObservacao.setText(dados.getObservacao());
        txtVotos.setText(dados.getVotos() == 1 ? dados.getVotos() + " voto" : dados.getVotos() + " votos");

    }



}