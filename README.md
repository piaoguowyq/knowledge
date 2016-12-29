# knowledge
#一、ListView的优化
1.在adapter中的getView方法中尽量少使用逻辑

2.尽最大可能避免GC

3.滑动的时候不加载图片

4.将ListView的scrollingCache和animateCache设置为false

5.item的布局层级越少越好

6.使用ViewHolder

文／左老怪（简书作者）
原文链接：http://www.jianshu.com/p/3e22d53286ca
著作权归作者所有，转载请联系作者获得授权，并标注“简书作者”。

#二、imageloader picasso glide fresco等图片加载框架的区别
http://blog.csdn.net/qq_25690935/article/details/50548457

#三、android互联网面试题
https://github.com/JackyAndroid/AndroidInterview-Q-A/blob/master/README-CN.md
https://gold.xitu.io/entry/576a67902e958a00699faaec

#四、apk打包
http://www.2cto.com/kf/201606/517300.html

#五、微信支付
http://www.thinksaas.cn/topics/0/493/493596.html

#六、加密、解密与Https单向认证和双向认证
对称加密与非对称加密：
  http://www.cnblogs.com/jfzhu/p/4020928.html                             
Https单向认证和双向认证：
  http://blog.csdn.net/duanbokan/article/details/50847612

#七、github上的优秀开源项目
http://www.cnblogs.com/hawkon/p/3593709.html

#八、studio中使用git进行版本控制
1、Android studio使用git提交但是没有push,如何回退并保存
    http://www.th7.cn/Program/Android/201506/487759.shtml
    
#九、Android5.1系统WebView内存泄漏
问题现象

    （该文章，引自零号路的私人博客，本人在浏览框架的开发过程中，用该方式，规避了内存泄露的问题。）

在Android5.1系统中，会发现App存在 WebView 泄漏情况，还比较严重。并且只是发生在 Android 5.1 系统。

GC roots 如下:



每新打开一次这个WebViewActivity，就会发生就会发生一次改Webview实例无法释放，新增一个对象。

上图中的两个 AppSearchWebView实例，就是由于打开了两次导致。

问题分析

出现了这个问题分析起来还是比较简单的，根据这个引用关系，我们可以直观的看到是由于 Appsearch（extends Application）的 mComponentCallbacks 一直在强引用AWComponentCallbacks，导致无法释放。然后AWComponentCallbacks -> AWContents > AppSearchWebView。

通过分析代码发现关键在于 AwContents 这里的 AwComponentsCallbacks 为什么没有释放。

    @Override
    public void onAttachedToWindow() {
        if (isDestroyed()) return;
        if (mIsAttachedToWindow) {
            Log.w(TAG, "onAttachedToWindow called when already attached. Ignoring");
            return;
        }

		......

        mComponentCallbacks = new AwComponentCallbacks();
        mContext.registerComponentCallbacks(mComponentCallbacks);
    }

    @Override
    public void onDetachedFromWindow() {
        if (isDestroyed()) return;
        if (!mIsAttachedToWindow) {
            Log.w(TAG, "onDetachedFromWindow called when already detached. Ignoring");
            return;
        }
		......

        if (mComponentCallbacks != null) {
            mContext.unregisterComponentCallbacks(mComponentCallbacks);
            mComponentCallbacks = null;
        }

		......
    } 
看这段代码看不出来什么问题，onAttach的时候 register，detach的时候 unregister, 不会存在问题。

但是为什么呢？

难道是由于 if (isDestroyed()) return 这条return引起的？

当调用 Webview.destroy() 后 这个判断 返回true。
我们看下哪里调用了 webview.destroy()

// source from WebViewActivity

@Override
protected void onDestroy() {
    super.onDestroy();
    
    if (mWebView != null) {
        mWebView.destroy();
    }
}
很多应用应该都是这么做的，包括系统浏览器，在Activity destroy的时候，调用 webview的destroy。并且一直工作的很好。

通过调试发现，确实是由于此调用导致的。onDestroy 发生在 onDetach 之前。

那为什么 android 5.1 之前的代码没有问题呢？

看下代码：

	// AwContents.java

    @Override
    public void onDetachedFromWindow() {
        if (!mIsAttachedToWindow) {
            Log.w(TAG, "onDetachedFromWindow called when already detached. Ignoring");
            return;
        }
		
		......

        if (mComponentCallbacks != null) {
            mContext.unregisterComponentCallbacks(mComponentCallbacks);
            mComponentCallbacks = null;
        }
		......
	}
相对于 5.1 的代码少了那句 if (isDestroyed()) return;

规避方法

解决方案可以：在destroy之前，把webview 从 parent 中 remove 掉，同样可以提前detach。

  protected void onDestroy() {
      if (mWebView != null) {
          ((ViewGroup) mWebView.getParent()).removeView(mWebView);
          mWebView.destroy();
          mWebView = null;
      }
      super.onDestroy();
  }
