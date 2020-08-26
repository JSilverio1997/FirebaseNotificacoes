package Firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.firebasecursonotificacao.MainActivity;
import com.example.firebasecursonotificacao.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;

import Util.NotificacaoActivity;

public class FirebaseService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage notificacao)
    {
        String titulo;
        String mensagem;

        String nome;
        String idade;
        String urlImagem;
        String mensagemCompactada;

        // Código para Notificação Personalizada
       if(notificacao.getData().size() > 0)
       {
            titulo = notificacao.getData().get("titulo");
            mensagem = notificacao.getData().get("mensagem");
            nome = notificacao.getData().get("nome");
            idade = notificacao.getData().get("idade");
            urlImagem = notificacao.getData().get("urlimagem");

            mensagemCompactada = mensagem + " - " + nome + " - " + idade + " - " + urlImagem;
            sendNotification_2(titulo, mensagemCompactada, urlImagem);

        }
        // Código para Notificação Simples
        else if(notificacao.getNotification() != null)
        {
             titulo = notificacao.getNotification().getTitle();
             mensagem = notificacao.getNotification().getBody();

            sendNotification_1(titulo, mensagem);
        }

        super.onMessageReceived(notificacao);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        Log.d("tokenTeste", s);
        super.onNewToken(s);
    }

    private  void sendNotification_1(String titulo, String mensagem)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String canal = getString(R.string.default_notification_channel_id);

        Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, canal)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(titulo)
                .setContentText(mensagem)
                .setSound( som)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(canal, "canal", NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(channel);

        }

        notificationManager.notify(0, notification.build());
    }

    private  void sendNotification_2(final String titulo, final String mensagem, final String url) {
        Glide.with(getBaseContext()).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap bitmap, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                Intent intent = new Intent(getBaseContext(), NotificacaoActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("msg", mensagem);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

                String canal = getString(R.string.default_notification_channel_id);

                Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notification = new NotificationCompat.Builder(getBaseContext(), canal)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(titulo)
                        .setContentText(mensagem)
                        // .setStyle(new NotificationCompat.BigTextStyle().bigText(mensagem))
                        .setLargeIcon(bitmap)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                        .setSound(som)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(canal, "canal", NotificationManager.IMPORTANCE_DEFAULT);

                    notificationManager.createNotificationChannel(channel);

                }

                notificationManager.notify(0, notification.build());

                return false;
            }


        }).submit();

        /* Bitmap bitmap = null;

        try
        {
            bitmap = Picasso.get().load(url).get();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        } */

    }
}
