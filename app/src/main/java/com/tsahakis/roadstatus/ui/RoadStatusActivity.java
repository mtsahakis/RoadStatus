package com.tsahakis.roadstatus.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tsahakis.roadstatus.R;
import com.tsahakis.roadstatus.app.RoadApplication;
import com.tsahakis.roadstatus.data.RoadService;
import com.tsahakis.roadstatus.databinding.ActivityRoadStatusBinding;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class RoadStatusActivity extends AppCompatActivity implements RoadContract.View {

    @Inject
    RoadService mRoadService;

    private ActivityRoadStatusBinding mBinding;
    private RoadPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // data binding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_road_status);

        // dependency injection
        ((RoadApplication) getApplicationContext()).getAppComponent().inject(this);

        CompositeDisposable compositeDisposable = new CompositeDisposable();

        mPresenter = new RoadPresenter(this, mRoadService, compositeDisposable);
        mPresenter.init();
        mBinding.buttonRequest.setOnClickListener(view -> mPresenter.onRequestButtonClicked(mBinding.roadId.getText().toString()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.unsubscribe();
    }

    @Override
    public void showProgressBar() {
        mBinding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mBinding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showResultContainer() {
        mBinding.resultContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideResultContainer() {
        mBinding.resultContainer.setVisibility(View.GONE);
    }

    @Override
    public void showErrorContainer() {
        mBinding.errorContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideErrorContainer() {
        mBinding.errorContainer.setVisibility(View.GONE);
    }

    @Override
    public void setDisplayNameText(String displayName) {
        mBinding.displayName.setText(displayName);
    }

    @Override
    public void setRoadStatusText(String status) {
        mBinding.status.setText(status);
    }

    @Override
    public void setRoadStatusDescriptionText(String description) {
        mBinding.statusDescription.setText(description);
    }

    @Override
    public void showConnectivityError() {
        mBinding.errorText.setText(getString(R.string.cannot_contact_server_please_check_your_connection));
    }

    @Override
    public void showContactSupportError() {
        mBinding.errorText.setText(getString(R.string.please_contact_support));
    }

    @Override
    public void showInvalidInputError() {
        mBinding.errorText.setText(getString(R.string.please_enter_valid_road_id));
    }

    @Override
    public void logException(Throwable e) {
        Timber.e(e);
    }
}
