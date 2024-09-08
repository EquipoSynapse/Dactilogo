package com.redmatory.dactilogo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.util.HashMap;

public class p5 extends AppCompatActivity {
    EditText n1; // nombre
    TextView n2; // edad
    Button n3; // registrar usuario
    EditText n4; // correo
    EditText n5; // contraseña
    Button cancelButton; // botón cancelar
    Button backButton; // botón volver

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p5); // Usamos el layout p5 correspondiente al registro

        n1 = findViewById(R.id.Nombres);
        n2 = findViewById(R.id.Edad);
        n3 = findViewById(R.id.REGISTRARUSUARIO);
        n4 = findViewById(R.id.id1); // correo
        n5 = findViewById(R.id.id2); // contraseña
        cancelButton = findViewById(R.id.cancel_button); // botón cancelar
        backButton = findViewById(R.id.back_button); // botón volver

        firebaseAuth = FirebaseAuth.getInstance();

        // Lógica para mostrar el calendario al seleccionar el campo de edad
        n2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                mostrarCalendario();
            }
        });

        // Lógica para registrar el usuario al hacer clic en el botón de registro
        n3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = n4.getText().toString();
                String contraseña = n5.getText().toString();
                if (validarEntradas()) {
                    REGISTRAR(correo, contraseña);
                }
            }
        });

        // Botón cancelar para limpiar los campos
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarCampos();
            }
        });

        // Botón volver para regresar a la actividad anterior
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    // Método para registrar usuario en Firebase
    private void REGISTRAR(String correo, String contraseña) {
        firebaseAuth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            String uid = user.getUid();
                            String nombres = n1.getText().toString();
                            String edad = n2.getText().toString();

                            // Verificar el nombre y la edad
                            if (nombres.length() < 8) {
                                Toast.makeText(p5.this, "El nombre debe tener al menos 8 caracteres.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (Integer.parseInt(edad) < 5) {
                                Toast.makeText(p5.this, "Por favor, ingrese una edad válida.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Crear un HashMap para almacenar los datos del usuario
                            HashMap<String, String> DatosUsuario = new HashMap<>();
                            DatosUsuario.put("ID", uid);
                            DatosUsuario.put("Correo", correo);
                            DatosUsuario.put("Contraseña", contraseña);
                            DatosUsuario.put("Nombres", nombres);
                            DatosUsuario.put("Edad", edad);

                            // Guardar en la base de datos de Firebase
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Usuarios");
                            reference.child(uid).setValue(DatosUsuario);

                            Toast.makeText(p5.this, "Bienvenido, usuario registrado con éxito.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(p5.this, Inicio.class));
                        } else {
                            Toast.makeText(p5.this, "Error en el registro, inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(p5.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Mostrar calendario para seleccionar la fecha de nacimiento
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void mostrarCalendario() {
        final LocalDate fechaActual = LocalDate.now();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int anio, int mes, int dia) {
                        LocalDate fechaSeleccionada = LocalDate.of(anio, mes + 1, dia);
                        long años = fechaActual.getYear() - fechaSeleccionada.getYear();
                        n2.setText(String.valueOf(años)); // Mostrar la edad en el TextView
                    }
                }, fechaActual.getYear(), fechaActual.getMonthValue() - 1, fechaActual.getDayOfMonth());
        datePickerDialog.show();
    }

    // Método para validar las entradas del formulario
    private boolean validarEntradas() {
        String correo = n4.getText().toString();
        String contraseña = n5.getText().toString();
        String nombre = n1.getText().toString();
        String edad = n2.getText().toString();

        if (nombre.isEmpty() || nombre.length() < 8) {
            Toast.makeText(p5.this, "El nombre debe tener al menos 8 caracteres.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (correo.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(p5.this, "Por favor, ingrese un correo válido.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (contraseña.isEmpty() || contraseña.length() < 6) {
            Toast.makeText(p5.this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edad.isEmpty() || Integer.parseInt(edad) < 5) {
            Toast.makeText(p5.this, "Por favor, ingrese una edad válida.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Método para limpiar los campos del formulario
    private void limpiarCampos() {
        n1.setText("");
        n2.setText("");
        n4.setText("");
        n5.setText("");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
