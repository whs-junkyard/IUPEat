package th.`in`.whs.ku.iupeat

import android.content.Context
import org.json.JSONObject
import java.util.Scanner
import java.util.ArrayList
import org.json.JSONArray
import kotlin.properties.Delegates

fun JSONArray.toArray() : Array<String> = Array(length()) { i -> getString(i) }

class MenuReader(context : Context) {
    val context = context

    val menuFile : JSONObject by Delegates.lazy {
        val json = context.getResources()!!.openRawResource(R.raw.menu)
        val scanner = Scanner(json)
        scanner.useDelimiter("\\A")
        JSONObject(scanner.next())
    }

    val menuList : List<String> by Delegates.lazy {
        var keys = menuFile.keys() as Iterator<String>
        var out = ArrayList<String>()
        for(item in keys){
            out.add(item)
        }
        out
    }

    public fun getMenu(menuType : String) : Array<String> {
        return menuFile.getJSONArray(menuType).toArray()
    }
}