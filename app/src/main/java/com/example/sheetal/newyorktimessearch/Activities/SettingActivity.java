package com.example.sheetal.newyorktimessearch.Activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.sheetal.newyorktimessearch.Fragments.DatePickerFragment;
import com.example.sheetal.newyorktimessearch.Models.SettingData;
import com.example.sheetal.newyorktimessearch.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.etDatePicker) EditText etDatePicker;
    @BindView(R.id.btnSave) Button btnSave;
    @BindView(R.id.spinSortOrder) Spinner spinSortOrder;
    @BindView(R.id.checkbox_arts)CheckBox chkArts;
    @BindView(R.id.checkbox_fashtionStyle) CheckBox chkFashionStyle;
    @BindView(R.id.checkbox_sports) CheckBox chkSports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        etDatePicker.setText(dateFormat.format(cal.getTime()));  //set today's date as default

        SettingData filterData = SettingData.getInstance();

        if(filterData !=null)
        {
            Log.d("DEBUG", "filte rData Received begin date:" + filterData.getBeginDate());
            etDatePicker.setText(filterData.getBeginDate());

            spinSortOrder.setSelection(filterData.getSortOrderPosition());

            if(filterData.isCheckedArts() !=null)
                chkArts.setChecked(true);
            else
                chkArts.setChecked(false);


            if(filterData.isCheckedFashionStyle() !=null)
                chkFashionStyle.setChecked(true);
            else
                chkFashionStyle.setChecked(false);

            if(filterData.isCheckedSports() !=null)
                chkSports.setChecked(true);
            else
                chkSports.setChecked(false);

        }



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String beginDate = etDatePicker.getText().toString().trim();

                int sortOrderPosition = spinSortOrder.getSelectedItemPosition();
                boolean isCheckedArts = chkArts.isChecked();
                boolean isCheckedFashionStyle = chkFashionStyle.isChecked();
                boolean isCheckedSports = chkSports.isChecked();

                SettingData filterDataToSend = SettingData.getInstance();

                filterDataToSend.filterSetData(  beginDate
                                                ,sortOrderPosition
                                                ,isCheckedArts
                                                ,isCheckedFashionStyle
                                                ,isCheckedSports);

                setResult(RESULT_OK, null);

                //return to parent activity
                finish();
            }
        });

    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        //store the values stored in a calendar instance

        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,monthOfYear);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = df.format(c.getTime());
        etDatePicker.setText(formattedDate);

    }

    //attach to onclick handler to show the date picker

    public  void showDatePicketDialog(View view)
    {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker");

    }


}
