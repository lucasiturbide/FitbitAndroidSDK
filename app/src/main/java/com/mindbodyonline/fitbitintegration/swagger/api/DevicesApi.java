package com.mindbodyonline.fitbitintegration.swagger.api;

import retrofit2.Call;
import retrofit2.http.*;

public interface DevicesApi {
  /**
   * Get Alarms
   * Returns alarms for a device
   * @param trackerId The ID of the tracker for which data is returned. The tracker-id value is found via the Get Devices endpoint. (required)
   * @return Call&lt;Void&gt;
   */
  @GET("1/user/-/devices/tracker/{tracker-id}/alarms.json")
  Call<Void> alarms(
    @retrofit2.http.Path("tracker-id") Integer trackerId
  );

  /**
   * Add Alarm
   * Adds the alarm settings to a given ID for a given device.
   * @param trackerId The ID of the tracker for which data is returned. The tracker-id value is found via the Get Devices endpoint. (required)
   * @param time Time of day that the alarm vibrates with a UTC timezone offset, e.g. 07:15-08:00. (required)
   * @param enabled true or false. If false, alarm does not vibrate until enabled is set to true. (required)
   * @param recurring true or false. If false, the alarm is a single event. (required)
   * @param weekDays Comma separated list of days of the week on which the alarm vibrates, e.g. MONDAY, TUESDAY. (required)
   * @return Call&lt;Void&gt;
   */
  @POST("1/user/-/devices/tracker/{tracker-id}/alarms.json")
  Call<Void> alarms_0(
    @retrofit2.http.Path("tracker-id") Integer trackerId, @retrofit2.http.Query("time") String time, @retrofit2.http.Query("enabled") Boolean enabled, @retrofit2.http.Query("recurring") String recurring, @retrofit2.http.Query("weekDays") String weekDays
  );

  /**
   * Update Alarm
   * Updates the alarm entry with a given ID for a given device. It also gets a response in the format requested.
   * @param trackerId The ID of the tracker for which data is returned. The tracker-id value is found via the Get Devices endpoint. (required)
   * @param alarmId The ID of the alarm to be updated. The alarm-id value is found in the response of the Get Activity endpoint. (required)
   * @param time Time of day that the alarm vibrates with a UTC timezone offset, e.g. 07:15-08:00. (required)
   * @param enabled true or false. If false, the alarm does not vibrate until enabled is set to true. (required)
   * @param recurring true or false. If false, the alarm is a single event. (required)
   * @param weekDays Comma seperated list of days of the week on which the alarm vibrates, e.g. MONDAY, TUESDAY. (required)
   * @param snoozeLength Minutes between alarms. (required)
   * @param snoozeCount Maximum snooze count. (required)
   * @return Call&lt;Void&gt;
   */
  @POST("1/user/-/devices/tracker/{tracker-id}/alarms/{alarm-id}.json")
  Call<Void> alarms_1(
    @retrofit2.http.Path("tracker-id") Integer trackerId, @retrofit2.http.Path("alarm-id") Integer alarmId, @retrofit2.http.Query("time") String time, @retrofit2.http.Query("enabled") Boolean enabled, @retrofit2.http.Query("recurring") String recurring, @retrofit2.http.Query("weekDays") String weekDays, @retrofit2.http.Query("snoozeLength") Integer snoozeLength, @retrofit2.http.Query("snoozeCount") Integer snoozeCount
  );

  /**
   * Delete Alarm
   * Deletes the user&#39;s device alarm entry with the given ID for a given device.
   * @param trackerId The ID of the tracker whose alarms is managed. The tracker-id value is found via the Get Devices endpoint. (required)
   * @param alarmId The ID of the alarm to be updated. The alarm-id value is found via the Get Alarms endpoint. (required)
   * @return Call&lt;Void&gt;
   */
  @DELETE("1/user/-/devices/tracker/{tracker-id}/alarms/{alarm-id}.json")
  Call<Void> alarms_2(
    @retrofit2.http.Path("tracker-id") Integer trackerId, @retrofit2.http.Path("alarm-id") Integer alarmId
  );

  /**
   * Get Devices
   * Returns a list of the Fitbit devices connected to a user&#39;s account.
   * @return Call&lt;Void&gt;
   */
  @GET("1/user/-/devices.json")
  Call<Void> devices();
    

}
