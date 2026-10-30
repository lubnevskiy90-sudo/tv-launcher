package by.tvlauncher

import android.content.Context

class Prefs(c: Context) {
    private val p = c.getSharedPreferences("cfg", Context.MODE_PRIVATE)

    private fun get(k: String, d: String): String = p.getString(k, d) ?: d
    private fun put(k: String, v: String) = p.edit().putString(k, v).apply()

    var channelsUrl: String
        get() = get("channels", "https://iptv-org.github.io/iptv/countries/by.m3u")
        set(v) = put("channels", v)
    var moviesUrl: String
        get() = get("movies", "https://iptv-org.github.io/iptv/categories/movies.m3u")
        set(v) = put("movies", v)
    var city: String
        get() = get("city", "Минск")
        set(v) = put("city", v)
    var lat: String
        get() = get("lat", "53.9")
        set(v) = put("lat", v)
    var lon: String
        get() = get("lon", "27.5667")
        set(v) = put("lon", v)
}
