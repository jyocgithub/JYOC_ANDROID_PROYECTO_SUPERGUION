package com.jyoc.jyoc_firestore_guion;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;

public class JyocFS_DAO<T> {
    FirebaseFirestore mFirestoreDB;
    IAfterJyocFS_DAO mActivity;
    private String mFirebaseCollection;
    // Necesitamos el .class de la clase que se almacena en la bbdd
    // No es facil obtenerlo de un generico, asi que mejor se solicita al construir el DAO
    private Class<T> mObjectClassOfStoredElement;

    public JyocFS_DAO(IAfterJyocFS_DAO activity, String firebaseCollection, Class<T> objectClassOfStoredElement) {
        this.mActivity = activity;
        this.mFirebaseCollection = firebaseCollection;
        this.mObjectClassOfStoredElement = objectClassOfStoredElement;
    }

    public void readAllElements(int numRecordsSelected) {
        if (mFirestoreDB == null)
            mFirestoreDB = FirebaseFirestore.getInstance();

        Query query1 = mFirestoreDB.collection(mFirebaseCollection)   // consultar 100 primeros registros
                .limit(numRecordsSelected);
        //Query mQuery2 = mFirestoreDB.collection(COLECCION)  // consultar 100 primeros registros ordenados por id
        //        .orderBy("id")
        //        .limit(100);
        //Query mQuery3 = mFirestoreDB.collection(COLECCION)  // consultar 100 primeros registros ordenados por id DESCENDENTE
        //        .orderBy("id", Query.Direction.DESCENDING)
        //        .limit(100);
        //Query mQuery4 = mFirestoreDB.collection(mColeccionFirebase) // consultar registros con id 3
        //        .whereEqualTo("id", "3");

        query1
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<T> list = new ArrayList<T>();
                            Log.d("JyocFS_DAO ***>", "leyendo " + task.getResult().size() + " elementos");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //for (DocumentSnapshot document : task.getResult()) {    //  tambien vale
                                T element = document.toObject(mObjectClassOfStoredElement);
                                list.add(element);
                                Log.d("JyocFS_DAO ***>", "leido correctamente : " + element.toString());
                            }
                            mActivity.afterReadAllElements(list);
                        } else {
                            Log.d("JyocFS_DAO ***>", "Error en query : ", task.getException());
                        }
                    }
                });
    }

    public void readElementByEquality(final String field, final String value) {
        if (mFirestoreDB == null)
            mFirestoreDB = FirebaseFirestore.getInstance();

        Query query1 = mFirestoreDB.collection(mFirebaseCollection)
                .whereEqualTo(field, value);

        query1.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {   // debe devolver solo un elemento si la condicion es correcta, o null 
                            T element = null;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //for (DocumentSnapshot document : task.getResult()) {    //  tambien vale
                                element = document.toObject(mObjectClassOfStoredElement);
                                Log.d("JyocFS_DAO ***>", "leido correctamente :" + element.toString());
                            }
                            if (element == null) {
                                Log.d("JyocFS_DAO ***>", "Cuidado-- : registro no encontrado : "+ value);
                            }
                            mActivity.afterReadELementByEquality(element);
                        } else {
                            Log.d("JyocFS_DAO ***>", "Error en query : ", task.getException());
                        }
                    }
                });
    }

    public void addElement(T element, String id) {
        if (mFirestoreDB == null)
            mFirestoreDB = FirebaseFirestore.getInstance();

        CollectionReference collection = mFirestoreDB.collection(mFirebaseCollection);

        if (id == null) {   // crea el documento con un id generado por Firestore
            collection.add(element)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("JyocFS_DAO ***>", "Insercion correcta");
                            mActivity.afterAddElement(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("JyocFS_DAO ***>", "Error en query : " + e.getMessage());
                            mActivity.afterAddElement(false);
                        }
                    });
        } else {                 // crea el documento un id propio y con control de error

            collection.document(id).set(element)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("JyocFS_DAO ***>", "Insercion correcta");
                            mActivity.afterAddElement(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("JyocFS_DAO ***>", "Error en query : " + e.getMessage());
                            mActivity.afterAddElement(false);
                        }
                    });
        }
    }

    public void deleteElement(final String id) {
        if (mFirestoreDB == null)
            mFirestoreDB = FirebaseFirestore.getInstance();

        // si sabemos SEGURO la reference del documento ....
        mFirestoreDB.collection(mFirebaseCollection)
                .document(id)
                .delete();

        // si no sabemos la reference del documento ....
        Query query1 = mFirestoreDB.collection(mFirebaseCollection)
                .whereEqualTo("id", id);
        query1.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();
                            }
                            Log.d("JyocFS_DAO ***>", "borrado correcto de registro " + id);
                            mActivity.afterDeleteElements(task.getResult().size());
                        } else {
                            Log.d("JyocFS_DAO ***>", "Error en borrado");
                            mActivity.afterDeleteElements(-1);
                        }
                    }
                });
    }

    public void deleteAllElements() {
        Query query1 = mFirestoreDB.collection(mFirebaseCollection);// consultar registros con id 3

        query1
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();
                            }
                            Log.d("JyocFS_DAO ***>", "borrado correcto de " + task.getResult().size() + "registros ");
                            mActivity.afterDeleteElements(task.getResult().size());
                        } else {
                            Log.d("JyocFS_DAO ***>", "Error en borrado");
                            mActivity.afterDeleteElements(-1);
                        }
                    }
                });
    }

    /**
     * generateRandomKey
     * Crea una cadena de texto aleatoria con letras mayuscuasl minusculas y numeros, de un tamaño definido
     *
     * @param size tamaño de la cadena a crear
     * @return una cadena aleatoria
     */
    public static String generateRandomKey(int size) {
        String pattern = "ABCDEFGHIJKLMNOPQRSTUVWXUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        Random random = new Random();
        String result = "";
        for (int i = 0; i < size; i++) {
            result += pattern.charAt(random.nextInt(pattern.length()));
        }
        return result;
    }
}
