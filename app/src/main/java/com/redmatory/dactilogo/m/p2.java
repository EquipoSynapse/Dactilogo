package com.redmatory.dactilogo.m;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.util.TypedValue;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.redmatory.dactilogo.MainActivity;
import com.redmatory.dactilogo.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Locale;

public class p2 extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private SpeechRecognizer speechRecognizer;
    private ImageView cod1;
    private ImageView cod2;
    private TextView cod3;
    private ImageView cod4, cod5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p2);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            initializeSpeechRecognizer();
        }

        cod1 = findViewById(R.id.id2);
        cod2 = findViewById(R.id.id3);
        cod3 = findViewById(R.id.id4);
        cod3 = findViewById(R.id.id4);
        cod4 = findViewById(R.id.id5);
        cod5 = findViewById(R.id.id6);
        cod2.setOnClickListener(view -> startSpeechRecognition());

        cod4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyboardDialog();
            }
        });

        cod5.setOnClickListener(v -> {
            // Inicia MainActivity
            Intent intent = new Intent(p2.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void showKeyboardDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.f1);

        TextView display = dialog.findViewById(R.id.id39);
        TextView Enviar = dialog.findViewById(R.id.id40);
        Enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st = display.getText().toString();
                IniciarAnimación(st);
                dialog.dismiss();
            }
        });
        // Set listeners for number buttons
        setNumberButtonListener(dialog, R.id.id1, display, "1");
        setNumberButtonListener(dialog, R.id.id2, display, "2");
        setNumberButtonListener(dialog, R.id.id3, display, "3");
        setNumberButtonListener(dialog, R.id.id4, display, "4");
        setNumberButtonListener(dialog, R.id.id5, display, "5");
        setNumberButtonListener(dialog, R.id.id6, display, "6");
        setNumberButtonListener(dialog, R.id.id7, display, "7");
        setNumberButtonListener(dialog, R.id.id8, display, "8");
        setNumberButtonListener(dialog, R.id.id9, display, "9");
        setNumberButtonListener(dialog, R.id.id10, display, "0");

        // Set listeners for letter buttons
        setLetterButtonListener(dialog, R.id.id11, display, "q");
        setLetterButtonListener(dialog, R.id.id12, display, "w");
        setLetterButtonListener(dialog, R.id.id13, display, "e");
        setLetterButtonListener(dialog, R.id.id14, display, "r");
        setLetterButtonListener(dialog, R.id.id15, display, "t");
        setLetterButtonListener(dialog, R.id.id16, display, "y");
        setLetterButtonListener(dialog, R.id.id17, display, "u");
        setLetterButtonListener(dialog, R.id.id18, display, "i");
        setLetterButtonListener(dialog, R.id.id19, display, "o");
        setLetterButtonListener(dialog, R.id.id20, display, "p");
        setLetterButtonListener(dialog, R.id.id21, display, "a");
        setLetterButtonListener(dialog, R.id.id22, display, "s");
        setLetterButtonListener(dialog, R.id.id23, display, "d");
        setLetterButtonListener(dialog, R.id.id24, display, "f");
        setLetterButtonListener(dialog, R.id.id25, display, "g");
        setLetterButtonListener(dialog, R.id.id26, display, "h");
        setLetterButtonListener(dialog, R.id.id27, display, "j");
        setLetterButtonListener(dialog, R.id.id28, display, "k");
        setLetterButtonListener(dialog, R.id.id29, display, "l");
        setLetterButtonListener(dialog, R.id.id30, display, "z");
        setLetterButtonListener(dialog, R.id.id31, display, "x");
        setLetterButtonListener(dialog, R.id.id32, display, "c");
        setLetterButtonListener(dialog, R.id.id33, display, "v");
        setLetterButtonListener(dialog, R.id.id34, display, "b");
        setLetterButtonListener(dialog, R.id.id35, display, "n");
        setLetterButtonListener(dialog, R.id.id36, display, "m");

        // Set listeners for special buttons
        dialog.findViewById(R.id.id37).setOnClickListener(v -> {
            String text = display.getText().toString();
            if (text.length() > 0) {
                display.setText(text.substring(0, text.length() - 1));
            }
        });

        dialog.findViewById(R.id.id38).setOnClickListener(v -> {
            display.append(" ");
        });

        dialog.show();
    }

    private void setNumberButtonListener(Dialog dialog, int buttonId, TextView display, String number) {
        dialog.findViewById(buttonId).setOnClickListener(v -> {
            display.append(number);
        });
    }

    private void setLetterButtonListener(Dialog dialog, int buttonId, TextView display, String letter) {
        dialog.findViewById(buttonId).setOnClickListener(v -> {
            display.append(letter);
        });
    }








    //Voz
    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    // El reconocimiento está listo para comenzar
                }

                @Override
                public void onBeginningOfSpeech() {
                    // Se ha comenzado a hablar
                }

                @Override
                public void onRmsChanged(float rmsdB) {
                    // Cambio en el nivel de entrada de voz
                }

                @Override
                public void onBufferReceived(byte[] buffer) {
                    // Se ha recibido un buffer de audio
                }

                @Override
                public void onEndOfSpeech() {
                    // Fin de la entrada de voz
                }

                @Override
                public void onError(int error) {
                    // Manejar errores aquí
                    cod2.setImageResource(R.drawable.r6);
                    Toast.makeText(p2.this, "Error en el reconocimiento de voz", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResults(Bundle results) {
                    // Se han recibido resultados
                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if (matches != null && !matches.isEmpty()) {
                        String spokenText = matches.get(0);
                        cod3.setText(spokenText);
                        IniciarAnimación(spokenText);
                    }
                }

                @Override
                public void onPartialResults(Bundle partialResults) {
                    // Resultados parciales (si los necesitas)
                }

                @Override
                public void onEvent(int eventType, Bundle params) {
                    // Eventos adicionales (si es necesario)
                }
            });
        }
    }

    private void IniciarAnimación(String spokenText) {

        try {
            // Obtener las rutas de los archivos
            File archivoSeñas = new File(this.getExternalFilesDir(null), "señas.txt");
            File archivoSeñaPalabras = new File(this.getExternalFilesDir(null), "señapalabras.txt");

            // Crear un handler para manejar la actualización de la UI
            Handler handler = new Handler();

            // Guardar el texto original para el subtítulo sin normalizar
            final String subtituloText = spokenText;

            // Normalizar el texto hablado para comparaciones (solo se aplica a las letras)
            String spokenTextNormalizado = Normalizer.normalize(spokenText.toLowerCase(), Normalizer.Form.NFD)
                    .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

            // Dividir el texto hablado en palabras
            String[] palabras = spokenTextNormalizado.split("\\s+");  // Dividir por espacios
            String[] palabrasOriginales = subtituloText.split("\\s+");  // Dividir el texto original para subtítulos

            // Variable para llevar el tiempo de retraso acumulado
            int delay = 0;

            // Recorrer cada palabra del texto hablado
            for (int i = 0; i < palabras.length; i++) {
                final String palabraHabladaNormalizada = palabras[i];
                final String palabraHabladaOriginal = palabrasOriginales[i];  // Para mostrar en subtítulos
                boolean palabraEncontrada = false;

                // Mostrar la palabra en el TextView "Subtitulos"
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cod3.setText(palabraHabladaOriginal);  // Mostrar la palabra completa en el TextView
                    }
                }, delay);

                Toast.makeText(p2.this, "7", Toast.LENGTH_SHORT).show();

                /*
                // Leer el archivo "señapalabras" para verificar si la palabra está en el archivo
                BufferedReader brPalabras = new BufferedReader(new InputStreamReader(new FileInputStream(archivoSeñaPalabras)));
                String lineaPalabras;

                Toast.makeText(p2.this, "6", Toast.LENGTH_SHORT).show();
                // Buscar la palabra en el archivo de señapalabras
                while ((lineaPalabras = brPalabras.readLine()) != null) {
                    String[] partesPalabras = lineaPalabras.split(";");

                    if (partesPalabras.length == 2) {
                        String palabraArchivo = partesPalabras[0];

                        // Comparar la palabra del archivo con la palabra normalizada hablada
                        if (palabraArchivo.equals(palabraHabladaNormalizada)) {
                            final String base64Image = partesPalabras[1];

                            // Establecer la imagen de la palabra completa con un retraso
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                                    cod1.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
                                }
                            }, delay);

                            delay += 500;  // Aumentar el retraso por palabra encontrada
                            palabraEncontrada = true;
                            break;  // Salir del ciclo si se encuentra la palabra completa
                        }
                    }
                }
                brPalabras.close();

                Toast.makeText(p2.this, "5", Toast.LENGTH_SHORT).show();
                // Si la palabra no fue encontrada en el archivo "señapalabras", procesarla letra por letra
                if (!palabraEncontrada) {*/
                    Toast.makeText(p2.this, "4", Toast.LENGTH_SHORT).show();
                    BufferedReader brSeñas = new BufferedReader(new InputStreamReader(new FileInputStream(archivoSeñas)));

                    // Recorrer cada letra de la palabra
                    for (int j = 0; j < palabraHabladaNormalizada.length(); j++) {
                        final char letraHablada = palabraHabladaNormalizada.charAt(j);
                        String lineaSeñas;
                        boolean letraEncontrada = false;

                        // Buscar cada letra en el archivo "señas"
                        while ((lineaSeñas = brSeñas.readLine()) != null) {
                            String[] partesSeñas = lineaSeñas.split(";");
                            Toast.makeText(p2.this, "3", Toast.LENGTH_SHORT).show();

                            if (partesSeñas.length == 2) {
                                Toast.makeText(p2.this, "2", Toast.LENGTH_SHORT).show();
                                char letraArchivo = partesSeñas[0].charAt(0);

                                // Comparar la letra del archivo con la letra hablada
                                if (letraArchivo == letraHablada) {
                                    Toast.makeText(p2.this, "1", Toast.LENGTH_SHORT).show();

                                    final String base64Image = partesSeñas[1];

                                    // Mostrar la imagen de la letra con el retraso
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                                            cod1.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
                                        }
                                    }, delay);

                                    letraEncontrada = true;
                                    delay += 500;  // Aumentar el retraso por cada letra encontrada
                                    break;  // Salir del ciclo si se encuentra la letra
                                }
                            }
                        }

                        // Resetear el buffer para la próxima letra
                        brSeñas = new BufferedReader(new InputStreamReader(new FileInputStream(archivoSeñas)));

                        // Si la letra no fue encontrada, mostrar una imagen predeterminada con retraso
                        if (!letraEncontrada) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    cod1.setImageResource(R.drawable.vacio);
                                }
                            }, delay);
                            delay += 500;  // Aumentar el retraso por la letra no encontrada
                        }
                    }

                    brSeñas.close();
                //}

                // Eliminar la palabra del TextView "Subtitulos" después de que se ha procesado
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cod3.setText("");  // Limpiar el subtítulo cuando se termina de procesar la palabra
                    }
                }, delay);
            }

            cod2.setImageResource(R.drawable.r6);  // Establecer la imagen final

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void startSpeechRecognition() {
        Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-MX"); // Cambia a tu idioma preferido
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora");

        cod2.setImageResource(R.drawable.r7);
        speechRecognizer.startListening(recognizerIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeSpeechRecognizer();
            } else {
                Toast.makeText(this, "Permiso de grabación de audio denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        super.onDestroy();
    }
}
