package com.ratnavidyakanawade.myapplication.reusables;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class CommonFunctions {

    //Check Network availability
    public static boolean chkStatus(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static void displayToast(final Context context, final String msg) {

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

    }
}