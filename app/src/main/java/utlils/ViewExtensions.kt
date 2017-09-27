package utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.ProgressDialog
//import android.app.ProgressDialog
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import utils.onEnd
import utils.onStart


/**
 * Created by aditlal on 04/07/17.
 */
fun View.isVisible() = visibility == View.VISIBLE

var progressDialog: ProgressDialog? = null

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}


fun View.getString(res: Int) = resources.getString(res)


fun View.fadeIn(duration: Long, f: () -> Unit) {
    ObjectAnimator.ofFloat(this, "alpha", 0f, 1f).apply {
        setDuration(duration)
        onStart { f() }
        start()
    }
}

fun View.fadeOut(duration: Long, f: () -> Unit) {
    ObjectAnimator.ofFloat(this, "alpha", 1f, 0f).apply {
        setDuration(duration)
        onEnd { f() }
        start()
    }
}

fun View.hideKeyboard(inputMethodManager: InputMethodManager) {
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard(inputMethodManager: InputMethodManager) {
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}


fun onViews(views: List<View>, func: View.() -> Unit) {
    views.map { it.func() }
}

fun Activity.showProgress(message: String): ProgressDialog {
    progressDialog = ProgressDialog(window.context)
    progressDialog!!.setCanceledOnTouchOutside(false)
    progressDialog!!.setCancelable(false)
    progressDialog!!.setMessage(message)
    progressDialog!!.show()
    return progressDialog as ProgressDialog
}

fun Activity.hideProgress() {
    progressDialog!!.cancel()
}

fun hideViews(views: List<View>) {
    onViews(views) { hide() }
}

fun showViews(views: List<View>) {
    onViews(views) { show() }
}

fun conditionalShowViews(views: List<View>, predicate: () -> Boolean) {
    if (predicate()) showViews(views) else hideViews(views)
}

fun View.slideExit() {
    if (translationY == 0f) animate().translationY(-height.toFloat())
}

fun View.slideEnter() {
    if (translationY < 0f) animate().translationY(0f)
}

operator fun ViewGroup.get(position: Int): View {
    return getChildAt(position)
}

fun inflateLayout(parent: ViewGroup?, layout: Int): View =
        LayoutInflater.from(parent!!.context).inflate(layout, parent, false)


fun View.waitForMeasure(func: (v: View, w: Int, h: Int) -> Unit) {

    if (width > 0 && height > 0) {
        func(this, width, height)
        return
    }

    val listener = object : ViewTreeObserver.OnPreDrawListener {

        override fun onPreDraw(): Boolean {
            val observer = viewTreeObserver
            if (observer.isAlive) {
                observer.removeOnPreDrawListener(this)
            }

            func(this@waitForMeasure, width, height)
            return true
        }
    }

    viewTreeObserver.addOnPreDrawListener(listener)
}

fun Activity.screenWidth(): Int {
    val metrics: DisplayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)
    return metrics.widthPixels
}

fun Activity.screenHeight(): Int {
    val metrics: DisplayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)
    return metrics.heightPixels
}

fun View.slideDown() {
    animate()
            .translationY(height.toFloat())
            .alpha(0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    // superfluous restoration
                    visibility = View.GONE
                    alpha = 1f
                    translationY = 0f
                }
            })
}

fun View.slideUp() {
    visibility = View.VISIBLE
    alpha = 0f

    if (height > 0) {
        translationY = height.toFloat()
        animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(200)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        visibility = View.VISIBLE
                        alpha = 1f
                    }
                })
    } else {
        // wait till height is measured
        post {
            translationY = height.toFloat()
            animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setDuration(200)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            visibility = View.VISIBLE
                            alpha = 1f
                        }
                    })
        }
    }
}
