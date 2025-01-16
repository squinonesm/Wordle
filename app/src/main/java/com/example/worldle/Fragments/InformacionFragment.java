package com.example.worldle.Fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.worldle.R;


/**
 * Clase que representa un fragmento de información en forma de diálogo.
 * Este fragmento se muestrora cuando el usuario pulse el botn de información.
 * En este fragmento se mostrar un GIF de fondo y un texto que describe la funcionalidad del juego.
 *
 * Esta clase extiende "Dialog Fragment", lo cual nos permite mostrar un diálogo modal en la pantalla.
 * El diseño del diálogo es personalizado, con un fondo transparente y una animación cuando se presiona
 * el botón de cierre.
 */
public class InformacionFragment extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Si lo haces sin esto, sale con un recuadro blanco alrededor y de esta forma
        //podemos quitarselo y que solo aparezca el fondo animado y queda mejor visualmente.
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Hacer que el fondo del diálogo sea transparente
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }


        // Inflar el layout del dialogo
        View view = inflater.inflate(R.layout.informacion_fragment, container, false);

        //Obtenemos el imageView para añadirle el gif mediante la libreria Glide
        ImageView gifFondo = view.findViewById(R.id.gifInfo);
        Glide.with(this)
                .load(R.drawable.info_game)
                .into(gifFondo);

        // Configuramos el texto de información sobre las reglas del juego
        TextView infoText = view.findViewById(R.id.textoInformativo);
        String info = "Este es un juego del estilo 'Wordle'. El objetivo es adivinar una palabra de 5 letras. " +
                "En cada intento, las letras cambiaran de color segun como coincidan con la palabra secreta, la cual se escogera al azar:\n" +
                "- Verde: Letra correcta en la posición correcta.\n" +
                "- Amarillo: Letra correcta en la posición incorrecta.\n" +
                "- Rojo: Letra incorrecta. \n" +
                "Tambien podras acceder al archivo, donde jugaras con palabras ya predefinidas(1 en cada numero).";

        // Establecemos el color y estilo del texto informativo
        //Utilizamos un color llamativo para que se vea bien en el fondo animado
        infoText.setTextColor(Color.WHITE);  // Cambia el color del texto a blanco
        infoText.setTypeface(Typeface.DEFAULT_BOLD);  // Aplica el estilo de negrita

        // Asignamos el texto al TextView
        infoText.setText(info);

        // Configurar la animación al pulsar
        Animation animacionPulsar = AnimationUtils.loadAnimation(requireContext(), R.anim.animacion);

        // Configuramos el boton de cierre del fragment dialog
        ImageButton btnCerrar = view.findViewById(R.id.btnCerrar);
        btnCerrar.setOnClickListener(v -> {
            // Iniciar la animación
            v.startAnimation(animacionPulsar);

            // Usar postDelayed para cerrar el fragmento con retraso
            new Handler().postDelayed(() -> {
                dismiss();  // Cerrar el fragmento después del retraso
            }, 500);  // Retraso de 500 milisegundos (0.5 segundos)
        });


        return view;
    }


}
