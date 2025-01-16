package com.example.worldle.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Esta clase extiende SQLiteOpenHelper y se encarga de gestionar la base de datos
 * que contiene las palabras del juego. Incluye la creación de la base de datos y la inserción
 * de las palabras.
 */
public class PalabrasBD extends SQLiteOpenHelper {

    // Nombre de la base de datos
    private static final String NOMBRE_BD = "palabras.db";
    // Versión de la base de datos
    private static final int VERSION_BD = 3;

    // Definición de los nombres de la tabla y las columnas
    public static final String TABLA_PALABRAS = "palabras";
    public static final String ID = "id";
    public static final String PALABRA = "palabra";

    public PalabrasBD(Context contexto) {
        super(contexto, NOMBRE_BD, null, VERSION_BD);
    }

    // Se llama a la base de datos cuando es creada por primera vez
    @Override
    public void onCreate(SQLiteDatabase bd) {
        // Creación de la tabla 'palabras' con un campo 'id' autoincrement y 'palabra' de tipo texto
        String CREATE_TABLE = "CREATE TABLE " + TABLA_PALABRAS + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PALABRA + " TEXT NOT NULL);";
        bd.execSQL(CREATE_TABLE);// Ejecuta la consulta sql

        // Llamada al método para insertar las palabras iniciales
        rellenarBaseDeDatos(bd);
    }

    // Método llamado cuando la base de datos necesita ser actualizada
    // Este metodo se llamara cuando cambiemos el valor de la version de la BD
    @Override
    public void onUpgrade(SQLiteDatabase bd, int oldVersion, int newVersion) {
        bd.execSQL("DROP TABLE IF EXISTS " + TABLA_PALABRAS);
        onCreate(bd);
    }

    // Método para insertar palabras predefinidas en la base de datos
    private void rellenarBaseDeDatos(SQLiteDatabase bd) {
        // Lista de palabras que se insertarán en la base de datos
        String[] palabras = {
                "mango", "silla", "donde", "cielo", "corre",
                "plaza", "piano", "raton", "papel", "clavo",
                "salta", "flota", "lucha", "lince", "lunar",
                "nubes", "lomos", "grito", "lirio", "guapo",
                "cebra", "banco", "verde", "fuego", "grano",
                "rojas", "luzco", "ancho", "arena", "bravo",
                "vuelo", "tarde", "dardo", "arbol", "hojas",
                "tigre", "pulga", "perro", "giras", "norte",
                "vapor", "plomo", "cubre", "monte", "ojala",
                "cerdo", "tapas", "tejas", "brisa", "niega"
        };
        // Inserta cada palabra en la base de datos
        for (String palabra : palabras) {
            ContentValues valores = new ContentValues();
            valores.put(PALABRA, palabra); // Coloca la palabra en el campo correspondiente
            bd.insert(TABLA_PALABRAS, null, valores);  // Inserta los valores en la tabla 'palabras'
        }
    }
}
