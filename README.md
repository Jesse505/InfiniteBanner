InfiniteBanner-Android
============

## 目录

* [功能介绍](#功能介绍)
* [效果图与示例 apk](#效果图与示例-apk)
* [使用](#使用)
* [方法说明](#方法说明)
* [自定义属性说明](#自定义属性说明)

## 功能介绍

- [x] 支持大于等于1页时的无限循环自动轮播、手指按下暂停轮播、抬起手指开始轮播
- [x] 支持设置页面切换间隔的时间
- [x] 支持设置页面切换持续的时间
- [x] 支持设置是否可以手动滑动
- [x] 支持设置是否自动轮播
- [x] 支持设置自定义指示器背景和位置
- [x] 支持设置图片指示器和数字指示器
- [x] 支持 ViewPager 各种切换动画

## 效果图与示例 apk




## 使用

### 1.添加 Gradle 依赖

```groovy
dependencies {

}
```

### 2.在布局文件中添加 InfiniteBanner

```xml
<com.jesse.infinitebanner.library.InfiniteBanner
    android:id="@+id/commonBanner"
    android:layout_width="match_parent"
    android:layout_height="140dp"
    app:banner_pointContainerBackground = "@android:color/transparent"
    app:banner_pointDrawable="@drawable/bga_banner_selector_point_hollow"
    app:banner_transitionEffect="defaultEffect"/>
```

### 3.继承InfinitePagerAdapter重写getView()和getRealItemCount()方法


> getRealItemCount()返回真实的数据长度，getView()中填充每个item

```java
    @Override
    public int getRealItemCount() {
        return (null != bannerList && bannerList.size() > 0) ? bannerList.size() : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        ViewHolder viewHolder;
        if (null != convertView) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = layoutInflater.inflate(R.layout.layout_banner_item, container, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        if (bannerList != null && bannerList.size() > 0) {
            viewHolder.position = position;
            viewHolder.imageView.setImageResource(bannerList.get(position));
        }
        return convertView;
    }
```


### 4.在Fragment或者Activity中设置adapter

```java
    InfiniteBanner infiniteBanner = (InfiniteBanner) findViewById(R.id.commonBanner);
    List<Integer> homeSplashResourceList = new ArrayList<>();
    homeSplashResourceList.add(R.mipmap.home_splash_page_1);
    homeSplashResourceList.add(R.mipmap.home_splash_page_2);
    homeSplashResourceList.add(R.mipmap.home_splash_page_3);
    infiniteBanner.setAdapter(new HomePageTopViewPagerAdapter(homeSplashResourceList, this));
```

## 方法说明
##### 1.setPageChangeDuration(int duration)
##### 设置页码切换过程的时间长度
##### 2.setAutoPlayAble(boolean autoPlayAble)
##### 设置是否开启自动轮播
##### 3.setAutoPlayInterval(int autoPlayInterval)
##### 设置自动轮播的时间间隔
##### 4.setAllowUserScrollable(boolean allowUserScrollable)
##### 设置是否允许用户手指滑动
##### 5.setAdapter(InfinitePagerAdapter infinitePagerAdapter)
##### 设置adapter
##### 6.setIsNeedShowIndicatorOnOnlyOnePage(boolean isNeedShowIndicatorOnOnlyOnePage)
##### 设置当只有一页数据时是否显示指示器
##### 7.setTransitionEffect(TransitionEffect effect)
##### 设置页面切换换动画（这个方法必须在setAdapter()之后）
##### 8.setPageTransformer(ViewPager.PageTransformer transformer)
##### 设置自定义页面切换动画（这个方法必须在setAdapter()之后）
##### 9.getViewPager()
##### 获取ViewPager
##### 10.setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener)
##### 添加ViewPager滚动监听器


## 自定义属性说明
```xml
<declare-styleable name="BGABanner">
    <!-- 指示点容器背景 -->
    <attr name="banner_pointContainerBackground" format="reference|color" />
    <!-- 指示点背景 -->
    <attr name="banner_pointDrawable" format="reference" />
    <!-- 指示点容器左右内间距 -->
    <attr name="banner_pointContainerLeftRightPadding" format="dimension" />
    <!-- 指示点上下外间距 -->
    <attr name="banner_pointTopBottomMargin" format="dimension" />
    <!-- 指示点左右外间距 -->
    <attr name="banner_pointLeftRightMargin" format="dimension" />
    <!-- 指示器的位置 -->
    <attr name="banner_indicatorGravity">
        <flag name="top" value="0x30" />
        <flag name="bottom" value="0x50" />
        <flag name="left" value="0x03" />
        <flag name="right" value="0x05" />
        <flag name="center_horizontal" value="0x01" />
    </attr>
    <!-- 是否开启自动轮播 -->
    <attr name="banner_pointAutoPlayAble" format="boolean" />
    <!-- 自动轮播的时间间隔 -->
    <attr name="banner_pointAutoPlayInterval" format="integer" />
    <!-- 页码切换过程的时间长度 -->
    <attr name="banner_pageChangeDuration" format="integer" />
    <!-- 页面切换的动画效果 -->
    <attr name="banner_transitionEffect" format="enum">
        <enum name="defaultEffect" value="0" />
        <enum name="alpha" value="1" />
        <enum name="rotate" value="2" />
        <enum name="cube" value="3" />
        <enum name="flip" value="4" />
        <enum name="accordion" value="5" />
        <enum name="zoomFade" value="6" />
        <enum name="fade" value="7" />
        <enum name="zoomCenter" value="8" />
        <enum name="zoomStack" value="9" />
        <enum name="stack" value="10" />
        <enum name="depth" value="11" />
        <enum name="zoom" value="12" />
    </attr>
    <!-- 是否是数字指示器 -->
    <attr name="banner_isNumberIndicator" format="boolean" />
    <!-- 数字指示器文字颜色 -->
    <attr name="banner_numberIndicatorTextColor" format="reference|color" />
    <!-- 数字指示器文字大小 -->
    <attr name="banner_numberIndicatorTextSize" format="dimension" />
    <!-- 数字指示器背景 -->
    <attr name="banner_numberIndicatorBackground" format="reference" />
    <!-- 当只有一页数据时是否显示指示器，默认值为 false -->
    <attr name="banner_isNeedShowIndicatorOnOnlyOnePage" format="boolean" />
    <!-- 自动轮播区域距离 BGABanner 底部的距离，用于使指示器区域与自动轮播区域不重叠 -->
    <attr name="banner_contentBottomMargin" format="dimension"/>
</declare-styleable>
```


