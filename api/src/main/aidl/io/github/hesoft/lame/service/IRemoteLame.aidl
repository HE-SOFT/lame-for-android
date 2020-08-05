package io.github.hesoft.lame.service;

interface IRemoteLame {

    boolean initParams(
        int inputSampleRate,
        int outputSampleRate, int outputBitRate, int outputQuality, int outputMode
    );

    int encodeUnsigned8bit(in byte[] pcm_l, in byte[] pcm_r, int inSize, out byte[] outBytes);
    int encodeSigned16bit(in byte[] pcm_l, in byte[] pcm_r, int inSize, out byte[] outBytes);
    int encodeSigned32bit(in byte[] pcm_l, in byte[] pcm_r, int inSize, out byte[] outBytes);
    int encodeSigned32bitFloat(in byte[] pcm_l, in byte[] pcm_r, int inSize, out byte[] outBytes);
    int encodeSigned64bitDouble(in byte[] pcm_l, in byte[] pcm_r, int inSize, out byte[] outBytes);

    int encodeUnsigned8bitInterleaved(in byte[] pcm, int inSize, out byte[] outBytes);
    int encodeSigned16bitInterleaved(in byte[] pcm, int inSize, out byte[] outBytes);
    int encodeSigned32bitInterleaved(in byte[] pcm, int inSize, out byte[] outBytes);
    int encodeSigned32bitFloatInterleaved(in byte[] pcm, int inSize, out byte[] outBytes);
    int encodeSigned64bitDoubleInterleaved(in byte[] pcm, int inSize, out byte[] outBytes);


    int flush(out byte[] outBytes);

    void destroy();
}