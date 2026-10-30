package by.tvlauncher

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale

data class DayForecast(val label: String, val min: Int, val max: Int, val code: Int)
data class Forecast(val temp: Int, val code: Int, val days: List<DayForecast>)

object Weather {
    fun load(lat: String, lon: String): Forecast {
        val u = "https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lon" +
            "&current=temperature_2m,weather_code" +
            "&daily=weather_code,temperature_2m_max,temperature_2m_min" +
            "&timezone=auto&forecast_days=4"
        val c = URL(u).openConnection() as HttpURLConnection
        c.connectTimeout = 10000
        c.readTimeout = 15000
        val j = JSONObject(c.inputStream.bufferedReader().use { it.readText() })
        val cur = j.getJSONObject("current")
        val d = j.getJSONObject("daily")
        val times = d.getJSONArray("time")
        val codes = d.getJSONArray("weather_code")
        val mx = d.getJSONArray("temperature_2m_max")
        val mn = d.getJSONArray("temperature_2m_min")
        val inFmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val outFmt = SimpleDateFormat("EEE d MMM", Locale("ru"))
        val days = ArrayList<DayForecast>()
        for (i in 0 until times.length()) {
            val label = if (i == 0) "Сегодня" else outFmt.format(inFmt.parse(times.getString(i))!!)
            days.add(
                DayForecast(
                    label,
                    Math.round(mn.getDouble(i)).toInt(),
                    Math.round(mx.getDouble(i)).toInt(),
                    codes.getInt(i)
                )
            )
        }
        return Forecast(Math.round(cur.getDouble("temperature_2m")).toInt(), cur.getInt("weather_code"), days)
    }

    fun describe(code: Int): String = when (code) {
        0 -> "ясно"
        1, 2 -> "малооблачно"
        3 -> "пасмурно"
        45, 48 -> "туман"
        51, 53, 55, 56, 57 -> "морось"
        61, 63, 65, 66, 67 -> "дождь"
        71, 73, 75, 77 -> "снег"
        80, 81, 82 -> "ливень"
        85, 86 -> "снегопад"
        95, 96, 99 -> "гроза"
        else -> ""
    }
}
