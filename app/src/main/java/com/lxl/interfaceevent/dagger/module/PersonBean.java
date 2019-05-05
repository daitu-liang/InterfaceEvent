package com.lxl.interfaceevent.dagger.module;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

@Module
public class PersonBean {


    @Inject
    public PersonBean() {

    }


    public String getPerName() {
        return "我是卡卡西---";
    }

    @Provides
    public Car getCarInstance(){
        return  new Car();
    }
}
