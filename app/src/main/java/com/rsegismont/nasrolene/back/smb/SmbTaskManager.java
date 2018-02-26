package com.rsegismont.nasrolene.back.smb;

import com.rsegismont.nasrolene.back.smb.tasks.SmbExploreTask;

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

    public void exploreSmbPath(String url){
        if( exploreTask != null ){
            exploreTask.cancel(true);
        }
        exploreTask = new SmbExploreTask();
        exploreTask.execute(url);
    }
}
