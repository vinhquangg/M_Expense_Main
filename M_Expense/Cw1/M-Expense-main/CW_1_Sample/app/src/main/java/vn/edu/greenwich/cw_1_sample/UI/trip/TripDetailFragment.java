package vn.edu.greenwich.cw_1_sample.UI.trip;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomappbar.BottomAppBar;
import vn.edu.greenwich.cw_1_sample.R;
import vn.edu.greenwich.cw_1_sample.database.ResimaDAO;
import vn.edu.greenwich.cw_1_sample.models.Request;
import vn.edu.greenwich.cw_1_sample.models.Trip;
import vn.edu.greenwich.cw_1_sample.UI.dialog.DeleteConfirmFragment;
import vn.edu.greenwich.cw_1_sample.UI.request.RequestCreateFragment;
import vn.edu.greenwich.cw_1_sample.UI.request.list.RequestListFragment;

public class TripDetailFragment extends Fragment
        implements DeleteConfirmFragment.FragmentListener, RequestCreateFragment.FragmentListener {
    public static final String ARG_PARAM_TRIP = "trip";

    protected ResimaDAO _db;
    protected Trip _trip;
    protected Button fmTripDetailRequestButton;
    protected BottomAppBar fmTripDetailBottomAppBar;
    protected FragmentContainerView fmTripDetailRequestList;
    protected TextView fmTripDetailName, fmTripDetailStartDate, fmTripDetailOwner,fmTripDetailDestination, fmTripDetailDescription;

    public TripDetailFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _db = new ResimaDAO(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_detail, container, false);

        fmTripDetailName = view.findViewById(R.id.fmTripDetailName);
        fmTripDetailDestination = view.findViewById(R.id.fmTripDetailDestination);
        fmTripDetailDescription = view.findViewById(R.id.fmTripDetailDescription);
        fmTripDetailStartDate = view.findViewById(R.id.fmTripDetailStartDate);
        fmTripDetailOwner = view.findViewById(R.id.fmTripDetailOwner);
        fmTripDetailBottomAppBar = view.findViewById(R.id.fmTripDetailBottomAppBar);
        fmTripDetailRequestButton = view.findViewById(R.id.fmTripDetailRequestButton);
        fmTripDetailRequestList = view.findViewById(R.id.fmTripDetailRequestList);

        fmTripDetailBottomAppBar.setOnMenuItemClickListener(item -> menuItemSelected(item));
        fmTripDetailRequestButton.setOnClickListener(v -> showAddRequestFragment());

        showDetails();
        showRequestList();

        return view;
    }

    protected void showDetails() {
        String name = getString(R.string.error_not_found);
        String destination = getString(R.string.error_not_found);
        String descriptions = getString(R.string.error_not_found);
        String startDate = getString(R.string.error_not_found);
        String owner = getString(R.string.error_not_found);

        if (getArguments() != null) {
            _trip = (Trip) getArguments().getSerializable(ARG_PARAM_TRIP);
            _trip = _db.getTripById(_trip.getId()); // Retrieve data from Database.

            name = _trip.getName();
            destination = _trip.getDestination();
            descriptions = _trip.getDescription();
            startDate = _trip.getStartDate();
            owner = _trip.getOwner() == 1 ? getString(R.string.label_owner) : getString(R.string.label_tenant);
        }

        fmTripDetailName.setText(name);
        fmTripDetailDestination.setText(destination);
        fmTripDetailDescription.setText(descriptions);
        fmTripDetailStartDate.setText(startDate);
        fmTripDetailOwner.setText(owner);
    }

    protected void showRequestList() {
        if (getArguments() != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(RequestListFragment.ARG_PARAM_TRIP_ID, _trip.getId());

            // Send arguments (trip id) to RequestListFragment.
            getChildFragmentManager().getFragments().get(0).setArguments(bundle);
        }
    }

    protected boolean menuItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tripUpdateFragment:
                showUpdateFragment();
                return true;

            case R.id.tripDeleteFragment:
                showDeleteConfirmFragment();
                return true;
        }

        return true;
    }

    protected void showUpdateFragment() {
        Bundle bundle = null;

        if (_trip != null) {
            bundle = new Bundle();
            bundle.putSerializable(TripUpdateFragment.ARG_PARAM_TRIP, _trip);
        }

        Navigation.findNavController(getView()).navigate(R.id.tripUpdateFragment, bundle);
    }

    protected void showDeleteConfirmFragment() {
        new DeleteConfirmFragment(getString(R.string.notification_delete_confirm)).show(getChildFragmentManager(), null);
    }

    protected void showAddRequestFragment() {
        new RequestCreateFragment(_trip.getId()).show(getChildFragmentManager(), null);
    }

    @Override
    public void sendFromDeleteConfirmFragment(int status) {
        if (status == 1 && _trip != null) {
            long numOfDeletedRows = _db.deleteTrip(_trip.getId());

            if (numOfDeletedRows > 0) {
                Toast.makeText(getContext(), R.string.notification_delete_success, Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getView()).navigateUp();

                return;
            }
        }

        Toast.makeText(getContext(), R.string.notification_delete_fail, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendFromRequestCreateFragment(Request request) {
        if (request != null) {
            request.setTripId(_trip.getId());

            long id = _db.insertRequest(request);

            Toast.makeText(getContext(), id == -1 ? R.string.notification_create_fail : R.string.notification_create_success, Toast.LENGTH_SHORT).show();

            reloadRequestList();
        }
    }

    protected void reloadRequestList() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(RequestListFragment.ARG_PARAM_TRIP_ID, _trip.getId());

        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fmTripDetailRequestList, RequestListFragment.class, bundle)
                .commit();
    }
}