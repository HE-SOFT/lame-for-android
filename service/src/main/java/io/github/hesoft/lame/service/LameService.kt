package io.github.hesoft.lame.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteException

/**
 * lame for android
 * Created by HE on 2018/10/29.
 */
class LameService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = START_NOT_STICKY

    override fun onBind(intent: Intent?): IBinder {
        return object : ILameService.Stub() {
            override fun create(token: IBinder): IRemoteLame {
                val lame = RemoteLame()

                try {
                    token.linkToDeath({
                        // Service.onDestroy可能先触发
                        lame.destroy()
                    }, 0)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }

                return lame
            }
        }
    }

}