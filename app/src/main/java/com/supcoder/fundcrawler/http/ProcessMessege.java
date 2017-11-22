package com.supcoder.fundcrawler.http;

import android.text.TextUtils;

public class ProcessMessege {
    private String processName = "888888";

    private String processScale;

    private String fluctuate;

    public ProcessMessege() {
        super();
    }

    public ProcessMessege(String processName, String processScale, String fluctuate) {
        super();
        this.processName = processName;
        this.processScale = processScale;
        this.fluctuate = fluctuate;
    }

    public ProcessMessege(String processScale, String fluctuate) {
        super();
        this.processScale = processScale;
        this.fluctuate = fluctuate;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProcessScale() {
        return TextUtils.isEmpty(processScale) ? "0" : processScale;
    }

    public void setProcessScale(String processScale) {
        this.processScale = processScale;
    }

    public String getFluctuate() {
        return fluctuate;
    }

    public void setFluctuate(String fluctuate) {
        this.fluctuate = fluctuate;
    }

    @Override
    public String toString() {
        return "ProcessMessege [processName=" + processName + ", processScale=" + processScale + ", fluctuate="
                + fluctuate + "]";
    }

}
