package br.com.dekatrian.calendar.dekatrian_calendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SimpleCalendar simpleCalendar = findViewById(R.id.square_day);

        //simpleCalendar.setUserCurrentMonthYear(1, 2018);

        simpleCalendar.setCallBack(new SimpleCalendar.DayClickListener() {
            @Override
            public void onDayClick(View view) {

            }
        });
    }
}
