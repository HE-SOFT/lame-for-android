package io.github.hesoft.lame.test

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import androidx.test.core.app.ApplicationProvider
import io.github.hesoft.lame.service.ILameService
import io.github.hesoft.lame.service.IRemoteLame
import io.github.hesoft.lame.BufferReader
import io.github.hesoft.lame.MPEGMode
import org.junit.Test
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.CountDownLatch

/**
 * lame for android
 * Created by HE on 2018/10/29.
 */
class LameServiceTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val assetManager = context.assets
    private val intent = Intent("io.github.hesoft.lame.MP3EncodeSERVICE")
    private val outBuff = ByteArray(8192)

    @Test
    fun test() {
        val latch = CountDownLatch(1)

        context.bindService(intent, object : ServiceConnection {

            override fun onServiceConnected(name: ComponentName?, serviceBinder: IBinder) {
                val service = ILameService.Stub.asInterface(serviceBinder)
                val lame = service.create(Binder())
                test(lame)
                latch.countDown()
            }

            override fun onServiceDisconnected(name: ComponentName?) {
            }

        }, Context.BIND_AUTO_CREATE)

        latch.await()
    }


    private fun test(lame: IRemoteLame) {
        val outFile = File(context.dataDir, "out.mp3")
        if (outFile.exists() && !outFile.delete()) throw RuntimeException()
        val out = FileOutputStream(outFile)

        lame.initParams(
            44100,
            44100, 320, 3, MPEGMode.MONO
        )


        fun encode(fileName: String, byteSize: Int, block: (ByteArray, Int) -> Int) {
            val buffer = BufferReader(1024).apply { this.byteSize = byteSize }
            test(out, fileName, buffer, block)
        }

        encode("pcm_unsigned_8bit_mono.wav", 1) { buff, read ->
            lame.encodeUnsigned8bit(buff, buff, read, outBuff)
        }
        encode("pcm_unsigned_8bit_stereo.wav", 2) { buff, read ->
            lame.encodeUnsigned8bitInterleaved(buff, read, outBuff)
        }
        encode("pcm_signed_16bit_mono.wav", 2) { buff, read ->
            lame.encodeSigned16bit(buff, buff, read, outBuff)
        }
        encode("pcm_signed_16bit_stereo.wav", 4) { buff, read ->
            lame.encodeSigned16bitInterleaved(buff, read, outBuff)
        }
        encode("pcm_signed_32bit_mono.wav", 4) { buff, read ->
            lame.encodeSigned32bit(buff, buff, read, outBuff)
        }
        encode("pcm_signed_32bit_stereo.wav", 8) { buff, read ->
            lame.encodeSigned32bitInterleaved(buff, read, outBuff)
        }

        flush(out, lame)
        out.close()
        lame.destroy()
    }

    private fun test(
        out: FileOutputStream, fileName: String, readBuff: BufferReader, block: (ByteArray, Int) -> Int
    ) {
        val input = assetManager.open(fileName)
        // xxx jump file header

        var read: Int
        while (true) {
            read = readBuff.read(input)
            if (read <= 0) break
            val outSize = block(readBuff.get(), read)
            if (outSize == -1) throw RuntimeException()
            out.write(outBuff, 0, outSize)
        }
        input.close()
    }

    @Throws(IOException::class)
    private fun flush(out: FileOutputStream, lame: IRemoteLame) {
        val outSize = lame.flush(outBuff)
        if (outSize == -1) throw RuntimeException()
        out.write(outBuff, 0, outSize)
    }

}