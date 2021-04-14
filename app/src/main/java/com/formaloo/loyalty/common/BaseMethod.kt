package com.formaloo.loyalty.common

import android.content.res.Resources
import android.view.View
import com.formaloo.loyalty.common.Constants.ERRORS
import com.formaloo.loyalty.common.Constants.FORM_ERRORS
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray
import org.json.JSONObject

class BaseMethod {

    fun getJSONObject(it: JSONObject, key: String): JSONObject {
        return if (it.has(key)) {
            it.getJSONObject(key) as JSONObject

        } else {
            JSONObject()
        }
    }

    fun getJSONArray(it: JSONObject, key: String): JSONArray{
        return   if (it.has(key)) {
             it.getJSONArray(key)

        }else{
            JSONArray()
        }
    }

    fun retrieveJSONArrayFirstItem(gErrors: JSONArray): String {
        gErrors.let {
            return if (it.length() > 0) {
                it[0].toString()
            } else ""
        }
    }

    fun showMsg(view: View, msg: Int) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show()

    }

    fun showMsg(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun pxToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }
}