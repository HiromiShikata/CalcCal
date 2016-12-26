package com.imorih.calccal;

import android.Manifest;
import android.graphics.Color;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.imorih.common.BaseActivity;
import com.imorih.common.GCalendarService;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final Pattern PAT_PRICE = Pattern.compile("#oc ([,\\-\\d]*) *");

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.fab)
    FloatingActionButton fab;

    @ViewById(R.id.drawer_layout)
    DrawerLayout drawer;

    @ViewById(R.id.nav_view)
    NavigationView navigationView;

    @Bean
    GCalendarService gCalendarService;

//  @ViewById(R.id.activity_main_chart)
//  LineChartView lineChartView;

    @ViewById(R.id.activity_content_main_list)
    ListView list;

//  @ViewById(R.id.cf_row_input_current_amount)
//  EditText currentAmount;

    CfListAdapter listAdapter;

    List<GCalendarService.GEvent> events;

    @AfterViews
    void afterViews() {
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final int callbackId = 42;
        checkPermissions(callbackId, Manifest.permission.READ_CALENDAR);


        List<GCalendarService.GEvent> origList = gCalendarService.findEvent();
        events = new ArrayList<>();
        for (GCalendarService.GEvent event : origList) {
            Matcher mat = PAT_PRICE.matcher(event.getTitle());
            if (!mat.find()) {
                continue;
            }
            events.add(event);
        }

        setupList();
//    drawGraph();


        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupList() {
        listAdapter = new CfListAdapter(this);
        LayoutInflater inflator = LayoutInflater.from(this);
        View header = inflator.inflate(R.layout.cf_row_header, null);
        View inputCurrent = inflator.inflate(R.layout.cf_row_input_current, null);
        list.addHeaderView(header);
        list.addHeaderView(inputCurrent);


        EditText currentAmount = (EditText) inputCurrent.findViewById(R.id.cf_row_input_current_amount);
        currentAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                int amount = 0;
                if (!StringUtils.isEmpty(charSequence.toString())) {
                    amount = Integer.parseInt(charSequence.toString());
                }
                refreshList(amount);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        list.setAdapter(listAdapter);
        list.setHeaderDividersEnabled(true);
        int amount = 0;
        refreshList(amount);
    }

    private void refreshList(int currentAmount) {

        int beforeAmount = currentAmount;
        listAdapter.clear();
        for (int ii = 0; ii < events.size(); ii++) {
            GCalendarService.GEvent event = events.get(ii);
            int amount = findAmountFromEvent(event);
            listAdapter.add(new CfRow(event, amount, beforeAmount));
            beforeAmount = beforeAmount + amount;
        }
        listAdapter.notifyDataSetChanged();

    }

    private int findAmountFromEvent(GCalendarService.GEvent event) {
        Matcher mat = PAT_PRICE.matcher(event.getTitle());
        mat.find();
        String priceStr = mat.group(1);
        int price = Integer.parseInt(priceStr) * -1;
        return price;

    }

//  void drawGraph(List<GCalendarService.GEvent> list) {
//    lineChartView.setInteractive(true);
////    lineChartView.setZoomEnabled(true);
//
//    List<PointValue> values = new ArrayList<PointValue>();
//
//    int current = 0;
//    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
//    for (int ii = 0; ii < list.size(); ii++) {
//      GCalendarService.GEvent event = list.get(ii);
//
//      current += findAmountFromEvent(event);
//      PointValue value = new PointValue(ii, current);
//
//
//      value.setLabel(sdf.format(event.getBegin()) + " " + event.getTitle());
//
//
//      values.add(value);
//
//    }
//
//    Line line = new Line(values).setColor(Color.parseColor("#ff4444")).setCubic(false);
//    List<Line> lines = new ArrayList<>();
//    lines.add(line);
//
//    LineChartData data = new LineChartData();
//    data.setLines(lines);
//    lineChartView.setLineChartData(data);
//
//
//  }

    @Click(R.id.fab)
    void clickFloatingActionButton(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
