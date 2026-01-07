# @Serializable and @Polymorphic are used at runtime for polymorphic serialization.
#-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

### ---- OTHER ---- ###

### ---- Update to AGP 8.x (from $root/androidApp/build/outputs/mapping/prod/missing_rules.txt) ---- ###
### ----- SRAV Player Data SDK ----- ###

# Keep sealed classes from your package
-keepattributes PermittedSubclasses

-keepclassmembers class * implements ag.sportradar.mobile.player.data.sdk.exception.SRAVPlayerException {
    <fields>;
}
-keep public class ag.sportradar.mobile.player.data.sdk.exception.** { *; }

# Keep any custom serialization-related classes and methods
-keep public class ag.sportradar.mobile.player.data.sdk.network.calm.** { *; }

-keep class ag.sportradar.mobile.player.data.sdk.player.entities.SRAVTrack { *; }
-keep class ag.sportradar.mobile.player.data.sdk.player.entities.* { *; }
-keep class ag.sportradar.mobile.player.data.sdk.session.channel.PlaybackChannel { *; }
-keep class ag.sportradar.mobile.player.data.sdk.session.channel.** { *; }
-keep class ag.sportradar.mobile.player.data.sdk.session.** { *; }
-keep class ag.sportradar.mobile.player.data.sdk.player.state.controls.** { *; }
-keep class ag.sportradar.mobile.player.data.sdk.player.state.* { *; }

-keep class ag.sportradar.mobile.player.data.sdk.player.PlayerViewModel { *; }
-keep class ag.sportradar.mobile.player.data.sdk.player.PlayerControllable { *; }
-keep class ag.sportradar.mobile.player.data.sdk.loging.PlayerEventLogger { *; }
-keep class ag.sportradar.mobile.player.data.sdk.SRAVPlayerConfiguration { *; }
-keep class ag.sportradar.mobile.player.data.sdk.asset.PlaybackAsset { *; }
-keep class ag.sportradar.mobile.player.data.sdk.provider.DefaultPlaybackAssetProvider { *; }
-keep class ag.sportradar.mobile.player.data.sdk.provider.type.DefaultProviderInput { *; }
-keep class ag.sportradar.mobile.player.data.sdk.analytics.* { *; }
-keep class ag.sportradar.mobile.player.data.sdk.ClientConfig { *; }

-keep class ag.sportradar.mobile.player.data.sdk.injection.modules.* { *; }

# Keep all public classes in your main API package
#-keep public class ag.sportradar.mobile.player.data.sdk.** {
#    public <methods>;
#    public <fields>;
#}

# Keep Koin DI framework
#-keep class org.koin.** { *; }
#-dontwarn org.koin.**

# Keep Kotlin serialization
#-keep class kotlinx.serialization.** { *; }
#-dontwarn kotlinx.serialization.**

# Keep Ktor client
#-keep class io.ktor.** { *; }
#-dontwarn io.ktor.**

# Keep coroutines
#-keep class kotlinx.coroutines.** { *; }
#-dontwarn kotlinx.coroutines.**

#-keep public class ag.sportradar.virtualstadium.datasdk.services.** {
#    public *;
#}
#-keep public class ag.sportradar.virtualstadium.datasdk.state.** {
#    public *;
#}
#-keep public class ag.sportradar.virtualstadium.datasdk.utils.** {
#    public *;
#}
#-keep public class ag.sportradar.virtualstadium.datasdk.model.** {
#    public *;
#}

#solves issue with sealed classes
#-dontshrink

# Standard ProGuard optimizations
#-optimizationpasses 5
#-verbose
