package by.tvlauncher

import java.net.HttpURLConnection
import java.net.URL

data class Channel(val name: String, val url: String)

object M3u {
    fun load(url: String): List<Channel> {
        val c = URL(url).openConnection() as HttpURLConnection
        c.connectTimeout = 10000
        c.readTimeout = 20000
        val text = c.inputStream.bufferedReader().use { it.readText() }
        return parse(text)
    }

    fun parse(text: String): List<Channel> {
        val out = ArrayList<Channel>()
        var name: String? = null
        for (raw in text.lineSequence()) {
            val l = raw.trim()
            if (l.startsWith("#EXTINF")) {
                name = l.substringAfterLast(",").trim()
            } else if (l.isNotEmpty() && !l.startsWith("#")) {
                out.add(Channel(name?.ifEmpty { null } ?: l, l))
                name = null
            }
        }
        return out
    }
}
