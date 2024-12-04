package com.robedev.museai.service;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class TTSService {
    private static TTSService instance;
    private TextToSpeech textToSpeech;
    private boolean isInitialized = false;

    // Constructor privado para evitar instanciaciones
    private TTSService(Context context) {
        textToSpeech = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                Locale locale = getUserLocale();
                int result = textToSpeech.setLanguage(locale);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTSService", "Language not supported: " + locale);
                } else {
                    isInitialized = true;
                }
            } else {
                Log.e("TTSService", "Initialization failed");
            }
        });
    }

    // Método para obtener la instancia singleton
    public static synchronized TTSService getInstance(Context context) {
        if (instance == null) {
            instance = new TTSService(context.getApplicationContext());
        }
        return instance;
    }

    // Método para obtener el idioma del usuario basado en la configuración del sistema
    private Locale getUserLocale() {
        Locale systemLocale = Locale.getDefault(); // Idioma del sistema
        Log.i("TTSService", "System locale: " + systemLocale);
        return systemLocale; // Puedes personalizar esto según tu necesidad
    }

    // Método para hablar texto
    public void speak(String text, Context context) {
        if (isInitialized) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            Log.e("TTSService", "TTS not initialized, displaying text instead");
            displayFallbackText(text, context);
        }
    }

    // Mostrar texto en pantalla como alternativa al TTS
    private void displayFallbackText(String text, Context context) {
        // Implementa aquí cómo mostrarías el texto al usuario.
        // Por ejemplo, podrías usar un Toast, un diálogo o actualizar la UI.
        Log.i("TTSService", "Fallback text: " + text);
    }

    // Método para liberar recursos
    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
