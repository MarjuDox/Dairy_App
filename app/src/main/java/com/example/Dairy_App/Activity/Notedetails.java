package com.example.Dairy_App.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class Notedetails extends AppCompatActivity {

    EditText titleDetail,contentDetail;
    TextView dateTimeNow;
    ImageView upDateNote;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ImageView imv_Voice_Note;
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
        setContentView(R.layout.activity_notedetails);
        init();

        Intent data = getIntent();

        String noteTitle = data.getStringExtra("title");
        String noteContent = data.getStringExtra("content");
        String docID = data.getStringExtra("noteID");

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
                    contentDetail.setText("");
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Your device doesn't support Speech to Text", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        upDateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newtitle = titleDetail.getText().toString();
                String newcontent = contentDetail.getText().toString();
                String newdate = dateTimeNow.getText().toString();

                if(newtitle.isEmpty() || newcontent.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Something is empty", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docID);
                    Map<String, Object> note = new HashMap<>();
                    note.put("title",newtitle);
                    note.put("content",newcontent);
                    note.put("dateTime",newdate);
                    note.put("isDeleted","false");
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Note is updated",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed to update note",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        titleDetail.setText(noteTitle);
        contentDetail.setText(noteContent);

    }

    void init(){
        titleDetail = findViewById(R.id.titlenotedetail);
        contentDetail = findViewById(R.id.contentnotedetail);
        upDateNote = findViewById(R.id.UpdateNote);
        dateTimeNow = findViewById(R.id.tvDateTimeNow);
        imv_Voice_Note = findViewById(R.id.imv_Voice_Note);
        dateTimeNow.setText(new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date()));
        Toolbar toolbar = findViewById(R.id.toolbardetailnote);
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
                    contentDetail.setText(text.get(0));
                }
                break;
        }
    }
}