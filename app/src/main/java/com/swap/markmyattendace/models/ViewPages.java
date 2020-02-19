package com.swap.markmyattendace.models;

public class ViewPages {
    private String iconPath;
    private String name;
    private String desc;

    public ViewPages(String iconPath, String name, String desc) {
        this.iconPath = iconPath;
        this.name = name;
        this.desc = desc;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String icon) {
        this.iconPath = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
