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
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

-keep class com.hh.data.model.** {
    *;
}

-keep class com.hh.data.repo.** {
    *;
}

# Moshi
-keep class com.squareup.moshi.** { *; }
-dontwarn com.squareup.moshi.**

# Удержание сгенерированных Moshi адаптеров
-keep class *JsonAdapter { *; }

-dontwarn java.lang.invoke.StringConcatFactory

# Moshi - предотвращаем обфускацию классов Moshi и сгенерированных адаптеров
-keep class com.squareup.moshi.** { *; }
-dontwarn com.squareup.moshi.**

# Не обфусцировать модели данных, используемые в сериализации
-keep class com.hh.data.repo.wiki.** { *; }

# Удержание сгенерированных Moshi адаптеров
-keepclassmembers class * {
    @com.squareup.moshi.JsonClass <fields>;
}

-keep class **JsonAdapter { *; }

# Если вы используете аннотации @Json, их тоже нужно сохранить
-keepattributes RuntimeVisibleAnnotations

# Предотвращение обфускации классов, которые используются для рефлексии
-keepnames class com.hh.data.repo.wiki.WikiResponse { *; }

# Удержание абстрактных классов и их наследников
-keep class com.hh.data.repo.wiki.WikiResponse { *; }
-keep class com.hh.data.repo.wiki.** { *; }
