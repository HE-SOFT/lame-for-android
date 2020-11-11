
/* MPEG modes */
typedef enum J_MPEG_MODE_ENUM {
    J_NOT_SET = 0,
    J_MONO,
    J_STEREO,
    J_JOINT_STEREO,
    // DUAL_CHANNEL,   /* LAME doesn't supports this! */
    // MAX_INDICATOR   /* Don't use this! It's used for sanity checks. */
} J_MPEG_MODE;