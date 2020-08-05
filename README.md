# Lame mp3 encoder for Android

Based on [**LAME Project (lame.sourceforge.net)**](http://lame.sourceforge.net/)

## Usage

### 1.Create Lame encoder object 

directly create a Lame object

```kotlin
val lame = Lame()
...
```

Or binding the service (cross-process)

```kotlin
private val intent = Intent("io.github.hesoft.lame.MP3EncodeSERVICE")
context.bindService(intent, object : ServiceConnection {
    override fun onServiceConnected(name: ComponentName?, serviceBinder: IBinder) {
        val service = ILameService.Stub.asInterface(serviceBinder)
        val lame = service.create(Binder())
        ...
    }

    override fun onServiceDisconnected(name: ComponentName?) {}

}, Context.BIND_AUTO_CREATE)
```

### 2.Set parameter properties

```kotlin
// inputSampleRate
// outputSampleRate
// outputBitRate
// outputQuality
// outputMode
lame.initParams(
    44100,
    44100, 320, 3, MPEGMode.MONO
)
```

### 3.Encode

Use the following method to encode, each method is for a different input format  

```
encodeUnsigned8bit(byte[] pcm_l, byte[] pcm_r, int inSize, byte[] outBytes)  
encodeSigned16bit(byte[] pcm_l, byte[] pcm_r, int inSize, byte[] outBytes)  
encodeSigned32bit(byte[] pcm_l, byte[] pcm_r, int inSize, byte[] outBytes)  
encodeSigned32bitFloat(byte[] pcm_l, byte[] pcm_r, int inSize, byte[] outBytes)  
encodeSigned64bitDouble(byte[] pcm_l, byte[] pcm_r, int inSize, byte[] outBytes)  
encodeUnsigned8bitInterleaved(byte[] pcm, int inSize, byte[] outBytes)  
encodeSigned16bitInterleaved(byte[] pcm, int inSize, byte[] outBytes)  
encodeSigned32bitInterleaved(byte[] pcm, int inSize, byte[] outBytes)  
encodeSigned32bitFloatInterleaved(byte[] pcm, int inSize, byte[] outBytes)  
encodeSigned64bitDoubleInterleaved(byte[] pcm, int inSize, byte[] outBytes)  
```

Demo: [**LameServiceTest.kt**](https://github.com/HE-SOFT/lame-for-android/blob/master//test/src/androidTest/java/io/github/hesoft/lame/LameServiceTest.kt)

### 4.flush & close

```kotlin
val outSize = lame.flush(outBuff)
output.write(outBuff, 0, outSize)
output.close()
lame.destroy()
```

## Modifications to the original LAME library

add `lame_encode_buffer_byte` and `lame_encode_buffer_interleaved_byte` method to support 8-bit format encoding

## reference project & acknowledgements

https://github.com/4332weizi/AME/

## License

License inherits from original LAME Project.  
[**GNU LESSER GENERAL PUBLIC LICENSE**](https://github.com/HE-SOFT/lame-for-android/blob/master/LICENSE)
