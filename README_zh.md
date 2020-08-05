# Lame mp3 encoder for Android

基于[**LAME Project(lame.sourceforge.net)**](http://lame.sourceforge.net/)

## 使用方法

### 1.创建Lame编码器对象

可直接创建Lame对象

```kotlin
val lame = Lame()
...
```

或通过绑定服务跨进程使用

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

### 2.设置参数属性

```kotlin
// 参数分别是 
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
lame 库本来还有很多其它可设置参数，这里java接口做了简化。如有需要请自行修改。

### 3.转码
针对不同输入格式，使用下面方法转码  
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
具体可参考 [**LameServiceTest.kt**](https://github.com/hesoft/lame-for-android/blob/master//test/src/androidTest/java/io/github/hesoft/lame/LameServiceTest.kt)

### 4.flush & close
```kotlin
val outSize = lame.flush(outBuff)
output.write(outBuff, 0, outSize)
output.close()
lame.destroy()
```

## 对原始LAME库的修改

添加`lame_encode_buffer_byte` 和 `lame_encode_buffer_interleaved_byte` 方法以支持8bit格式编码

## 参考项目 & 鸣谢

https://github.com/4332weizi/AME/

## License

License 继承自原始LAME项目  
[**GNU LESSER GENERAL PUBLIC LICENSE**](https://github.com/HE-SOFT/lame-for-android/blob/master/LICENSE)
