package vn.edu.greenwich.cw_1_sample.UI.trip;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import vn.edu.greenwich.cw_1_sample.R;
import vn.edu.greenwich.cw_1_sample.models.Trip;
import vn.edu.greenwich.cw_1_sample.UI.dialog.CalendarFragment;

public class TripSearchFragment extends DialogFragment implements CalendarFragment.FragmentListener {
    protected EditText fmTripSearchDate, fmTripSearchName;
    protected Button fmTripSearchButtonCancel, fmTripSearchButtonSearch;

    public TripSearchFragment() {}

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_search, container, false);

        fmTripSearchDate = view.findViewById(R.id.fmTripSearchDate);
        fmTripSearchName = view.findViewById(R.id.fmTripSearchName);
        fmTripSearchButtonCancel = view.findViewById(R.id.fmTripSearchButtonCancel);
        fmTripSearchButtonSearch = view.findViewById(R.id.fmTripSearchButtonSearch);

        fmTripSearchButtonSearch.setOnClickListener(v -> search());
        fmTripSearchButtonCancel.setOnClickListener(v -> dismiss());
        fmTripSearchDate.setOnTouchListener((v, motionEvent) -> showCalendar(motionEvent));

        return view;
    }

    protected void search() {
        Trip _trip = new Trip();

        String date = fmTripSearchDate.getText().toString();
        String name = fmTripSearchName.getText().toString();

        if (date != null && !date.trim().isEmpty())
            _trip.setStartDate(date);

        if (name != null && !name.trim().isEmpty())
            _trip.setName(name);

        FragmentListener listener = (FragmentListener) getParentFragment();
        listener.sendFromResidentSearchFragment(_trip);

        dismiss();
    }

    protected boolean showCalendar(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new CalendarFragment().show(getChildFragmentManager(), null);
        }

        return false;
    }

    @Override
    public void sendFromCalendarFragment(String date) {
        fmTripSearchDate.setText(date);
    }

    public interface FragmentListener {
        void sendFromResidentSearchFragment(Trip resident);
    }
}