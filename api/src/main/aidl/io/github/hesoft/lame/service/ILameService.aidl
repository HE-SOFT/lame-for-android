package io.github.hesoft.lame.service;

import io.github.hesoft.lame.service.IRemoteLame;

interface ILameService {
    IRemoteLame create(in IBinder clientDeathListener);
}