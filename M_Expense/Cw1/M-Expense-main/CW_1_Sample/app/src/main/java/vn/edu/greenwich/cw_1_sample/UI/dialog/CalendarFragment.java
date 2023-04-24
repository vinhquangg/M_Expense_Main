package vn.edu.greenwich.cw_1_sample.UI.dialog;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import vn.edu.greenwich.cw_1_sample.R;

public class CalendarFragment extends DialogFragment {
    CalendarView fmCalendarCalendar;

    public CalendarFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        fmCalendarCalendar = view.findViewById(R.id.fmCalendarCalendar);
        fmCalendarCalendar.setOnDateChangeListener((calendarView, year, month, day) -> {
            String date = "";

            ++month;

            date += day < 10 ? "0" + day : day;
            date += "/";
            date += month < 10 ? "0" + month : month;
            date += "/";
            date += year;

            FragmentListener listener = (FragmentListener) getParentFragment();
            listener.sendFromCalendarFragment(date);

            dismiss();
        });

        return view;
    }

    public interface FragmentListener {
        void sendFromCalendarFragment(String date);
    }
}