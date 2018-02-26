package com.rsegismont.nasrolene.back.eventbus.events;

import jcifs.smb.SmbFile;

/**
 * Created by Rolène on 04/02/2018.
 */

public class SmbFileEvent {

    public String url;
    public SmbFile file;

    public SmbFileEvent(String url,SmbFile file){
        this.url = url;
        this.file = file;
    }


}
