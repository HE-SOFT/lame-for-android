package io.github.hesoft.lame;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;

import static io.github.hesoft.lame.MPEGMode.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * lame for android
 * Created by HE on 2018/10/27.
 */

@IntDef({/*NOT_SET,*/ MONO, STEREO, JOINT_STEREO})
@Retention(SOURCE)
public @interface MPEGMode {
    //int NOT_SET    = 0;
    int MONO         = 1;
    int STEREO       = 2;
    int JOINT_STEREO = 3;
}