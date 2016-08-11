# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\AndriodDevTools\AndoridSDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-optimizationpasses 5                                                           # 指定代码的压缩级别
-dontusemixedcaseclassnames                                                     # 是否使用大小写混合
-dontskipnonpubliclibraryclasses                                                # 是否混淆第三方jar
-dontpreverify                                                                  # 混淆时是否做预校验
-verbose                                                                        # 混淆时是否记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*        # 混淆时所采用的算法
#忽略警告
-dontwarn

-keep public class * extends android.app.Activity                               # 保持哪些类不被混淆
-keep public class * extends android.app.Application                            # 保持哪些类不被混淆
-keep public class * extends android.app.Service                                # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver                  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider                    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper               # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference                      # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService              # 保持哪些类不被混淆

-keepclasseswithmembernames class * {                                           # 保持 native 方法不被混淆
    native <methods>;
}

-keepclasseswithmembers class * {                                               # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);     # 保持自定义控件类不被混淆
}

-keepclassmembers class * extends android.app.Activity {                        # 保持自定义控件类不被混淆
   public void *(android.view.View);
}

-keepclassmembers enum * {                                                      # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {                                # 保持 Parcelable 不被混淆
  public static final android.os.Parcelable$Creator *;
}
#不混淆反射类
-keep class * extends com.joe.zatuji.base.BasePresenter
-keep class * extends com.joe.zatuji.base.ui.basestaggered.BaseStaggeredPresenter
-keep class * implements com.joe.zatuji.base.view.BaseView
-keep class * extends com.joe.zatuji.base.view.BaseView
-keep class * implements com.joe.zatuji.base.model.BaseModel
-keep class * extends com.joe.zatuji.base.BaseModel

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#如果有引用v4包可以添加下面这行
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment


#如果引用了v4或者v7包，可以忽略警告，因为用不到android.support
-dontwarn android.support.**


#不混淆实体类
-keep public class com.joe.zatuji.data.** { *;}
-keep public class com.joe.zatuji.dao.** { *;}

#不混淆异常类
-keep public class com.joe.zatuji.api.exception.** {*;}

#glide
-keep class com.bumptech.glide.** {*;}
#Gson
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** {*;}

#不混淆jar包中的类
-keep class org.jsoup.** { *;}
-dontwarn org.kxml2.**

-dontwarn com.github.siyamed.shapeimageview.**
-keep class com.github.siyamed.shapeimageview.** {*;}
#okio
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}

#retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# rxjava
-dontwarn rx.**
-keep class rx.** {*;}
-keep class rx.android.** {*;}
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}

#####################记录生成的日志数据,gradle build时在本项目根目录输出################

#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt

#####################记录生成的日志数据，gradle build时 在本项目根目录输出-end################

-keepattributes Signature #泛型

#友盟
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class com.joe.zatuji.R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
