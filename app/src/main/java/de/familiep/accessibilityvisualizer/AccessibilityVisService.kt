package de.familiep.accessibilityvisualizer

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityEvent.*
import android.view.accessibility.AccessibilityNodeInfo

class AccessibilityVisService : AccessibilityService() {

    private lateinit var drawView : DrawView

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

        if(event == null) return
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

    private fun printEventType(eventType: Int){
        val stringType = when (eventType) {
            TYPE_ANNOUNCEMENT -> "announcement"
            TYPE_ASSIST_READING_CONTEXT -> "reading scrren context"
            TYPE_GESTURE_DETECTION_START -> "start of gesture"
            TYPE_GESTURE_DETECTION_END -> "end of gesture"
            TYPE_NOTIFICATION_STATE_CHANGED -> "new notification"
            TYPE_TOUCH_EXPLORATION_GESTURE_START -> "start of touch expl."
            TYPE_TOUCH_EXPLORATION_GESTURE_END -> "end of touch exmpl."
            TYPE_TOUCH_INTERACTION_START -> "start of touch event"
            TYPE_TOUCH_INTERACTION_END -> "end of touch event"
            TYPE_VIEW_ACCESSIBILITY_FOCUSED -> "accessiblility focus start"
            TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED -> "accessibility foucs end"
            TYPE_VIEW_CLICKED -> "view clicked"
            TYPE_VIEW_CONTEXT_CLICKED -> "context click on view"
            TYPE_VIEW_FOCUSED -> "view is focused"
            TYPE_VIEW_HOVER_ENTER -> "hover enter"
            TYPE_VIEW_HOVER_EXIT -> "hover exit"
            TYPE_VIEW_LONG_CLICKED -> "view long clicked"
            TYPE_VIEW_SCROLLED -> "view scrolled"
            TYPE_VIEW_SELECTED -> "element selected"
            TYPE_VIEW_TEXT_CHANGED -> "text edit"
            TYPE_VIEW_TEXT_SELECTION_CHANGED -> "text selected changed"
            TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY -> "text travered"
            TYPE_WINDOWS_CHANGED -> "windows changed"
            TYPE_WINDOW_CONTENT_CHANGED -> "window content changed"
            TYPE_WINDOW_STATE_CHANGED -> "window state changed"
            else -> "unknown type LOL"
        }
        Log.d("debugg", "type is: $stringType")
    }
}
