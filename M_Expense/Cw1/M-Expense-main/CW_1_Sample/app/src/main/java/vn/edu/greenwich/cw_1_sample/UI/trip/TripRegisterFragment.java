package vn.edu.greenwich.cw_1_sample.UI.trip;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.switchmaterial.SwitchMaterial;
import vn.edu.greenwich.cw_1_sample.R;
import vn.edu.greenwich.cw_1_sample.database.ResimaDAO;
import vn.edu.greenwich.cw_1_sample.models.Trip;
import vn.edu.greenwich.cw_1_sample.UI.dialog.CalendarFragment;

public class TripRegisterFragment extends Fragment
        implements TripRegisterConfirmFragment.FragmentListener, CalendarFragment.FragmentListener {
    public static final String ARG_PARAM_TRIP = "trip";

    protected EditText fmTripRegisterName, fmTripRegisterStartDate, fmTripRegisterDestination, fmTripRegisterDescription;
    protected LinearLayout fmTripRegisterLinearLayout;
    protected SwitchMaterial fmTripRegisterOwner;
    protected TextView fmTripRegisterError;
    protected Button fmTripRegisterButton;

    protected ResimaDAO _db;

    public TripRegisterFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _db = new ResimaDAO(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_register, container, false);

        fmTripRegisterError = view.findViewById(R.id.fmTripRegisterError);
        fmTripRegisterName = view.findViewById(R.id.fmTripRegisterName);
        fmTripRegisterDestination = view.findViewById(R.id.fmTripRegisterDestination);
        fmTripRegisterDescription = view.findViewById(R.id.fmTripRegisterDescription);
        fmTripRegisterStartDate = view.findViewById(R.id.fmTripRegisterStartDate);
        fmTripRegisterOwner = view.findViewById(R.id.fmTripRegisterOwner);
        fmTripRegisterButton = view.findViewById(R.id.fmTripRegisterButton);
        fmTripRegisterLinearLayout = view.findViewById(R.id.fmTripRegisterLinearLayout);

        // Show Calendar for choosing a date.
        fmTripRegisterStartDate.setOnTouchListener((v, motionEvent) -> showCalendar(motionEvent));

        // Update current trip.
        if (getArguments() != null) {
            Trip trip = (Trip) getArguments().getSerializable(ARG_PARAM_TRIP);

            fmTripRegisterName.setText(trip.getName());
            fmTripRegisterStartDate.setText(trip.getStartDate());
            fmTripRegisterOwner.setChecked(trip.getOwner() == 1 ? true : false);
            fmTripRegisterDestination.setText(trip.getDestination());
            fmTripRegisterDescription.setText(trip.getDescription());

            fmTripRegisterButton.setText(R.string.label_update);
            fmTripRegisterButton.setOnClickListener(v -> update(trip.getId()));

            return view;
        }

        // Create new trip.
        fmTripRegisterButton.setOnClickListener(v -> register());

        return view;
    }

    protected void register() {
        if (isValidForm()) {
            Trip trip = getTripFromInput(-1);

            new TripRegisterConfirmFragment(trip).show(getChildFragmentManager(), null);

            return;
        }

//        moveButton();
    }

    protected void update(long id) {
        if (isValidForm()) {
            Trip trip = getTripFromInput(id);

            long status = _db.updateTrip(trip);

            FragmentListener listener = (FragmentListener) getParentFragment();
            listener.sendFromTripRegisterFragment(status);

            return;
        }

//        moveButton();
    }

    protected boolean showCalendar(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new CalendarFragment().show(getChildFragmentManager(), null);
        }

        return false;
    }

    protected Trip getTripFromInput(long id) {
        String name = fmTripRegisterName.getText().toString();
        String startDate = fmTripRegisterStartDate.getText().toString();
        int owner = fmTripRegisterOwner.isChecked() ? 1 : 0;
        String destination = fmTripRegisterDestination.getText().toString();
        String description = fmTripRegisterDescription.getText().toString();

        return new Trip(id, name, startDate, owner, destination, description);
    }

    protected boolean isValidForm() {
        boolean isValid = true;

        String error = "";
        String name = fmTripRegisterName.getText().toString();
        String startDate = fmTripRegisterStartDate.getText().toString();
        String destination = fmTripRegisterDestination.getText().toString();
        String description = fmTripRegisterDescription.getText().toString();

        if (name == null || name.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_name) + "\n";
            isValid = false;
        }

        if (destination == null || destination.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_destination) + "\n";
            isValid = false;
        }

        if (description == null || description.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_description) + "\n";
            isValid = false;
        }

        if (startDate == null || startDate.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_start_date) + "\n";
            isValid = false;
        }

        fmTripRegisterError.setText(error);

        return isValid;
    }

//    protected void moveButton() {
//        LinearLayout.LayoutParams btnParams = (LinearLayout.LayoutParams) fmTripRegisterButton.getLayoutParams();
//
//        int linearLayoutPaddingLeft = fmTripRegisterLinearLayout.getPaddingLeft();
//        int linearLayoutPaddingRight = fmTripRegisterLinearLayout.getPaddingRight();
//        int linearLayoutWidth = fmTripRegisterLinearLayout.getWidth() - linearLayoutPaddingLeft - linearLayoutPaddingRight;
//
//        btnParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
//        btnParams.topMargin += fmTripRegisterButton.getHeight();
//        btnParams.leftMargin = btnParams.leftMargin == 0 ? linearLayoutWidth - fmTripRegisterButton.getWidth() : 0;
//
//        fmTripRegisterButton.setLayoutParams(btnParams);
//    }

    @Override
    public void sendFromTripRegisterConfirmFragment(long status) {
        switch ((int) status) {
            case -1:
                Toast.makeText(getContext(), R.string.notification_create_fail, Toast.LENGTH_SHORT).show();
                return;

            default:
                Toast.makeText(getContext(), R.string.notification_create_success, Toast.LENGTH_SHORT).show();

                fmTripRegisterName.setText("");
                fmTripRegisterStartDate.setText("");
                fmTripRegisterDestination.setText("");
                fmTripRegisterDescription.setText("");
                fmTripRegisterName.requestFocus();
        }
    }

    @Override
    public void sendFromCalendarFragment(String date) {
        fmTripRegisterStartDate.setText(date);
    }

    public interface FragmentListener {
        void sendFromTripRegisterFragment(long status);
    }
}