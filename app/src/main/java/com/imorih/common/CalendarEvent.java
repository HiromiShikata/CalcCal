package com.imorih.common;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Created by hiromi on 11/29/15.
 */
public class CalendarEvent {
  protected String id;
  protected String eventId;
  protected long begin;
  protected long end;
  protected String title;
  protected String description;
  protected String calendarDisplayName;
  protected String eventLocation;
  protected int allDay;
  protected String rrule;

  public long getBegin() {
    return begin;
  }


  public static Cursor execQuery(Activity activity, int begin, int end) {
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


    StringBuilder path = new StringBuilder();
    path.append(begin)
        .append('/')
        .append(end)
    ;
    Uri uri = Uri.withAppendedPath(CalendarContract.Instances.CONTENT_BY_DAY_URI, path.toString());

    return activity.managedQuery(uri, projection, null, null, sortOrder);

  }

  public CalendarEvent(Cursor c) {
    id = c.getString(c.getColumnIndex(CalendarContract.Instances._ID));
    eventId = c.getString(c.getColumnIndex(CalendarContract.Instances.EVENT_ID));
    begin = c.getLong(c.getColumnIndex(CalendarContract.Instances.BEGIN));
    end = c.getLong(c.getColumnIndex(CalendarContract.Instances.END));
    title = c.getString(c.getColumnIndex(CalendarContract.Instances.TITLE));
    description = c.getString(c.getColumnIndex(CalendarContract.Instances.DESCRIPTION));
    calendarDisplayName = c.getString(c.getColumnIndex(CalendarContract.Instances.CALENDAR_DISPLAY_NAME));
    eventLocation = c.getString(c.getColumnIndex(CalendarContract.Instances.EVENT_LOCATION));
    allDay = c.getInt(c.getColumnIndex(CalendarContract.Instances.ALL_DAY));
    rrule = c.getString(c.getColumnIndex(CalendarContract.Instances.RRULE));

  }

  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }

}
