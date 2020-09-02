package com.example.firebasecursonotificacao;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

import Util.Util;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Switch switch_Sports, switch_Politica;
    private EditText editTetxt_NomeUsuario;
    private Button button_CadastrarToken, button_EnviarNotificacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switch_Sports = (Switch)findViewById(R.id.switch_Sports);
        switch_Politica = (Switch)findViewById(R.id.switch_Politica);



        /*switch_Sports.setOnClickListener(this);
        switch_Politica.setOnClickListener(this); */

        button_CadastrarToken =(Button)findViewById(R.id.button_CadastrarToken);
        button_EnviarNotificacao = (Button)findViewById(R.id.button_EnviarNotificacao);

        button_CadastrarToken.setOnClickListener(this);
        button_EnviarNotificacao.setOnClickListener(this);

        configurarSwitch();

        switch_Sports.setOnCheckedChangeListener(this);
        switch_Politica.setOnCheckedChangeListener(this);

    }

    private void configurarSwitch()
    {
        String topicoSports = Util.getTopico(getBaseContext(), "sports", "sports");
        String topicoPolitica = Util.getTopico(getBaseContext(), "politica", "politica");

        if(!topicoSports.isEmpty())
        {
            switch_Sports.setChecked(true);
        }
        else
        {
            switch_Sports.setChecked(false);
        }

        if(!topicoPolitica.isEmpty())
        {
            switch_Politica.setChecked(true);

        }
        else
        {
            switch_Politica.setChecked(false);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
       switch (buttonView.getId())
       {
           case R.id.switch_Sports:
               notificaoSports();
               break;

           case R.id.switch_Politica:
               notificaoPolitica();
               break;

       }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.button_CadastrarToken:
                obterToken2();
                //obterToken();
                break;

            case R.id.button_EnviarNotificacao:
                Toast.makeText(getBaseContext(), "Botão Enviar Notificação", Toast.LENGTH_LONG).show();
                break;
        }

    }

    private void notificaoSports()
    {
        boolean conexaoInternet = Util.statusInternet_MoWi(getBaseContext());

        if(conexaoInternet)
        {
            boolean status_Sports = switch_Sports.isChecked();

            if(status_Sports)
            {
                FirebaseMessaging.getInstance().subscribeToTopic("sports").addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        boolean inscricaoNotificacao = task.isComplete();
                        if(inscricaoNotificacao)
                        {
                            Toast.makeText(getBaseContext(), "A Inscrição para Sports foi realizada com sucesso.", Toast.LENGTH_LONG).show();
                            Util.setTopico(getBaseContext(), "sports", "sports");
                        }
                        else
                        {
                            switch_Sports.setChecked(false);
                            Toast.makeText(getBaseContext(), "A Inscrição para Sports não foi bem sucedida.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
            else
            {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("sports").addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        boolean inscricaoNotSports_Excluida = task.isComplete();

                        if(inscricaoNotSports_Excluida)
                        {
                            Util.setTopico(getBaseContext(), "sports", "");
                            Toast.makeText(getBaseContext(), "A Inscrição para Sports foi cancelada.", Toast.LENGTH_LONG).show();
                            configurarSwitch();
                        }
                        else
                        {
                            configurarSwitch();
                            Toast.makeText(getBaseContext(), "A Inscrição para Sports não foi cancelada, devido algum erro.", Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }
        }
        else
        {
           configurarSwitch();
           Toast.makeText(getBaseContext(), "Sem conexão com a Internet, Verique sua rede.", Toast.LENGTH_LONG).show();
        }

    }

    private void notificaoPolitica()
    {
        boolean conexaoInternet = Util.statusInternet_MoWi(getBaseContext());

        if(conexaoInternet)
        {
            boolean status_Politica = switch_Politica.isChecked();

            if(status_Politica)
            {
                FirebaseMessaging.getInstance().subscribeToTopic("politica").addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        boolean inscricaoNotificacao = task.isComplete();
                        if(inscricaoNotificacao)
                        {
                            Toast.makeText(getBaseContext(), "A Inscrição para Política foi realizada com sucesso.", Toast.LENGTH_LONG).show();
                            Util.setTopico(getBaseContext(), "politica", "politica");
                        }
                        else
                        {
                            switch_Politica.setChecked(false);
                            Toast.makeText(getBaseContext(), "A Inscrição para Politica não foi bem sucedida.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
            else
            {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("politica").addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        boolean inscricaoNotPolitica_Excluida = task.isComplete();

                        if(inscricaoNotPolitica_Excluida)
                        {
                            Util.setTopico(getBaseContext(), "politica", "");
                            Toast.makeText(getBaseContext(), "A Inscrição para Politica foi cancelada.", Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            configurarSwitch();
                            Toast.makeText(getBaseContext(), "A Inscrição para Politica não foi cancelada, devido algum erro.", Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }
        }
        else
        {
            configurarSwitch();
            Toast.makeText(getBaseContext(), "Sem conexão com a Internet, Verique sua rede.", Toast.LENGTH_LONG).show();
        }

    }
    private void obterToken()
    {
       final String autorizacao = "423037240842";

       final String firebase = "FCM";

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                   FirebaseInstanceId.getInstance().deleteInstanceId(); // Remoção do Token

                   String token = FirebaseInstanceId.getInstance().getToken(autorizacao, firebase);
                   Log.d("TokenTeste2", token);

                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();

        {

        }
    }

    private void obterToken2()
    {

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task)
            {
                boolean resultado = task.isSuccessful();

                if(resultado)
                {
                    String token = task.getResult().toString();
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Erro ao Tentar Obter o Token",
                                   Toast.LENGTH_LONG).show();
                }
            }
        });
    }



}