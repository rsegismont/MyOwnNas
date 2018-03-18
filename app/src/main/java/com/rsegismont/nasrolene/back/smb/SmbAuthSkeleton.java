package com.rsegismont.nasrolene.back.smb;

import jcifs.smb.NtlmPasswordAuthentication;

/**
 * Created by Rolène on 17/03/2018.
 */

public interface SmbAuthSkeleton {

    public String getUser();

    public String getPassword();

    public NtlmPasswordAuthentication getAuthToken();

    public String getUrl();

    public String getPhotoPath();

}
