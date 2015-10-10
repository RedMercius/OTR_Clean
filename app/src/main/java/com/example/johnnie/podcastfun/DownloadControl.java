/*
 * Copyright 2015 © Johnnie Ruffin
 *
 * Unless required by applicable law or agreed to in writing, software is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package com.example.johnnie.podcastfun;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

//////////////////////////////////////////////////////////////////////////////////////
//
///  @class DownloadControl
//
///  @author Johnnie
//
///  @brief A class to control the Download of files
//
///  @created 10/10/2015
//
//////////////////////////////////////////////////////////////////////////////////////
public class DownloadControl extends Activity {
    private long enqueue;
    private DownloadManager dm;

    /** Called when the activity is first created. */
    public void DownloadControl(Activity context) {

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void downloadFile(String filePath) {
        filePath = "http://www.vogella.de/img/lars/LarsVogelArticle7.png";
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Request request = new Request(
                Uri.parse(filePath));
        enqueue = dm.enqueue(request);
    }
}