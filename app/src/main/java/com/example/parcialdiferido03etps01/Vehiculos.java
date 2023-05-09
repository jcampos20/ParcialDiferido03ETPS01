package com.example.parcialdiferido03etps01;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parcialdiferido03etps01.HelperBase.BaseHelper;

public class Vehiculos extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private EditText edMarca;
    private EditText edModelo;
    private Integer txtId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculos);
        SQLiteOpenHelper dbHelper = new BaseHelper(getApplicationContext(), "ClientesDB", null, 2);
        mDatabase = dbHelper.getWritableDatabase();
        edMarca = findViewById(R.id.edit_text_marca);
        edModelo = findViewById(R.id.edit_text_modelo);
        txtId=-1;
        Button agregarBtn = findViewById(R.id.button_guardar);
        agregarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarRegistro();
            }
        });
        Button actualizarBtn = findViewById(R.id.button_actualizar);
        actualizarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarRegistro();
            }
        });

        Button eliminarBtn = findViewById(R.id.button_eliminar);
        eliminarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarRegistro();
            }
        });
        Button btnBuscar = findViewById(R.id.button_buscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });
    }
    private void agregarRegistro() {
        ContentValues values = new ContentValues();
        values.put("sMarca", edMarca.getText().toString());
        values.put("sModelo", edModelo.getText().toString());
        mDatabase.insert("MD_Vehiculos", null, values);
        Toast.makeText(this, "Se ha agregado un registro con éxito", Toast.LENGTH_SHORT).show();
        Limpiar();

    }

    private void actualizarRegistro() {
        // Log.d("MiApp", txtId.toString());
        if(txtId != -1 ){
            ContentValues values = new ContentValues();
            values.put("sMarca", edMarca.getText().toString());
            values.put("sModelo", edModelo.getText().toString());
            mDatabase.update("MD_Vehiculos", values, "ID_Vehiculo=?", new String[]{String.valueOf(txtId)});
            Toast.makeText(this, "Se ha actualizado el registro con exito", Toast.LENGTH_SHORT).show();
            txtId =-1;
        }else{
            Toast.makeText(this, "Debe buscar el vehiculo primero", Toast.LENGTH_SHORT).show();
        }
        Limpiar();


    }

    private void eliminarRegistro() {
        if(txtId!=-1 ){
            mDatabase.delete("MD_Vehiculos", "ID_Vehiculo=?", new String[]{String.valueOf(txtId)});
            Toast.makeText(this, "Se elimino el registro correctamente", Toast.LENGTH_SHORT).show();
            txtId =-1;
        }else{
            Toast.makeText(this, "Debe buscar el vehiculo primero", Toast.LENGTH_SHORT).show();
        }
        Limpiar();

    }

    /*Para la busqueda del cliente*/
    private void showInputDialog() {
        // Crear un EditText para ingresar el ID
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        // Crear el diálogo de alerta con el EditText y dos botones
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Buscar Vehiculo por ID");
        builder.setMessage("Ingrese el ID del Vehiculo:");
        builder.setView(input);
        builder.setPositiveButton("Buscar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Obtener el ID ingresado y buscar el cliente en la base de datos
                int id = Integer.parseInt(input.getText().toString());

                buscarVehiculo(id);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Mostrar el diálogo de alerta
        builder.show();
    }
    private void buscarVehiculo(int id) {
        // Buscar el cliente en la base de datos por su ID
        String consulta = "SELECT ID_Vehiculo, sMarca, sModelo " +
                "FROM MD_Vehiculos " +
                "WHERE ID_Vehiculo = " + id + " LIMIT 1";

        Cursor cursor = mDatabase.rawQuery(consulta, null);

        if (cursor.moveToFirst()) {
            // Mostrar los detalles del cliente en los EditText correspondientes
            this.txtId = Integer.parseInt(cursor.getString(0));
            edMarca.setText(cursor.getString(1));
            edModelo.setText(cursor.getString(2));

        } else {
            // Mostrar un mensaje de error si el cliente no fue encontrado
            Toast.makeText(this, "Vehiculo no encontrado", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }
    private void Limpiar() {
        edMarca.setText("");
        edModelo.setText("");
        txtId=-1;
    }
}