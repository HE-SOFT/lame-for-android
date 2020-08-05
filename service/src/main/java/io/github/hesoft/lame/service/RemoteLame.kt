package io.github.hesoft.lame.service

import io.github.hesoft.lame.ILame
import io.github.hesoft.lame.Lame

/**
 * lame for android
 * Created by HE on 2018/10/31.
 */

internal class RemoteLame : IRemoteLame.Stub() {

    private val lame = Lame()


    override fun initParams(
        inputSampleRate: Int,
        outputSampleRate: Int, outputBitRate: Int, outputQuality: Int, outputMode: Int
    ): Boolean {
        return try {
            lame.initParams(
                ILame.Input(inputSampleRate),
                ILame.Output(outputSampleRate, outputBitRate, outputQuality, outputMode)
            )
            true
        } catch (e: IllegalStateException) {
            false
        }
    }


    @Synchronized
    override fun encodeUnsigned8bit(pcm_l: ByteArray?, pcm_r: ByteArray?, inSize: Int, outBytes: ByteArray?): Int {
        if (pcm_l == null || outBytes == null) return -1
        return lame.encodeUnsigned8bit(pcm_l, pcm_r, inSize, outBytes)
    }

    @Synchronized
    override fun encodeSigned16bit(pcm_l: ByteArray?, pcm_r: ByteArray?, inSize: Int, outBytes: ByteArray?): Int {
        if (pcm_l == null || outBytes == null) return -1
        return lame.encodeSigned16bit(pcm_l, pcm_r, inSize, outBytes)
    }

    @Synchronized
    override fun encodeSigned32bit(pcm_l: ByteArray?, pcm_r: ByteArray?, inSize: Int, outBytes: ByteArray?): Int {
        if (pcm_l == null || outBytes == null) return -1
        return lame.encodeSigned32bit(pcm_l, pcm_r, inSize, outBytes)
    }

    @Synchronized
    override fun encodeSigned32bitFloat(pcm_l: ByteArray?, pcm_r: ByteArray?, inSize: Int, outBytes: ByteArray?): Int {
        if (pcm_l == null || outBytes == null) return -1
        return lame.encodeSigned32bitFloat(pcm_l, pcm_r, inSize, outBytes)
    }

    @Synchronized
    override fun encodeSigned64bitDouble(pcm_l: ByteArray?, pcm_r: ByteArray?, inSize: Int, outBytes: ByteArray?): Int {
        if (pcm_l == null || outBytes == null) return -1
        return lame.encodeSigned64bitDouble(pcm_l, pcm_r, inSize, outBytes)
    }


    @Synchronized
    override fun encodeUnsigned8bitInterleaved(pcm: ByteArray?, inSize: Int, outBytes: ByteArray?): Int {
        if (pcm == null || outBytes == null) return -1
        return lame.encodeUnsigned8bitInterleaved(pcm, inSize, outBytes)
    }

    @Synchronized
    override fun encodeSigned16bitInterleaved(pcm: ByteArray?, inSize: Int, outBytes: ByteArray?): Int {
        if (pcm == null || outBytes == null) return -1
        return lame.encodeSigned16bitInterleaved(pcm, inSize, outBytes)
    }

    @Synchronized
    override fun encodeSigned32bitInterleaved(pcm: ByteArray?, inSize: Int, outBytes: ByteArray?): Int {
        if (pcm == null || outBytes == null) return -1
        return lame.encodeSigned32bitInterleaved(pcm, inSize, outBytes)
    }

    @Synchronized
    override fun encodeSigned32bitFloatInterleaved(pcm: ByteArray?, inSize: Int, outBytes: ByteArray?): Int {
        if (pcm == null || outBytes == null) return -1
        return lame.encodeSigned32bitFloatInterleaved(pcm, inSize, outBytes)
    }

    @Synchronized
    override fun encodeSigned64bitDoubleInterleaved(pcm: ByteArray?, inSize: Int, outBytes: ByteArray?): Int {
        if (pcm == null || outBytes == null) return -1
        return lame.encodeSigned64bitDoubleInterleaved(pcm, inSize, outBytes)
    }


    @Synchronized
    override fun flush(outBytes: ByteArray?): Int {
        if (outBytes == null) return -1
        return lame.flush(outBytes)
    }

    @Synchronized
    override fun destroy() {
        lame.destroy()
    }

}
