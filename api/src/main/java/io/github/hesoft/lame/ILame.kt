package io.github.hesoft.lame

import androidx.annotation.IntRange

/**
 * lame
 * Created by HE on 2018/10/12.
 */
interface ILame {

    //region configuration
    /**
     * @param sampleRate input sample rate in Hz,  default = 44100 Hz
     */
    data class Input(val sampleRate: Int = 44100)

    /**
     * @param sampleRate output sample rate in Hz
     * default = 0: LAME picks best value based on the amount of compression
     * MPEG only allows:
     * MPEG1    32, 44.1,   48khz
     * MPEG2    16, 22.05,  24
     * MPEG2.5   8, 11.025, 12
     *
     *
     * Valid value can only be:
     * 44100;48000;32000;22050;24000;16000;11025;12000;8000
     *
     * @param bitRate    Set one of
     * - brate
     * - compression ratio.
     *
     *
     * Default is compression ratio of 11.
     *
     * @param quality    True quality is determined by the bitrate but this variable will effect
     * quality by selecting expensive or cheap algorithms.
     *
     *
     * quality=0..9.  0=best (very slow).  9=worst.
     *
     *
     * recommended:
     * 3     near-best quality, not too slow
     * 5     good quality, fast
     * 7     ok quality, really fast
     * @param mode       @see MPEGMode
     */
    data class Output(
        val sampleRate: Int = 22050,
        val bitRate: Int = 160,
        @IntRange(from = 0, to = 10) val quality: Int = 3,
        @MPEGMode val mode: Int = MPEGMode.MONO
    )

    fun initParams(input: Input = Input(), output: Output = Output())
    //endregion

    //region encode

    // 输出格式是单声道的话pcm_r可为空
    // 输出格式是立体声的话pcm_r不能为空，否则不能正常转换(转换输出大小为0)

    fun encodeUnsigned8bit(pcm_l: ByteArray, pcm_r: ByteArray?, inSize: Int, out: ByteArray): Int
    fun encodeSigned16bit(pcm_l: ByteArray, pcm_r: ByteArray?, inSize: Int, out: ByteArray): Int

    fun encodeSigned32bit(pcm_l: ByteArray, pcm_r: ByteArray?, inSize: Int, out: ByteArray): Int

    fun encodeSigned32bitFloat(pcm_l: ByteArray, pcm_r: ByteArray?, inSize: Int, out: ByteArray): Int

    fun encodeSigned64bitDouble(pcm_l: ByteArray, pcm_r: ByteArray?, inSize: Int, out: ByteArray): Int


    fun encodeUnsigned8bitInterleaved(pcm: ByteArray, inSize: Int, out: ByteArray): Int

    fun encodeSigned16bitInterleaved(pcm: ByteArray, inSize: Int, out: ByteArray): Int

    fun encodeSigned32bitInterleaved(pcm: ByteArray, inSize: Int, out: ByteArray): Int

    fun encodeSigned32bitFloatInterleaved(pcm: ByteArray, inSize: Int, out: ByteArray): Int

    fun encodeSigned64bitDoubleInterleaved(pcm: ByteArray, inSize: Int, out: ByteArray): Int

    fun flush(out: ByteArray): Int
    //endregion

    fun destroy()

}
