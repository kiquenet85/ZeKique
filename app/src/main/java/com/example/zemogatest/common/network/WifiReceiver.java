package com.example.zemogatest.common.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

/**
 * This broadcast receiver detects when connection changes appears.
 *
 * @author n.diazgranados
 */
public class WifiReceiver extends BroadcastReceiver {

    public static final String WIFI_RECEIVER_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    private NetworkManager networkManager;

    @Inject
    public WifiReceiver(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isOnline = networkManager.isOnline();

        NetworkConnectionChange networkConnectionChange = new NetworkConnectionChange();
        networkConnectionChange.setOnline(isOnline);
    }

    /**
     * The Event produced by the interactor
     */
    public class NetworkConnectionChange {
        private boolean isOnline;

        public NetworkConnectionChange() {
        }

        public boolean isOnline() {
            return isOnline;
        }

        public void setOnline(boolean online) {
            isOnline = online;
        }

    }
}
