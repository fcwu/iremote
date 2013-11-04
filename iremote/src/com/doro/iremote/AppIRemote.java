package com.doro.iremote;

import android.app.Application;

public class AppIRemote extends Application {
	private String mServer = "192.168.0.50:8850";

	public String getServer() {
		return mServer;
	}

	public void setServer(String mServer) {
		this.mServer = mServer;
	}
	
}
