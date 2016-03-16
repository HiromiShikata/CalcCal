package com.imorih.calccal;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.imorih.common.BaseActivity;
import com.imorih.common.GCalendarService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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

  @ViewById(R.id.activity_main_chart)
  LineChartView lineChartView;


  @AfterViews
  void afterViews() {
    setSupportActionBar(toolbar);

    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();


    List<GCalendarService.GEvent> origList = gCalendarService.findEvent();
    List<GCalendarService.GEvent> list = new ArrayList<>();
    for (GCalendarService.GEvent event : origList) {
      Matcher mat = PAT_PRICE.matcher(event.getTitle());
      if (!mat.find()) {
        continue;
      }
      list.add(event);


    }


    drawGraph(list);

    navigationView.setNavigationItemSelectedListener(this);


  }

  void drawGraph(List<GCalendarService.GEvent> list) {
    lineChartView.setInteractive(true);
//    lineChartView.setZoomEnabled(true);

    List<PointValue> values = new ArrayList<PointValue>();

    int current = 0;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
    for (int ii = 0; ii < list.size(); ii++) {
      GCalendarService.GEvent event = list.get(ii);

      Matcher mat = PAT_PRICE.matcher(event.getTitle());
      mat.find();
      String priceStr = mat.group(1);
      int price = Integer.parseInt(priceStr) * -1;
      current += price;
      PointValue value = new PointValue(ii, current);


      value.setLabel(sdf.format(event.getBegin()) + " " + event.getTitle());


      values.add(value);

    }
    
    Line line = new Line(values).setColor(Color.parseColor("#ff4444")).setCubic(false);
    List<Line> lines = new ArrayList<>();
    lines.add(line);

    LineChartData data = new LineChartData();
    data.setLines(lines);
    lineChartView.setLineChartData(data);


  }

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

}
