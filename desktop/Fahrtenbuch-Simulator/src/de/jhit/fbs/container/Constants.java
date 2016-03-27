/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.container;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 *
 * @author Jens
 */
public final class Constants {

    private Constants() {
    }
    // Options
    public static final int APPEARANCE_COUNT_FOR_SHORTCUT = 6;
    public static final int TIME_DIVERSION_RANGE = 15;
    public static final int KM_DIVERSION_RANGE = 1;
    // Type Zones
    public static final int TIME_ZONE_1 = 0;
    public static final int TIME_ZONE_2 = 1;
    public static final int TIME_ZONE_3 = 2;
    // Entry types
    public static final int TYPE_PRIVATE = 0;
    public static final int TYPE_OFFICE = 1;
    public static final int TYPE_WORK = 2;
    public static final String TYPE_PRIVATE_STRING = "P";
    public static final String TYPE_WORK_STRING = "W";
    public static final String TYPE_OFFICE_STRING = "O";
    // Table markers
    public static final String DETOURS_DELIMITER = ";";
    public static final String MARKER_DELIMITER = ";";
    public static final String MARKER_VALUE_DELIMITER = ":";
    public static final String DUPLICATE_MARKER = "!-DUB";
    public static final String OTHER_VALUES_MARKER = "!-VAL";
    public static final String DB_VALUES_MARKER = "!-DB";
    public static final String SUGGESTION_MARKER = "?-SUG";
    public static final String TIME_WARNING_MARKER = "?-TIM";
    public static final String WAYPOINT_WARNING_MARKER = "?-WAY";
    public static final String DISTANCE_WARNING_MARKER = "?-DIS";
    public static final String KM_COUNTER_ERROR_MARKER = "!-KMC";
    public static final String TARGET_ERROR_MARKER = "!-TGE";
    public static final String TARGET_WARNING_MARKER = "?-TGW";
    public static final String TARGET_MISSING_MARKER = "!-TGM";
    public static final String TARGET_FUEL_WARNING_MARKER = "!-TGF";
    // table headers
    public static final String THEADER_DATE = "Date";
    public static final String THEADER_TIME_START = "Start T";
    public static final String THEADER_TIME_END = "End T";
    public static final String THEADER_LOCATION_START = "Start L";
    public static final String THEADER_LOCATION_END = "End L";
    public static final String THEADER_LOCATION_DETOURS = "Detours";
    public static final String THEADER_REASON = "Reason";
    public static final String THEADER_PERSON = "Person";
    public static final String THEADER_KM = "km";
    public static final String THEADER_KM_COUNTER = "km Counter (end)";
    public static final String THEADER_FUEL = "Fuel";
    public static final String THEADER_TYPE = "Type";
    public static final String THEADER_SYSTEM_COMMENT = "System Comment";
    // extended table headers
    public static final String THEADER_MARKER = "Marker";
    public static final String THEADER_FUEL_CONSUMPTION = "Consumption";
    public static final String THEADER_DURATION = "Duration";
    // routing table headers
    public static final String THEADER_TIME_Z1 = "Time 6-9";
    public static final String THEADER_TIME_Z2 = "Time 16-19";
    public static final String THEADER_TIME_Z3 = "Time else";
    // Date formatter
    public static final DateFormat INPUT_DATE_TIME_FORMATTER = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMANY);
    public static final DateFormat OUTPUT_DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
    public static final DateFormat OUTPUT_TIME_FORMATTER = new SimpleDateFormat("HH:mm", Locale.GERMANY);
    // Target Types
    public static final int TARGET_TYPE_BASIC = 0;
    public static final int TARGET_TYPE_FUEL = 1;
    public static final int TARGET_TYPE_KM_COUNTER = 2;
    public static final int TARGET_TYPE_REASONED = 3;
    public static final String TARGET_TYPE_BASIC_STRING = "BSC";
    public static final String TARGET_TYPE_FUEL_STRING = "FUL";
    public static final String TARGET_TYPE_KM_COUNTER_STRING = "CNT";
    public static final String TARGET_TYPE_REASONED_STRING = "RSN";

}
