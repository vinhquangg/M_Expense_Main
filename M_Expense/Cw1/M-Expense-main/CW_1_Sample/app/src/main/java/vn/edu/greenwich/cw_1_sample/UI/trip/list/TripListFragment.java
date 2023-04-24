package vn.edu.greenwich.cw_1_sample.UI.trip.list;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;
import vn.edu.greenwich.cw_1_sample.R;
import vn.edu.greenwich.cw_1_sample.UI.trip.TripSearchFragment;
import vn.edu.greenwich.cw_1_sample.database.ResimaDAO;
import vn.edu.greenwich.cw_1_sample.models.Trip;

public class TripListFragment extends Fragment implements TripSearchFragment.FragmentListener {
    protected ArrayList<Trip> tripList = new ArrayList<>();

    protected ResimaDAO _db;
    protected EditText fmTripListFilter;
    protected TripAdapter tripAdapter;
    protected TextView fmTripListEmptyNotice;
    protected RecyclerView fmTripListRecylerView;
    protected ImageButton fmTripListButtonSearch, fmTripListButtonResetSearch;

    public TripListFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        _db = new ResimaDAO(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_list, container, false);

        fmTripListRecylerView = view.findViewById(R.id.fmTripListRecylerView);
        fmTripListEmptyNotice = view.findViewById(R.id.fmTripListEmptyNotice);

        fmTripListButtonResetSearch = view.findViewById(R.id.fmTripListButtonResetSearch);
        fmTripListButtonResetSearch.setOnClickListener(v -> resetSearch());

        fmTripListButtonSearch = view.findViewById(R.id.fmTripListButtonSearch);
        fmTripListButtonSearch.setOnClickListener(v -> showSearchDialog());

        fmTripListFilter = view.findViewById(R.id.fmTripListFilter);
        fmTripListFilter.addTextChangedListener(filter());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());

        tripAdapter = new TripAdapter(tripList);

        fmTripListRecylerView.addItemDecoration(dividerItemDecoration);
        fmTripListRecylerView.setAdapter(tripAdapter);
        fmTripListRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        reloadList(null);
    }

    protected void reloadList(Trip trip) {
        tripList = _db.getTripList(trip, null, false);
        tripAdapter.updateList(tripList);

        // Show "No Trip." message.
        fmTripListEmptyNotice.setVisibility(tripList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    protected TextWatcher filter() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                tripAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }

    protected void resetSearch() {
        fmTripListFilter.setText("");
        reloadList(null);
    }

    protected void showSearchDialog() {
        new TripSearchFragment().show(getChildFragmentManager(), null);
    }

    @Override
    public void sendFromResidentSearchFragment(Trip resident) {
        if (!resident.isEmpty()) {
            reloadList(resident);
            return;
        }

        reloadList(null);
    }
}