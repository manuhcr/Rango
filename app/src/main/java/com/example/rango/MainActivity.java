package com.example.rango;

import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rango.adapter.LugarAdapter;
import com.example.rango.data.Catalogo;
import com.example.rango.model.Lugar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LugarAdapter.Acao {

    static List<Lugar> listaLugares = new ArrayList<>();
    private LugarAdapter adapter;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            FloatingActionButton btNovo = findViewById(R.id.fabNovo);
            btNovo.setOnClickListener(v -> {
                Intent intent = new Intent(this, NovoLugarActivity.class);
                startActivity(intent);
            });

            listaLugares = Catalogo.inicial();

            RecyclerView rvLugares = findViewById(R.id.rvLugares);
            rvLugares.setLayoutManager(new LinearLayoutManager(this));

            adapter = new LugarAdapter(listaLugares, MainActivity.this);
            rvLugares.setAdapter(adapter);

            //Deslizar
            configDeslizar();
        }


        //Configurar o deslizar
        private void configDeslizar(){
            ItemTouchHelper.SimpleCallback deslizar = new ItemTouchHelper.SimpleCallback(
                    0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
            ) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                    //usado para movimentar os itens, está falso pois eu quero que esse método deslize
                }
                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    int posicao = viewHolder.getAdapterPosition();
                    Lugar item = listaLugares.get(posicao);
                    listaLugares.remove(posicao);
                    adapter.notifyItemRemoved(posicao);

                    //Avisar

                    Snackbar.make(findViewById(R.id.rvLugares), "Lugar removido", Snackbar.LENGTH_LONG).
                            setAction("Desfazer", v -> {
                                listaLugares.add(item);
                                adapter.notifyDataSetChanged();
                            }).show();
                }
            };
            new ItemTouchHelper(deslizar).attachToRecyclerView(findViewById(R.id.rvLugares));
        }
        @Override
        public void votar(Lugar lugar) {
            lugar.setVotos(lugar.getVotos() + 1);
            Catalogo.ordenarPorVotos(listaLugares);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void detalhar(Lugar lugar) {
            Intent rota = new Intent(this, DetalheActivity.class);
            rota.putExtra("obj", lugar);
            startActivity(rota);
        }

        @Override
        protected void onResume(){
            super.onResume();
            adapter.notifyDataSetChanged();
        }
}
