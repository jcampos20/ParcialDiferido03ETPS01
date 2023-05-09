package com.example.parcialdiferido03etps01.HelperBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BaseHelper  extends SQLiteOpenHelper {

    public BaseHelper(@Nullable Context context, @Nullable String NombreBase, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, NombreBase, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla MD_Clientes
        db.execSQL("CREATE TABLE MD_Clientes (ID_cliente INTEGER PRIMARY KEY, " +
                "sNombreCliente TEXT, sApellidoCliente TEXT, sDireccionCliente TEXT, sCiudadCliente TEXT)");

        // Crear la tabla MD_Vehiculos
        db.execSQL("CREATE TABLE MD_Vehiculos (ID_Vehiculo INTEGER PRIMARY KEY, " +
                "sMarca TEXT, sModelo TEXT)");

        // Crear la tabla MD_ClienteVehiculo
        db.execSQL("CREATE TABLE MD_ClienteVehiculo (ID_VehiculoCliente INTEGER PRIMARY KEY,ID_cliente INTEGER, " +
                "ID_Vehiculo INTEGER, sMatricula TEXT, sKilometros INTEGER, " +
                "FOREIGN KEY (ID_cliente) REFERENCES MD_Clientes(ID_cliente), " +
                "FOREIGN KEY (ID_Vehiculo) REFERENCES MD_Vehiculos(ID_Vehiculo))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
