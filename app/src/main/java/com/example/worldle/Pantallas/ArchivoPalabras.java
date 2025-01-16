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
 * Esta clase es la actividad "ArchivoPalabras", en la cual se presentan 25 botones.
 * Al hacer clic en cada uno de ellos, se iniciara una animación y se pasara un ID de palabra
 * a la actividad "PalabraDelDia", de esta forma cada boton nos permitira acceder a una palabra concreta
 * asociada a un ID, para poder jugar con ella en "Palabra del dia".
 */
public class ArchivoPalabras extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archivo_palabras);

        //Se carga la animacion
        Animation animScale = AnimationUtils.loadAnimation(this, R.anim.animacion);

        //Iniciar la musica
        iniciarMusica("archivo");

        // Array que contiene las IDs de los botones
        // Que el usuario pulsara para seleccionar una palabra del archivo
        int[] buttonIds = {
                R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5,
                R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btn10,
                R.id.btn11, R.id.btn12, R.id.btn13, R.id.btn14, R.id.btn15,
                R.id.btn16, R.id.btn17, R.id.btn18, R.id.btn19, R.id.btn20,
                R.id.btn21, R.id.btn22, R.id.btn23, R.id.btn24, R.id.btn25,
                R.id.btn26, R.id.btn27, R.id.btn28, R.id.btn29, R.id.btn30,
                R.id.btn31, R.id.btn32, R.id.btn33, R.id.btn34, R.id.btn35,
                R.id.btn36, R.id.btn37, R.id.btn38, R.id.btn39, R.id.btn40,
                R.id.btn41, R.id.btn42, R.id.btn43, R.id.btn44, R.id.btn45,
                R.id.btn46, R.id.btn47, R.id.btn48, R.id.btn49, R.id.btn50
        };

        // Recorreremos los botones, les asignaremos un listener y un tag
        //El tag nos servira como identificativo de su número.
        for (int i = 0; i < buttonIds.length; i++) {
            ImageButton button = findViewById(buttonIds[i]);

            // Asignar el número como tag (i+1 porque los botones empiezan desde 1)
            button.setTag(i + 1);

            button.setOnClickListener(view -> {
                // Iniciar la animación
                view.startAnimation(animScale);

                // Obtener el tag del botón pulsado y lo convertimos a un entero
                String tag = view.getTag().toString();
                int idPalabra = Integer.parseInt(tag);

                // Añadir un retraso para dar sensacion de pulsar un boton
                new Handler().postDelayed(() -> {
                    // Crear un Intent para ir a PalabraDelDia y le pasamos el ID de la palabra asociado
                    Intent intent = new Intent(ArchivoPalabras.this, PalabraDelDia.class);
                    intent.putExtra("idPalabra", idPalabra); // Pasamos el ID de la palabra
                    startActivity(intent);
                }, 500);
            });

            Animation animacionPulsar = AnimationUtils.loadAnimation(this, R.anim.animacion);
            ImageButton botonVolver = findViewById(R.id.btnVolver);

            // Acción para el botón de volver
            botonVolver.setOnClickListener(view -> {
                view.startAnimation(animacionPulsar);

                // Añadir un retraso para dar sensacion de pulsar un boton
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(ArchivoPalabras.this, MainActivity.class);
                    startActivity(intent);
                }, 750);
            });

            // Cargar GIF como portada (fondo de pantalla)
            ImageView portadaAnimada = findViewById(R.id.gifArchivo);
            Glide.with(this)
                    .load(R.drawable.archivo_palabras_fondo)
                    .into(portadaAnimada);
        }
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
