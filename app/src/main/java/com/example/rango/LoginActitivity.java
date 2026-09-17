package com.example.rango;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;


import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActitivity extends AppCompatActivity {

    EditText campoEmail, campoSenha;

    Button btnEntrar, btnCriarConta, btnEsqueceuSenha;

    SignInButton btnGoogle;

    private FirebaseAuth autenticar;

    private ActivityResultLauncher<Intent> signInLauncher =
            registerForActivityResult( new ActivityResultContracts.StartActivityForResult(),
                                       result -> {
                                           Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                                           task.addOnSuccessListener(googleAccount -> {
                                              AuthCredential credential = GoogleAuthProvider.getCredential(googleAccount.getIdToken(), null);
                                              autenticar.signInWithCredential(credential);

                                              Intent rota = new Intent(this, MainActivity.class);
                                              startActivity(rota);
                                              finish();
                                           }).addOnFailureListener(e -> {
                                               Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                           });
                                       });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        campoSenha = findViewById(R.id.campoSenha);
        campoEmail = findViewById(R.id.campoEmail);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnCriarConta = findViewById(R.id.btnCriarConta);
        btnEsqueceuSenha = findViewById(R.id.btnEsqueceuSenha);
        btnGoogle = findViewById(R.id.btnGoogle);

        //Abrir conexão com serviço de autenticação
        autenticar = FirebaseAuth.getInstance();
        //Verificar se o usuário está logado
        if (autenticar.getCurrentUser() != null){
            Intent rota = new Intent(this, MainActivity.class);
            startActivity(rota);
            finish();
        };
        btnEntrar.setOnClickListener(v -> entrar());
        btnCriarConta.setOnClickListener(v -> criarConta());
        btnEsqueceuSenha.setOnClickListener(v -> recuperarSenha());
        btnGoogle.setOnClickListener(v -> google());
    }
    private void entrar(){
        if (campoEmail.getText().toString().isEmpty()){
            campoEmail.setError("Preencha o e-mail (Obrigatório)");

        }
        else if (campoSenha.getText().toString().isEmpty()){
            campoSenha.setError("Preencha a senha (Obrigatório)");

        } else {
            autenticar.signInWithEmailAndPassword(campoEmail.getText().toString(), campoSenha.getText().toString())
                    .addOnFailureListener(e -> {
                        if (e instanceof FirebaseAuthInvalidCredentialsException){
                            Toast.makeText(this, "E-mail ou senha inválidos", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (e instanceof FirebaseAuthEmailException){
                            Toast.makeText(this, "E-mail não cadastrado ou inválido", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnSuccessListener( authResult -> {
                        Toast.makeText(this, "Conta criada com sucesso", Toast.LENGTH_SHORT).show();
                        Intent rota = new Intent(this, MainActivity.class);
                        startActivity(rota);
                    });
        }
    }
    private void criarConta(){
        if (campoEmail.getText().toString().isEmpty()){
            campoEmail.setError("Preencha o e-mail (Obrigatório)");

        }
        else if (campoSenha.getText().toString().isEmpty()){
            campoSenha.setError("Preencha a senha (Obrigatório)");

        } else {
            autenticar.createUserWithEmailAndPassword(campoEmail.getText().toString(), campoSenha.getText().toString())
                    .addOnFailureListener(e -> {
                       if (e instanceof FirebaseAuthInvalidCredentialsException){
                           Toast.makeText(this, "E-mail ou senha inválidos", Toast.LENGTH_SHORT).show();
                           return;
                       }
                       if (e instanceof FirebaseAuthEmailException){
                           Toast.makeText(this, "E-mail não cadastrado ou inválido", Toast.LENGTH_SHORT).show();
                           return;
                       }
                       Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnSuccessListener( authResult -> {
                        Toast.makeText(this, "Conta criada com sucesso", Toast.LENGTH_SHORT).show();
                        Intent rota = new Intent(this, MainActivity.class);
                        startActivity(rota);
                    });
        }
    }
    private void recuperarSenha() {
        FirebaseAuth autenticar = FirebaseAuth.getInstance();
        String emailAdress = campoEmail.getText().toString();
        autenticar.sendPasswordResetEmail(emailAdress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActitivity.this, "E-mail enviado com sucesso", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Toast.makeText(LoginActitivity.this, "Erro ao enviar e-mail", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

    }

    private void google(){
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        signInLauncher.launch(signInIntent);
    }

}