package com.terry.librarysearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;

import com.terry.librarysearch.CustomView.CustomFontEditText;
import com.terry.librarysearch.CustomView.CustomFontTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReserveTwoActivity extends AppCompatActivity {

    @BindView(R.id.tv_detail_place)
    CustomFontTextView tvDetailPlace;
    @BindView(R.id.tv_detail_name)
    CustomFontTextView tvDetailName;
    @BindView(R.id.tv_detail_id)
    CustomFontTextView tvDetailId;
    @BindView(R.id.tv_detail_phone)
    CustomFontTextView tvDetailPhone;
    @BindView(R.id.tv_detail_date)
    CustomFontTextView tvDetailDate;
    @BindView(R.id.tv_detail_time)
    CustomFontTextView tvDetailTime;
    @BindView(R.id.et_dongban_id)
    CustomFontEditText etDongbanId;
    @BindView(R.id.et_dongban_name)
    CustomFontEditText etDongbanName;
    @BindView(R.id.tv_dongban_list)
    CustomFontTextView tvDongbanList;
    @BindView(R.id.tv_bipoom_name_detail)
    CustomFontTextView tvBipoomNameDetail;
    @BindView(R.id.et_usage_perpose)
    CustomFontEditText etUsagePerpose;
    @BindView(R.id.switch_bipoom)
    Switch switchBipoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_two);
        ButterKnife.bind(this);

        loadData();
    }

    private void loadData() {

    }

    @OnClick(R.id.switch_bipoom)
    public void onSwitchClick() {
        switchBipoom.setChecked(!switchBipoom.isChecked());
    }
}
