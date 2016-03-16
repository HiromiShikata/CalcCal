package com.imorih.common;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.text.format.Time;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hiromi on 1/10/16.
 */
@EBean(scope = EBean.Scope.Singleton)
public class GCalendarService {
  @RootContext
  Context context;


  public List<GEvent> findEvent() {
    List<GEvent> gEvents = new ArrayList<>();
    String[] projection = new String[]{
        CalendarContract.Instances._ID,
        CalendarContract.Instances.EVENT_ID,
        CalendarContract.Instances.BEGIN,
        CalendarContract.Instances.END,
        CalendarContract.Instances.TITLE,
        CalendarContract.Instances.DESCRIPTION,
        CalendarContract.Instances.CALENDAR_DISPLAY_NAME,
        CalendarContract.Instances.EVENT_LOCATION,
        CalendarContract.Instances.ALL_DAY,
        CalendarContract.Instances.RRULE,
    };
    String sortOrder = ""
        + CalendarContract.Instances.BEGIN + " ASC, "
        + CalendarContract.Instances.END + " DESC, "
        + CalendarContract.Instances.TITLE + " ASC ";

    Time time = new Time(Time.TIMEZONE_UTC);
    time.setToNow();
//    time.allDay = true;
//    time.monthDay -= 1;
//    time.hour = 0;
//    time.minute = 0;
//    time.second = 0;

    int start = time.getJulianDay(time.toMillis(true), 0);
    time.monthDay += 366;
    int end = time.getJulianDay(time.toMillis(true), 0);
    Uri uri = buildQuery(start, end);
    Cursor cursor = context.getContentResolver().query(uri, projection, null, null, sortOrder);


    while (cursor.moveToNext()) {
      GEvent event = new GEvent();
      event.setId(cursor.getString(cursor.getColumnIndex(CalendarContract.Instances._ID)));
      event.setEventId(cursor.getString(cursor.getColumnIndex(CalendarContract.Instances.EVENT_ID)));
      event.setBegin(cursor.getLong(cursor.getColumnIndex(CalendarContract.Instances.BEGIN)));
      event.setEnd(cursor.getLong(cursor.getColumnIndex(CalendarContract.Instances.END)));
      event.setTitle(cursor.getString(cursor.getColumnIndex(CalendarContract.Instances.TITLE)));
      event.setDescription(cursor.getString(cursor.getColumnIndex(CalendarContract.Instances.DESCRIPTION)));
      event.setCalendarDisplayName(cursor.getString(cursor.getColumnIndex(CalendarContract.Instances.CALENDAR_DISPLAY_NAME)));
      event.setEventLocation(cursor.getString(cursor.getColumnIndex(CalendarContract.Instances.EVENT_LOCATION)));
      event.setAllDay(cursor.getInt(cursor.getColumnIndex(CalendarContract.Instances.ALL_DAY)));
      event.setRrule(cursor.getString(cursor.getColumnIndex(CalendarContract.Instances.RRULE)));
      gEvents.add(event);
    }
    return gEvents;


  }

  private boolean isAlarmEvent(GEvent event) {
    if (event.isAllDay()) {
      return false;
    }

    String calendarDisplayName = event.getCalendarDisplayName();
    if (!calendarDisplayName.equals("hiromi shikata")
        && !calendarDisplayName.equals("notif")
        && !calendarDisplayName.equals("auto")
        ) {
      return false;
    }
    String title = event.getTitle();
    if (!title.equals("alarm")
        && !title.equals("")
        && !title.equals("")
        && !title.equals("")
        && !title.equals("")
        ) {
      return false;
    }

    if (StringUtils.isEmpty(event.getDescription())) {
      return false;
    }


    return true;

  }

  private Uri buildQuery(long start, long end) {
    StringBuilder path = new StringBuilder();
    path.append(start)
        .append('/')
        .append(end)
    ;
    Uri uri = Uri.withAppendedPath(CalendarContract.Instances.CONTENT_BY_DAY_URI, path.toString());
    return uri;

  }

  public List<GCalendar> getCalendars() {
    List<GCalendar> gCalendars = new ArrayList<>();
    String[] projection = {
        CalendarContract.Calendars._ID,
        CalendarContract.Calendars.NAME,
        CalendarContract.Calendars.ACCOUNT_NAME,
        CalendarContract.Calendars.ACCOUNT_TYPE,
        CalendarContract.Calendars.CALENDAR_COLOR
    };
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
      return gCalendars;
    }
    Cursor cursor =
        context.getContentResolver().query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            null,
            null,
            null);
    for (boolean hasNext = cursor.moveToFirst(); hasNext; hasNext = cursor.moveToNext()) {
      GCalendar gcal = new GCalendar();
      gcal.setId(cursor.getString(0));
      gcal.setName(cursor.getString(1));
      gcal.setAccountName(cursor.getString(2));
      gcal.setAccountType(cursor.getString(3));
      gcal.setColor(cursor.getInt(4));
      gCalendars.add(gcal);
    }
    return gCalendars;
  }


  public static class GCalendar {
    private String id;
    private String name;
    private String accountType;
    private String accountName;


    private int color;


    public int getColor() {
      return color;
    }

    public void setColor(int color) {
      this.color = color;
    }

    public String getAccountName() {
      return accountName;
    }

    public void setAccountName(String accountName) {
      this.accountName = accountName;
    }

    public String getAccountType() {
      return accountType;
    }

    public void setAccountType(String accountType) {
      this.accountType = accountType;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }


  }

  public static class GEvent {
    private String id;
    private String eventId;
    private long begin;
    private long end;


    private String description;
    private String title;
    private String calendarDisplayName;
    private String eventLocation;
    private int allDay;

    public GEvent() {

    }

    public String getRrule() {
      return rrule;
    }

    public void setRrule(String rrule) {
      this.rrule = rrule;
    }

    public boolean isAllDay() {
      return allDay == 1;
    }

    public void setAllDay(int allDay) {
      this.allDay = allDay;
    }

    public String getEventLocation() {
      return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
      this.eventLocation = eventLocation;
    }

    public String getCalendarDisplayName() {
      return calendarDisplayName;
    }

    public void setCalendarDisplayName(String calendarDisplayName) {
      this.calendarDisplayName = calendarDisplayName;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public long getEnd() {
      return end;
    }

    public void setEnd(long end) {
      this.end = end;
    }

    public long getBegin() {
      return begin;
    }

    public void setBegin(long begin) {
      this.begin = begin;
    }

    public String getEventId() {
      return eventId;
    }

    public void setEventId(String eventId) {
      this.eventId = eventId;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    private String rrule;

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }
  }

}
