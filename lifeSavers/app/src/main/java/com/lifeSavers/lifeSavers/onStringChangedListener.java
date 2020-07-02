package com.lifeSavers.lifeSavers;

public class onStringChangedListener {
    private String boo = "";
    private ChangeListener listener;

    public String isBoo() {
        return boo;
    }

    public void setBoo(String boo) {
        this.boo = boo;
        if (listener != null) listener.onChange();
    }

    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public interface ChangeListener {
        void onChange();
    }
}
