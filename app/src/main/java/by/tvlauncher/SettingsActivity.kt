package by.tvlauncher

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class SettingsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val p = Prefs(this)
        val col = LinearLayout(this)
        col.orientation = LinearLayout.VERTICAL
        col.setPadding(dp(32), dp(20), dp(32), dp(20))

        fun field(label: String, value: String): EditText {
            val l = TextView(this)
            l.text = label
            l.textSize = 13f
            l.setTextColor(GREY)
            col.addView(l)
            val e = EditText(this)
            e.setText(value)
            e.setTextColor(Color.WHITE)
            e.textSize = 15f
            e.setSingleLine(true)
            col.addView(e)
            return e
        }

        val ch = field("Плейлист каналов (m3u)", p.channelsUrl)
        val mv = field("Плейлист фильмов (m3u)", p.moviesUrl)
        val city = field("Город", p.city)
        val lat = field("Широта", p.lat)
        val lon = field("Долгота", p.lon)

        val save = tile("Сохранить") {
            p.channelsUrl = ch.text.toString().trim()
            p.moviesUrl = mv.text.toString().trim()
            p.city = city.text.toString().trim()
            val la = lat.text.toString().trim().replace(',', '.')
            val lo = lon.text.toString().trim().replace(',', '.')
            if (la.toDoubleOrNull() != null && lo.toDoubleOrNull() != null) {
                p.lat = la
                p.lon = lo
            }
            finish()
        }
        val lp = LinearLayout.LayoutParams(MATCH, dp(56))
        lp.topMargin = dp(16)
        col.addView(save, lp)

        val sv = ScrollView(this)
        sv.setBackgroundColor(BG)
        sv.addView(col)
        setContentView(sv)
    }
}
