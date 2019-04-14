package com.william_chow.contact.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.william_chow.contact.R;
import com.william_chow.contact.mvp.model.entity.Contact;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by William Chow on 14/04/2019.
 */
public class PrefUtils {

    private static volatile PrefUtils instance;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public static PrefUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (PrefUtils.class) {
                if (instance == null) {
                    instance = new PrefUtils(context, context.getString(R.string.app_name));
                }
            }
        }
        return instance;
    }

    private PrefUtils(Context context, String name) {
        sp = context.getApplicationContext().getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.apply();
    }

        /**
         * Save and get ArrayList in SharedPreference
         */
    public static void saveContactArrayList(Activity _activity, List<Contact> contactArrayList, String key) {
        SharedPreferences prefs = _activity.getApplicationContext().getSharedPreferences(_activity.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(contactArrayList);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public static ArrayList<Contact> getContactArrayList(Activity _activity, String key) {
        SharedPreferences prefs = _activity.getApplicationContext().getSharedPreferences(_activity.getString(R.string.app_name), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<Contact>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void removeContactArrayList() {
        remove(Constant.CONTACT_KEY);
    }

    /**
     * SP中移除该key
     *
     * @param key 键
     */
    public void remove(String key) {
        editor.remove(key).apply();
    }
}
