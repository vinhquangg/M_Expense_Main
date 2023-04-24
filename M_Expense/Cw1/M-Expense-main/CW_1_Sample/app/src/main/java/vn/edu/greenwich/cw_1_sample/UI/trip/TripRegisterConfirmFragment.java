package vn.edu.greenwich.cw_1_sample.UI.trip;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import vn.edu.greenwich.cw_1_sample.R;
import vn.edu.greenwich.cw_1_sample.database.ResimaDAO;
import vn.edu.greenwich.cw_1_sample.models.Trip;

public class TripRegisterConfirmFragment extends DialogFragment {
    protected ResimaDAO _db;
    protected Trip _trip;
    protected Button fmTripRegisterConfirmButtonConfirm, fmTripRegisterConfirmButtonCancel;
    protected TextView fmTripRegisterConfirmName, fmTripRegisterConfirmStartDate, fmTripRegisterConfirmOwner, fmTripRegisterConfirmDestination, fmTripRegisterConfirmDescription;

    public TripRegisterConfirmFragment() {
        _trip = new Trip();
    }

    public TripRegisterConfirmFragment(Trip trip) {
        _trip = trip;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _db = new ResimaDAO(getContext());
    }

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
        View view = inflater.inflate(R.layout.fragment_trip_register_confirm, container, false);

        String name = getString(R.string.error_no_info);
        String destination = getString(R.string.error_no_info);
        String description = getString(R.string.error_no_info);
        String startDate = getString(R.string.error_no_info);
        String ownerType = getString(R.string.error_no_info);

        fmTripRegisterConfirmName = view.findViewById(R.id.fmTripRegisterConfirmName);
        fmTripRegisterConfirmDestination = view.findViewById(R.id.fmTripRegisterConfirmDestination);
        fmTripRegisterConfirmDescription = view.findViewById(R.id.fmTripRegisterConfirmDescription);
        fmTripRegisterConfirmStartDate = view.findViewById(R.id.fmTripRegisterConfirmStartDate);
        fmTripRegisterConfirmOwner = view.findViewById(R.id.fmTripRegisterConfirmOwner);
        fmTripRegisterConfirmButtonCancel = view.findViewById(R.id.fmTripRegisterConfirmButtonCancel);
        fmTripRegisterConfirmButtonConfirm = view.findViewById(R.id.fmTripRegisterConfirmButtonConfirm);

        if (_trip.getOwner() != -1) {
            ownerType = _trip.getOwner() == 1 ? getString(R.string.label_owner) : getString(R.string.label_tenant);
        }

        if (_trip.getName() != null && !_trip.getName().trim().isEmpty()) {
            name = _trip.getName();
        }

        if (_trip.getDestination() != null && !_trip.getDestination().trim().isEmpty()) {
            destination = _trip.getDestination();
        }

        if (_trip.getDescription() != null && !_trip.getDescription().trim().isEmpty()) {
            description = _trip.getDescription();
        }

        if (_trip.getStartDate() != null && !_trip.getStartDate().trim().isEmpty()) {
            startDate = _trip.getStartDate();
        }


        fmTripRegisterConfirmName.setText(name);
        fmTripRegisterConfirmDestination.setText(destination);
        fmTripRegisterConfirmDescription.setText(description);
        fmTripRegisterConfirmStartDate.setText(startDate);
        fmTripRegisterConfirmOwner.setText(ownerType);

        fmTripRegisterConfirmButtonCancel.setOnClickListener(v -> dismiss());
        fmTripRegisterConfirmButtonConfirm.setOnClickListener(v -> confirm());

        return view;
    }

    protected void confirm() {
        long status = _db.insertTrip(_trip);

        FragmentListener listener = (FragmentListener) getParentFragment();
        listener.sendFromTripRegisterConfirmFragment(status);

        dismiss();
    }

    public interface FragmentListener {
        void sendFromTripRegisterConfirmFragment(long status);
    }
}