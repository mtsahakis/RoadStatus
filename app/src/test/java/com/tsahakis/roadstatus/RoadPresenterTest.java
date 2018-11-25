package com.tsahakis.roadstatus;

import com.tsahakis.roadstatus.data.RoadData;
import com.tsahakis.roadstatus.data.RoadService;
import com.tsahakis.roadstatus.ui.RoadContract;
import com.tsahakis.roadstatus.ui.RoadPresenter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoadPresenterTest {

    @Mock
    private RoadContract.View mView;

    @Mock
    private RoadService mRoadService;

    @Mock
    private CompositeDisposable mCompositeDisposable;

    @Rule
    public TrampolineSchedulerRule mTrampolineSchedulerRule = new TrampolineSchedulerRule();

    private RoadPresenter mPresenter;

    @Before
    public void setup() {
        mPresenter = new RoadPresenter(mView, mRoadService, mCompositeDisposable);
    }

    @Test
    public void on_request_button_clicked_correct_views_displayed() {
        // given
        String roadId = "A-20";
        when(mRoadService.getRoadStatus(roadId)).thenReturn(Single.just(new RoadData()));

        // when
        mPresenter.onRequestButtonClicked(roadId);

        //then
        verify(mView).showProgressBar();
        verify(mView).hideResultContainer();
        verify(mView).hideErrorContainer();
    }

    @Test
    public void on_request_button_clicked_success_response() {
        // given
        String roadId = "A-20";
        RoadData roadData = testRoadData();
        when(mRoadService.getRoadStatus(roadId)).thenReturn(Single.just(roadData));

        // when
        mPresenter.onRequestButtonClicked(roadId);

        //then
        verify(mView).hideProgressBar();
        verify(mView).showResultContainer();
        verify(mView).setDisplayNameText(roadData.getDisplayName());
        verify(mView).setRoadStatusText(roadData.getStatusSeverity());
        verify(mView).setRoadStatusDescriptionText(roadData.getStatusSeverityDescription());
    }

    @Test
    public void on_request_button_clicked_fail_http_entity_not_found_exception() {
        // given
        String roadId = "A-20";
        Exception exception = new HttpException(Response.error(
                404,
                ResponseBody.create(
                        MediaType.parse("application/json"),
                        "{\"$type\":\"Tfl.Api.Presentation.Entities.ApiError, Tfl.Api.Presentation.Entities\"," +
                                "\"timestampUtc\":\"2018-11-25T20:32:21.3208585Z\"," +
                                "\"exceptionType\":\"EntityNotFoundException\"," +
                                "\"httpStatusCode\":404," +
                                "\"httpStatus\":\"Generic\"," +
                                "\"relativeUri\":\"/Road/foo\"," +
                                "\"message\":\"The following road id is not recognised: foo\"}")
        ));

        when(mRoadService.getRoadStatus(roadId)).thenReturn(Single.error(exception));

        // when
        mPresenter.onRequestButtonClicked(roadId);

        //then
        verify(mView).hideProgressBar();
        verify(mView).logException(exception);
        verify(mView).showErrorContainer();
        verify(mView).showInvalidInputError();
    }

    @Test
    public void on_request_button_clicked_fail_http_generic_exception() {
        // given
        String roadId = "A-20";
        Exception exception = new HttpException(Response.error(
                500,
                ResponseBody.create(
                        MediaType.parse("application/json"),
                        "{\"$type\":\"Tfl.Api.Presentation.Entities.ApiError, Tfl.Api.Presentation.Entities\"," +
                                "\"timestampUtc\":\"2018-11-25T20:32:21.3208585Z\"," +
                                "\"exceptionType\":\"Exception\"," +
                                "\"httpStatusCode\":500," +
                                "\"httpStatus\":\"Generic\"," +
                                "\"relativeUri\":\"/Road/unknown\"," +
                                "\"message\":\"Generic\"}")
        ));

        when(mRoadService.getRoadStatus(roadId)).thenReturn(Single.error(exception));

        // when
        mPresenter.onRequestButtonClicked(roadId);

        //then
        verify(mView).hideProgressBar();
        verify(mView).logException(exception);
        verify(mView).showErrorContainer();
        verify(mView).showContactSupportError();
    }

    @Test
    public void on_request_button_clicked_fail_io_exception() {
        // given
        String roadId = "A-20";
        Exception exception = new IOException();
        when(mRoadService.getRoadStatus(roadId)).thenReturn(Single.error(exception));

        // when
        mPresenter.onRequestButtonClicked(roadId);

        //then
        verify(mView).hideProgressBar();
        verify(mView).logException(exception);
        verify(mView).showErrorContainer();
        verify(mView).showConnectivityError();
    }

    @Test
    public void on_request_button_clicked_fail_generic_exception() {
        // given
        String roadId = "A-20";
        Exception exception = new Exception();
        when(mRoadService.getRoadStatus(roadId)).thenReturn(Single.error(exception));

        // when
        mPresenter.onRequestButtonClicked(roadId);

        //then
        verify(mView).hideProgressBar();
        verify(mView).logException(exception);
        verify(mView).showErrorContainer();
        verify(mView).showContactSupportError();
    }

    @Test
    public void init() {
        // when
        mPresenter.init();

        //then
        verify(mView).hideProgressBar();
        verify(mView).hideResultContainer();
        verify(mView).hideErrorContainer();
    }

    @Test
    public void unsubscribe() {
        // given
        when(mCompositeDisposable.isDisposed()).thenReturn(false);

        // when
        mPresenter.unsubscribe();

        // then
        verify(mCompositeDisposable).dispose();
    }

    private RoadData testRoadData() {
        RoadData roadData = new RoadData();
        roadData.setDisplayName("name");
        roadData.setStatusSeverity("Normal");
        roadData.setStatusSeverityDescription("Description");
        return roadData;
    }

}
