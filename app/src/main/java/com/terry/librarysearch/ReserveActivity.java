package com.terry.librarysearch;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
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

import static java.security.AccessController.getContext;

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

    private ActionBar actionBar;

    private String today;
    private int year, month, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        ButterKnife.bind(this);

        actionBar = getSupportActionBar();

        final TextView TextViewNewFont = new TextView(ReserveActivity.this);
        FrameLayout.LayoutParams layoutparams = new FrameLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        TextViewNewFont.setLayoutParams(layoutparams);
        TextViewNewFont.setText("세미나룸 예약");

        TextViewNewFont.setTextColor(getResources().getColor(R.color.colorWhite));
        TextViewNewFont.setTextSize(18);

        Typeface FontLoaderTypeface = Typeface.createFromAsset(getAssets(), getString(R.string.naum_square_bold));
        TextViewNewFont.setTypeface(FontLoaderTypeface);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(TextViewNewFont);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Initialize Date TextView
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(calendar.YEAR);
        month = calendar.get(calendar.MONTH);
        date = calendar.get(calendar.DATE);
        today = year + "-" + addZeroDate(month + 1) + "-" + addZeroDate(date);
        selectedDate.setText(today);

        placeSelect.setAdapter(new PlaceSpinnerAdapter(this, getResources().getStringArray(R.array.seminar_room)));
        placeSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(ReserveActivity.this, "Position : " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @OnClick(R.id.tv_selected_date)
    public void onClickSelectedDate() {
        DatePickerDialog dialog = new DatePickerDialog(this, listener, year, month, date);
        dialog.show();
    }

    @OnClick(R.id.ib_date)
    public void onClickDate() {
        DatePickerDialog dialog = new DatePickerDialog(this, listener, year, month, date);
        dialog.show();
    }

    @OnClick(R.id.nextButton)
    public void nextActivity() {
        // Next Button 눌렀을 경우
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                selectedDate.setText(selectedDateString);
                refreshRecyclerView();
            }
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
