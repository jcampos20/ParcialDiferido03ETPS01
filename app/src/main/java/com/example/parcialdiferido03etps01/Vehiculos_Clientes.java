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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.parcialdiferido03etps01.HelperBase.BaseHelper;

import java.util.ArrayList;
import java.util.List;

public class Vehiculos_Clientes extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private Spinner spinnerClientes, spinnerVehiculos;
    private EditText etMatricula, etKilometros;
    private Integer txtId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculos_clientes);
        SQLiteOpenHelper dbHelper = new BaseHelper(getApplicationContext(), "ClientesDB", null, 2);
        mDatabase = dbHelper.getWritableDatabase();
        // Obtener las referencias a los Views
        spinnerClientes = findViewById(R.id.spinner);
        spinnerVehiculos = findViewById(R.id.spinner2);
        etMatricula = findViewById(R.id.edit_text_matricula);
        etKilometros = findViewById(R.id.edit_text_kilometros);

        // Cargar los datos en los spinners
        cargarSpinnerClientes();
        cargarSpinnerVehiculos();

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
        values.put("ID_Cliente", spinnerClientes.getSelectedItem().toString());
        values.put("ID_Vehiculo", spinnerVehiculos.getSelectedItem().toString());
        values.put("sMatricula", etMatricula.getText().toString());
        values.put("sKilometros", etKilometros.getText().toString());
        mDatabase.insert("MD_ClienteVehiculo", null, values);
        Toast.makeText(this, "Se ha agregado un registro con éxito", Toast.LENGTH_SHORT).show();
        Limpiar();

    }

    private void actualizarRegistro() {
        // Log.d("MiApp", txtId.toString());
        if(txtId != -1 ){
            ContentValues values = new ContentValues();
            values.put("ID_Cliente", spinnerClientes.getSelectedItem().toString());
            values.put("ID_Vehiculo", spinnerVehiculos.getSelectedItem().toString());
            values.put("sMatricula", etMatricula.getText().toString());
            values.put("sKilometros", etKilometros.getText().toString());
            mDatabase.update("MD_ClienteVehiculo", values, "ID_VehiculoCliente=?", new String[]{String.valueOf(txtId)});
            Toast.makeText(this, "Se ha actualizado el registro con exito", Toast.LENGTH_SHORT).show();
            txtId =-1;
        }else{
            Toast.makeText(this, "Debe buscar el Registro primero", Toast.LENGTH_SHORT).show();
        }
        Limpiar();


    }

    private void eliminarRegistro() {
        if(txtId!=-1 ){
            mDatabase.delete("MD_ClienteVehiculo", "ID_VehiculoCliente=?", new String[]{String.valueOf(txtId)});
            Toast.makeText(this, "Se elimino el registro correctamente", Toast.LENGTH_SHORT).show();
            txtId =-1;
        }else{
            Toast.makeText(this, "Debe buscar el registro primero", Toast.LENGTH_SHORT).show();
        }
        Limpiar();

    }


    private void showInputDialog() {
        // Crear un EditText para ingresar el ID
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        // Crear el diálogo de alerta con el EditText y dos botones
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Buscar Registro por ID");
        builder.setMessage("Ingrese el ID del Registro:");
        builder.setView(input);
        builder.setPositiveButton("Buscar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Obtener el ID ingresado
                int id = Integer.parseInt(input.getText().toString());

                buscarRegistro(id);
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
    private void buscarRegistro(int id) {
        String consulta = "SELECT ID_VehiculoCliente, ID_cliente, ID_Vehiculo,sKilometros,sMatricula " +
                "FROM MD_ClienteVehiculo " +
                "WHERE ID_VehiculoCliente = " + id + " LIMIT 1";

        Cursor cursor = mDatabase.rawQuery(consulta, null);

        if (cursor.moveToFirst()) {
            // Mostrar los detalles del cliente en los EditText correspondientes
            this.txtId = Integer.parseInt(cursor.getString(0));

            etKilometros.setText(cursor.getString(3));
            etMatricula.setText(cursor.getString(4));


        } else {
            // Mostrar un mensaje de error si el cliente no fue encontrado
            Toast.makeText(this, "Registro no encontrado", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }

    private void cargarSpinnerClientes() {
        // Obtener una lista con todos los clientes de la tabla MD_Clientes
        List<String> listaClientes = new ArrayList<>();
        Cursor cursorClientes = mDatabase.rawQuery("SELECT ID_Cliente FROM MD_Clientes", null);
        if (cursorClientes.moveToFirst()) {
            do {
                // Agregar el ID_cliente a la lista
                listaClientes.add(cursorClientes.getString(0));
            } while (cursorClientes.moveToNext());
        }
        cursorClientes.close();

        // Crear un ArrayAdapter para el Spinner de clientes
        ArrayAdapter<String> adapterClientes = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listaClientes);
        adapterClientes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClientes.setAdapter(adapterClientes);
    }

    private void cargarSpinnerVehiculos() {
        // Obtener una lista con todos los vehiculos de la tabla MD_Vehiculos
        List<String> listaVehiculos = new ArrayList<>();
        Cursor cursorVehiculos = mDatabase.rawQuery("SELECT ID_Vehiculo FROM MD_Vehiculos", null);
        if (cursorVehiculos.moveToFirst()) {
            do {
                // Agregar el ID_Vehiculo a la lista
                listaVehiculos.add(cursorVehiculos.getString(0));
            } while (cursorVehiculos.moveToNext());
        }
        cursorVehiculos.close();

        // Crear un ArrayAdapter para el Spinner de vehiculos
        ArrayAdapter<String> adapterVehiculos = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listaVehiculos);
        adapterVehiculos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVehiculos.setAdapter(adapterVehiculos);
    }
    private void Limpiar() {
        etMatricula.setText("");
        etKilometros.setText("");
        txtId=-1;
    }
}