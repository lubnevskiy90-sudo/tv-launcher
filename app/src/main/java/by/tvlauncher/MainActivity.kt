package by.tvlauncher

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextClock
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var weatherView: TextView
    private lateinit var forecastView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(BG)
        root.setPadding(dp(32), dp(20), dp(32), dp(20))

        val top = LinearLayout(this)
        top.orientation = LinearLayout.HORIZONTAL
        top.gravity = Gravity.CENTER_VERTICAL

        val clock = TextClock(this)
        clock.format24Hour = "HH:mm"
        clock.format12Hour = "HH:mm"
        clock.textSize = 48f
        clock.setTextColor(Color.WHITE)
        top.addView(clock)

        val date = TextClock(this)
        date.format24Hour = "EEEE, d MMMM"
        date.format12Hour = "EEEE, d MMMM"
        date.textSize = 16f
        date.setTextColor(GREY)
        date.setPadding(dp(16), 0, 0, 0)
        top.addView(date)

        top.addView(View(this), LinearLayout.LayoutParams(0, 1, 1f))

        weatherView = TextView(this)
        weatherView.textSize = 20f
        weatherView.setTextColor(Color.WHITE)
        weatherView.text = "Погода…"
        top.addView(weatherView)
        root.addView(top, LinearLayout.LayoutParams(MATCH, WRAP))

        forecastView = TextView(this)
        forecastView.textSize = 15f
        forecastView.setTextColor(GREY)
        forecastView.gravity = Gravity.END
        forecastView.setPadding(0, dp(8), 0, dp(12))
        root.addView(forecastView, LinearLayout.LayoutParams(MATCH, WRAP))

        fun row(vararg items: TextView): LinearLayout {
            val r = LinearLayout(this)
            r.orientation = LinearLayout.HORIZONTAL
            for (t in items) {
                val lp = LinearLayout.LayoutParams(0, MATCH, 1f)
                lp.setMargins(dp(6), dp(6), dp(6), dp(6))
                r.addView(t, lp)
            }
            return r
        }

        fun go(c: Class<*>, kind: String? = null) {
            val i = Intent(this, c)
            if (kind != null) i.putExtra("kind", kind)
            startActivity(i)
        }

        fun app(name: String, vararg words: String) {
            if (!Apps.launchByWords(this, *words)) {
                Toast.makeText(this, "$name не найден. Установите приложение на ТВ", Toast.LENGTH_LONG).show()
            }
        }

        root.addView(
            row(
                tile("Каналы") { go(ChannelsActivity::class.java, "channels") },
                tile("Фильмы") { go(ChannelsActivity::class.java, "movies") },
                tile("Кинопоиск") { app("Кинопоиск", "кинопоиск", "kinopoisk") }
            ),
            LinearLayout.LayoutParams(MATCH, 0, 1f)
        )
        root.addView(
            row(
                tile("YouTube") { app("YouTube", "youtube") },
                tile("Приложения") { go(AppsActivity::class.java) },
                tile("Настройки") { go(SettingsActivity::class.java) }
            ),
            LinearLayout.LayoutParams(MATCH, 0, 1f)
        )
        setContentView(root)
    }

    override fun onResume() {
        super.onResume()
        loadWeather()
    }

    // Лаунчер не должен закрываться кнопкой «Назад».
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {}

    private fun loadWeather() {
        val p = Prefs(this)
        Thread {
            try {
                val f = Weather.load(p.lat, p.lon)
                runOnUiThread {
                    weatherView.text = "${p.city}  ${sign(f.temp)}°  ${Weather.describe(f.code)}"
                    forecastView.text = f.days.joinToString("     ") {
                        "${it.label}: ${sign(it.max)}°/${sign(it.min)}° ${Weather.describe(it.code)}"
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    weatherView.text = "Погода недоступна"
                    forecastView.text = ""
                }
            }
        }.start()
    }

    private fun sign(v: Int) = if (v > 0) "+$v" else "$v"
}
