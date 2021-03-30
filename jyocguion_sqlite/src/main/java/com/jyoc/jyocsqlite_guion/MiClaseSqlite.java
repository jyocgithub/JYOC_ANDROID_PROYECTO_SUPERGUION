package com.jyoc.jyocsqlite_guion;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * La forma normal de trabajar con BBDD sera creando clases que deriven de SQLiteOpenHelper.
 * SQLiteOpenHelper tiene sólo un constructor, que no es necesario sobrescribir,
 * y dos métodos abstractos a sobrescribir
 * - onCreate() para crear nuestra base de datos
 * - onUpgrade() para actualizar una bbdd existete con una nueva version
 */
public class MiClaseSqlite extends SQLiteOpenHelper {

    // ***********************************************************  ATRIBUTOS
    // **************************************************************************************
    // Objeto de la bbdd que se usa en todos los metodos de esta clase
    SQLiteDatabase objetoBBDD;

    // ***********************************************************  CONSTRUCTOR
    // **************************************************************************************
    // Constructor, que no se toca
    public MiClaseSqlite(Context contexto, String nombre, SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);

    }

    // ***********************************************************  METODOS A SOBRESCRIBIR
    // **************************************************************************************

    /**
     * onCreate()
     * Se ejecuta automáticamente cuando sea necesaria la creación de la base de datos por que ésta no exista.
     * Actividades típicas que se hacen aqui: crear todas las tablas e indices y
     * la inserción de los datos de carga iniciales
     * Para la gestion de la tabla usamos por ahora el metodo execSQL() de la API de SQLite
     * Este método simplemente ejecuta el código SQL pasado como parámetro.
     *
     * @param db Como parámetro el metodo recibe el nombre de la bbdd que esta procesandose
     *           y sobre la cual podemos llamar al metodo execSql();
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Sentencia SQL para crear la tabla de Jugadores
        String sql = "CREATE TABLE JugadoresDB (idJugador INTEGER, nombre TEXT, puntos INTEGER)";
        //Ejecutar la sentencia SQL de creación de la tabla
        db.execSQL(sql);
    }

    /**
     * OnUpdate()  se ejecuta cuando se adviete que existe una nueva version de la base de datos
     * Las BBDD en SQLite se almacenan con un nuemero de version.
     * Cuando una bbdd cambia (crece en campos, en tablas, ect) y necesita una nueva version
     * se ejecuta este metodo, que normalmente realiza tres pasos:
     * - copia la base de datos antigua,
     * - procesa la migracion de la estructura del modelo antiguo al nuevo
     * - actualiza los datos con el nuevo formato y deja este como el activo
     * onUpgrade() cuando se intente abrir una versión  de la base de datos que aún no exista,
     * ejecuta el código interno.
     * Para ello, como parámetros recibe :
     *
     * @param db              nombre de la bbdd
     * @param versionAnterior la versión actual de la base de datos en el sistema,
     * @param versionNueva    la nueva versión a la que se quiere convertir.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {

        String sql = "CREATE TABLE JugadoresDB2 (idJugador INTEGER, nombre TEXT, puntos INTEGER, partidasganadas INTEGER)";
        db.execSQL(sql);
        sql = "INSERT INTO JugadoresDB2 (idJugador , nombre , puntos )  SELECT idJugador , nombre , puntos  FROM JugadoresDB";
        db.execSQL(sql);
        sql = "DROP TABLE JugadoresDB";
        db.execSQL(sql);
        sql = "ALTER TABLE JugadoresDB2 RENAME TO JugadoresDB";
        db.execSQL(sql);

    }

    // ***********************************************************  METODOS DE CONTROL DE BBDD
    // **************************************************************************************

    /**
     * conectar()
     * Obtiene la conexion de la base de datos y la abre
     */
    public void conectar() {
        objetoBBDD = getWritableDatabase();
    }

    /**
     * desconectar()
     * Cierra el objeto de la base de datos
     */
    public void desconectar() {
        objetoBBDD.close();
    }


    // ***********************************************************  METODOS DE ACCESO A DATOS
    // **************************************************************************************

    /**
     * guardarJugador
     *
     * @param j el juegador que se almacena en la bbdd
     */
    public void guardarJugador(Jugador j) {
        conectar();
        // version SIN parametros
        String sql = "INSERT INTO JugadoresDB (idJugador , nombre , puntos ) VALUES (" + j.getIdJugador() + ",'" + j.getNombre() + "'," + j.getPuntos() + ")";
        objetoBBDD.execSQL(sql);

        // version CON parametros
        String sql2 = "INSERT INTO JugadoresDB (idJugador , nombre , puntos ) VALUES (?,?,?)";

        String[] parametrosdelsql = new String[3];
        parametrosdelsql[0] = j.getIdJugador() + "";
        parametrosdelsql[1] = "'" + j.getNombre() + "'";
        parametrosdelsql[2] = j.getPuntos() + "";

        objetoBBDD.execSQL(sql2, parametrosdelsql);
        desconectar();
    }

    /**
     * borrarJugador
     *
     * @param idjugadoraborrar el jugador que se ha de borrar, se busca por el idJugador para encontrarlo
     */
    public void borrarJugador(int idjugadoraborrar) {
        conectar();

        // version SIN parametros
        String sql = "DELETE FROM JugadoresDB WHERE idJugador=" + idjugadoraborrar;
        objetoBBDD.execSQL(sql);

        // version CON parametros
        String sql2 = "DELETE FROM JugadoresDB WHERE idJugador=?";

        String[] parametrosdelsql = new String[1];
        parametrosdelsql[0] = idjugadoraborrar + "";
        objetoBBDD.execSQL(sql2, parametrosdelsql);
        desconectar();

    }


    /**
     * actualizarJugador
     * Se modifican todos los campos menos el idJugador
     *
     * @param j el jugador que se ha de mmodificar, se busca por el idJugador para encontrarlo
     */
    public void actualizarJugador(Jugador j) {
        conectar();
        // version CON parametros (no se incluye la version sin parametros, se puede hacer ....)
        String sql2 = "UPDATE JugadoresDB SET ( nombre=? , puntos=? ) WHERE idJugador = " + j.getIdJugador();
        String[] parametrosdelsql = new String[2];
        parametrosdelsql[0] = "'" + j.getNombre() + "'";
        parametrosdelsql[1] = j.getPuntos() + "";
        objetoBBDD.execSQL(sql2, parametrosdelsql);
        desconectar();
    }


    /**
     * leerTodosLosJugadores
     * Lee jugadores de la bbdd
     *
     * @return un ArrayList<Jugador> con todos los jugadores leidos de la bbdd
     */
    public ArrayList<Jugador> leerTodosLosJugadores() {
        conectar();

        // creo un arraylist para rellenarlo
        ArrayList<Jugador> listajugadores = new ArrayList<>();

        // Ejemplo con cursor sin parametros;
        String sql = "SELECT idJugador, nombre, puntos FROM JugadoresDB";
        Cursor c = objetoBBDD.rawQuery(sql, null);

        // Se mueve al primer regiustro y se entra en el if si hay al menos un registro en el cursor
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no hay más registros
            // Cada campo se identifica por su ordinal, y se debe recuperar con el metodo adecuado
            // a su tipo (getString(), getInt(), etc)
            do {
                int idJugador = c.getInt(0);   // accedo al campo por su posicion en la select
                String nombre = c.getString(c.getColumnIndex("nombre"));  // accedo al campo por su nombre
                int puntos = c.getInt(2);
                // hacer con los datos leidos lo que haga falta
                Jugador nuevojugador = new Jugador(idJugador, nombre, puntos);
                listajugadores.add(nuevojugador);
            } while (c.moveToNext());
        }

        // *****************************************************************************
        // ---- OTROS METODOS DE LA CLASE CURSOR (AUNQUE QUE NO SE USAN EN ESTE EJEMPLO)
        // *****************************************************************************
        int x = c.getCount();           // total de registros del cursor
        String n = c.getColumnName(1);  // devuelve el nombre de la columna con índice 1
        int k = c.getColumnIndex("apellido");  // devuelve el indice del orden de la de la columna con nombre "apellido"
        c.moveToPosition(1);            //  mueve el puntero del cursor al registro con índice 1

        desconectar();
        return listajugadores;
    }


    /**
     * leerJugadoresPorPuntos
     * Lee jugadores de la bbdd si tienen una puntuacion minima
     *
     * @param puntosminimos los puntos minimos que han de cumplir la condicion de busqueda
     * @return un ArrayList<Jugador> con todos los jugadores leidos de la bbdd
     */
    public ArrayList<Jugador> leerJugadoresPorPuntos(int puntosminimos) {
        conectar();

        // creo un arraylist para rellenarlo
        ArrayList<Jugador> listajugadores = new ArrayList<>();

        // Ejemplo con cursor sin parametros;
        String sql = "SELECT idJugador, nombre, puntos FROM JugadoresDB where puntos > " + puntosminimos;
        Cursor c = objetoBBDD.rawQuery(sql, null);

        // Se mueve al primer regiustro y se entra en el if si hay al menos un registro en el cursor
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no hay más registros
            // Cada campo se identifica por su ordinal, y se debe recuperar con el metodo adecuado
            // a su tipo (getString(), getInt(), etc)
            do {
                int idJugador = c.getInt(0);   // accedo al campo por su posicion en la select
                String nombre = c.getString(c.getColumnIndex("nombre"));  // accedo al campo por su nombre
                int puntos = c.getInt(2);
                // hacer con los datos leidos lo que haga falta
                Jugador nuevojugador = new Jugador(idJugador, nombre, puntos);
                listajugadores.add(nuevojugador);

            } while (c.moveToNext());
        }
        desconectar();
        return listajugadores;
    }

    /**
     * leerJugadoresSegundaMitadPorNombre
     * Lee jugadores de la bbdd, solo los de la segunda mitad de los existentes
     * considerando para calcular la mitad un orden alfabetico por nombre
     *
     * @return un ArrayList<Jugador> con todos los jugadores leidos de la bbdd
     */
    public ArrayList<Jugador> leerJugadoresSegundaMitadPorNombre() {
        conectar();

        // creo un arraylist para rellenarlo
        ArrayList<Jugador> listajugadores = new ArrayList<>();

        // Ejemplo con cursor sin parametros
        String sql = "SELECT idJugador, nombre, puntos FROM JugadoresDB ORDER BY nombre";
        Cursor c = objetoBBDD.rawQuery(sql, null);
        int totalregistros = c.getCount();           // total de registros del cursor

        //mievo el puntero del cursor a la mitar del tamaño del cursor
        c.moveToPosition(totalregistros / 2);

        // bucle que da tantas vueltas como la mitad de los registros leidos
        for (int i = 0; i < totalregistros / 2; i++) {  // el int i no se usa internamente, solo vale para dar tantas vueltas como la mitad del cursor
            c.moveToNext();

            int idJugador = c.getInt(0);   // accedo al campo por su posicion en la select
            String nombre = c.getString(c.getColumnIndex("nombre"));  // accedo al campo por su nombre
            int puntos = c.getInt(2);
            // hacer con los datos leidos lo que haga falta
            Jugador nuevojugador = new Jugador(idJugador, nombre, puntos);
            listajugadores.add(nuevojugador);
        }

        desconectar();
        return listajugadores;
    }

    /**
     * leerUnJugadoresPorSuNombre
     * Devuelve un jugador, sabiendo su nombre
     *
     * @param nombreabuscar el nombre del jugador a buscar
     * @return un objeto jugador con el jugador encontrado, o null si no existe
     */
    public Jugador leerUnJugadoresPorSuNombre(String nombreabuscar) {
        conectar();

        // Ejemplo sin parametros
        String sql = "SELECT idJugador, nombre, puntos FROM JugadoresDB WHERE nombre = '" + nombreabuscar + "'";
        Cursor c = objetoBBDD.rawQuery(sql, null);


        // en el caso de que la select devuelva mas de 1 registro, solo interesa el primer registro,
        if (c.moveToFirst()) {
            int idJugador = c.getInt(0);   // accedo al campo por su posicion en la select
            String nombre = c.getString(c.getColumnIndex("nombre"));  // accedo al campo por su nombre
            int puntos = c.getInt(2);

            Jugador nuevojugador = new Jugador(idJugador, nombre, puntos);
            desconectar();
            return nuevojugador;
        }

        // Si no se ha encontrado ningun registro, devuelve null
        desconectar();
        return null;
    }

    /**
     * copiaDeSeguridadBBDDActualTodosLosRegistros
     * Cuarda una copia de todos los registros de la tabla Jugador
     */
    public void copiaDeSeguridadBBDDActualTodosLosRegistros() {
        conectar();

        //Sentencia SQL para crear la tabla de Jugadores copia
        String sql = "CREATE TABLE JugadoresDBCopia (idJugador INTEGER, nombre TEXT, puntos INTEGER)";
        //Ejecutar la sentencia SQL de creación de la tabla
        objetoBBDD.execSQL(sql);

        // copiamos los regsitros de una en otra
        String sql2 = "INSERT INTO JugadoresDBCopia (idJugador , nombre , puntos )  SELECT idJugador , nombre , puntos  FROM JugadoresDB";
        objetoBBDD.execSQL(sql2);
        desconectar();
    }

    /**
     * borrarRegistroYGuardarloAntesEnHistorico
     * Borra un jugador dado su idJugador, pero antes, lo añade a una tabla de historicos
     *
     * @param idjugadoraborrar id del jugador a borrar
     */
    public void borrarRegistroYGuardarloAntesEnHistorico(int idjugadoraborrar) {
        conectar();

        // SE SUPONE QUE LA TABLA DE HISTORICOS YA EXISTE, se deberia haber creado en el onCreate de esta clase

        // copiamos los regsitros de una en otra
        String sql2 = "INSERT INTO JugadoresDBHistorico (idJugador , nombre , puntos )  SELECT idJugador , nombre , puntos " +
                " FROM JugadoresDB  WHERE idJugador=" + idjugadoraborrar;
        objetoBBDD.execSQL(sql2);

        // version SIN parametros
        String sql = "DELETE FROM JugadoresDB WHERE idJugador=" + idjugadoraborrar;
        objetoBBDD.execSQL(sql);

        desconectar();
    }

    // ***********************************************************  OTROS METODOS NATIVOS DE SQLITE
    // ********************************************************************************************

    /**
     * otrosMetodosDeSqlite
     * Explora otros métodos de SQLIte: insert(), delete(), update() y query()
     */
    public void otrosMetodosDeSqlite() {


    }


}
