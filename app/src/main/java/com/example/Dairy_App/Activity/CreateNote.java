package com.example.Dairy_App.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Dairy_App.R;
import com.example.Dairy_App.SharedPreferences.DataLocalManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateNote extends AppCompatActivity {

    EditText createcontentnote;
    EditText createtitlenote;
    TextView dateTime;
    ImageView notesave;
    ImageView imv_Voice_Note;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    protected static final int RESULT_SPEECH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DataLocalManager.getCheckSwitch() == 2){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            //setTheme(R.style.Theme_Dairy_AppDark);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            //setTheme(R.style.Theme_Dairy_App);
        }
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dairy_AppDark);
        } else {
            setTheme(R.style.Theme_Dairy_App);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        init();
        dateTime.setText(new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date()));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Voice icon click (change speech to text)
        imv_Voice_Note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    createcontentnote.setText("");
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Your device doesn't support Speech to Text", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        notesave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = createtitlenote.getText().toString();
                String content = createcontentnote.getText().toString();
                String date = dateTime.getText().toString();
                if(title.isEmpty() || content.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Both field are require", Toast.LENGTH_SHORT).show();
                }else{
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();
                    Map<String,Object> note = new HashMap<>();
                    note.put("title",title);
                    note.put("content",content);
                    note.put("dateTime",date);
                    note.put("isDeleted","false");

                    documentReference
                            .set(note)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void avoid) {
                                    Toast.makeText(getApplicationContext(), "Note create successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CreateNote.this, Control_fragment.class);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Fail to create note", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    void init(){
        createcontentnote = findViewById(R.id.createcontentnote);
        createtitlenote = findViewById(R.id.createtitlenote);
        notesave = findViewById(R.id.savenote);
        dateTime = findViewById(R.id.tvDateTimeCreate);
        imv_Voice_Note = findViewById(R.id.imv_Voice_Note);
        Toolbar toolbar = findViewById(R.id.toolbarcreatenote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RESULT_SPEECH:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    createcontentnote.setText(text.get(0));
                }
                break;
        }
    }
}