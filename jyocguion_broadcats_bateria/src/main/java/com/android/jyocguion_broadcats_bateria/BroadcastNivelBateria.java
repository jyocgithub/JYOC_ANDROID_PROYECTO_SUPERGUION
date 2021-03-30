package com.android.jyocguion_broadcats_bateria;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BroadcastNivelBateria extends AppCompatActivity {
    MiBroadcastDeBateria broadcastDeBateria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_nivel_bateria);

        broadcastDeBateria = new MiBroadcastDeBateria();

        // registramos el broadcast, no por el manifiesto, sino por código
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        this.registerReceiver(broadcastDeBateria, filter);

    }


    @Override

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastDeBateria);
    }

}


class MiBroadcastDeBateria extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //if (intent.getAction().equals(broadcast_SMS_RECEIVED))
        //    Toast.makeText(context, "Se ha recibido un SMS", Toast.LENGTH_LONG).show();

        int nivel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

        String salud = intent.getStringExtra(BatteryManager.EXTRA_HEALTH);
        int estado = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);

        String info = "Nivel: " + nivel + "% \n " +
                " Salud " + salud + "\n " +
                " Estado " + evaluarEstado(estado);
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

    private String evaluarEstado(int estado) {
        String mensaje = "Desconocido";

        switch (estado) {
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                mensaje = "Bateria Descargando ";
                break;
            case BatteryManager.BATTERY_STATUS_CHARGING:
                mensaje = "Bateria Cargando ";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                mensaje = "Bateria Llena ";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                mensaje = "Bateria No Cargando ";
                break;
        }
        return mensaje;
    }

};
