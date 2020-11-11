package io.github.hesoft.lame;

import androidx.annotation.IntRange;

/**
 * lame for android
 * Created by HE on 2018/10/26.
 */
class Native {

    static {
        System.loadLibrary("lame-jni");
    }

    public static native String getVersion();


    static native long create();
    static native void close(long p);
    /** 执行成功后再次执行返回-1 */
    static native int init_params(long p);

    static native void set_input(long p, int sampleRate, int channels);
    static native void set_output(
        long p, int sampleRate, int bitRate,
        @IntRange(from = 0, to = 10) int quality, @MPEGMode int mode
    );

    //region to do?
    /*
    static native void set_in_samplerate(long p, int in_samplerate);
    static native int get_in_samplerate(long p);

    static native void set_num_channels(long p, int num_channels);
    static native int get_num_channels(long p);
    */

    /*
     * Internal algorithm selection.
     * True quality is determined by the bitrate but this variable will effect
     * quality by selecting expensive or cheap algorithms.
     * quality=0..9.  0=best (very slow).  9=worst.
     *
     * recommended:
     * 3     near-best quality, not too slow
     * 5     good quality, fast
     * 7     ok quality, really fast
     */
    /*
    static native void set_quality(long p, @IntRange(from = 0, to = 10) int quality);
    @IntRange(from = 0, to = 10)
    static native int get_quality(long p);
    */
    //endregion

    //region encode
    static native int encodeByte(long p, byte[] pcm_l, byte[] pcm_r, int inSize, byte[] out);

    static native int encode(long p, byte[] pcm_l, byte[] pcm_r, int inSize, byte[] out);

    static native int encodeInt(long p, byte[] pcm_l, byte[] pcm_r, int inSize, byte[] out);

    static native int encodeFloat(long p, byte[] pcm_l, byte[] pcm_r, int inSize, byte[] out);

    static native int encodeDouble(long p, byte[] pcm_l, byte[] pcm_r, int inSize, byte[] out);


    static native int encodeInterleavedByte(long p, byte[] pcm, int inSize, byte[] out);

    static native int encodeInterleaved(long p, byte[] pcm, int inSize, byte[] out);

    static native int encodeInterleavedInt(long p, byte[] pcm, int inSize, byte[] out);

    static native int encodeInterleavedFloat(long p, byte[] pcm, int inSize, byte[] out);

    static native int encodeInterleavedDouble(long p, byte[] pcm, int inSize, byte[] out);
    //endregion

    static native int flush(long p, byte[] out);

}
