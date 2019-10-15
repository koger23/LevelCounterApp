package com.kogero.levelcounter.hub

import android.os.AsyncTask
import com.microsoft.signalr.HubConnection


class HubConnectionTask : AsyncTask<HubConnection, Void, Void>() {

    override fun doInBackground(vararg hubConnections: HubConnection): Void? {
        val hubConnection = hubConnections[0]
        hubConnection.start().blockingAwait()
        return null
    }
}