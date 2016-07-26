# knowledge
#一、ListView的优化
1.在adapter中的getView方法中尽量少使用逻辑

2.尽最大可能避免GC

3.滑动的时候不加载图片

4.将ListView的scrollingCache和animateCache设置为false

5.item的布局层级越烧越好

6.使用ViewHolder

1.在adapter中的getView方法中尽量少使用逻辑

不要在你的getView()中写过多的逻辑代码，我们可以将这些代码放在别的地方，例如：

优化前的getView（）：

@Override

public  View  getView(intposition, View convertView, ViewGroup paramViewGroup) {

Object current_event = mObjects.get(position);

ViewHolder holder =null;if(convertView ==null) {

holder =newViewHolder();

convertView = inflater.inflate(R.layout.row_event,null);

holder.ThreeDimension = (ImageView) convertView.findViewById(R.id.ThreeDim);

holder.EventPoster = (ImageView) convertView.findViewById(R.id.EventPoster);

convertView.setTag(holder);

}else{

holder = (ViewHolder) convertView.getTag();

}

//在这里进行逻辑判断，这是有问题的

if(doesSomeComplexChecking()) {

holder.ThreeDimention.setVisibility(View.VISIBLE);

}else{

holder.ThreeDimention.setVisibility(View.GONE);

}

// 这是设置image的参数，每次getView方法执行时都会执行这段代码，这显然是有问题的

RelativeLayout.LayoutParams imageParams =newRelativeLayout.LayoutParams(measuredwidth, rowHeight);

holder.EventPoster.setLayoutParams(imageParams);

returnconvertView;

}

优化后的getView（）：

@Override

public  View   getView(intposition, View convertView, ViewGroup paramViewGroup) {

Object object = mObjects.get(position);

ViewHolder holder =null;if(convertView ==null) {

holder =newViewHolder();

convertView = inflater.inflate(R.layout.row_event,null);

holder.ThreeDimension = (ImageView) convertView.findViewById(R.id.ThreeDim);

holder.EventPoster = (ImageView) convertView.findViewById(R.id.EventPoster);

//设置参数提到这里，只有第一次的时候会执行，之后会复用

RelativeLayout.LayoutParams imageParams =newRelativeLayout.LayoutParams(measuredwidth, rowHeight);

holder.EventPoster.setLayoutParams(imageParams);

convertView.setTag(holder);

}else{

holder = (ViewHolder) convertView.getTag();

}// 我们直接通过对象的getter方法代替刚才那些逻辑判断，那些逻辑判断放到别的地方去执行了holder.ThreeDimension.setVisibility(object.getVisibility());returnconvertView;

}

2.GC 垃圾回收器

当你创建了大量的对象的时候，GC就会频繁的执行，所以在getView（）方法中不要创建很多的对象，最好的优化是，不要在ViewHolder以外创建任何对象，如果你的你的log里面发现“GC has freed some memory”频繁出现的话，那你的程序肯定有问题了。你可以检查一下：

a) item布局的层级是否太深

b) getView（）方法中是否有大量对象存在

c) ListView的布局属性

3.加载图片

如果你的ListView中需要显示从网络上下载的图片的话，我们不要在ListView滑动的时候加载图片，那样会使ListView变得卡顿，所以我们需要再监听器里面监听ListView的状态，如果滑动的时候，停止加载图片，如果没有滑动，则开始加载图片

listView.setOnScrollListener(newOnScrollListener() {

@Override

public  void  onScrollStateChanged(AbsListView listView,intscrollState) {

//停止加载图片

if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {

imageLoader.stopProcessingQueue();

}else{

//开始加载图片

imageLoader.startProcessingQueue();

}

}

@Override

public void onScroll(AbsListView view,intfirstVisibleItem,intvisibleItemCount,inttotalItemCount) {

// TODO Auto-generated method stub}

});

4.将ListView的scrollingCache和animateCache设置为false

scrollingCache:scrollingCache本质上是drawing cache，你可以让一个View将他自己的drawing保存在cache中（保存为一个bitmap），这样下次再显示View的时候就不用重画了，而是从cache中取出。默认情况下drawing cahce是禁用的，因为它太耗内存了，但是它确实比重画来的更加平滑。而在ListView中，scrollingCache是默认开启的，我们可以手动将它关闭。

animateCache:ListView默认开启了animateCache，这会消耗大量的内存，因此会频繁调用GC，我们可以手动将它关闭掉

优化前的ListView

<android:id="@android:id/list"

android:layout_width="match_parent"

android:layout_height="wrap_content"

android:cacheColorHint="#00000000"

android:divider="@color/list_background_color"

android:dividerHeight="0dp"

android:listSelector="#00000000"

android:smoothScrollbar="true"

android:visibility="gone"/>

优化后的ListView

<android:id="@android:id/list"

android:layout_width="match_parent"

android:layout_height="wrap_content"

android:divider="@color/list_background_color"

android:dividerHeight="0dp"

android:listSelector="#00000000"

android:scrollingCache="false"

android:animationCache="false"

android:smoothScrollbar="true"

android:visibility="gone"/>

5.减少item的布局的深度

我们应该尽量减少item布局深度，因为当滑动ListView的时候，这回直接导致测量与绘制，因此会浪费大量的时间，所以我们应该将一些不必要的布局嵌套关系去掉。减少item布局深度

6.使用ViewHolder

这个大家应该非常熟悉了，但是不要小看这个ViewHolder，它可以大大提高我们ListView的性能

文／左老怪（简书作者）
原文链接：http://www.jianshu.com/p/3e22d53286ca
著作权归作者所有，转载请联系作者获得授权，并标注“简书作者”。
