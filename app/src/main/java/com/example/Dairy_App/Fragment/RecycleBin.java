package com.example.Dairy_App.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.Dairy_App.Adapter.Firebasemodel;
import com.example.Dairy_App.Adapter.NotesAdapter;
import com.example.Dairy_App.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class RecycleBin extends Fragment {
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private NotesAdapter noteAdapter;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private View mView;


    public RecycleBin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            mView = inflater.inflate(R.layout.activity_recycle_bin, container, false);
            init(mView);

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            firebaseFirestore = FirebaseFirestore.getInstance();

            Query query = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").orderBy("title", Query.Direction.ASCENDING).whereEqualTo("isDeleted", "true");

            FirestoreRecyclerOptions<Firebasemodel> allusernotes = new FirestoreRecyclerOptions.Builder<Firebasemodel>().setQuery(query, Firebasemodel.class).build();
            noteAdapter = new NotesAdapter(getActivity(),allusernotes);

            recyclerView.setHasFixedSize(true);
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            recyclerView.setAdapter(noteAdapter);
            return mView;
        } catch (Exception e) {
            throw e;
        }
    }

    public void init(View mView){
        recyclerView = mView.findViewById(R.id.deleteRecyclerView);
    }

    @Override
    public void onStart() {
        noteAdapter.startListening();
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(noteAdapter!=null){
            noteAdapter.startListening();
        }
    }
}
