package com.redmatory.dactilogo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 100;

    private EditText editTextLetra;
    private Button btnSelectImage, btnAdd;
    private String imageBase64 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextLetra = findViewById(R.id.editTextLetra);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnAdd = findViewById(R.id.btnAdd);

        btnSelectImage.setOnClickListener(v -> openGallery());
        btnAdd.setOnClickListener(v -> addEntryToFile());

        // Check for storage permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = getScaledBitmap(imageUri, 300, 300);
                imageBase64 = bitmapToBase64(bitmap);
                Toast.makeText(this, "Imagen cargada correctamente", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap getScaledBitmap(Uri imageUri, int width, int height) throws IOException {
        ContentResolver resolver = getContentResolver();
        InputStream inputStream = resolver.openInputStream(imageUri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        if (bitmap != null) {
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        }

        inputStream.close();
        return bitmap;
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64String = Base64.encodeToString(byteArray, Base64.NO_WRAP); // Elimina los saltos de línea
        return base64String;
    }

    private void addEntryToFile() {
        String letra = editTextLetra.getText().toString().trim();
        if (letra.isEmpty() || imageBase64.isEmpty()) {
            Toast.makeText(this, "Debe ingresar una letra y seleccionar una imagen", Toast.LENGTH_SHORT).show();
            return;
        }

        String entry = letra + ";" + imageBase64 + "\n";
        writeToFile(entry);
    }

    private void writeToFile(String entry) {
        File path = getExternalFilesDir(null);
        File file = new File(path, "señas.txt");

        try (FileOutputStream fos = new FileOutputStream(file, true)) { // 'true' para añadir, no sobrescribir
            fos.write(entry.getBytes());
            Toast.makeText(this, "Entrada añadida al archivo", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al escribir en el archivo", Toast.LENGTH_SHORT).show();
        }
    }
}
