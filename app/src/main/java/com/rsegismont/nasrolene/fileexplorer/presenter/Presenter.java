package com.rsegismont.nasrolene.fileexplorer.presenter;

import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ListView;

import com.rsegismont.nasrolene.R;
import com.rsegismont.nasrolene.fileexplorer.FileExplorerFragment;
import com.rsegismont.nasrolene.fileexplorer.model.FileArrayAdapter;
import com.rsegismont.nasrolene.fileexplorer.model.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

/**
 * @Author Tom Farrell.   License: Whatever...
 *         The main job of the presenter is to marshall data to and from the view.   Logic in the
 *         presenter is kept to a minimum, with only the logic required to format and marshall data between
 *         the view and model done here.
 **/
public class Presenter implements LoaderManager.LoaderCallbacks<List<SmbFile>> {
    private FileExplorerFragment mView; //Our view.
    private Model mModel; //Our model.
    private FileArrayAdapter mFileArrayAdapter; //The adapter containing data for our list.
    private List<SmbFile> mData; //The list of all files for a specific dir.
    private AsyncTaskLoader<List<SmbFile>> mFileLoader; /*Loads the list of files from the model in
    a background thread.*/

    public Presenter(FileExplorerFragment mView) {
        this.mView = mView;
        mModel = new Model();
        mModel.setupCurrentDir();
        mData = new ArrayList<>();
        init();
    }

    private void init() {
        //Instantiate and configure the smbFiles adapter with an empty list that our loader will update..
        mFileArrayAdapter = new FileArrayAdapter(mView.getActivity(),
                R.layout.list_row, mData);

        mView.setListAdapter(mFileArrayAdapter);

        /*Start the AsyncTaskLoader that will update the adapter for
        the ListView. We update the adapter in the onLoadFinished() callback.
        */
        mView.getActivity().getLoaderManager().initLoader(0, null, this);

        //Grab our first list of results from our loader.  onFinishLoad() will call updataAdapter().
        mFileLoader.forceLoad();
    }

    /*Called to update the Adapter with a new list of files when mCurrentDir changes.*/
    private void updateAdapter(List<SmbFile> data) {
        //clear the old data.
        mFileArrayAdapter.clear();
        //add the new data.
        mFileArrayAdapter.addAll(data);
        //inform the ListView to refrest itself with the new data.
        mFileArrayAdapter.notifyDataSetChanged();
    }

    public void listItemClicked(ListView l, View v, int position, long id) throws SmbException {
        //The smbFiles we clicked based on row position where we clicked.  I could probably word that better. :)
        final SmbFile fileClicked = mFileArrayAdapter.getItem(position);


                try {
                    if (fileClicked.isDirectory()) {
                        //we are changing dirs, so save the previous dir as the one we are currently in.
                        mModel.setmPreviousDir(mModel.getmCurrentDir());

                        //set the current dir to the dir we clicked in the listview.
                        mModel.setmCurrentDir(fileClicked);

                        //Let the loader know that our content has changed and we need a new load.
                        if (mFileLoader.isStarted()) {
                            mView.getActivity().getLoaderManager().restartLoader(0, null, Presenter.this);
                            mFileLoader.forceLoad();
                        }
                    } else { //Otherwise, we have clicked a smbFiles, so attempt to open it.
                        //TODO : ouvrir fichier
                        openFile(Uri.fromFile(new File(fileClicked.getPath())));
                    }
                }
                catch (SmbException e){
                    e.printStackTrace();
                }



    }

    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }

    //Fires intents to handle files of known mime types.
    private void openFile(Uri fileUri) {


        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        String mimeType = myMime.getMimeTypeFromExtension(fileExt(fileUri.getPath())).substring(1);
        newIntent.setDataAndType(Uri.fromFile(new File(fileUri.getPath())),mimeType);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            mView.getActivity().getApplicationContext().startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
           e.printStackTrace();
        }
    }

    /*Called when the user presses the home button on the ActionBar to navigate back to
     our previous location, if we have one.*/
    public void homePressed() {
        //If we have a previous dir to go back to, do it.
        if (mModel.hasmPreviousDir()) {
            mModel.setmCurrentDir(mModel.getmPreviousDir());

            //Our content has changed, so we need a new load.
            mFileLoader.onContentChanged();
        }
    }

    //Loader callbacks.
    @Override
    public Loader<List<SmbFile>> onCreateLoader(int id, Bundle args) {
        mFileLoader = new AsyncTaskLoader<List<SmbFile>>(mView.getActivity()) {

            //Get our new data load.
            @Override
            public List<SmbFile> loadInBackground() {
                Log.i("Loader", "loadInBackground()");
                try {

                    return mModel.getAllFiles(mModel.getmCurrentDir());
                } catch (SmbException e) {
                    e.printStackTrace();
                    return new ArrayList<SmbFile>();
                }
            }
        };

        return mFileLoader;
    }

    //Called when the loader has finished acquiring its load.
    @Override
    public void onLoadFinished(Loader<List<SmbFile>> loader, List<SmbFile> data) {

        this.mData = data;

        /* My data source has changed so now the adapter needs to be reset to reflect the changes
        in the ListView.*/
        updateAdapter(data);
    }

    @Override
    public void onLoaderReset(Loader<List<SmbFile>> loader) {
        //not used for this data source.
    }
}
