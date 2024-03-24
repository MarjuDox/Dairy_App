package com.example.Dairy_App.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.example.Dairy_App.Activity.CreateNote;
import com.example.Dairy_App.Adapter.Firebasemodel;
import com.example.Dairy_App.Adapter.NotesAdapter;
import com.example.Dairy_App.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Notes extends Fragment {


    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private LinearLayout createnote;
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private SearchView svSearch;
    private FirestoreRecyclerOptions<Firebasemodel> allusernotes;
    private LinearLayout layout_search;
    private NotesAdapter noteAdapter;
    private View mView;

    public Notes() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            mView = inflater.inflate(R.layout.activity_notes, container, false);
            init(mView);

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            firebaseFirestore = FirebaseFirestore.getInstance();

            createnote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CreateNote.class);
                    startActivity(intent);
                }
            });

            //setting navigation

            //This One Using Firebase Adapter
            Query query = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").orderBy("title", Query.Direction.ASCENDING).whereEqualTo("isDeleted", "false");

            allusernotes = new FirestoreRecyclerOptions.Builder<Firebasemodel>().setQuery(query, Firebasemodel.class).build();

            noteAdapter = new NotesAdapter(getContext(),allusernotes);

            recyclerView = mView.findViewById(R.id.recyclerview);
            recyclerView.setHasFixedSize(true);
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            recyclerView.setAdapter(noteAdapter);
            return mView;
        }catch (Exception e) {
            throw e;
        }
    }

    void init(View mView){
        createnote = mView.findViewById(R.id.createnote);
        svSearch  = mView.findViewById(R.id.svSearch);
        layout_search = mView.findViewById(R.id.layout_search);
    }

    @Override
    public void onStart() {
        noteAdapter.startListening();
        super.onStart();
        if (svSearch != null){
            svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    search(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return false;
                }
            });
        }
    }
    public void search(String text) {
        Query query = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").orderBy("title", Query.Direction.ASCENDING).startAt(text).endAt(text+"\uf8ff").whereEqualTo("isDeleted","false");
        allusernotes = new FirestoreRecyclerOptions.Builder<Firebasemodel>().setQuery(query, Firebasemodel.class).build();
        noteAdapter = new NotesAdapter(getContext(),allusernotes);
        noteAdapter.startListening();
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(noteAdapter!=null){
            noteAdapter.startListening();
        }
    }
}