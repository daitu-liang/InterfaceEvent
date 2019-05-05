package com.lxl.interfaceevent;

public class UserInfo  {
    private String desc;
    private String pwd;

    public UserInfo(String desc, String pwd) {
        this.desc = desc;
        this.pwd = pwd;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPwd() {
        return pwd;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "desc='" + desc + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
