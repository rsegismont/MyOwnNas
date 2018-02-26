package com.rsegismont.nasrolene.fileexplorer.model;

import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

/**
* @Author Tom Farrell.   License: Whatever...
 *
 * The model for a simple File Manager.
*/
public class Model {
    private SmbFile mCurrentDir; //Our current location.
    private SmbFile mPreviousDir; //Our previous location.
    private Stack<SmbFile> mHistory; //Our navigation History.
    public static final String TAG = "Current dir"; //for debugging purposes.

    public Model() {
        init();
    }

    private void init() {
        mHistory = new Stack<>();
    }

    /* Now for the getters, setters, and utlity methods.*/
    
    //getInstance the current directory.
    public SmbFile getmCurrentDir() {
        return mCurrentDir;
    }

    //set the current directory.
    public void setmCurrentDir(SmbFile mCurrentDir) {
        this.mCurrentDir = mCurrentDir;
    }

    //Returns whether or not we have a previous dir in our history.  If the stack is not empty, we have one.
    public boolean hasmPreviousDir() {
        return !mHistory.isEmpty();
    }

    //return the previous dir and remove it from the stack.
    public SmbFile getmPreviousDir() {
        return mHistory.pop();
    }

    //set the previous dir for navigation.
    public void setmPreviousDir(SmbFile mPreviousDir) {
        this.mPreviousDir = mPreviousDir;
        mHistory.add(mPreviousDir);

    }

    //Returns a sorted list of all dirs and files in a given directory.
    public List<SmbFile> getAllFiles(SmbFile f) throws SmbException {
        SmbFile[] allFiles = f.listFiles();

        /* I want all directories to appear before files do, so I have separate lists for both that are merged into one later.*/
        List<SmbFile> dirs = new ArrayList<>();
        List<SmbFile> files = new ArrayList<>();

        for (SmbFile file : allFiles) {
            if (file.isDirectory()) {
                dirs.add(file);
            } else {
                files.add(file);
            }
        }

        //TODO : trier les listes
       // Collections.sort(dirs);
       // Collections.sort(files);

        /*Both lists are sorted, so I can just add the files to the dirs list.
        This will give me a list of dirs on top and files on bottom. */
        dirs.addAll(files);

        return dirs;
    }
    
    //Try to determine the mime type of a file based on extension.
    public String getMimeType(Uri uri) {
        String mimeType = null;
        Log.e("DEBUG","uri"+uri.getPath());
        String extension = MimeTypeMap.getFileExtensionFromUrl(uri.getPath());
        Log.e("DEBUG","extension"+extension);
        if (MimeTypeMap.getSingleton().hasExtension(extension)) {

            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return mimeType;
    }

    public void setupCurrentDir() {
        String user = "Guest";
        String pass ="";

        String url = "smb://mafreebox.freebox.fr/Disque dur/";
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(
                "", user, pass);
        try {
            mCurrentDir = new SmbFile(url, auth);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}