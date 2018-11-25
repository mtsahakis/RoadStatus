package com.tsahakis.roadstatus.ui;

public interface RoadContract {

    interface View {

        void showProgressBar();

        void hideProgressBar();

        void showResultContainer();

        void hideResultContainer();

        void showErrorContainer();

        void hideErrorContainer();

        void setDisplayNameText(String displayName);

        void setRoadStatusText(String status);

        void setRoadStatusDescriptionText(String description);

        void logException(Throwable e);

        void showConnectivityError();

        void showContactSupportError();

        void showInvalidInputError();
    }

    interface Presenter {

        void init();

        void onRequestButtonClicked(String roadId);

        void unsubscribe();

    }
}
