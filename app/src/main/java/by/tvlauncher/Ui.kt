package by.tvlauncher

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.view.Gravity
import android.widget.TextView

const val MATCH = -1
const val WRAP = -2
val BG: Int = 0xFF101820.toInt()
val GREY: Int = 0xFFAAB7C4.toInt()

fun Context.dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()

private fun Context.box(color: Int) = GradientDrawable().apply {
    setColor(color)
    cornerRadius = dp(10).toFloat()
}

fun Context.tile(label: String, onClick: () -> Unit): TextView {
    val t = TextView(this)
    t.text = label
    t.textSize = 20f
    t.gravity = Gravity.CENTER
    t.setTextColor(Color.WHITE)
    t.isFocusable = true
    t.isClickable = true
    val bg = StateListDrawable()
    bg.addState(intArrayOf(android.R.attr.state_focused), box(0xFF2E7DFF.toInt()))
    bg.addState(intArrayOf(), box(0xFF22303C.toInt()))
    t.background = bg
    t.setOnClickListener { onClick() }
    return t
}
