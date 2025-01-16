package com.example.worldle.Pantallas;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.worldle.R;
import com.example.worldle.Servicios.ReproductorMusica;

/**
 * Esta actividad se muestra al finalizar el juego, donde se indica si el jugador ha ganado o perdido.
 * Además, ofrece la opción de rejugar, lo que te redirige a la pantalla inicial.
 */
public class PantallaFinal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_final);
        Animation animacionPulsar = AnimationUtils.loadAnimation(this, R.anim.animacion);

        // Recupera el valor de "ganador" enviado por la actividad anterior
        boolean ganador = getIntent().getBooleanExtra("ganador", false);

        ImageView imagenFinal = findViewById(R.id.imagenFinal);

        // Si el jugador ganó, muestra el gif de ganar_partida
        // Sin embargo si el jugador peirde, se muestra el gif de game_over
        if (ganador) {
            Glide.with(this)
                    .load(R.drawable.ganar_partida)
                    .into(imagenFinal);
        } else {
            Glide.with(this)
                    .load(R.drawable.game_over)
                    .into(imagenFinal);
        }

        // Referencia al botón de rejugar
        ImageButton btnRejugar = findViewById(R.id.btnRejugar);

        // Añadir un retraso para dar sensacion de pulsar un boton
        btnRejugar.setOnClickListener(view -> {
            view.startAnimation(animacionPulsar);
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(PantallaFinal.this, MainActivity.class);
                startActivity(intent);
            }, 350);
        });


        // Llamar al servicio de música según el resultado
        String tipoMusica = ganador ? "ganar" : "perder"; // Elegir la música correspondiente
        iniciarMusica(tipoMusica);
    }

    /**
     * Inicia el servicio de música con el tipo de música elegido.
     *
     * @param tipoMusica El tipo de música a reproducir (ganar, perder).
     */
    private void iniciarMusica(String tipoMusica) {
        Intent musica = new Intent(this, ReproductorMusica.class);
        musica.putExtra("musica", tipoMusica);
        startService(musica);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detener el servicio de música cuando se destruye la actividad
        Intent musica = new Intent(this, ReproductorMusica.class);
        stopService(musica);
    }
}

