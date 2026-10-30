package by.tvlauncher

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

class AppsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apps = Apps.list(this)
        val list = ListView(this)
        list.setBackgroundColor(BG)
        list.divider = null
        list.setPadding(dp(32), dp(20), dp(32), dp(20))
        val self = this
        list.adapter = object : ArrayAdapter<AppInfo>(self, android.R.layout.simple_list_item_1, apps) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val t = super.getView(position, convertView, parent) as TextView
                val a = getItem(position)!!
                t.text = a.label
                t.setTextColor(Color.WHITE)
                t.textSize = 18f
                val ic = a.icon
                ic.setBounds(0, 0, dp(40), dp(40))
                t.setCompoundDrawables(ic, null, null, null)
                t.compoundDrawablePadding = dp(16)
                return t
            }
        }
        list.setOnItemClickListener { _, _, pos, _ -> Apps.launch(self, apps[pos]) }
        setContentView(list)
    }
}
