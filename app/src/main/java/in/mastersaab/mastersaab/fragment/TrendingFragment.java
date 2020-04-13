package in.mastersaab.mastersaab.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import in.mastersaab.mastersaab.R;
import in.mastersaab.mastersaab.dataModel.ContentData;
import in.mastersaab.mastersaab.adapter.FirestoreAdapter;

public class TrendingFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private String collection = "default";


    public TrendingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_latest, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.latest_recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Bundle bundle = getArguments();
        if (getArguments() != null) {
            collection = bundle.getString("collection");
            Log.d("collection",collection);
        }
        firebaseFirestore = FirebaseFirestore.getInstance();
        Query query;

        switch (collection){
            case "environment":
                query = firebaseFirestore.collection("Enviorment");
                break;
            default:
                query = firebaseFirestore.collection("Content");

        }

        fetchData(query);
    }

    public void fetchData(Query query) {
        Log.d("firebaseInstance", firebaseFirestore + " F1");

        //Pagination
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(3)
                .build();

        //RecyclerOption
        FirestorePagingOptions<ContentData> options = new FirestorePagingOptions.Builder<ContentData>()
                .setLifecycleOwner(this)
                .setQuery(query, config, ContentData.class)
                .build();

        FirestoreAdapter firestorePagingAdapter = new FirestoreAdapter(options);
        recyclerView.setAdapter(firestorePagingAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("state","activity start1");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("state","activity stop1");
    }
}