package com.robedev.museai.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;

import java.io.Serializable;
import java.util.Locale;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.robedev.museai.R;
import com.robedev.museai.data.model.Artwork;
import com.robedev.museai.data.model.Message;
import com.robedev.museai.service.ChatService;
import com.robedev.museai.service.MessageHistoryService;
import com.robedev.museai.service.TTSService;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends DialogFragment implements Serializable {
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<Message> messages; // Lista de mensajes
    private Artwork artwork;
    private EditText inputMessage; // Input de mensaje

    private TextToSpeech textToSpeech;
    private ChatService chatService;
    private MessageHistoryService messageHistoryService;

    private List<Artwork> artworks;

    private Handler mainHandler;

    private int currentiArtwork = 0;

    public static ChatFragment newInstance(List<Artwork> artworks, int currentIndex) {
        ChatFragment chatFragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putSerializable("artworks", (Serializable) artworks);
        args.putInt("currentIndex", currentIndex);
        chatFragment.setArguments(args);
        return chatFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            artworks = (List<Artwork>) getArguments().getSerializable("artworks");
            currentiArtwork = getArguments().getInt("currentIndex");
            artwork = artworks.get(currentiArtwork);
        }

        // Obtener la instancia del servicio TTS
        TTSService ttsService = TTSService.getInstance(getContext());

        messageHistoryService = new MessageHistoryService();
        mainHandler = new Handler(Looper.getMainLooper());
        chatService = new ChatService(messageHistoryService, mainHandler);
        chatService.setCallback(new ChatService.ChatServiceCallback() {
            @Override
            public void onSuccess(String response) {
                messages.add(new Message("assistant", response));
                chatAdapter.notifyItemInserted(messages.size() - 1);
                chatRecyclerView.scrollToPosition(messages.size() - 1);

                // Verificar si TTS está disponible antes de usarlo
                if (isTTSAvailable()) {
                    TTSService.getInstance(getContext()).speak(response, requireContext()); // Usar el servicio TTS
                }

                Log.d("ChatFragment", "Response received: " + response);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getActivity(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        TTSService.getInstance(getContext()).shutdown(); // Liberar recursos del TTS
        super.onDestroy();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = new RelativeLayout(getActivity());
        chatRecyclerView = new RecyclerView(getActivity());
        inputMessage = new EditText(getActivity()); // Inicializar el EditText
        inputMessage.setSingleLine(true);

        // Inicializar la lista de mensajes
        messages = new ArrayList<>();

        // Agregar mensajes de ejemplo (invertir el orden para que la IA sea primero)
        messages.add(new Message("assistant", getResources().getString(R.string.ai_chat_welcome))); // IA primero
        //messages.add(new Message("user", "Hola, ¿cómo estás?"));

        // Configurar el RecyclerView
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatAdapter = new ChatAdapter(messages); // Inicializar el adapter con la lista de mensajes
        chatRecyclerView.setAdapter(chatAdapter); // Establecer el adapter

        // Configurar el inputMessage
        inputMessage.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        inputMessage.setHint("Escribe un mensaje..."); // Placeholder

        inputMessage.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                sendMessage();
                return true;
            }
            return false;
        });

        // Botón para enviar mensaje
        Button sendButton = new Button(getActivity());
        sendButton.setText("Enviar");
        sendButton.setOnClickListener(v -> sendMessage());

        // Botón para reconocimiento de voz
        Button voiceButton = new Button(getActivity());
        voiceButton.setText("🎤");
        voiceButton.setOnClickListener(v -> startVoiceInput());
        voiceButton.setFocusable(true); // Asegúrate de que el botón pueda recibir el enfoque
        voiceButton.setFocusableInTouchMode(true); // Permitir enfoque en modo táctil
        voiceButton.requestFocus(); // Solicitar enfoque al botón de voz

        // Crear un LinearLayout para el input
        LinearLayout inputLayout = new LinearLayout(getActivity());
        inputLayout.setOrientation(LinearLayout.HORIZONTAL);
        inputLayout.addView(inputMessage);
        inputLayout.addView(sendButton);
        inputLayout.addView(voiceButton);

        // Configurar el contenedor principal
        LinearLayout mainLayout = new LinearLayout(getActivity());
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        // Añadir el RecyclerView y el layout de input al contenedor principal
        mainLayout.addView(chatRecyclerView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0, // Para que ocupe el espacio restante
                1 // Peso para que el RecyclerView tome el espacio
        ));
        mainLayout.addView(inputLayout, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        layout.addView(mainLayout);
        return layout;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();

            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int width = (int) (metrics.widthPixels * 0.33);
            int height = (int) (metrics.heightPixels * 0.7);

            layoutParams.width = width;
            layoutParams.height = height;
            layoutParams.gravity = Gravity.BOTTOM | Gravity.END;

            window.setAttributes(layoutParams);
        }

        // Forzar la visualización del teclado
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


    private void sendMessage() {
        String messageContent = inputMessage.getText().toString().trim();
        if (!messageContent.isEmpty()) {
            // Añadir el mensaje del usuario a la lista
            messages.add(new Message("user", messageContent)); // Añadir el mensaje del usuario
            chatAdapter.notifyItemInserted(messages.size() - 1);
            chatRecyclerView.scrollToPosition(messages.size() - 1); // Desplazarse al nuevo mensaje
            inputMessage.setText(""); // Limpiar el campo de texto

            // Enviar el mensaje al servidor
            String language = "es"; // Idioma español
            String instructions = "Respond in: " + language + "Eres el mayor experto en arte, concretamente del museo Met, usa un tono apasionante y educativo pero se no te extiendas mucho, que el usuario no se canse, tu objetivo es brindar una experiencia de conocimiento, por ello, traduce el nombre de las obras a una version en el idioma que das la respuesta, solo puedes responder preguntas sobre arte y nada mas. Se te pasará un artista, ya que estaremos en su colección, que será el principio de la interacción, en este caso: el artisa : " +  artworks.get(currentiArtwork).getArtistDisplayName(); // Instrucciones completas
            chatService.sendMessage(language, instructions, messageContent);

            // Agregar log para verificar el envío del mensaje
            Log.d("ChatFragment", "Message sent: " + messageContent);
        }
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Diga algo...");
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (results != null && !results.isEmpty()) {
                    String spokenMessage = results.get(0);
                    inputMessage.setText(spokenMessage); // Colocar el mensaje hablado en el EditText
                }
            }
        }
    }

    // Adaptador para el RecyclerView
    private class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
        private List<Message> messages;

        public ChatAdapter(List<Message> messages) {
            this.messages = messages;
        }

        @NonNull
        @Override
        public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Crear un LinearLayout para cada mensaje
            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, // Ancho completo
                    ViewGroup.LayoutParams.WRAP_CONTENT // Alto ajustable
            ));
            layout.setPadding(10, 10, 10, 10); // Padding

            return new ChatViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
            Message message = messages.get(position);
            holder.bind(message);
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        class ChatViewHolder extends RecyclerView.ViewHolder {
            private LinearLayout messageContainer; // Contenedor para el mensaje
            private TextView messageTextView; // TextView para el contenido del mensaje

            public ChatViewHolder(@NonNull View itemView) {
                super(itemView);
                messageContainer = new LinearLayout(getActivity());
                messageContainer.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                messageTextView = new TextView(getActivity());
                messageTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                messageTextView.setPadding(20, 10, 20, 10); // Padding interno para el mensaje

                messageContainer.addView(messageTextView);
                ((LinearLayout) itemView).addView(messageContainer);
            }

            public void bind(Message message) {
                messageTextView.setText(message.getContent());

                messageTextView.setTextSize(18);

                // Configurar colores de fondo y alineación según el rol
                GradientDrawable drawable = new GradientDrawable();
                if ("user".equals(message.getRole())) {
                    drawable.setColor(getResources().getColorStateList(R.color.search_opaque)); // Verde para el usuario
                    messageTextView.setGravity(Gravity.START); // Alinear a la izquierda
                    messageContainer.setGravity(Gravity.END); // Alinear contenedor a la derecha
                } else if ("assistant".equals(message.getRole())) {
                    drawable.setColor(getResources().getColorStateList(R.color.background_gradient_start)); // Azul para la IA
                    messageTextView.setGravity(Gravity.START); // Alinear a la izquierda
                    messageContainer.setGravity(Gravity.START); // Alinear contenedor a la izquierda
                    // **Aquí activamos el TTS**
                    if (textToSpeech != null) {
                        textToSpeech.speak(message.getContent(), TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                }

                // Establecer el radio de borde
                drawable.setCornerRadius(5f); // Radio de esquina
                messageTextView.setBackground(drawable);

                // Aplicar márgenes
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(5, 5, 5, 5); // Márgenes entre mensajes
                messageTextView.setLayoutParams(params);
            }
        }
    }

    private boolean isTTSAvailable() {
        return textToSpeech != null && !textToSpeech.isSpeaking();
    }
}

