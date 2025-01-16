package com.example.worldle.Pantallas;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.worldle.BD.PalabraRepository;
import com.example.worldle.R;
import com.example.worldle.Servicios.ReproductorMusica;


/**
 * Actividad que gestiona la pantalla para adivinar la palabra del día en el juego.
 * Permite al usuario intentar adivinar la palabra secreta.
 */
public class PalabraDelDia extends AppCompatActivity {

    private String palabraSecreta; // Almacena la palabra secreta
    private EditText entradaUsuario; // Almacena las entradas del usuario
    private ImageButton botonPalabra; // Boton para enviar el intento
    private ImageButton botonVolver; // boton para volver a la pantalla principal
    private boolean ganador = false; // Indica si el usuario ha ganado o ha perdido

    private TextView[] listaLetras; // Un array de TextView (los huecos donde escribe el usuario)
    private int intentoActual = 0; //Numero de intentos realizados
    private PalabraRepository pr; // Clase usada para obtener las palabras de la base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.palabra_del_dia);

        //Inicializar la musica
        iniciarMusica("juego");

        // Inicialización de vistas
        entradaUsuario = findViewById(R.id.entradaUsuario);
        botonPalabra = findViewById(R.id.btnIntroducir);
        botonVolver = findViewById(R.id.btnVolver);

        //Inicializacion de animaciones
        Animation animacionPulsar = AnimationUtils.loadAnimation(this, R.anim.animacion);


        // Configuración del estilo de la entrada por pantalla
        entradaUsuario.setTextColor(Color.WHITE);  // Cambia el color del texto a blanco
        entradaUsuario.setTypeface(Typeface.DEFAULT_BOLD);  // Aplica el estilo de negrita

        // Acción para el botón de volver
        botonVolver.setOnClickListener(view -> {
            view.startAnimation(animacionPulsar);

            // Añadir un retraso para dar sensacion de pulsar un boton
            view.postDelayed(() -> {
                Intent intent = new Intent(PalabraDelDia.this, MainActivity.class);
                startActivity(intent);
            }, 350);
        });

        // Cargar el gif de "jugando"
        ImageView enPartidaAnimado = findViewById(R.id.gifArchivo);
        Glide.with(this)
                .load(R.drawable.en_partida)
                .into(enPartidaAnimado);

        // Inicialización de los huecos para las palabras
        listaLetras = new TextView[]{
                findViewById(R.id.l1), findViewById(R.id.l2), findViewById(R.id.l3), findViewById(R.id.l4), findViewById(R.id.l5), //Fila 1
                findViewById(R.id.l6), findViewById(R.id.l7), findViewById(R.id.l8), findViewById(R.id.l9), findViewById(R.id.l10), //Fila 2
                findViewById(R.id.l11), findViewById(R.id.l12), findViewById(R.id.l13), findViewById(R.id.l14), findViewById(R.id.l15), //Fila 3
                findViewById(R.id.l16), findViewById(R.id.l17), findViewById(R.id.l18), findViewById(R.id.l19), findViewById(R.id.l20), //Fila 4
                findViewById(R.id.l21), findViewById(R.id.l22), findViewById(R.id.l23), findViewById(R.id.l24), findViewById(R.id.l25) //Fila 5
        };

        pr = new PalabraRepository(this);

        // Verificar si el Intent contiene un ID
        if (getIntent().hasExtra("idPalabra")) {
            int idPalabra = getIntent().getIntExtra("idPalabra", -1);
            if (idPalabra != -1) {
                palabraSecreta = pr.obtenerPalabraPorId(idPalabra).toUpperCase();
            }
        } else {
            palabraSecreta = pr.obtenerPalabraAleatoria().toUpperCase();
        }

        // Verificación en caso de que no se obtenga una palabra válida
        if (palabraSecreta == null || palabraSecreta.isEmpty()) {
            palabraSecreta = "ERROR"; // Control de errores
        }

        //palabraSecreta = "FLOTA";


        // Acción para el botón de introducir la palabra
        botonPalabra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animacionPulsar);
                // Obtener el intento del usuario
                String intento = entradaUsuario.getText().toString().toUpperCase();

                if (intento.length() == 5) {
                    comprobarPalabra(intento);
                }
            }
        });

    }

    /**
     * Método encargado de comprobar si la palabra introducida por el usuario es correcta comparándola con la palabra secreta.
     * Se realizan las siguientes comprobaciones:
     * - Letras correctas (verdes): si la letra de la palabra introducida está en la misma posición que la de la palabra secreta.
     * - Letras incorrectas pero que estan en la palabra secreta (amarillas): si la letra está en la palabra secreta, pero en una posición diferente.
     *   (se debe tener en cuenta la exepcion de que esa letra ya este en verde, o la cantidad de veces que se repite esa letra)
     * - Letras incorrectas (rojas): si la letra no está en la palabra secreta.
     *
     * Además, se gestiona la visualización de los intentos y se actualiza la interfaz con el color adecuado para cada tipo de letra.
     * Si el intento es correcto, se desactiva el botón para inbtroducir palabras, de esta forma se evitan más intentos.
     *
     * @param intento El intento del usuario (cadena de 5 letras) a comparar con la palabra secreta.
     */
    private void comprobarPalabra(String intento) {
        // Inicialización de arrays para las letras restantes, acertadas, no acertadas y amarillas
        String[] letrasRestantes = new String[5];
        String[] letrasAcertadas = new String[5];
        String[] letrasNoAcertadas = new String[5];
        int aciertos = 0;

        // Dividimos la palabra del intento y la palabra secreta en letras
        String[] letrasIntento = new String[5];
        for (int i = 0; i < 5; i++) {
            letrasIntento[i] = String.valueOf(intento.charAt(i));
        }

        String[] letrasPalabraSecreta = new String[5];
        for (int i = 0; i < 5; i++) {
            letrasPalabraSecreta[i] = String.valueOf(palabraSecreta.charAt(i));
        }

        // Paso 1: Comprobamos las letras correctas (verdes)
        for (int i = 0; i < 5; i++) {
            char letraIntento = letrasIntento[i].charAt(0);
            // Si la letra está en la misma posición en la palabra secreta
            if (letraIntento == letrasPalabraSecreta[i].charAt(0)) {
                letrasAcertadas[aciertos] = letrasPalabraSecreta[i];// Almacenamos en letras acertadas
                // Actualizamos el campo de la letra
                listaLetras[i + intentoActual * 5].setText(String.valueOf(letraIntento));
                listaLetras[i + intentoActual * 5].setBackgroundColor(Color.parseColor("#6AFF46"));
                aciertos++; // Incrementamos los aciertos
                letrasRestantes[i] = null; // No procesamos esta letra en la siguiente fase
                letrasNoAcertadas[i] = null; // Eliminamos la letra de las no acertadas
            } else {
                // En caso de que la letra no sea verde, esta se almacena para la siguiente comprobacion
                letrasRestantes[i] = letrasIntento[i];
                letrasNoAcertadas[i] = letrasPalabraSecreta[i];
            }
        }

        // Paso 2: Comprobamos las letras amarillas (en la palabra pero en una posición diferente)
        for (int i = 0; i < 5; i++) {
            if (letrasRestantes[i] != null) { // Solo procesamos letras no acertadas
                for (int j = 0; j < 5; j++) {
                    // Comprobamos si la letra del intento está en la palabra secreta (y no está en la misma posición)
                    // Además, aseguramos que no hemos marcado la letra como "verde" antes
                    if (letrasRestantes[i].equals(letrasNoAcertadas[j])) {
                        //Eliminamos de la lista la que hemos puesto en amarilla, evitamos duplicadas
                        letrasNoAcertadas[j] = null;
                        // Actualizamos el campo de la letra
                        listaLetras[i + intentoActual * 5].setText(letrasRestantes[i]);
                        listaLetras[i + intentoActual * 5].setBackgroundColor(Color.parseColor("#FFF586"));
                        letrasRestantes[i] = null; // Se quita la letra de las letras restantes
                        break; // Terminamos la búsqueda de la letra amarilla para evitar duplicados
                    }
                }
            }
        }

        // Paso 3: Comprobamos las letras incorrectas (rojas)
        for (int i = 0; i < 5; i++) {
            if (letrasRestantes[i] != null) { // Si la letra no ha sido procesada (ni verde ni amarilla)
                // Actualizamos el campo de la letra
                listaLetras[i + intentoActual * 5].setText(letrasRestantes[i]);
                listaLetras[i + intentoActual * 5].setBackgroundColor(Color.parseColor("#E53830"));
            }
        }

        // Comprobamos si el intento es correcto, es decir, si el usuario ha adivinado la palabra
        if (intento.equals(palabraSecreta)) {
            ganador = true;
        }

        // Aumentamos el contador de intentos y limpiamos el campo de texto
        intentoActual++;
        entradaUsuario.setText("");

        // Verificamos si el juego ha terminado (si el usuario adivino la palabra o se ha quedado sin intentos)
        verificarFinDelJuego();
    }

    /**
     * Método encargado de verificar el estado del juego al final de un intento.
     * Dependiendo del resultado del juego, muestra un mensaje de "ganador" o "perdedor"
     * y se redirige a la pantalla final con el estado correspondiente(ganando o perdiendo).
     *
     * Si el jugador ha adivinado la palabra secreta, se muestra un mensaje de éxito, se
     * desactiva el botón de palabra y se redirige a la pantalla final indicando que el jugador ganó.
     *
     * Si el jugador ha gastado los 5 intentos y no ha adivinado la palabra, se muestra un mensaje de derrota
     * y se redirige a la pantalla final indicando que el jugador perdió.
     *
     * Se utiliza un retraso de 5 segundos antes de cambiar de actividad para que el jugador pueda
     * ver el mensaje correspondiente antes de que se cambie de actividad.
     */
    private void verificarFinDelJuego() {

        if (ganador) {
            // Para evitar que el usuario pueda introducir nuevas palabras o retroceder antes de la pantalla final
            botonPalabra.setEnabled(false);
            botonVolver.setEnabled(false);
            // Si el jugador ha ganado, se muestra un mensaje de victoria
            Toast.makeText(this, "¡Correcto! Has adivinado la palabra.", Toast.LENGTH_SHORT).show();
            botonPalabra.setEnabled(false);

            // Retrasar el cambio de actividad 5 segundos
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Redirige a la actividad PantallaFinal con el estado de ganador
                    Intent intent = new Intent(PalabraDelDia.this, PantallaFinal.class);
                    intent.putExtra("ganador", true); // Pasa que el jugador ha ganado
                    startActivity(intent);
                }
            }, 5000); // 5 segundos de retraso

        } else if (intentoActual == 5) {
            // Para evitar que el usuario pueda introducir nuevas palabras o retroceder antes de la pantalla final
            botonPalabra.setEnabled(false);
            botonVolver.setEnabled(false);
            // Si el jugador ha agotado los intentos y no ha acertado la palabra, se muestra un mensaje de derrota
            Toast.makeText(this, "¡Una pena! La palabra era: " + palabraSecreta, Toast.LENGTH_SHORT).show();

            // Retrasar el cambio de actividad 5 segundos
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Redirige a la actividad PantallaFinal con el estado de perdedor
                    Intent intent = new Intent(PalabraDelDia.this, PantallaFinal.class);
                    intent.putExtra("ganador", false); // Pasa que el jugador ha perdido
                    startActivity(intent);
                }
            }, 5000); // 5 segundos de retraso
        }
    }

    /**
     * Inicia la música según el tipo de música seleccionado.
     *
     * @param tipoMusica El tipo de música a reproducir (predeterminada).
     */
    private void iniciarMusica(String tipoMusica) {
        Intent musica = new Intent(this, ReproductorMusica.class);
        musica.putExtra("musica", tipoMusica); // Envía el tipo de música (juego)
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

