package com.kogero.levelcounter.hub

import android.os.AsyncTask
import com.kogero.levelcounter.helpers.HttpsTrustManager
import com.microsoft.signalr.HubConnection


class HubConnectionTask : AsyncTask<HubConnection, Void, Void>() {

    override fun doInBackground(vararg hubConnections: HubConnection): Void? {
        HttpsTrustManager.allowAllSSL()
        val hubConnection = hubConnections[0]
        hubConnection.start().blockingAwait()
        return null
    }
}