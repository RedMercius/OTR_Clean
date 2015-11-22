/*
 * Copyright 2015 © Johnnie Ruffin
 *
 * Unless required by applicable law or agreed to in writing, software is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package com.example.johnnie.podcastfun;

/////////////////////////////////////////////////////////////////////////////
//
/// @class CustomList
//
/// @brief CustomList class controls the item list
//
/// @author Johnnie Ruffin
//
////////////////////////////////////////////////////////////////////////////

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CustomList extends ArrayAdapter<String> {

    // This is an attempt at a view holder pattern
    static class ViewHolderItem {

        TextView txtTitle;
        TextView txtStatus;

        ImageButton playButton;
        ImageView stopButton;
        ImageButton deleteButton;
        ImageButton downloadButton;
    }

    private final Activity context;
    private final String[] radioTitle;
    private final Integer[] imageButtonList;
    private String artist;
    private MediaControl mc;
    private BroadcastReceiver receiver;
    private List<String> mRemoveList;
    private boolean mdownloadInProgress;
    final String TAG = "CustomList";

    public CustomList(Activity context, String[] radioTitle, Integer[] imageButtonList, String artist) {
        super(context, R.layout.custom_list_multi, radioTitle);

        MediaPlayer mp;
        this.context = context;
        this.radioTitle = radioTitle;
        this.imageButtonList = imageButtonList;
        mp = new MediaPlayer();
        this.artist = artist;
        mRemoveList = new ArrayList<>();
        mdownloadInProgress = false;

        mc =
                new MediaControl(context, mp, artist);

        IntentFilter filter = new IntentFilter();

        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (mdownloadInProgress) {
                    String filename;
                    Bundle extras = intent.getExtras();
                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
                    Cursor c = mc.dc.dm.query(q);

                    if (c.moveToFirst()) {
                        int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            String filePath = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                            filename = filePath.substring(filePath.lastIndexOf('/') + 1, filePath.length());
                            Log.d(TAG, "Download Complete: " + filename);
                            mRemoveList.remove(filename);
                        }
                    }

                    c.close();
                    notifyDataSetChanged();
                }
            }
        };

        context.registerReceiver(receiver, filter);
    }

    public boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(SelectActivity.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /*public Boolean isAvailable() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1    www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            if(reachable){
                System.out.println("Internet access");
                return true;
            }
            else{
                System.out.println("No Internet access");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }*/

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        final ViewHolderItem viewHolder;

        context.setTitle(artist);
        if (view == null) {
            viewHolder = new ViewHolderItem();

            view = View.inflate( context, R.layout.custom_list_multi, null);
            viewHolder.txtTitle = (TextView) view.findViewById(R.id.txt);
            viewHolder.txtStatus = (TextView) view.findViewById(R.id.txtstatus);

            viewHolder.playButton = (ImageButton) view.findViewById(R.id.playbtn);
            viewHolder.stopButton = (ImageView) view.findViewById(R.id.stopbtn);
            viewHolder.deleteButton = (ImageButton) view.findViewById(R.id.deletebtn);
            viewHolder.downloadButton = (ImageButton) view.findViewById(R.id.downloadbtn);

            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolderItem) view.getTag();
        }

        final String mediaTitle = radioTitle[position];

        RadioTitle rt = new RadioTitle();

        rt.initTitles();

        String MediaFile = null;

        switch (artist) {
            case "Burns And Allen": {
                for (String mediaFile : rt.getBaMap().keySet()) {
                    if (rt.getBaMap().get(mediaFile).equals(mediaTitle)) {
                        MediaFile = mediaFile;
                    }
                }
                break;
            }

            case "Fibber McGee And Molly":
            {
                for (String mediaFile : rt.getFbMap().keySet()) {
                    if (rt.getFbMap().get(mediaFile).equals(mediaTitle)) {
                        MediaFile = mediaFile;
                    }
                }
                break;
            }

            case "Martin And Lewis":
            {
                for (String mediaFile : rt.getMlMap().keySet()) {
                    if (rt.getMlMap().get(mediaFile).equals(mediaTitle)) {
                        MediaFile = mediaFile;
                    }
                }
                break;
            }

            case "The Great GilderSleeves":
            {
                for (String mediaFile : rt.getGlMap().keySet()) {
                    if (rt.getGlMap().get(mediaFile).equals(mediaTitle)) {
                        MediaFile = mediaFile;
                    }
                }
                break;
            }

            case "XMinus1":
            {
                for (String mediaFile : rt.getXMMap().keySet()) {
                    if (rt.getXMMap().get(mediaFile).equals(mediaTitle)) {
                        MediaFile = mediaFile;
                    }
                }
                break;
            }

            case "Inner Sanctum":
            {
                for (String mediaFile : rt.getIsMap().keySet()) {
                    if (rt.getIsMap().get(mediaFile).equals(mediaTitle)) {
                        MediaFile = mediaFile;
                    }
                }
                break;
            }

            case "Dimension X":
            {
                for (String mediaFile : rt.getDxMap().keySet()) {
                    if (rt.getDxMap().get(mediaFile).equals(mediaTitle)) {
                        MediaFile = mediaFile;
                    }
                }
                break;
            }

            case "Night Beat":
            {
                for (String mediaFile : rt.getNbMap().keySet()) {
                    if (rt.getNbMap().get(mediaFile).equals(mediaTitle)) {
                        MediaFile = mediaFile;
                    }
                }
                break;
            }

            case "Speed":
            {
                for (String mediaFile : rt.getSgMap().keySet()) {
                    if (rt.getSgMap().get(mediaFile).equals(mediaTitle)) {
                        MediaFile = mediaFile;
                    }
                }
                break;
            }
        }

            final String mediaFileName = MediaFile;

            boolean isItInRaw = mc.checkResourceInRaw(MediaFile);
            final boolean doesMediaExist = mc.checkForMedia(MediaFile);

            viewHolder.txtTitle.setText(mediaTitle);

        boolean ignoreThisItem = false;

        if (mRemoveList != null && mediaFileName != null) {
            for (int i = 0; i < mRemoveList.size(); ++i) {
                if (mediaFileName.equals(mRemoveList.get(i))) {
                    ignoreThisItem = true;
                }
            }
        }

            if (!isItInRaw && !doesMediaExist && !ignoreThisItem) {
                viewHolder.downloadButton.setImageResource(imageButtonList[4]);
                viewHolder.playButton.setImageResource(imageButtonList[0]);
                viewHolder.stopButton.setImageResource(imageButtonList[8]);

                viewHolder.txtStatus.setVisibility(View.INVISIBLE);
                viewHolder.deleteButton.setVisibility(View.INVISIBLE);

                viewHolder.downloadButton.setVisibility(View.VISIBLE);
                viewHolder.txtTitle.setVisibility(View.VISIBLE);
                viewHolder.stopButton.setVisibility(View.VISIBLE);
                viewHolder.playButton.setVisibility(View.VISIBLE);
            }

            if ((isItInRaw || doesMediaExist) && !ignoreThisItem) {
                viewHolder.playButton.setImageResource(imageButtonList[0]);
                viewHolder.deleteButton.setImageResource(imageButtonList[7]);

                viewHolder.downloadButton.setVisibility(View.INVISIBLE);
                viewHolder.txtStatus.setVisibility(View.INVISIBLE);
                viewHolder.stopButton.setVisibility(View.INVISIBLE);

                viewHolder.deleteButton.setVisibility(View.VISIBLE);
                viewHolder.playButton.setVisibility(View.VISIBLE);
            }

        if (ignoreThisItem) {
            Log.d(TAG, "Ignore This Item: " + mediaFileName);

            viewHolder.downloadButton.setVisibility(View.INVISIBLE);
            viewHolder.playButton.setVisibility(View.INVISIBLE);
            viewHolder.stopButton.setVisibility(View.INVISIBLE);
            viewHolder.deleteButton.setVisibility(View.INVISIBLE);
            viewHolder.txtStatus.setVisibility(View.VISIBLE);
            viewHolder.txtStatus.setText(context.getResources().getString(R.string.downloading));
        }
            viewHolder.playButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // if we need to stream this, check for internet connection.
                    if (!doesMediaExist) {
                        if (!isNetworkAvailable()) {
                            Toast.makeText(context, context.getResources().getString(R.string.no_internet_stream),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    final Intent i = new Intent(context, PlayActivity.class);
                    i.putExtra("MediaTitle", mediaFileName);
                    i.putExtra("Selection", artist);
                    i.putExtra("Title", mediaTitle);
                    context.startActivity(i);
                    context.finish();
                }
            });

            viewHolder.downloadButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (!mdownloadInProgress) {
                        mdownloadInProgress = true;

                    }

                    // if network connection is down, inform the user that we cannot download.
                    if (!isNetworkAvailable())
                    {
                        Toast.makeText(context, context.getResources().getString(R.string.no_internet_stream),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mc.downloadMedia(mediaFileName);
                    mRemoveList.add(mediaFileName);
                    notifyDataSetChanged();
                    Toast.makeText(context, context.getResources().getString(R.string.download_in_progress) + mediaFileName, Toast.LENGTH_SHORT).show();
                }
            });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                mc.deleteMedia(mediaFileName);
                notifyDataSetChanged();
                Toast.makeText(context, context.getResources().getString(R.string.deleting) + " " + mediaFileName, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void cleanUp(Activity context)
    {
        context.unregisterReceiver(receiver);
    }
}