package vn.edu.greenwich.cw_1_sample.UI.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    public TimePickerFragment() {}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        String time = "";

        time += hour < 10 ? "0" + hour : hour;
        time += ":";
        time += minute < 10 ? "0" + minute : minute;

        TimePickerFragment.FragmentListener listener = (TimePickerFragment.FragmentListener) getParentFragment();
        listener.sendFromTimePickerFragment(time);

        dismiss();
    }

    public interface FragmentListener {
        void sendFromTimePickerFragment(String time);
    }
}