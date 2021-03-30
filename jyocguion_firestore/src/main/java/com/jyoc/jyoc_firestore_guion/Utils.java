package com.jyoc.jyoc_firestore_guion;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class Utils {


  /**
     * ***************************************************************************
     * ************            STRING               (JAVA)          **************
     * ***************************************************************************
     */
    /**
     * indiceDeEnesimaOcurrencia
     *
     * Devuelve la posision de una cadena en otra, en su enésima aparición
     *
     * @param origen          Cadena donde buscar
     * @param busqueda        Cadena que se busca
     * @param ocurrencia      numero de ocurrencia buscada
     * @return                la posision de la enésima aparición de la cadena buscada
     */
     public static int indiceDeEnesimaOcurrencia(String origen, String busqueda, int ocurrencia) {
        int pos = -1;
        do {
            pos = origen.indexOf(busqueda, pos + 1);
        } while (ocurrencia-- > 0 && pos != -1);
        return pos;
    }
 
 
  /**
     * ***************************************************************************
     * ************            DATE               (JAVA)            **************
     * ***************************************************************************
     */
    /**
     * hoy_en_DATE
     *
     * @return la fecha actual en formato DATE
     */
    public static Date hoy_en_DATE() {
        Calendar cc = Calendar.getInstance();
        Date hoyEnDate = cc.getTime();
        return hoyEnDate;
    }

    /**
     * hoy_en_String
     * 
     * Devuelve la fechas de hoy como un string segun el formato dado
     * 
     * @param formato  formato, como p.e. "dd/MM/yyyy"
     * @return fecha en string en dicho formato, por ejemplo, "12/22/2016"
     */
    public static String hoy_en_String (String formato) {
        Calendar cc = Calendar.getInstance();
        Date hoyEnDate = cc.getTime();
        SimpleDateFormat miFormato = new SimpleDateFormat(formato);
        String fechaEnString = miFormato.format(hoyEnDate);
        return fechaEnString;
    }
    
    /**
     * de_DATE_a_STRING
     * <p>
     * convertir de un Date de java.util a STRING, con un formato
     *
     * @param fechaEnDate Objeto Date de la fecha a cambiar
     * @param formato     formato, como p.e. "dd/MM/yyyy"
     *
     * @return fecha en string en dicho formato, por ejemplo, "12/22/2016"
     */
    public static String de_DATE_a_STRING(Date fechaEnDate, String formato) {
        SimpleDateFormat miFormato = new SimpleDateFormat(formato);
        String fechaEnString = miFormato.format(fechaEnDate);
        return fechaEnString;
    }

    
    /**
     * de_STRING_a_DATE
     * <p>
     * Convierte un String en un util.Date
     *
     * @param fechaEnString
     *
     * @return
     */
    public static Date de_STRING_a_DATE(String fechaEnString) {
        SimpleDateFormat miFormato2 = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaenjava = null;
        try {
            fechaenjava = miFormato2.parse(fechaEnString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fechaenjava;
    }
 
    
    /**
     * de_DATEUTIL_a_DATESQL
     * <p>
     * convertir de un Date de java.util a u Date de Sql
     *
     * @param fechaEnUtil Objeto Date de la fecha a cambiar, com oDate de UTIL
     *
     * @return fecha en Date, como Date de SQL
     */
    public static java.sql.Date de_DATEUTIL_a_DATESQL(Date fechaEnUtil) {
        java.sql.Date fechaEnSql = new java.sql.Date(fechaEnUtil.getTime());
        return fechaEnSql;
    }

                                                      
    /**
     * de_DATESQL_a_DATEUTIL
     * <p>
     * convertir de un Date de java.util a u Date de Sql
     *
     * @param fechaEnSql Objeto Date de la fecha a cambiar, com oDate de SQL
     *
     * @return fecha en UTIL , COMO Date de UTIL
     */
    public static Date de_DATESQL_a_DATEUTIL(java.sql.Date fechaEnSql) {
        // opcion 1, la mas correcta
        Date fechaEnUtil =  new Date(fechaEnSql.getTime());
        // opcion 2, la mas simple, con casting implícito, que NO HACE FALTA HACER TRANSFORMACION
        // java.util.Date fechaEnUtil =  fechaEnSql;
        return  fechaEnUtil;
    }

        /**
         * hoy_en_STRING_PARA_MYSQL
         * <p>
         * Conseguir el String del dia de hoy para una query de MySql 
         *
         * 
         * @return fecha de hoy com STRING para consulas MYSQL
         */
        public static String hoy_en_STRING_PARA_MYSQL() {
            Calendar cc = Calendar.getInstance();
            Date hoyEnDate = cc.getTime();
            SimpleDateFormat miFormato = new SimpleDateFormat("dd/MM/yyyy");
            String hoyEnString = miFormato.format(hoyEnDate);
            String hoyEnStringSql = "STR_TO_DATE('" + hoyEnString + "','%d/%m/%Y')";
            return hoyEnStringSql;
        }                                        
          
                                                      
                                                      
        /**
         * de_DATE_UTIL_a_STRING_PARA_MYSQL
         * <p>
         * Conseguir el String del dia que se pasa por parametro para una query de MySql 
         *
         * @return fecha de hoy com STRING para consulas MYSQL
         */                                                  
         	
	
    public static String de_DATEUTIL_a_STRING_PARA_MYSQL(Date fecha) {
        SimpleDateFormat miFormato = new SimpleDateFormat("dd/MM/yyyy");
        String hoyEnString = miFormato.format(fecha);
        String hoyEnStringSql = "STR_TO_DATE('" + hoyEnString + "','%d/%m/%Y')";
        return hoyEnStringSql;
    }                                              
                                                      
                                                 
                                                      
                                                      
     /**
     * diferenciaEnDiasEntreDates
     * <p>
     * devuelve el numero de dias existente entre dos date, puede se positivo o negativo dependiendo 
     * del orden de las fechas
     *
     * @param fechaMayor
     * @param fechaMenor
     * @return
     */

    public static int diferenciaEnDiasEntreDates(Date fechaMayor, Date fechaMenor) {
        long diferenciaEnMilisegs = fechaMayor.getTime() - fechaMenor.getTime();
        long dias = diferenciaEnMilisegs / (1000 * 60 * 60 * 24);
        return (int) dias;
    }

      /**
     * diferenciaEnAnosEntreDates
     * <p>
     * devuelve el numero de años existente entre dos date, puede se positivo o negativo dependiendo 
     * del orden de las fechas
     *
     * @param fechaMayor
     * @param fechaMenor
     * @return
     */
    public static int diferenciaEnAnosEntreDates(Date fechaMayor, Date fechaMenor) {
        long diferenciaEnMilisegs = fechaMayor.getTime() - fechaMenor.getTime();
        long dias = diferenciaEnMilisegs / (1000 * 60 * 60 * 24);
        return (int) (dias / 356) - 1;
    }
 
 
 
    /*
     * edadEnAnios
     * <p>
     * devuelve un int que representa el numero de años enteros desde una edad dada hasta hoy
     *
     * @param   anonacimiento  date con la fecha de nacimiento
     * @return  int con la edad en anios
     */
    public static int edadEnAnios(Date anonacimiento) {

        Date hoyEnDate = Calendar.getInstance().getTime();

        Calendar a= Calendar.getInstance();
        a.setTime(anonacimiento);
        Calendar b = Calendar.getInstance();
        b.setTime(hoyEnDate);

        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||  (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DAY_OF_MONTH) > b.get(Calendar.DAY_OF_MONTH))) {
            diff--;
        }
        return diff;
    }
 

    
    
     /**
     * milisegundosAStringHora
     * <p>
     * devuelve un Stroing que representa un numero de milisegundos, segun un formato establecido
     * del orden de las fechas
     *
     * @param milisegundos  milisegundos a calcular
     * @param formato   String que representa el formato de salida. Ejepmlos: "dd-MM-yy" ,  "mm:ss"
     * @return  String con la fecha formateada
     */
     public static String milisegundosAStringHora(long milisegundos, String formato) {
        Date date = new Date(milisegundos);
        DateFormat formatter = new SimpleDateFormat(formato);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }

 
 
    /**
     * ***************************************************************************
     * ************            FICHEROS (JAVA)                      **************
     * ***************************************************************************
     */

 
    /**
     * hermanosQueSonCarpetas
     * 
     * Devuelve un array de File, con los hermanos de la carpeta actual que tambien son carpetas 
     *
     * @param origen     Objeto File que representa el directorio actual
     * @return  []File    array de File, con los hermanos de la carpeta actual que tambien son carpetas
     */
    public File[] hermanosQueSonCarpetas(File origen){
        File padre = origen.getParentFile();
        File [] hermandad =  padre.listFiles();
        ArrayList<File> hermanosValidos = new ArrayList<>();
        for(File f : hermandad){
            if (f.isDirectory()){
                hermanosValidos.add(f);
            }
        }
        // si toArray() no tiene parametro, devuelve un Object[], dificil de convertir en un File[]
        return  hermanosValidos.toArray(new File[hermanosValidos.size()]);
    }


}
//
//
///**
// * Clase
// * Clase ejemplo de como hacer para que un metodo devuelva dos valores
// *
// * @param <R>
// * @param <S>
// */
// class DosValoresCualesquiera<R,S> {
//
//    private final R primero;
//    private final S segundo;
//
//    public DosValoresCualesquiera(R primero, S segundo) {
//        this.primero = primero;
//        this.segundo = segundo;
//    }
//
//    public R getPrimero() {
//        return primero;
//    }
//
//    public S getSegundo() {
//        return segundo;
//    }
//}
