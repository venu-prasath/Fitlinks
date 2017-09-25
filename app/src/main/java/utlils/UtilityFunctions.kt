package utils

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import java.util.concurrent.TimeUnit


fun Int.isOdd() = this.rem(2) == 1

fun Int.isEven() = this.rem(2) == 0
fun Int.format(digits: Int) = String.format("%0${digits}d%n", this)

fun Boolean.asInt(): Int {
    return if (this) 1 else 0
}

fun Int.asBoolean(): Boolean {
    return (this == 1)
}

fun Long.toMinutes() = TimeUnit.MILLISECONDS.toMinutes(this)
fun Long.toSeconds() = TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this))

inline fun supportsLollipop(func: () -> Unit) =
        supportsVersion(Build.VERSION_CODES.LOLLIPOP, func)

fun isLollipopOrBellow(): Boolean = (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.LOLLIPOP)

inline fun supportsVersion(ver: Int, func: () -> Unit) {
    if (Build.VERSION.SDK_INT >= ver) {
        func.invoke()
    }
}

inline fun inReleaseMode(func: () -> Unit) {
    //if (BuildConfig.BUILD_TYPE.equals("release")) {
        func()
    //}
}

inline fun inDebugMode(func: () -> Unit) {
   // if (BuildConfig.BUILD_TYPE.equals("debug")) {
        func()
    //}
}


fun getDeviceId(context: Context): String = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID);

inline fun ObjectAnimator.onStart(crossinline func: () -> Unit) {
    addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {}
        override fun onAnimationEnd(animation: Animator?) {}
        override fun onAnimationCancel(animation: Animator?) {}
        override fun onAnimationStart(animation: Animator?) {
            func()
        }
    })
}

inline fun ObjectAnimator.onEnd(crossinline func: () -> Unit) {
    addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {}
        override fun onAnimationEnd(animation: Animator?) {
            func()
        }

        override fun onAnimationCancel(animation: Animator?) {}
        override fun onAnimationStart(animation: Animator?) {}
    })
}

fun Intent.hasExtras(extras: List<String>): Boolean = extras.all { hasExtra(it) }
