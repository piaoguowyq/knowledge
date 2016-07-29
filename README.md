# knowledge
#一、ListView的优化
1.在adapter中的getView方法中尽量少使用逻辑

2.尽最大可能避免GC

3.滑动的时候不加载图片

4.将ListView的scrollingCache和animateCache设置为false

5.item的布局层级越烧越好

6.使用ViewHolder

文／左老怪（简书作者）
原文链接：http://www.jianshu.com/p/3e22d53286ca
著作权归作者所有，转载请联系作者获得授权，并标注“简书作者”。

#二、imageloader picasso glide fresco等图片加载框架的区别
http://blog.csdn.net/qq_25690935/article/details/50548457
