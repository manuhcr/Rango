package com.example.rango;

import static com.example.rango.MainActivity.listaLugares;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rango.data.LugarRepository;
import com.example.rango.model.Lugar;

public class NovoLugarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_novo_lugar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configurando a toolbar
        Toolbar toolbar = findViewById(R.id.toolbarNovo);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Novo Lugar");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        EditText editarNome = findViewById(R.id.edtNome);
        EditText editarCategoria = findViewById(R.id.edtCategoria);
        EditText editarPreco = findViewById(R.id.edtPreco);
        EditText editarObservacao = findViewById(R.id.edtObservacao);
        Button btSalvar = findViewById(R.id.btnSalvar);

        // Configurar o botão de salvar
        btSalvar.setOnClickListener(v -> {
            if (editarNome.getText().toString().isEmpty()) {
                editarNome.setError("Digite um nome: ");
            } else if (editarCategoria.getText().toString().isEmpty()) {
                editarCategoria.setError("Obrigatório");
            } else if (editarPreco.getText().toString().isEmpty()) {
                editarPreco.setError("Obrigatório");
            } else {
                // Salvar o lugar no banco de dados
                Lugar novo = new Lugar(
                        editarNome.getText().toString(),
                        editarCategoria.getText().toString(),
                        Double.parseDouble(editarPreco.getText().toString()),
                        editarObservacao.getText().toString()
                );
                //listaLugares.add(novo);
                LugarRepository repository = new LugarRepository();
                repository.inserir(novo)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(this, "Lugar salvo com sucesso",
                                            Toast.LENGTH_SHORT).show();

                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Erro ao salvar: " ,
                                            Toast.LENGTH_SHORT).show();
                                });
                finish();
            }
        });
    }
}
