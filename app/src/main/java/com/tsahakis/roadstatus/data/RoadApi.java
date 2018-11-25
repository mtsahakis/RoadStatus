package com.tsahakis.roadstatus.data;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RoadApi {

    @GET("Road/{road_id}")
    Single<List<RoadData>> getRoadStatus(@Path("road_id") String roadId,
                                         @Query("app_id") String appId,
                                         @Query("app_key") String appKey);

}
