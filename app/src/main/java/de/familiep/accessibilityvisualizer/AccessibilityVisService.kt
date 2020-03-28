package de.familiep.accessibilityvisualizer

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.graphics.*
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class AccessibilityVisService : AccessibilityService() {

    private lateinit var drawView: DrawView

    override fun onServiceConnected() {

        drawView = DrawView(applicationContext)
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, //TODO: for compat, add support for devices < Android O
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.addView(drawView, params)
        super.onServiceConnected()
    }

    override fun onInterrupt() {}

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        if (event == null) return
        drawView.clear()

        val src = rootInActiveWindow
        traverseViewHierarchy(src)

        event.recycle()
    }

    private fun traverseViewHierarchy(parent: AccessibilityNodeInfo?) {

        if (parent == null) return

        if (!parent.isVisibleToUser) return

        val bounds = Rect()
        parent.getBoundsInScreen(bounds)
        drawView.addRect(bounds)

        if (parent.childCount > 0) {
            for (i in 0 until parent.childCount) {
                traverseViewHierarchy(parent.getChild(i))
            }
        }

        parent.recycle()
    }
}
