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

public class Clientes extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private EditText editTextNombre;
    private EditText editTextApellido;
    private EditText editTextDireccion;
    private EditText editTextCiudad;
    private Integer txtId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        SQLiteOpenHelper dbHelper = new BaseHelper(getApplicationContext(), "ClientesDB", null, 2);
        mDatabase = dbHelper.getWritableDatabase();
        editTextNombre = findViewById(R.id.edit_text_nombre_cliente);
        editTextApellido = findViewById(R.id.edit_text_apellido_cliente);
        editTextDireccion = findViewById(R.id.edit_text_direccion_cliente);
        editTextCiudad = findViewById(R.id.edit_text_ciudad_cliente);
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
        values.put("sNombreCliente", editTextNombre.getText().toString());
        values.put("sApellidoCliente", editTextApellido.getText().toString());
        values.put("sDireccionCliente",editTextDireccion.getText().toString());
        values.put("sCiudadCliente", editTextCiudad.getText().toString());
        mDatabase.insert("MD_Clientes", null, values);
        Toast.makeText(this, "Se ha agregado un registro con éxito", Toast.LENGTH_SHORT).show();
        Limpiar();

    }

    private void actualizarRegistro() {
       // Log.d("MiApp", txtId.toString());
        if(txtId != -1 ){
            ContentValues values = new ContentValues();
            values.put("sNombreCliente", editTextNombre.getText().toString());
            values.put("sApellidoCliente", editTextApellido.getText().toString());
            values.put("sDireccionCliente",editTextDireccion.getText().toString());
            values.put("sCiudadCliente", editTextCiudad.getText().toString());
            mDatabase.update("MD_Clientes", values, "ID_cliente=?", new String[]{String.valueOf(txtId)});
            Toast.makeText(this, "Se ha actualizado el registro con exito", Toast.LENGTH_SHORT).show();
            txtId =-1;
        }else{
            Toast.makeText(this, "Debe buscar el cliente primero", Toast.LENGTH_SHORT).show();
        }
        Limpiar();


    }

    private void eliminarRegistro() {
        if(txtId!=-1 ){
            mDatabase.delete("MD_Clientes", "ID_cliente=?", new String[]{String.valueOf(txtId)});
            Toast.makeText(this, "Se elimino el registro correctamente", Toast.LENGTH_SHORT).show();
            txtId =-1;
        }else{
            Toast.makeText(this, "Debe buscar el cliente primero", Toast.LENGTH_SHORT).show();
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
        builder.setTitle("Buscar cliente por ID");
        builder.setMessage("Ingrese el ID del cliente:");
        builder.setView(input);
        builder.setPositiveButton("Buscar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Obtener el ID ingresado y buscar el cliente en la base de datos
                int id = Integer.parseInt(input.getText().toString());
                Log.d("MiApp", input.getText().toString());
                buscarCliente(id);
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
    private void buscarCliente(int id) {
        // Buscar el cliente en la base de datos por su ID
        String consulta = "SELECT ID_cliente, sNombreCliente, sApellidoCliente, sDireccionCliente, sCiudadCliente " +
                "FROM MD_Clientes " +
                "WHERE ID_cliente = " + id + " LIMIT 1";

        Cursor cursor = mDatabase.rawQuery(consulta, null);

        if (cursor.moveToFirst()) {
            // Mostrar los detalles del cliente en los EditText correspondientes
            this.txtId = Integer.parseInt(cursor.getString(0));
            editTextNombre.setText(cursor.getString(1));
            editTextApellido.setText(cursor.getString(2));
            editTextDireccion.setText(cursor.getString(3));
            editTextCiudad.setText(cursor.getString(4));

        } else {
            // Mostrar un mensaje de error si el cliente no fue encontrado
            Toast.makeText(this, "Cliente no encontrado", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }
    private void Limpiar() {
        editTextNombre.setText("");
        editTextApellido.setText("");
        editTextDireccion.setText("");
        editTextCiudad.setText("");
        txtId=-1;
    }
}