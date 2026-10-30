package by.tvlauncher

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView

class ChannelsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movies = intent.getStringExtra("kind") == "movies"
        val prefs = Prefs(this)
        val url = if (movies) prefs.moviesUrl else prefs.channelsUrl

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(BG)
        root.setPadding(dp(32), dp(20), dp(32), dp(20))

        val title = TextView(this)
        title.text = if (movies) "Фильмы" else "Каналы"
        title.textSize = 26f
        title.setTextColor(Color.WHITE)
        root.addView(title)

        val status = TextView(this)
        status.text = "Загрузка…"
        status.textSize = 14f
        status.setTextColor(GREY)
        root.addView(status)

        val list = ListView(this)
        list.divider = null
        root.addView(list, LinearLayout.LayoutParams(MATCH, 0, 1f))
        setContentView(root)

        val self = this
        Thread {
            try {
                val items = M3u.load(url)
                runOnUiThread {
                    status.text = if (items.isEmpty()) "Список пуст" else "Найдено: ${items.size}"
                    list.adapter = object : ArrayAdapter<Channel>(self, android.R.layout.simple_list_item_1, items) {
                        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                            val t = super.getView(position, convertView, parent) as TextView
                            t.text = getItem(position)?.name ?: ""
                            t.setTextColor(Color.WHITE)
                            t.textSize = 18f
                            return t
                        }
                    }
                    list.setOnItemClickListener { _, _, pos, _ ->
                        startActivity(Intent(self, PlayerActivity::class.java).putExtra("url", items[pos].url))
                    }
                    list.requestFocus()
                }
            } catch (e: Exception) {
                runOnUiThread { status.text = "Не удалось загрузить список: ${e.message}" }
            }
        }.start()
    }
}
