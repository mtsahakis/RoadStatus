package com.tsahakis.roadstatus.data;

import com.tsahakis.roadstatus.BuildConfig;

import io.reactivex.Single;

public class RoadService {

    private static final String ROAD_API_ID = BuildConfig.ROAD_API_ID;
    private static final String ROAD_API_KEY = BuildConfig.ROAD_API_KEY;

    private final RoadApi mRoadApi;


    public RoadService(RoadApi roadApi) {
        mRoadApi = roadApi;
    }

    public Single<RoadData> getRoadStatus(String roadId) {
        return mRoadApi.getRoadStatus(roadId, ROAD_API_ID, ROAD_API_KEY).map(roadResponse -> roadResponse.get(0));
    }
}
