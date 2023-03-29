# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Gson uses generic type information stored in a class file when working with
#fields. Proguard removes such information by default, so configure it to keep
#all of it.

# Keep models class
-keep class co.nimblehq.loyalty.sdk.model.** { *; }
-keep class co.nimblehq.loyalty.sdk.api.response.** { *; }

# Keep Result sealed class
-keep class co.nimblehq.loyalty.sdk.Result { *; }
-keep class co.nimblehq.loyalty.sdk.Result$** { *; }

# Keep the Builder companion object
-keep class co.nimblehq.loyalty.sdk.LoyaltySdk { *; }
-keep class co.nimblehq.loyalty.sdk.LoyaltySdk$Builder { *; }
-keep class co.nimblehq.loyalty.sdk.LoyaltySdk$Companion { *; }

# Retrofit and OkHttp rules
-keepattributes Signature
-keepattributes Exceptions

-keepclassmembers class retrofit2.** {
    @retrofit2.http.* <methods>;
}

-keep class okhttp3.** { *; }

-keep interface okhttp3.** { *; }

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**

# Moshi rules
-keepclassmembers class com.squareup.moshi.** { *; }
-keep @com.squareup.moshi.JsonQualifier interface *
-keepclassmembers class * {
    @com.squareup.moshi.FromJson *;
    @com.squareup.moshi.ToJson *;
}
