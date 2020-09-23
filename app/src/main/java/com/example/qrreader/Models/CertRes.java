package com.example.qrreader.Models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Model of ssl validation results
 */

public class CertRes {
    private String link;
    private ArrayList<String> cert;
    private ArrayList<Boolean> status;

    public String getLink()
    {return link;}

    public int getLength()
    {return cert.size();}

    public void setCert(String link)
    {
        cert.add(link);
    }

    public String getCert(int pos)
    {
        return cert.get(pos);
    }

    public void setStatus(Boolean status)
    {
        this.status.add(status);
    }

    public Boolean getStatus(int pos)
    {
        return status.get(pos);
    }

    public CertRes(String link)
    {
        this.link=link;
        cert = new ArrayList<String>();
        status = new ArrayList<Boolean>();
    }


}
