package com.mobatia.bisad.manager

import android.content.Context
import com.mobatia.bisad.R

@Suppress("DEPRECATION")
class PreferenceData {
    private val PREFSNAME = "BISAD"


    /**
     * SET ACCESS TOKEN
     */

    fun setaccesstoken(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("access_token", id)
        editor.apply()
    }

    /**
     * GET ACCESS TOKEN
     */

    fun getaccesstoken(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("access_token", "")
    }

    /**
     * SET BUTTON ONE TAB ID
     */

    fun setbuttononetabid(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_onetabid", id)
        editor.apply()
    }

    /**
     * GET BUTTON ONE TAB ID
     */

    fun getbuttononetabid(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_onetabid", "")
    }

    fun setbuttontwotabid(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_twotabid", id)
        editor.apply()
    }

    /**
     * GET BUTTON TWO TAB ID
     */

    fun getbuttontwotabid(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_twotabid", "5")
    }

    fun setbuttonthreetabid(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_threetabid", id)
        editor.apply()
    }

    /**
     * GET BUTTON THREE TAB ID
     */

    fun getbuttonthreetabid(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_threetabid", "8")
    }

    fun setbuttonfourtabid(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_fourtabid", id)
        editor.apply()
    }

    /**
     * GET BUTTON FOUR TAB ID
     */

    fun getbuttonfourtabid(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_fourtabid", "9")
    }

    fun setbuttonfivetabid(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_fivetabid", id)
        editor.apply()
    }

    /**
     * GET BUTTON FIVE TAB ID
     */

    fun getbuttonfivetabid(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_fivetabid", "3")
    }

    fun setbuttonsixtabid(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_sixtabid", id)
        editor.apply()
    }

    /**
     * GET BUTTON SIX TAB ID
     */

    fun getbuttonsixtabid(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_sixtabid", "4")
    }

    fun setbuttonseventabid(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_seventabid", id)
        editor.apply()
    }

    /**
     * GET BUTTON SEVEN TAB ID
     */

    fun getbuttonseventabid(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_seventabid", "2")
    }

    fun setbuttoneighttabid(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_eighttabid", id)
        editor.apply()
    }

    /**
     * GET BUTTON EIGHT TAB ID
     */

    fun getbuttoneighttabid(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_eighttabid", "10")
    }

    fun setbuttonninetabid(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_ninetabid", id)
        editor.apply()
    }

    /**
     * GET BUTTON NINE TAB ID
     */

    fun getbuttonninetabid(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_ninetabid", "6")
    }


    /**
     * SET BUTTON ONE TEXT IMAGE
     */

    fun setbuttononetextimage(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_onetextimage", id)
        editor.apply()
    }

    /**
     * GET BUTTON ONE TEXT IMAGE
     */

    fun getbuttononetextimage(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_onetextimage", "1")
    }

    fun setbuttontwotextimage(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_twotextimage", id)
        editor.apply()
    }

    /**
     * GET BUTTON TWO TEXT IMAGE
     */

    fun getbuttontwotextimage(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_twotextimage", "5")
    }

    fun setbuttonthreetextimage(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_threetextimage", id)
        editor.apply()
    }

    /**
     * GET BUTTON THREE TEXT IMAGE
     */

    fun getbuttonthreetextimage(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_threetextimage", "8")
    }

    fun setbuttonfourtextimage(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_fourtextimage", id)
        editor.apply()
    }

    /**
     * GET BUTTON FOUR TEXT IMAGE
     */

    fun getbuttonfourtextimage(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_fourtextimage", "9")
    }

    fun setbuttonfivetextimage(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_fivetextimage", id)
        editor.apply()
    }

    /**
     * GET BUTTON FIVE TEXT IMAGE
     */

    fun getbuttonfivetextimage(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_fivetextimage", "3")
    }

    fun setbuttonsixtextimage(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_sixtextimage", id)
        editor.apply()
    }

    /**
     * GET BUTTON SIX TEXT IMAGE
     */

    fun getbuttonsixtextimage(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_sixtextimage", "4")
    }

    fun setbuttonseventextimage(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_seventextimage", id)
        editor.apply()
    }

    /**
     * GET BUTTON SEVEN TEXT IMAGE
     */

    fun getbuttonseventextimage(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_seventextimage", "2")
    }

    fun setbuttoneighttextimage(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_eighttextimage", id)
        editor.apply()
    }

    /**
     * GET BUTTON EIGHT TEXT IMAGE
     */

    fun getbuttoneighttextimage(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_eighttextimage", "10")
    }

    fun setbuttonninetextimage(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_ninetextimage", id)
        editor.apply()
    }

    /**
     * GET BUTTON NINE TEXT IMAGE
     */

    fun getbuttonninetextimage(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_ninetextimage", "6")
    }

    fun setButtonOneGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("buttononeguestbg", color)
        editor.apply()
    }

    fun getButtonOneGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(
            "buttononeguestbg", context.resources
                .getColor(R.color.transparent)
        )
    }

    fun setButtontwoGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("buttontwoguestbg", color)
        editor.apply()
    }

    fun getButtontwoGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(
            "buttontwoguestbg", context.resources
                .getColor(R.color.transparent)
        )
    }

    fun setButtonthreeGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("buttonthreeguestbg", color)
        editor.apply()
    }

    fun getButtonthreeGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(
            "buttonthreeguestbg", context.resources
                .getColor(R.color.transparent)
        )
    }

    fun setButtonfourGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("buttonfourguestbg", color)
        editor.apply()
    }

    fun getButtonfourGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(
            "buttonfourguestbg", context.resources
                .getColor(R.color.transparent)
        )
    }

    fun setButtonfiveGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("buttonfiveguestbg", color)
        editor.apply()
    }

    fun getButtonfiveGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(
            "buttonfiveguestbg", context.resources
                .getColor(R.color.transparent)
        )
    }

    fun setButtonsixGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("buttonsixguestbg", color)
        editor.apply()
    }

    fun getButtonsixGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(
            "buttonsixguestbg", context.resources
                .getColor(R.color.transparent)
        )
    }

    fun setButtonsevenGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("buttonsevenguestbg", color)
        editor.apply()
    }

    fun getButtonsevenGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(
            "buttonsevenguestbg", context.resources
                .getColor(R.color.transparent)
        )
    }

    fun setButtoneightGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("buttoneightguestbg", color)
        editor.apply()
    }

    fun getButtoneightGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(
            "buttoneightguestbg", context.resources
                .getColor(R.color.transparent)
        )
    }

    fun setButtonnineGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("buttonnineguestbg", color)
        editor.apply()
    }

    fun getButtonnineGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(
            "buttonnineguestbg", context.resources
                .getColor(R.color.transparent)
        )
    }

    //Login user check

    /**
     * SET ACCESS TOKEN
     */

    fun setUserID(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("user_id", id)
        editor.apply()
    }

    /**
     * GET ACCESS TOKEN
     */

    fun getUserID(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("user_id", "")
    }

    /*SET USER CODE*/
    fun setUserCode(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("user_code", id)
        editor.apply()
    }

    /*GET USER CODE*/
    fun getUserCode(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("user_code", "")
    }

    /*SET FIRE BASE ID*/
    fun setFcmID(context: Context, id: String) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("firebase id", id)
        editor.apply()
    }

    /*GET FIREBASE ID*/
    fun getFcmID(context: Context): String {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("firebase id", "").toString()
    }


}