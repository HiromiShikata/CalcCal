package com.imorih.calccal;

import com.imorih.common.GCalendarService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hiromi on 16/12/25.
 */

public class CfRow {
    private static final String FORMAT = "MM/dd'\n'hh:mm";
    int amount;
    int total;
    GCalendarService.GEvent event;

    public CfRow(GCalendarService.GEvent event, int amount, int beforeAmount) {
        this.event = event;
        this.amount = amount;
        this.total = beforeAmount + amount;

    }

    String getYmd() {
        SimpleDateFormat f = new SimpleDateFormat(FORMAT);
        return f.format(new Date(event.getBegin()));
    }

    public int getIn() {
        if (amount < 0) {
            return 0;
        }
        return amount;
    }

    public int getOut() {
        if (amount < 0) {
            return amount;
        }
        return 0;
    }
}
