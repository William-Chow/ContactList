package com.william_chow.contact.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;

import com.william_chow.contact.mvp.model.entity.Contact;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by William Chow on 14/04/2019.
 */
public class Utils {

    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";

    public static String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    private static final String REGEX_EMAIL_ = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    public static boolean isEmail_(String email) {
        return Pattern.matches(REGEX_EMAIL_, email);
    }

    public static void returnBundleIntentWithoutFinish(Activity _currentActivity, Class _moveActivity, Contact _contact) {
        Intent intent = new Intent(_currentActivity, _moveActivity);
        intent.putExtra("list", _contact);
        _currentActivity.startActivity(intent);
    }

    /* Get File in Assets Folder */
    public static String getAssetsJSON(Activity _activity, String fileName) {
        String json = null;
        try {
            InputStream inputStream = _activity.getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

    /**
     * @param _activity _activity
     */
    public static void hideKeyboard(Activity _activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) _activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(_activity.getWindow().getDecorView().getRootView().getWindowToken(), 0);
        }
    }
}
