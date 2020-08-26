package Util;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firebasecursonotificacao.R;
import com.squareup.picasso.Picasso;

public class NotificacaoActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificacao_activity);

        imageView = (ImageView)findViewById(R.id.imageView_Notificacao);
        textView = (TextView)findViewById(R.id.textView_Notificacao);

        String url = getIntent().getStringExtra("url");
        String mensagem = getIntent().getStringExtra("msg");

        exibirNotificacao(url, mensagem);
    }

    private  void exibirNotificacao(String url, String mensagem)
    {
        try
        {
            Picasso.get().load(url).into(imageView);
            textView.setText(mensagem);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}