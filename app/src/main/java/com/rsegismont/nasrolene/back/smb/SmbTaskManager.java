package com.rsegismont.nasrolene.back.smb;

import com.rsegismont.nasrolene.back.smb.tasks.SmbExploreTask;
import com.rsegismont.nasrolene.back.smb.tasks.SmbListTask;

/**
 * Created by Rolène on 04/02/2018.
 */

public class SmbTaskManager {

    private static final SmbTaskManager instance = new SmbTaskManager();

    public static SmbTaskManager getInstance() {
        return instance;
    }

    private SmbTaskManager () {
        // No instances.
    }

    private SmbExploreTask exploreTask = null;
    private SmbListTask listTask = null;

    public void exploreSmbPath(String url){
        if( exploreTask != null ){
            exploreTask.cancel(true);
        }
        exploreTask = new SmbExploreTask();
        exploreTask.execute(url);
    }

    public void listSmbFiles(String url){
        if( listTask != null ){
            listTask.cancel(true);
        }
        listTask = new SmbListTask();
        listTask.execute(url);
    }
}
