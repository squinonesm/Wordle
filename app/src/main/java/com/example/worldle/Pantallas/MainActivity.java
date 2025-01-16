package com.example.worldle.Pantallas;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.worldle.Fragments.InformacionFragment;
import com.example.worldle.R;
import com.example.worldle.Servicios.ReproductorMusica;


/**
 * Esta actividad es la pantalla principal de la aplicación donde el usuario puede:
 * 1. Iniciar el juego.
 * 2. Ver información sobre el juego.
 * 3. Acceder al archivo de palabras.
 * 4. Cerrar la aplicación.
 *
 * Se incluyen animaciones para la interacción del usuario y un GIF como fondo de pantalla.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ImageButton btnStart = findViewById(R.id.btnStart);
        ImageButton btnExit = findViewById(R.id.btnExit);
        Animation animacionPulsar = AnimationUtils.loadAnimation(this, R.anim.animacion);

        // Cargar GIF como portada (fondo de pantalla)
        ImageView portadaAnimada = findViewById(R.id.gifArchivo);
        Glide.with(this)
                .load(R.drawable.portada)
                .into(portadaAnimada);

        //Musica Base
        iniciarMusica("base");

        // Acción al pulsar el botón "Iniciar" para empezar a jugar
        btnStart.setOnClickListener(view -> {
            view.startAnimation(animacionPulsar);
            // Añadir un retraso de 1 segundo antes de iniciar la actividad
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, PalabraDelDia.class);
                startActivity(intent);
            }, 350);
        });

        // Acción al pulsar el botón "Información"
        ImageButton btnInformacion = findViewById(R.id.btnInformacion);
        btnInformacion.setOnClickListener(view -> {
            view.startAnimation(animacionPulsar);

            // Añadir un retraso para dar sensacion de pulsar un boton
            new Handler().postDelayed(() -> {
                InformacionFragment informacionDialogFragment = new InformacionFragment();
                informacionDialogFragment.show(getSupportFragmentManager(), "InformacionDialog");
            }, 350);
        });


        // Acción al pulsar el botón del archivo de palabras
        ImageButton btnArchivo = findViewById(R.id.btnArchivo);
        btnArchivo.setOnClickListener(view -> {
            view.startAnimation(animacionPulsar);

            // Añadir un retraso para dar sensacion de pulsar un boton
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, ArchivoPalabras.class);
                startActivity(intent);
            }, 350);
        });

        // Acción al pulsar el botón "Salir"
        btnExit.setOnClickListener(view -> {
            view.startAnimation(animacionPulsar);

            // Añadir un retraso para dar sensacion de pulsar un boton
            new Handler().postDelayed(() -> {
                finishAffinity(); // Cierra la aplicación
            }, 350);
        });

    }

    private void iniciarMusica(String tipoMusica) {
        Intent musica = new Intent(this, ReproductorMusica.class);
        musica.putExtra("musica", tipoMusica); // Envía el tipo de música
        startService(musica);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detiene el servicio de música
        Intent musica = new Intent(this, ReproductorMusica.class);
        stopService(musica);
    }
}
