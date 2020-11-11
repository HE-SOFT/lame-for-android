package io.github.hesoft.lame;

import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.NonNull;

/**
 * lame for android
 * Created by HE on 2018/10/27.
 */
public class BufferReader {

    private int  byteSize  = 1;
    private long mReadSize = -1;

    private final byte[] buff;
    private       int    buffSize;

    public BufferReader(int buffSize) {
        this.byteSize = buffSize;
        buff = new byte[buffSize];
    }


    public int getByteSize() {
        return byteSize;
    }
    public void setByteSize(int byteSize) {
        if (byteSize < 1 || buff.length < byteSize) {
            throw new IllegalArgumentException();
        }
        this.byteSize = byteSize;
        buffSize = buff.length - (buff.length % byteSize);
    }
    public long getReadSize() {
        return mReadSize;
    }
    public void setReadSize(long readSize) {
        this.mReadSize = readSize;
    }


    public int read(@NonNull InputStream inputStream) throws IOException {
        final int maxReadSize;
        if (mReadSize < 0 || buffSize <= mReadSize) {
            maxReadSize = buffSize;
        } else {
            if (mReadSize == 0) return -1;
            maxReadSize = (int) mReadSize;
        }

        int index = 0;
        while (true) {
            final int readSize = maxReadSize - index;
            if (readSize <= 0) break;
            int read = inputStream.read(buff, index, readSize);
            if (read < 0) {
                if (index == 0) return read;
                else break;
            }

            index += read;
            if (index % byteSize == 0) break;
        }

        mReadSize -= index;

        if (index % byteSize != 0) {
            //todo WARNING
            int superfluous = index % byteSize;
            index -= superfluous;
        }

        return index;
    }

    public byte[] get() {
        return buff;
    }
}
