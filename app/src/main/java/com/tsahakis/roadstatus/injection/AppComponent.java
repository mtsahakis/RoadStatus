package com.tsahakis.roadstatus.injection;

import com.tsahakis.roadstatus.ui.RoadStatusActivity;

import dagger.Component;

@ApplicationScope
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(RoadStatusActivity roadStatusActivity);

}
