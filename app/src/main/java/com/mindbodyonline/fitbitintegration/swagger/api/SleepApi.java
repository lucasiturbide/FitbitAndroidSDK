package com.mindbodyonline.fitbitintegration.swagger.api;

import retrofit2.Call;
import retrofit2.http.*;

public interface SleepApi {
  /**
   * Log Sleep
   * Creates a log entry for a sleep event and returns a response in the format requested.
   * @param startTime Start time includes hours and minutes in the format HH:mm. (required)
   * @param duration Duration in milliseconds. (required)
   * @param date Log entry in the format yyyy-MM-dd. (required)
   * @return Call&lt;Void&gt;
   */
  @POST("1.2/user/-/sleep.json")
  Call<Void> sleep(
    @retrofit2.http.Query("startTime") String startTime, @retrofit2.http.Query("duration") Integer duration, @retrofit2.http.Query("date") String date
  );

  /**
   * Get Sleep Logs by Date Range
   * The Get Sleep Logs by Date Range endpoint returns a list of a user&#39;s sleep log entries (including naps) as well as detailed sleep entry data for a given date range (inclusive of start and end dates).
   * @param baseDate The date of records to be returned. In the format yyyy-MM-dd. (required)
   * @param endDate The date of records to be returned. In the format yyyy-MM-dd. (required)
   * @return Call&lt;Void&gt;
   */
  @GET("1.2/user/-/sleep/date/{base-date}/{end-date}.json")
  Call<Void> sleep_0(
    @retrofit2.http.Path("base-date") String baseDate, @retrofit2.http.Path("end-date") String endDate
  );

  /**
   * Get Sleep Log
   * The Get Sleep Logs by Date endpoint returns a summary and list of a user&#39;s sleep log entries (including naps) as well as detailed sleep entry data for a given day.
   * @param date The date of records to be returned. In the format yyyy-MM-dd. (required)
   * @return Call&lt;Void&gt;
   */
  @GET("1.2/user/-/sleep/date/{date}.json")
  Call<Void> sleep_1(
    @retrofit2.http.Path("date") String date
  );

  /**
   * Get Sleep Goal
   * Returns the user&#39;s sleep goal.
   * @return Call&lt;Void&gt;
   */
  @GET("1.2/user/-/sleep/goal.json")
  Call<Void> sleep_2();
    

  /**
   * Update Sleep Goal
   * Create or update the user&#39;s sleep goal and get a response in the JSON format.
   * @param minDuration Duration of sleep goal. (required)
   * @return Call&lt;Void&gt;
   */
  @POST("1.2/user/-/sleep/goal.json")
  Call<Void> sleep_3(
    @retrofit2.http.Query("minDuration") String minDuration
  );

  /**
   * Get Sleep Logs List
   * The Get Sleep Logs List endpoint returns a list of a user&#39;s sleep logs (including naps) before or after a given day with offset, limit, and sort order.
   * @param sort The sort order of entries by date asc (ascending) or desc (descending). (required)
   * @param offset The offset number of entries. (required)
   * @param limit The maximum number of entries returned (maximum;100). (required)
   * @param beforeDate The date in the format yyyy-MM-ddTHH:mm:ss. Only yyyy-MM-dd is required. Either beforeDate or afterDate should be specified. (optional)
   * @param afterDate The date in the format yyyy-MM-ddTHH:mm:ss. (optional)
   * @return Call&lt;Void&gt;
   */
  @GET("1.2/user/-/sleep/list.json")
  Call<Void> sleep_4(
    @retrofit2.http.Query("sort") String sort, @retrofit2.http.Query("offset") Integer offset, @retrofit2.http.Query("limit") Integer limit, @retrofit2.http.Query("beforeDate") String beforeDate, @retrofit2.http.Query("afterDate") String afterDate
  );

  /**
   * Delete Sleep Log
   * Deletes a user&#39;s sleep log entry with the given ID.
   * @param logId The ID of the sleep log to be deleted. (required)
   * @return Call&lt;Void&gt;
   */
  @DELETE("1.2/user/-/sleep/{log-id}.json")
  Call<Void> sleep_5(
    @retrofit2.http.Path("log-id") String logId
  );

}
