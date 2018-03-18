package com.rsegismont.nasrolene.back.eventbus.events;

import java.util.List;

import jcifs.smb.SmbFile;

/**
 * Created by Rolène on 04/02/2018.
 */

public class SmbFileEvent {


    public List<SmbFile> smbFiles;

    public SmbFileEvent(List<SmbFile> files){

        this.smbFiles = files;
    }


}
