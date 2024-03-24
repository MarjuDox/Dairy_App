package com.example.Dairy_App.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Dairy_App.Activity.Notedetails;
import com.example.Dairy_App.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class NotesAdapter extends FirestoreRecyclerAdapter<Firebasemodel, NotesAdapter.NoteViewHolder>  {
    FirestoreRecyclerAdapter<Firebasemodel, NoteViewHolder> noteAdapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private Context context;

    public NotesAdapter(Context context, @NonNull FirestoreRecyclerOptions<Firebasemodel> options) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull Firebasemodel firebasemodel) {
        ImageView menupopbutton = noteViewHolder.itemView.findViewById(R.id.menupopbutton);

        int colourcode = getRandomColor();

        noteViewHolder.notetitle.setText(firebasemodel.getTitle());
        noteViewHolder.notecontent.setText(firebasemodel.getContent());
        noteViewHolder.textdatetime.setText(firebasemodel.getDateTime());
        noteViewHolder.mnote.setBackgroundColor(noteViewHolder.itemView.getResources().getColor(colourcode, null));

        String docID = getSnapshots().getSnapshot(i).getId();

        noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Notedetails.class);

                intent.putExtra("title", firebasemodel.getTitle());
                intent.putExtra("content", firebasemodel.getContent());
                intent.putExtra("noteID", docID);

                v.getContext().startActivity(intent);
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();


        menupopbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.setGravity(Gravity.END);
                if (firebasemodel.getIsDeleted().equals("false")) {
                    popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docID);
                            Map<String, Object> note = new HashMap<>();
                            note.put("title", firebasemodel.getTitle());
                            note.put("content", firebasemodel.getContent());
                            note.put("dateTime", firebasemodel.getDateTime());
                            note.put("isDeleted", "true");
                            documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(menupopbutton.getContext(), "This Note Is Deleted", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(menupopbutton.getContext(), "Failed To Delete", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return false;
                        }
                    });
                    popupMenu.show();
                } else if (firebasemodel.getIsDeleted().equals("true")) {
                    popupMenu.getMenu().add("Restore").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docID);
                            Map<String, Object> note = new HashMap<>();
                            note.put("title",firebasemodel.getTitle());
                            note.put("content",firebasemodel.getContent());
                            note.put("dateTime",firebasemodel.getDateTime());
                            note.put("isDeleted","false");
                            documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(menupopbutton.getContext(), "This Note is Restored", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(menupopbutton.getContext(), "Failed To Restore", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return false;
                        }
                    });

                    popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                            alertDialog.setTitle("Warming!!");
                            alertDialog.setMessage("This can't be restored after your choice");
                            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docID);
                                    documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(menupopbutton.getContext(), "This Note is Delete", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(menupopbutton.getContext(), "Failed To Delete", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alertDialog.show();

                            return false;
                        }
                    });
                    popupMenu.show();
                }
            }
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout, parent, false);
        return new NoteViewHolder(view);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{

        private TextView notetitle,notecontent,textdatetime;
        LinearLayout mnote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textdatetime = itemView.findViewById(R.id.tvDateTime);
            notetitle = itemView.findViewById(R.id.notetitle);
            notecontent = itemView.findViewById(R.id.notecontent);
            mnote = itemView.findViewById(R.id.note);
        }
    }

    private int getRandomColor(){
        List<Integer> colorcode = new ArrayList<>();
        colorcode.add(R.color.purple);
        colorcode.add(R.color.green);
        colorcode.add(R.color.peach);
        colorcode.add(R.color.blue);
        colorcode.add(R.color.orangeRed);
        colorcode.add(R.color.MangoTango);
        colorcode.add(R.color.yellowOrange);
        colorcode.add(R.color.maizeCrayola);
        colorcode.add(R.color.card_violet);
        colorcode.add(R.color.darkPurple);

        Random random = new Random();
        int number = random.nextInt(colorcode.size());
        return colorcode.get(number);
    }
}
