package com.terry.librarysearch;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.terry.librarysearch.CustomView.CustomFontButton;
import com.terry.librarysearch.CustomView.CustomFontTextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReserveActivity extends AppCompatActivity {

    private static String TAG = ReserveActivity.class.getSimpleName();

    @BindView(R.id.place_select)
    Spinner placeSelect;
    @BindView(R.id.tv_selected_date)
    CustomFontTextView selectedDate;
    @BindView(R.id.ib_date)
    ImageButton ibDate;
    @BindView(R.id.nextButton)
    CustomFontButton nextButton;

    private String today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.ib_date)
    public void showDateSelectDialog() {
        // Date Select 눌렀을 경우
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int date = calendar.get(calendar.DATE);
        today = year + "-" + addZeroDate(month + 1) + "-" + addZeroDate(date);

        DatePickerDialog dialog = new DatePickerDialog(this, listener, year, month, date);
        dialog.show();
    }

    @OnClick(R.id.nextButton)
    public void nextActivity() {
        // Next Button 눌렀을 경우
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String selectedDateString = year + "-" + addZeroDate(monthOfYear + 1) + "-" + addZeroDate(dayOfMonth);

            if (today.compareTo(selectedDateString) > 0) {
                // 오늘 이전 날짜일 경우
                AlertDialog builder = new AlertDialog.Builder(ReserveActivity.this)
                        .setMessage("오늘 날짜부터 예약이 가능합니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedDate.setText(today);
                            }
                        }).setCancelable(false).show();

                TextView textView = (TextView) builder.findViewById(android.R.id.message);
                TextView textView2 = (TextView) builder.findViewById(android.R.id.button1);
                Typeface face = Typeface.createFromAsset(getAssets(), getString(R.string.naum_square_bold));
                textView.setTypeface(face);
                textView2.setTypeface(face);
            } else {
                // 오늘 날짜와 같거나 오늘 이후 날짜의 경우
                refreshRecyclerView();
            }
            selectedDate.setText(selectedDateString);
        }
    };

    private String addZeroDate(int num) {
        if (num < 10) {
            return "0" + num;
        } else {
            return "" + num;
        }
    }

    private void refreshRecyclerView() {
        // 리스트 받아와서 적용하는 부분
    }
}
