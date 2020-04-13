package in.mastersaab.mastersaab.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import in.mastersaab.mastersaab.R;
import in.mastersaab.mastersaab.adapter.FirestoreAdapter;
import in.mastersaab.mastersaab.dataModel.ContentData;

public class DailyQuizFragment extends Fragment {

    View view;
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreAdapter firestorePagingAdapter;

    public DailyQuizFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_daily_quiz, container, false);

        recyclerView = view.findViewById(R.id.daily_quiz_recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fetch();

        return view;
    }

    public void fetch() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        Log.d("firebaseInstance", firebaseFirestore + " F3");


        //Query
        Query query = firebaseFirestore.collection("History");

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

        firestorePagingAdapter = new FirestoreAdapter(options);
        recyclerView.setAdapter(firestorePagingAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("state","activity start3");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("state","activity stop3");
    }
}
