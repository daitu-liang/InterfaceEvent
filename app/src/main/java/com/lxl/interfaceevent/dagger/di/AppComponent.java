package com.lxl.interfaceevent.dagger.di;

import com.lxl.interfaceevent.dagger.YourActivity;
import com.lxl.interfaceevent.dagger.module.PersonBean;

import dagger.Component;

@Component(modules = PersonBean.class)
public interface AppComponent {//extends AndroidInjector<YourApplication> {
    void injectFirstActivity(YourActivity activity);

//        void injectTwoActivity(DaggerTestSecondActivity activity);
//    @Component.Builder
//    interface Builder {
//       @BindsInstance
//       AppComponent.Builder application(Application application);
//       AppComponent build();
//    }
}
