package br.com.dekatrian.calendar.dekatrian_calendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class SimpleCalendar extends LinearLayout {

    private LinearLayout mCalendarWeekOne, mCalendarWeekTwo, mCalendarWeekThree;
    private LinearLayout mCalendarWeekFour, mCalendarWeekFive;

    private DekatrianCalendar dekatrianCalendar = new DekatrianCalendar();
    private String dekaCalendarDate;

    private Button selectedDayButton;
    private Button[] days;
    private LinearLayout[] weeks;

    private static final String CUSTOM_GREY = "#a0a0a0";
    private static final String ACHRONIAN = "Achronian";
    private static final String SINCHRONIAN = "Sinchronian";
    private static final String[] DEKA_MONTH_NAMES = {"", "Autoran", "Borean", "Coronian",
            "Driadan", "Electran", "Faian", "Gaian", "Hermetian",
            "Irisian", "Kaosian", "Lunan", "Maian", "Nixian"};

    private TextView currentDate, currentMonth, fullDate, gregDate;

    private int currentDateDay, chosenDateDay, currentDateMonth,
            chosenDateMonth, currentDateYear, chosenDateYear,
            pickedDateDay, pickedDateMonth, pickedDateYear;
    int userMonth, userYear;
    private DayClickListener mListener;
    private Drawable userDrawable;

    private Calendar calendar;
    private LinearLayout.LayoutParams defaultButtonParams;
    private LinearLayout.LayoutParams userButtonParams;

    public SimpleCalendar(Context context) {
        super(context);
        init(context);
    }

    public SimpleCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SimpleCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    /*@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SimpleCalendar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }*/

    private void init(Context context) {

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        View view = LayoutInflater.from(context).inflate(R.layout.simple_calendar, this, true);
        calendar = Calendar.getInstance();

        //setUserCurrentMonthYear(0,0);

        mCalendarWeekOne = findViewById(R.id.calendar_week_1);
        mCalendarWeekTwo = findViewById(R.id.calendar_week_2);
        mCalendarWeekThree = findViewById(R.id.calendar_week_3);
        mCalendarWeekFour = findViewById(R.id.calendar_week_4);
        mCalendarWeekFive = findViewById(R.id.calendar_week_5);
        currentDate = findViewById(R.id.current_date);
        currentMonth = findViewById(R.id.current_month);
        fullDate = findViewById(R.id.full_date);
        gregDate = findViewById(R.id.greg_date);

        currentDateDay = chosenDateDay = dekatrianCalendar.currentDekaDayOfMonth;

        if (userMonth != 0 && userYear != 0) {
            currentDateMonth = chosenDateMonth = userMonth;
            currentDateYear = chosenDateYear = userYear;
        } else if (userMonth == 0 && userYear != 0) {
            currentDateMonth = chosenDateMonth = userMonth;
            currentDateYear = chosenDateYear = userYear;
            //currentDateDay = chosenDateDay = 1;
            if(dekatrianCalendar.isLeapYear(currentDateYear)){
                if(currentDateDay != 2)
                    currentDateDay = chosenDateDay = 1;
            } else {
                currentDateDay = chosenDateDay = 1;
            }
        } else {
            currentDateMonth = chosenDateMonth = dekatrianCalendar.currentDekaMonth;
            currentDateYear = chosenDateYear = dekatrianCalendar.currentDekaYear;
        }

        initializeDaysWeeks();
        if (userButtonParams != null) {
            defaultButtonParams = userButtonParams;
        } else {
            defaultButtonParams = getDaysLayoutParams();
        }
        addDaysinCalendar(defaultButtonParams, context, metrics);

        String chosenDate = chosenDateDay+"-"+chosenDateMonth+"-"+chosenDateYear;

        if(dekatrianCalendar.isAchrorian(chosenDate)) {
            currentDate.setText("");
            currentMonth.setText(ACHRONIAN);
        } else if(dekatrianCalendar.isSinchronian(chosenDate)) {
            currentDate.setText("");
            currentMonth.setText(SINCHRONIAN);
        } else {
            currentDate.setText(""+chosenDateDay);
            currentMonth.setText(""+DEKA_MONTH_NAMES[chosenDateMonth]);
        }

        fullDate.setText(""+chosenDateYear);

        gregDate.setText(dekatrianCalendar.dekaToGreg(chosenDate).replace("-","/"));

        initCalendarWithDate(chosenDateYear, chosenDateMonth, chosenDateDay);

    }

    private void initializeDaysWeeks() {
        weeks = new LinearLayout[5];
        days = new Button[5 * 7];

        weeks[0] = mCalendarWeekOne;
        weeks[1] = mCalendarWeekTwo;
        weeks[2] = mCalendarWeekThree;
        weeks[3] = mCalendarWeekFour;
        weeks[4] = mCalendarWeekFive;
    }

    private void initCalendarWithDate(int year, int month, int day) {
        day = 1;
        dekaCalendarDate = day + "-" + month + "-" + year;

        chosenDateYear = year;
        chosenDateMonth = month;
        chosenDateDay = day;

        int daysInCurrentMonth = dekatrianCalendar.getDaysInMonth(dekaCalendarDate);

        int firstDayOfCurrentMonth = dekatrianCalendar.getDayOfWeek("1-"+month+"-"+year);

        int dayNumber = 1;
        int daysLeftInFirstWeek = 0;
        int indexOfDayAfterLastDayOfMonth;

        if (firstDayOfCurrentMonth != 0) {
            daysLeftInFirstWeek = firstDayOfCurrentMonth;
            indexOfDayAfterLastDayOfMonth = daysLeftInFirstWeek + daysInCurrentMonth;
            for (int i = firstDayOfCurrentMonth; i < firstDayOfCurrentMonth + daysInCurrentMonth; ++i) {
                if (currentDateMonth == chosenDateMonth
                        && currentDateYear == chosenDateYear
                        && dayNumber == currentDateDay) {
                    days[i].setBackgroundColor(getResources().getColor(R.color.pink));
                    days[i].setTextColor(Color.WHITE);
                } else {
                    days[i].setTextColor(Color.BLACK);
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                int[] dateArr = new int[3];
                dateArr[0] = dayNumber;
                dateArr[1] = chosenDateMonth;
                dateArr[2] = chosenDateYear;
                days[i].setTag(dateArr);
                days[i].setText(String.valueOf(dayNumber));

                days[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDayClick(v);
                    }
                });
                ++dayNumber;
            }
        } else {
            indexOfDayAfterLastDayOfMonth = daysInCurrentMonth;
            for (int i = 0; i < daysInCurrentMonth; ++i) {
                if (currentDateMonth == chosenDateMonth
                        && currentDateYear == chosenDateYear
                        && dayNumber == currentDateDay) {
                    days[i].setBackgroundColor(getResources().getColor(R.color.pink));
                    days[i].setTextColor(Color.WHITE);
                } else {
                    days[i].setTextColor(Color.BLACK);
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                int[] dateArr = new int[3];
                dateArr[0] = dayNumber;
                dateArr[1] = chosenDateMonth;
                dateArr[2] = chosenDateYear;
                days[i].setTag(dateArr);
                days[i].setText(String.valueOf(dayNumber));

                days[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDayClick(v);
                    }
                });
                ++dayNumber;
            }
        }

        if (month > 0)
            dekaCalendarDate = day + "-" + (month - 1) + "-" + year;
        else
            dekaCalendarDate = day + "-" + 13 + "-" + year;
        int daysInPreviousMonth = dekatrianCalendar.getDaysInMonth(dekaCalendarDate);

        for (int i = daysLeftInFirstWeek - 1; i >= 0 && daysInPreviousMonth > 0; --i) {
            int[] dateArr = new int[3];

            if (chosenDateMonth > 0) {
                if (currentDateMonth == chosenDateMonth - 1
                        && currentDateYear == chosenDateYear
                        && daysInPreviousMonth == currentDateDay) {
                } else {
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                dateArr[0] = daysInPreviousMonth;
                dateArr[1] = chosenDateMonth - 1;
                dateArr[2] = chosenDateYear;
            } else {
                if (currentDateMonth == 13
                        && currentDateYear == chosenDateYear - 1
                        && daysInPreviousMonth == currentDateDay) {
                } else {
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                dateArr[0] = daysInPreviousMonth;
                dateArr[1] = 13;
                dateArr[2] = chosenDateYear - 1;
            }

            days[i].setTag(dateArr);
            days[i].setText(String.valueOf(daysInPreviousMonth--));
            days[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDayClick(v);
                }
            });
        }

        int nextMonthDaysCounter = 1;
        for (int i = indexOfDayAfterLastDayOfMonth; i < days.length && i < indexOfDayAfterLastDayOfMonth + 28; ++i) {
            int[] dateArr = new int[3];

            if (chosenDateMonth < 13) {
                if (currentDateMonth == chosenDateMonth + 1
                        && currentDateYear == chosenDateYear
                        && nextMonthDaysCounter == currentDateDay) {
                    days[i].setBackgroundColor(getResources().getColor(R.color.pink));
                } else {
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                dateArr[0] = nextMonthDaysCounter;
                dateArr[1] = chosenDateMonth + 1;
                dateArr[2] = chosenDateYear;
            } else {
                if (currentDateMonth == 0
                        && currentDateYear == chosenDateYear + 1
                        && nextMonthDaysCounter == currentDateDay) {
                    days[i].setBackgroundColor(getResources().getColor(R.color.pink));
                } else {
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                dateArr[0] = nextMonthDaysCounter;
                dateArr[1] = 0;
                dateArr[2] = chosenDateYear + 1;
            }

            days[i].setTag(dateArr);
            days[i].setTextColor(Color.parseColor(CUSTOM_GREY));
            days[i].setText(String.valueOf(nextMonthDaysCounter++));
            days[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDayClick(v);
                }
            });
        }

        calendar.set(chosenDateYear, chosenDateMonth, chosenDateDay);
    }

    public void onDayClick(View view) {
        mListener.onDayClick(view);

        if (selectedDayButton != null) {
            if (chosenDateYear == currentDateYear
                    && chosenDateMonth == currentDateMonth
                    && pickedDateDay == currentDateDay) {
                selectedDayButton.setBackgroundColor(getResources().getColor(R.color.pink));
                selectedDayButton.setTextColor(Color.WHITE);
            } else {
                selectedDayButton.setBackgroundColor(Color.TRANSPARENT);
                if (selectedDayButton.getCurrentTextColor() != Color.RED) {
                    selectedDayButton.setTextColor(getResources()
                            .getColor(R.color.calendar_number));
                }
            }
        }

        selectedDayButton = (Button) view;
        if (selectedDayButton.getTag() != null) {
            int[] dateArray = (int[]) selectedDayButton.getTag();
            pickedDateDay = dateArray[0];
            pickedDateMonth = dateArray[1];
            pickedDateYear = dateArray[2];
        }

        if (pickedDateYear == currentDateYear
                && pickedDateMonth == currentDateMonth
                && pickedDateDay == currentDateDay) {
            selectedDayButton.setBackgroundColor(getResources().getColor(R.color.pink));
            selectedDayButton.setTextColor(Color.WHITE);
        } else {
            selectedDayButton.setBackgroundColor(getResources().getColor(R.color.grey));
            if (selectedDayButton.getCurrentTextColor() != Color.RED) {
                selectedDayButton.setTextColor(Color.WHITE);
            }
        }
    }

    private void addDaysinCalendar(LinearLayout.LayoutParams buttonParams, Context context,
                                   DisplayMetrics metrics) {
        int dekaDaysArrayCounter = 0;

        for (int weekNumber = 0; weekNumber < 5; ++weekNumber) {
            for (int dayInWeek = 0; dayInWeek < 7; ++dayInWeek) {
                final Button day = new Button(context);
                day.setTextColor(Color.parseColor(CUSTOM_GREY));
                day.setBackgroundColor(Color.TRANSPARENT);
                day.setLayoutParams(buttonParams);
                day.setTextSize((int) metrics.density * 8);
                day.setSingleLine();

                days[dekaDaysArrayCounter] = day;
                weeks[weekNumber].addView(day);

                ++dekaDaysArrayCounter;
            }
        }
    }

    public void setUserDaysLayoutParams(LinearLayout.LayoutParams userButtonParams) {
        this.userButtonParams = userButtonParams;
    }

    public void setUserCurrentMonthYear(int userMonth, int userYear) {
        this.userMonth = userMonth;
        this.userYear = userYear;
    }

    public void setDayBackground(Drawable userDrawable) {
        this.userDrawable = userDrawable;
    }

    private LinearLayout.LayoutParams getDaysLayoutParams() {
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        buttonParams.weight = 1;
        return buttonParams;
    }

    public interface DayClickListener {
        void onDayClick(View view);
    }

    public void setCallBack(DayClickListener mListener) {
        this.mListener = mListener;
    }

}
