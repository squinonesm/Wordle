package com.example.worldle.BD;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;


/**
 * Esta clase actúa como un repositorio de acceso a la base de datos.
 * Proporciona métodos para obtener una palabra aleatoria o una palabra específica
 * a traves de su id.
 */
public class PalabraRepository {

    private final PalabrasBD PBD;

    public PalabraRepository(Context context) {
        PBD = new PalabrasBD(context);
    }


    /**
     * Obtiene una palabra aleatoria desde la base de datos.
     *
     * @return La palabra aleatoria seleccionada.
     */
    public String obtenerPalabraAleatoria() {
        SQLiteDatabase db = PBD.getReadableDatabase();
        String query = "SELECT " + PalabrasBD.PALABRA + " FROM " + PalabrasBD.TABLA_PALABRAS +
                " ORDER BY RANDOM() LIMIT 1"; // Obtiene una palabra aleatoria
        Cursor cursor = db.rawQuery(query, null);

        String palabraAleatoria = null;
        if (cursor != null && cursor.moveToFirst()) {
            palabraAleatoria = cursor.getString(0); // Directamente obtener el valor de la primera columna
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return palabraAleatoria;
    }

    /**
     * Obtiene una palabra específica a partir de su id.
     *
     * @param id El identificador de la palabra en la base de datos.
     * @return La palabra asociada al ID pasado por parametro.
     */
    public String obtenerPalabraPorId(int id) {
        SQLiteDatabase db = PBD.getReadableDatabase();
        String query = "SELECT " + PalabrasBD.PALABRA +
                " FROM " + PalabrasBD.TABLA_PALABRAS +
                " WHERE " + PalabrasBD.ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        String palabraPorId = null;
        if (cursor != null && cursor.moveToFirst()) {
            palabraPorId = cursor.getString(0);
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return palabraPorId;
    }

}

