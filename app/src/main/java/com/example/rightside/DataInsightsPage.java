package com.example.rightside;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DataInsightsPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DataInsightsPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView unaffectedPercentageText;
    TextView affectedPercentageText;

    Date date;
    SimpleDateFormat dateFormatter;

    public DataInsightsPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_data_insights.
     */
    // TODO: Rename and change types and number of parameters
    public static DataInsightsPage newInstance(String param1, String param2) {
        DataInsightsPage fragment = new DataInsightsPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_insights, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatePickerDialog startDatePickerDialog;
        DatePickerDialog endDatePickerDialog;
        Button startDateButton;
        Button endDateButton;

        //Initialize all the views
        LineChart lineChart = view.findViewById(R.id.lineChart);
        BarChart barChart = view.findViewById(R.id.BarChart);
        PieChart pieChart= view.findViewById(R.id.PieChartUnaffected);
        startDateButton = view.findViewById(R.id.StartDateButton);
        endDateButton = view.findViewById(R.id.EndDateButton);
        affectedPercentageText = view.findViewById(R.id.AffectedPercentageText);
        unaffectedPercentageText = view.findViewById(R.id.UnaffectedPercentageText);

        //Setup Date Button and OnClickListener
        startDatePickerDialog = initializeDatePicker(startDateButton);
        endDatePickerDialog = initializeDatePicker(endDateButton);
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(startDatePickerDialog);
            }
        });
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(endDatePickerDialog);
            }
        });



        //Initialize values for charts and dropdown
        initializeStateDropDown(view);
        initializeDiscriminationTypeDropDown(view);
        //insertLineChartValue(view, lineChart);
        insertBarChartValue(view,barChart);
        insertPieChartValue(view,pieChart);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Initialize value for graphs
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void insertBarChartValue(View view, BarChart barChart){
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 10)); // X=1, Y=10
        entries.add(new BarEntry(1f, 20)); // X=2, Y=20
        entries.add(new BarEntry(2f, 30)); // X=3, Y=30

        String[] categories = {"Mental", "Gender", "Race"};

// Create the data set
        BarDataSet dataSet = new BarDataSet(entries, "Descrimination Type");
        dataSet.setColor(Color.BLUE); // Set bar color
        dataSet.setValueTextSize(10);

// Create the bar data
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.7f); // Optional: Set bar width

// Customize X-Axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(categories));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

// Customize Y-Axis
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setGranularity(10f);
        leftAxis.setGranularityEnabled(true);

// set the bar data to the bar chart and edit barchart
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.invalidate();
        barChart.getDescription().setEnabled(false);
    }

    public void insertPieChartValue(View view, PieChart pieChart){
        float numberAffected = 9;
        float numberUnaffected = 10;
        float total = numberUnaffected + numberAffected;
        float percentageAffected = ((float)Math.round((numberAffected/total)*10000))/100;
        float percentageUnaffected = ((float)Math.round((numberUnaffected/total)*10000))/100;

        unaffectedPercentageText.setText(percentageUnaffected + "%");
        affectedPercentageText.setText(percentageAffected + "%");

        PieChart pieChart1 = view.findViewById(R.id.PieChartUnaffected);
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(percentageUnaffected,"Unaffected"));
        entries.add(new PieEntry(percentageAffected,"Affected"));

        // create the data set
        int[] colors = {Color.parseColor("#5898fe"),Color.parseColor("#ef7e1e")};
        PieDataSet dataSet = new PieDataSet(entries, null);
        dataSet.setColors(colors);

        // create pie data
        PieData data = new PieData(dataSet);

        // assing piedata to piechart
        pieChart1.setData(data);

        // customize piechart
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(false);
        pieChart.setDrawSliceText(false);
        pieChart.invalidate();
    }
    public void insertLineChartValue(View view, LineChart lineChart, String  startDate){
        ArrayList<Entry> entries = new ArrayList<>();

        for(int i=0 ; i<48 ; i++){
            entries.add(new Entry((1/12f)*i,i+1));
        }
        //create the label for x axis
        String[] categories = {"2021","2022","2023","2024"};

        // Create a dataset and set it to the chart
        LineDataSet dataSet = new LineDataSet(entries, "Cases Reported");
        dataSet.setValueTextSize(8);
        LineData lineData = new LineData(dataSet);

        // Set the data to the chart
        lineChart.setData(lineData);

        // customizing line chart
        lineChart.getDescription().setEnabled(false);
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setGranularity(5f);
        rightAxis.setGranularityEnabled(true);
        XAxis xAxis = lineChart.getXAxis();

        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);

        lineChart.setVisibleXRangeMaximum(1);
        lineChart.moveViewToX(0);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(categories));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        lineChart.invalidate();  // Refresh the chart
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //initialize buttons and dropdowns
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void initializeDiscriminationTypeDropDown(View view) {
        Spinner discrimininationDropDown = view.findViewById(R.id.DiscriminationTypeSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.DiscriminationTypes,R.layout.layout_spinner);
        adapter.setDropDownViewResource(R.layout.layout_spinner);
        discrimininationDropDown.setAdapter(adapter);
    }

    public void initializeStateDropDown(View view){
        Spinner stateDropDown = view.findViewById(R.id.StateDropDown);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.States,R.layout.layout_spinner);
        adapter.setDropDownViewResource(R.layout.layout_spinner);
        stateDropDown.setAdapter(adapter);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Calendar popup
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




    public DatePickerDialog initializeDatePicker(Button dateButton){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day ,month,year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        return new DatePickerDialog(getActivity(),style,dateSetListener,year,month,day);
    }

    private String makeDateString(int day, int month, int year){
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "Jan";
        else if (month == 2)
            return "Feb";
        else if (month == 3)
            return "Mar";
        else if (month == 4)
            return "Apr";
        else if (month == 5)
            return "May";
        else if (month == 6)
            return "Jun";
        else if (month == 7)
            return "Jul";
        else if (month == 8)
            return "Aug";
        else if (month == 9)
            return "Sep";
        else if (month == 10)
            return "Oct";
        else if (month == 11)
            return "Nov";
        else if (month == 12)
            return "Dec";
        else
            return "Jan";
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day,month,year);
    }
    public void openDatePicker(DatePickerDialog datePickerDialog){
        datePickerDialog.show();
    }
}