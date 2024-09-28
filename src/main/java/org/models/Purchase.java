package org.models;

import java.sql.Date;

public class Purchase {

    private String puid;
    private Date pudate;

    public Purchase(String puid, Date pudate) {
        this.puid = puid;
        this.pudate = pudate;
    }
    public Purchase(){}

    public String getPuid() {
        return puid;
    }

    public void setPuid(String puid) {
        this.puid = puid;
    }

    public Date getPudate() {
        return pudate;
    }

    public void setPudate(Date pudate) {
        this.pudate = pudate;
    }
}
