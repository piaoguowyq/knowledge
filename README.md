##knowledge
###一、ListView的优化 
1.在adapter中的getView方法中尽量少使用逻辑
2.尽最大可能避免GC
3.滑动的时候不加载图片
4.将ListView的scrollingCache和animateCache设置为false
5.item的布局层级越少越好
6.使用ViewHolder
>原文链接：http://www.jianshu.com/p/3e22d53286ca

###二、imageloader picasso glide fresco等图片加载框架的区别 
>http://blog.csdn.net/qq_25690935/article/details/50548457

###三、android互联网面试题 
>https://github.com/JackyAndroid/AndroidInterview-Q-A/blob/master/README-CN.md 
>https://gold.xitu.io/entry/576a67902e958a00699faaec

###四、apk打包 
>http://www.2cto.com/kf/201606/517300.html

###五、微信支付 
>http://www.thinksaas.cn/topics/0/493/493596.html

###六、加密、解密与Https单向认证和双向认证 对称加密与非对称加密： 
>http://www.cnblogs.com/jfzhu/p/4020928.html   
>Https单向认证和双向认证： 
>http://blog.csdn.net/duanbokan/article/details/50847612

###七、github上的优秀开源项目 
>http://www.cnblogs.com/hawkon/p/3593709.html

###八、studio中使用git进行版本控制 
>1、Android studio使用git提交但是没有push,如何回退并保存
http://www.th7.cn/Program/Android/201506/487759.shtml.     

###九、Android5.1系统WebView内存泄漏 
>http://www.w2bc.com/article/133213

###十、NDK开发配置 
####1、安装配置NDK
1）解压NDK的zip包到非中文目录（同时不能有空格）
2）配置path：解压后NDK的根目录配置到环境变量path中----->用ndk-build检测是否配置成功
####2、给AndroidStudio配置关联NDK
1）local.properties中添加配置
2) gradle.properties中添加配置
 `android.useDeprecatedNdk=true`
####3、编写native方法：
```java
public class JNIS{
    public native String helloJNI();
}
```
####4、定义对应的JNI
1)在main下创建jni文件夹
2）生成native方法对应的JNI函数声明头文件：命令窗口中，进入Java文件夹，
 执行命令：`javah com.atguigu.jnitests2.JNIS(包名.类名)`
 生成头文件：`com_atguigu_jnitests2_JNIS.h`
 函数声明：
```c++
JNIEXPORT jstring JNICALL Java_com_atguigu_jnitests2_JNIS_helloJNI(JNIEnv* ,jobject);
```
 3)将生成的的头文件转移到jni文件夹下
 4）在jni下定义相对应的函数文件：test.c
```c++
#include com_atguigu_jnitests2_JNIS.h*
JNIEXPORT jstring JNICALL Java_com_atguigu_jnitests2_JNIS_helloJNI(JNIEnv* env,jobject jobj)
{
    return (*env)->NewStringUTF(env,"Hello from C");
}
```
5)在jni文件夹下创建一个空的C文件：empty.c
**说明：这是androidstudio的bug，必须至少2个C文件才能通过编译(可能已修复)**
####5、指定编译不同的cpu
```groovy
defaultConfig{
    ndk{
        moduleName "HelloJni"//so文件：lib+moduleName+.so
        abiFilters"armeabi","armeabi-v7a","x86"//cpu的类型
    }
}
```
####6、编译生成不同平台下的动态链接文件
1）执行rebuild，生成so文件
2）so文件目录：`build\intermediates\ndk\debug\lib...`
####7、调用native方法：
1）在native方法所在的类中加载so文件
```java
static{
    system.loadLibrary("HelloJni");
}
```
##十一、NDK开发流程 
1、在Java代码里面写native代码
2、在main目录下创建jni目录，写C代码-生成头文件
3、配置动态链接库的名称
4、加载动态连接库
5、使用
