package com.rsegismont.nasrolene.fileexplorer.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.rsegismont.nasrolene.R;

import java.io.File;
import java.util.List;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

/**
 * @Author Tom Farrell.   License: Whatever...
 *
 * This class represents the adapter for the listfragment.
 */
public class FileArrayAdapter extends ArrayAdapter<SmbFile> {
    private Context mContext; //Activity context.
    private int mResource; //Represents the list_rowl smbFiles (our rows) as an int e.g. R.layout.list_row
    private List<SmbFile> mObjects; //The List of objects we got from our model.

    public FileArrayAdapter(Context c, int res, List<SmbFile> o) {
        super(c, res, o);
        mContext = c;
        mResource = res;
        mObjects = o;
    }

    public FileArrayAdapter(Context c, int res) {
        super(c, res);
        mContext = c;
        mResource = res;
    }

    /*Does exactly what it looks like.  Pulls out a specific File Object at a specified index.
    Remember that our FileArrayAdapter contains a list of Files it gets from our model's getAllFiles(),
    so getitem(0) is the first smbFiles in that List, getItem(1), the second, etc.  ListView uses this
    method internally.*/
    @Override
    public SmbFile getItem(int i) {
        return mObjects.get(i);
    }

    /** Allows me to pull out specific views from the row xml smbFiles for the ListView.   I can then
    *make any modifications I want to the ImageView and TextViews inside it.
    *@param position - The position of an item in the List received from my model.
    *@param convertView - list_row.xml as a View object.
    *@param parent - The parent ViewGroup that holds the rows.  In this case, the ListView.
    ***/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*This is the entire smbFiles [list_rowl] with its RelativeLayout, ImageView, and two
        TextViews.  It will always be null the very first time, so we need to inflate it with a
           LayoutInflater.*/
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater.from(mContext));

            v = inflater.inflate(mResource, null);
        }

        /* We pull out the ImageView and TextViews so we can set their properties.*/
        ImageView iv = (ImageView) v.findViewById(R.id.imageView);

        TextView nameView = (TextView) v.findViewById(R.id.name_text_view);

        TextView detailsView = (TextView) v.findViewById(R.id.details_text_view);

        SmbFile file = getItem(position);

        /* If the smbFiles is a dir, set the image view's image to a folder, else, a smbFiles. */
        try {
            if (file.isDirectory()) {
                iv.setImageResource(R.drawable.folderxxhdpi);
            } else {
                iv.setImageResource(R.drawable.filexxhdpi);
                if (file.length() > 0) {
                    detailsView.setText(String.valueOf(file.length()));
                }
            }
        } catch (SmbException e) {
            e.printStackTrace();
        }

        //Finally, set the name of the smbFiles or directory.
        nameView.setText(file.getName());

        //Send the view back so the ListView can show it as a row, the way we modified it.
        return v;
    }
}
