package com.tsahakis.roadstatus.ui;

import android.support.annotation.NonNull;

import com.tsahakis.roadstatus.data.RoadData;
import com.tsahakis.roadstatus.data.RoadService;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class RoadPresenter implements RoadContract.Presenter {

    private final RoadContract.View mView;
    private final RoadService mRoadService;
    private final CompositeDisposable mCompositeDisposable;

    public RoadPresenter(@NonNull RoadContract.View view,
                         @NonNull RoadService roadService,
                         @NonNull CompositeDisposable compositeDisposable) {
        mView = view;
        mRoadService = roadService;
        mCompositeDisposable = compositeDisposable;
    }

    @Override
    public void init() {
        // hide progress and response containers
        mView.hideProgressBar();
        mView.hideResultContainer();
        mView.hideErrorContainer();
    }

    @Override
    public void onRequestButtonClicked(String roadId) {
        // show progress and hide response containers
        mView.showProgressBar();
        mView.hideResultContainer();
        mView.hideErrorContainer();

        // make an api call
        mCompositeDisposable.add(mRoadService.getRoadStatus(roadId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<RoadData>() {

                    @Override
                    public void onSuccess(RoadData roadData) {
                        onSuccessfulResult(roadData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        onErrorResult(e);
                    }
                })
        );
    }

    @Override
    public void unsubscribe() {
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }

    private void onSuccessfulResult(RoadData roadData) {
        mView.hideProgressBar();
        mView.showResultContainer();
        mView.setDisplayNameText(roadData.getDisplayName());
        mView.setRoadStatusText(roadData.getStatusSeverity());
        mView.setRoadStatusDescriptionText(roadData.getStatusSeverityDescription());
    }

    private void onErrorResult(Throwable e) {
        mView.hideProgressBar();
        mView.logException(e);
        mView.showErrorContainer();

        if (e instanceof HttpException) {
            if (((HttpException) e).code() == 404) {
                mView.showInvalidInputError();
            } else {
                mView.showContactSupportError();
            }

        } else if (e instanceof IOException) {
            mView.showConnectivityError();
        } else {
            mView.showContactSupportError();
        }
    }

}
