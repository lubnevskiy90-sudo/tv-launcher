package by.tvlauncher

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable

data class AppInfo(val label: String, val pkg: String, val activity: String, val icon: Drawable)

object Apps {
    fun list(c: Context): List<AppInfo> {
        val pm = c.packageManager
        val res = LinkedHashMap<String, AppInfo>()
        for (cat in listOf(Intent.CATEGORY_LEANBACK_LAUNCHER, Intent.CATEGORY_LAUNCHER)) {
            val i = Intent(Intent.ACTION_MAIN).addCategory(cat)
            for (r in pm.queryIntentActivities(i, 0)) {
                val p = r.activityInfo.packageName
                if (p == c.packageName || res.containsKey(p)) continue
                res[p] = AppInfo(r.loadLabel(pm).toString(), p, r.activityInfo.name, r.loadIcon(pm))
            }
        }
        return res.values.sortedBy { it.label.lowercase() }
    }

    fun launch(c: Context, a: AppInfo) {
        c.startActivity(
            Intent(Intent.ACTION_MAIN).setClassName(a.pkg, a.activity)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    /** Ищет установленное приложение по части названия или пакета. */
    fun launchByWords(c: Context, vararg words: String): Boolean {
        val app = list(c).firstOrNull { a ->
            words.any { w -> a.label.lowercase().contains(w) || a.pkg.lowercase().contains(w) }
        } ?: return false
        launch(c, app)
        return true
    }
}
