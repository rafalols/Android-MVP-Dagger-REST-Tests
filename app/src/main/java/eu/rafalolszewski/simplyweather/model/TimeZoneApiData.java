package eu.rafalolszewski.simplyweather.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Rafał Olszewski on 17.04.16.
 */
@Parcel
public class TimeZoneApiData {

    public static final String STATUS_OK = "OK";

    /**
     *  The offset for daylight-savings time in seconds.
     *  This will be zero if the time zone is not in Daylight Savings Time during the specified timestamp.
     */
    @SerializedName("dstOffset")
    public int daylightSavingsTime;

    /**
     *  The offset from UTC (in seconds) for the given location. This does not take into effect daylight savings.
     */
    @SerializedName("rawOffset")
    public int timeZoneTime;

    /**
     * A string indicating the status of the response.
     * OK indicates that the request was successful.
     */
    public String status;

    public boolean isStatusOk(){
        return status.equals(STATUS_OK);
    }

}
