package com.rsegismont.nasrolene.back.smb;

import jcifs.smb.NtlmPasswordAuthentication;

/**
 * Created by Rolène on 04/02/2018.
 */

public class SmbAuthentication {

    private static SmbAuthentication instance = null;
    private final String mUser,mPassword,mDomain;
    private final NtlmPasswordAuthentication mAuthToken;

    public static SmbAuthentication getInstance(){
        if(instance==null){
            instance = new SmbAuthentication();
        }
        return  instance;
    }

    SmbAuthentication(){
        super();
        mUser = "Guest";
        mPassword ="";
        mDomain = "";
        
        mAuthToken = new NtlmPasswordAuthentication(
                mDomain, mUser, mPassword);
    }

    public String getUser() {
        return mUser;
    }

    public String getPassword() {
        return mPassword;
    }

    public NtlmPasswordAuthentication getAuthToken() {
        return mAuthToken;
    }
}
