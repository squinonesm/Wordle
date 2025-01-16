package com.example.worldle.Servicios;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.example.worldle.R;

public class ReproductorMusica extends Service {
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Obtener el tipo de musica a reproducir
        String musica = intent.getStringExtra("musica");

        // Verifica qué música se va a reproducir
        if (musica != null) {
            int recurso = R.raw.musica_inicial; // Música base (predeterminada)
            if (musica.equals("ganar")) {
                recurso = R.raw.musica_ganar;  // Música al ganar
            } else if (musica.equals("perder")) {
                recurso = R.raw.musica_perder; // Música al perder
            } else if (musica.equals("juego")) {
                recurso = R.raw.musica_jugando;  // Música para jugar
            } else if (musica.equals("base")) {
                recurso = R.raw.musica_inicial;  // Música base
            } else if (musica.equals("archivo")) {
                recurso = R.raw.musica_archivo; // Música archivo
            }

            // Reproduce la música correspondiente
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }

            mediaPlayer = MediaPlayer.create(this, recurso);
            mediaPlayer.setLooping(true); // Para que se reproduzca en bucle
            mediaPlayer.start(); // Inicia la música
        }

        // El servicio se reiniciará si el sistema lo detiene
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Detener y liberar el MediaPlayer
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // Este método devuelve null porque no necesitamos vincular el servicio a ninguna actividad.
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
