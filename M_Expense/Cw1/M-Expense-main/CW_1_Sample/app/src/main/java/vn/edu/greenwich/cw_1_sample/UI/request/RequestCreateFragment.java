package vn.edu.greenwich.cw_1_sample.UI.request;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import vn.edu.greenwich.cw_1_sample.R;
import vn.edu.greenwich.cw_1_sample.models.Request;
import vn.edu.greenwich.cw_1_sample.UI.dialog.DatePickerFragment;
import vn.edu.greenwich.cw_1_sample.UI.dialog.TimePickerFragment;

public class RequestCreateFragment extends DialogFragment
        implements DatePickerFragment.FragmentListener, TimePickerFragment.FragmentListener {
    protected long _tripId;

    protected EditText fmRequestCreateDate, fmRequestCreateTime, fmRequestCreateContent, fmRequestCreateAmount;
    protected Button fmRequestCreateButtonCancel, fmRequestCreateButtonAdd;
    protected Spinner fmRequestCreateType;

    public RequestCreateFragment() {
        _tripId = -1;
    }

    public RequestCreateFragment(long tripId) {
        _tripId = tripId;
    }

    @Override
    public void sendFromDatePickerFragment(String date) {
        fmRequestCreateDate.setText(date);
    }

    @Override
    public void sendFromTimePickerFragment(String time) {
        fmRequestCreateTime.setText(time);
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
        View view = inflater.inflate(R.layout.fragment_request_create, container, false);

        fmRequestCreateAmount = view.findViewById(R.id.fmRequestCreateAmount);
        fmRequestCreateDate = view.findViewById(R.id.fmRequestCreateDate);
        fmRequestCreateTime = view.findViewById(R.id.fmRequestCreateTime);
        fmRequestCreateContent = view.findViewById(R.id.fmRequestCreateContent);
        fmRequestCreateButtonCancel = view.findViewById(R.id.fmRequestCreateButtonCancel);
        fmRequestCreateButtonAdd = view.findViewById(R.id.fmRequestCreateButtonAdd);
        fmRequestCreateType = view.findViewById(R.id.fmRequestCreateType);

        fmRequestCreateButtonCancel.setOnClickListener(v -> dismiss());
        fmRequestCreateButtonAdd.setOnClickListener(v -> createRequest());

        fmRequestCreateDate.setOnTouchListener((v, motionEvent) -> showDateDialog(motionEvent));
        fmRequestCreateTime.setOnTouchListener((v, motionEvent) -> showTimeDialog(motionEvent));

        setTypeSpinner();

        return view;
    }

    protected void setTypeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.request_type,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fmRequestCreateType.setAdapter(adapter);
    }

    protected boolean showDateDialog(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new DatePickerFragment().show(getChildFragmentManager(), null);
            return true;
        }

        return false;
    }

    protected boolean showTimeDialog(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new TimePickerFragment().show(getChildFragmentManager(), null);
            return true;
        }

        return false;
    }

    protected void createRequest() {
        Request request = new Request();

        request.setType(fmRequestCreateType.getSelectedItem().toString());
        request.setAmount(fmRequestCreateAmount.getText().toString());
        request.setTime(fmRequestCreateTime.getText().toString());
        request.setDate(fmRequestCreateDate.getText().toString());
        request.setContent(fmRequestCreateContent.getText().toString());

        FragmentListener listener = (FragmentListener) getParentFragment();
        listener.sendFromRequestCreateFragment(request);

        dismiss();
    }

    public interface FragmentListener {
        void sendFromRequestCreateFragment(Request request);
    }
}