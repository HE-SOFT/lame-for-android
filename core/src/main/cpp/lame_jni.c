#include <jni.h>
#include <malloc.h>
#include <string.h>
#include <math.h>
#include "log.h"
#include "lame_jni.h"
#include "lame.h"
#include "j_enum.h"

typedef lame_global_flags lameP;


//region create & close
JNIEXPORT jlong JNICALL Java_io_github_hesoft_lame_Native_create(JNIEnv *env, jclass clazz) {
    lame_global_flags *gf = lame_init();
    return (jlong) gf;
}

JNIEXPORT void JNICALL
Java_io_github_hesoft_lame_Native_close(JNIEnv *env, jclass clazz, jlong p) {
    lame_global_flags *gf = (lame_global_flags *) p;
    lame_close(gf);
    LOGI("close");
}
//endregion

//region cfg
JNIEXPORT jint JNICALL
Java_io_github_hesoft_lame_Native_init_1params(JNIEnv *env, jclass clazz, jlong p) {
    lame_global_flags *gf = (lame_global_flags *) p;
    return lame_init_params(gf);
}

JNIEXPORT void JNICALL
Java_io_github_hesoft_lame_Native_set_1input(
        JNIEnv *env, jclass clazz, jlong p, jint sampleRate, jint channels
) {
    lame_global_flags *gf = (lame_global_flags *) p;

    lame_set_in_samplerate(gf, sampleRate);
    lame_set_num_channels(gf, channels);
}

JNIEXPORT void JNICALL Java_io_github_hesoft_lame_Native_set_1output(
        JNIEnv *env, jclass clazz, jlong p, jint sampleRate, jint bitRate, jint quality, jint mode_
) {
    lame_global_flags *gf = (lame_global_flags *) p;

    lame_set_out_samplerate(gf, sampleRate);
    lame_set_brate(gf, bitRate);
    lame_set_quality(gf, quality);

    J_MPEG_MODE mode = (J_MPEG_MODE) mode_;
    switch (mode) {
        case J_NOT_SET:
            lame_set_mode(gf, NOT_SET);
            break;
        case J_MONO:
            lame_set_mode(gf, MONO);
            break;
        case J_STEREO:
            lame_set_mode(gf, STEREO);
            break;
        case J_JOINT_STEREO:
            lame_set_mode(gf, JOINT_STEREO);
            break;
    }
}
//endregion

//region  encode

//region fun

int encodeFun(
        JNIEnv *env, jlong p,
        jbyteArray pcm_l, jbyteArray pcm_r, jint inSize, jbyteArray out,
        int (*fun)(lameP *, jbyte *l, jbyte *r, jint inSize, jbyte *out, jsize outSize)
) {
    lameP *gf = (lameP *) p;

    jbyte *leftInput = NULL;
    jbyte *rightInput = NULL;
    if (pcm_l != NULL) {
        leftInput = (*env)->GetByteArrayElements(env, pcm_l, NULL);
    }
    if (pcm_r != NULL) {
        rightInput = (*env)->GetByteArrayElements(env, pcm_r, NULL);
    }

    jbyte *output = (*env)->GetByteArrayElements(env, out, NULL);
    jsize outSize = (*env)->GetArrayLength(env, out);


    int result = fun(gf, leftInput, rightInput, inSize, output, outSize);


    if (pcm_l != NULL) {
        (*env)->ReleaseByteArrayElements(env, pcm_l, leftInput, 0);
    }
    if (pcm_r != NULL) {
        (*env)->ReleaseByteArrayElements(env, pcm_r, rightInput, 0);
    }
    (*env)->ReleaseByteArrayElements(env, out, output, 0);

    return result;
}

int encodeInterleavedFun(
        JNIEnv *env, jlong p,
        jbyteArray pcm, jint inSize, jbyteArray out,
        int (*fun)(lameP *, jbyte *pcm, jint inSize, jbyte *out, jsize outSize)
) {
    lameP *gf = (lameP *) p;

    jbyte *input = (*env)->GetByteArrayElements(env, pcm, NULL);
    jbyte *output = (*env)->GetByteArrayElements(env, out, NULL);
    jsize outSize = (*env)->GetArrayLength(env, out);

    int result = fun(gf, input, inSize, output, outSize);

    (*env)->ReleaseByteArrayElements(env, pcm, input, 0);
    (*env)->ReleaseByteArrayElements(env, out, output, 0);
    return result;
}
//endregion

//region check float

int checkFloatPCMInput(float *input, jint size) {
    int errCount = 0;
    for (int i = 0; i < size; ++i) {
        float *f = input + i;
        if (*f != *f) { // NaN
            *f = 0;
            errCount++;
        } else if (*f > 1.1) {
            *f = 0;
            errCount++;
        } else if (*f < -1.1) {
            *f = 0;
            errCount++;
        }
    }
    return errCount;
}

int checkDoublePCMInput(double *input, jint size) {
    int errCount = 0;
    for (int i = 0; i < size; ++i) {
        double *f = input + i;
        if (*f != *f) { // NaN
            *f = 0;
            errCount++;
        } else if (*f > 1.1) {
            *f = 0;
            errCount++;
        } else if (*f < -1.1) {
            *f = 0;
            errCount++;
        }
    }
    return errCount;
}
//endregion

//region encode

int
encode16bit(lameP *lame, jbyte *l, jbyte *r, jint inSize, jbyte *out, jsize outSize) {
    // assert inSize % 2 == 0
    return lame_encode_buffer(lame, l, r, inSize >> 1, out, outSize);
}

JNIEXPORT jint JNICALL Java_io_github_hesoft_lame_Native_encode(
        JNIEnv *env, jclass clazz, jlong p,
        jbyteArray pcm_l, jbyteArray pcm_r, jint inSize, jbyteArray out
) {
    return encodeFun(env, p, pcm_l, pcm_r, inSize, out, encode16bit);
}


int
encode8bit(lameP *lame, jbyte *l, jbyte *r, jint inSize, jbyte *out, jsize outSize) {
    return lame_encode_buffer_byte(lame, l, r, inSize, out, outSize);
}

JNIEXPORT jint JNICALL Java_io_github_hesoft_lame_Native_encodeByte(
        JNIEnv *env, jclass clazz, jlong p,
        jbyteArray pcm_l, jbyteArray pcm_r, jint inSize, jbyteArray out
) {
    return encodeFun(env, p, pcm_l, pcm_r, inSize, out, encode8bit);
}


int encode32bit(lameP *lame, jbyte *l, jbyte *r, jint inSize, jbyte *out, jsize outSize) {
    // assert inSize % 4 == 0
    return lame_encode_buffer_int(lame, l, r, inSize >> 2, out, outSize);
}

JNIEXPORT jint JNICALL Java_io_github_hesoft_lame_Native_encodeInt(
        JNIEnv *env, jclass clazz, jlong p,
        jbyteArray pcm_l, jbyteArray pcm_r, jint inSize, jbyteArray out
) {
    return encodeFun(env, p, pcm_l, pcm_r, inSize, out, encode32bit);
}


int encode32bitFloat(lameP *lame, jbyte *l, jbyte *r, jint inSize, jbyte *out, jsize outSize) {
    // assert inSize % 4 == 0
    jint size = inSize >> 2;
    int errCount = checkFloatPCMInput((float *) l, size);
    if (errCount > 1)return -1;
    if (r != NULL) {
        errCount = checkFloatPCMInput((float *) r, size);
        if (errCount > 1)return -1;
    }

    return lame_encode_buffer_ieee_float(lame, l, r, size, out, outSize);
}

JNIEXPORT jint JNICALL Java_io_github_hesoft_lame_Native_encodeFloat(
        JNIEnv *env, jclass clazz, jlong p,
        jbyteArray pcm_l, jbyteArray pcm_r, jint inSize, jbyteArray out
) {
    return encodeFun(env, p, pcm_l, pcm_r, inSize, out, encode32bitFloat);
}


int encode64bitDouble(lameP *lame, jbyte *l, jbyte *r, jint inSize, jbyte *out, jsize outSize) {
    // assert inSize % 8 == 0
    jint size = inSize >> 3;
    int errCount = checkDoublePCMInput((double *) l, size);
    if (errCount > 1)return -1;
    if (r != NULL) {
        errCount = checkDoublePCMInput((double *) r, size);
        if (errCount > 1)return -1;
    }

    return lame_encode_buffer_ieee_double(lame, l, r, size, out, outSize);
}

JNIEXPORT jint JNICALL Java_io_github_hesoft_lame_Native_encodeDouble(
        JNIEnv *env, jclass clazz, jlong p,
        jbyteArray pcm_l, jbyteArray pcm_r, jint inSize, jbyteArray out
) {
    return encodeFun(env, p, pcm_l, pcm_r, inSize, out, encode64bitDouble);
}
//endregion

//region encode Interleaved

int encodeInterleaved16bit(lameP *lame, jbyte *pcm, jint inSize, jbyte *out, jsize outSize) {
    // assert inSize % 4 == 0
    return lame_encode_buffer_interleaved(lame, pcm, inSize >> 2, out, outSize);
}

JNIEXPORT jint JNICALL Java_io_github_hesoft_lame_Native_encodeInterleaved(
        JNIEnv *env, jclass clazz, jlong p,
        jbyteArray pcm, jint inSize, jbyteArray out
) {
    return encodeInterleavedFun(env, p, pcm, inSize, out, encodeInterleaved16bit);
}


int encodeInterleaved8bit(lameP *lame, jbyte *pcm, jint inSize, jbyte *out, jsize outSize) {
    // assert inSize % 2 == 0
    return lame_encode_buffer_interleaved_byte(lame, pcm, inSize >> 1, out, outSize);
}

JNIEXPORT jint JNICALL Java_io_github_hesoft_lame_Native_encodeInterleavedByte(
        JNIEnv *env, jclass clazz, jlong p,
        jbyteArray pcm, jint inSize, jbyteArray out
) {
    return encodeInterleavedFun(env, p, pcm, inSize, out, encodeInterleaved8bit);
}


int encodeInterleaved32bit(lameP *lame, jbyte *pcm, jint inSize, jbyte *out, jsize outSize) {
    // assert inSize % 8 == 0
    return lame_encode_buffer_interleaved_int(lame, pcm, inSize >> 3, out, outSize);
}

JNIEXPORT jint JNICALL Java_io_github_hesoft_lame_Native_encodeInterleavedInt(
        JNIEnv *env, jclass clazz, jlong p,
        jbyteArray pcm, jint inSize, jbyteArray out
) {
    return encodeInterleavedFun(env, p, pcm, inSize, out, encodeInterleaved32bit);
}


int encodeInterleaved32bitFloat(lameP *lame, jbyte *pcm, jint inSize, jbyte *out, jsize outSize) {
    // assert inSize % 8 == 0
    int errCount = checkFloatPCMInput((float *) pcm, inSize >> 2);
    if (errCount > 1) {
        return -1;
    }

    return lame_encode_buffer_interleaved_ieee_float(lame, pcm, inSize >> 3, out, outSize);
}

JNIEXPORT jint JNICALL Java_io_github_hesoft_lame_Native_encodeInterleavedFloat(
        JNIEnv *env, jclass clazz, jlong p,
        jbyteArray pcm, jint inSize, jbyteArray out
) {
    return encodeInterleavedFun(env, p, pcm, inSize, out, encodeInterleaved32bitFloat);
}

int encodeInterleaved64bitDouble(lameP *lame, jbyte *pcm, jint inSize, jbyte *out, jsize outSize) {
    // assert inSize % 16 == 0
    int errCount = checkDoublePCMInput((double *) pcm, inSize >> 3);
    if (errCount > 1) return -1;
    return lame_encode_buffer_interleaved_ieee_double(lame, pcm, inSize >> 4, out, outSize);
}

JNIEXPORT jint JNICALL Java_io_github_hesoft_lame_Native_encodeInterleavedDouble(
        JNIEnv *env, jclass clazz, jlong p,
        jbyteArray pcm, jint inSize, jbyteArray out
) {
    return encodeInterleavedFun(env, p, pcm, inSize, out, encodeInterleaved64bitDouble);
}
//endregion

//endregion


JNIEXPORT jint JNICALL Java_io_github_hesoft_lame_Native_flush(
        JNIEnv *env, jclass clazz, jlong p, jbyteArray out
) {
    lameP *lame = (lameP *) p;
    jbyte *output = (*env)->GetByteArrayElements(env, out, NULL);
    jsize outSize = (*env)->GetArrayLength(env, out);

    int result = lame_encode_flush(lame, output, outSize);

    (*env)->ReleaseByteArrayElements(env, out, output, 0);
    return result;
}


JNIEXPORT jstring JNICALL Java_io_github_hesoft_lame_Native_getVersion(JNIEnv *env, jclass clazz) {
    return (*env)->NewStringUTF(env, get_lame_version());
}
