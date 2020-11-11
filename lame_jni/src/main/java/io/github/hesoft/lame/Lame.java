package io.github.hesoft.lame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * lame for android
 * Created by HE on 2018/10/26.
 */
public class Lame implements ILame {

    private final long p;

    public Lame() {
        p = Native.create();
    }

    private boolean hasInitParam = false;

    @Override
    public void initParams(@NonNull Input input, @NonNull Output output) {
        if (hasInitParam) return;

        Native.set_output(p, output.getSampleRate(), output.getBitRate(), output.getQuality(), output.getMode());

        // input channel 好像只会影响输出的声道
        //     * 比如即使输出设置MPEGMode.STEREO，输入channel设置为1的话，实际输出为MPEGMode.MONO
        //     * 不过设置输出为MPEGMode.MONO，输入channel设置为2的话，输出仍然为MPEGMode.MONO
        Native.set_input(p, input.getSampleRate(), output.getMode() == MPEGMode.MONO ? 1 : 2);

        if (Native.init_params(p) == -1) {
            throw new IllegalArgumentException();
        }
        hasInitParam = true;
    }

    //region encode

    // 输出格式是单声道的话pcm_r可为空
    // 输出格式是立体声的话pcm_r不能为空，否则不能正常转换(转换输出大小为0)


    @Override
    public int encodeUnsigned8bit(@NonNull byte[] pcm_l, @Nullable byte[] pcm_r, int inSize, @NonNull byte[] out) {
        if (!hasInitParam) throw new IllegalStateException();
        return Native.encodeByte(p, pcm_l, pcm_r, inSize, out);
    }

    @Override
    public int encodeSigned16bit(@NonNull byte[] pcm_l, @Nullable byte[] pcm_r, int inSize, @NonNull byte[] out) {
        if (!hasInitParam) throw new IllegalStateException();
        if (inSize % 2 != 0) throw new IllegalArgumentException();
        return Native.encode(p, pcm_l, pcm_r, inSize, out);
    }

    @Override
    public int encodeSigned32bit(@NonNull byte[] pcm_l, @Nullable byte[] pcm_r, int inSize, @NonNull byte[] out) {
        if (!hasInitParam) throw new IllegalStateException();
        if (inSize % 4 != 0) throw new IllegalArgumentException();
        return Native.encodeInt(p, pcm_l, pcm_r, inSize, out);
    }

    @Override
    public int encodeSigned32bitFloat(@NonNull byte[] pcm_l, @Nullable byte[] pcm_r, int inSize, @NonNull byte[] out) {
        if (!hasInitParam) throw new IllegalStateException();
        if (inSize % 4 != 0) throw new IllegalArgumentException();
        return Native.encodeFloat(p, pcm_l, pcm_r, inSize, out);
    }

    @Override
    public int encodeSigned64bitDouble(@NonNull byte[] pcm_l, @Nullable byte[] pcm_r, int inSize, @NonNull byte[] out) {
        if (!hasInitParam) throw new IllegalStateException();
        if (inSize % 8 != 0) throw new IllegalArgumentException();
        return Native.encodeDouble(p, pcm_l, pcm_r, inSize, out);
    }


    @Override
    public int encodeUnsigned8bitInterleaved(@NonNull byte[] pcm, int inSize, @NonNull byte[] out) {
        if (!hasInitParam) throw new IllegalStateException();
        if (inSize % 2 != 0) throw new IllegalArgumentException();
        return Native.encodeInterleavedByte(p, pcm, inSize, out);
    }

    @Override
    public int encodeSigned16bitInterleaved(@NonNull byte[] pcm, int inSize, @NonNull byte[] out) {
        if (!hasInitParam) throw new IllegalStateException();
        if (inSize % 4 != 0) throw new IllegalArgumentException();
        return Native.encodeInterleaved(p, pcm, inSize, out);
    }

    @Override
    public int encodeSigned32bitInterleaved(@NonNull byte[] pcm, int inSize, @NonNull byte[] out) {
        if (!hasInitParam) throw new IllegalStateException();
        if (inSize % 8 != 0) throw new IllegalArgumentException();
        return Native.encodeInterleavedInt(p, pcm, inSize, out);
    }

    @Override
    public int encodeSigned32bitFloatInterleaved(@NonNull byte[] pcm, int inSize, @NonNull byte[] out) {
        if (!hasInitParam) throw new IllegalStateException();
        if (inSize % 8 != 0) throw new IllegalArgumentException();
        return Native.encodeInterleavedFloat(p, pcm, inSize, out);
    }

    @Override
    public int encodeSigned64bitDoubleInterleaved(@NonNull byte[] pcm, int inSize, @NonNull byte[] out) {
        if (!hasInitParam) throw new IllegalStateException();
        if (inSize % 16 != 0) throw new IllegalArgumentException();
        return Native.encodeInterleavedDouble(p, pcm, inSize, out);
    }

    @Override
    public int flush(@NonNull byte[] out) {
        if (!hasInitParam) throw new IllegalStateException();
        return Native.flush(p, out);
    }
    //endregion

    @Override
    public void destroy() {
        Native.close(p);
    }

    public static String getVersion() {
        return Native.getVersion();
    }

}
