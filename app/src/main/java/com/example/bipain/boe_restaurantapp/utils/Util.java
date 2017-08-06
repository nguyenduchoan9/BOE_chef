package com.example.bipain.boe_restaurantapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.model.GroupDishByTable;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by hoang on 24/06/2017.
 */

public class Util {
    public static void lockObjectAndWait(Object object, boolean[] lock) {
        // after adding the Request to the volley queue
        synchronized (object) {
            try {
                while (!lock[0]) {
                    object.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void unLockObject(Object object, boolean[] lock) {
        synchronized (object) {
            lock[0] = true;
            object.notify();
        }
    }

    public static Drawable changeDrawableColor(Drawable drawable, int newColor) {
//        Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
        drawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
        return drawable;
    }

    // Table 6 x 4 -
    static public CharSequence setupTableGroupName(List<GroupDishByTable> listTable, Context context) {
        float normalSize = 12;//sp
        float boldSize = 16;//sp
        SpannableStringBuilder builder = new SpannableStringBuilder();
        Resources resources = context.getResources();
        for (GroupDishByTable item : listTable) {
            if (item.isWarning()) {
                createTablePart(resources.getString(R.string.text_table_title) + item.getTableNumber(),
                        " x " + item.getQuantity(), builder, boldSize, boldSize, context, Constant.NotifyType.Warning);
                builder.append(" - ");
            } else if (item.isShortNotify()) {
                createTablePart(resources.getString(R.string.text_table_title) + item.getTableNumber(),
                        " x " + item.getQuantity(), builder, boldSize, boldSize, context, Constant.NotifyType.Short);
                builder.append(" - ");
            } else {
                createTablePart(resources.getString(R.string.text_table_title) + item.getTableNumber(),
                        " x " + item.getQuantity(), builder, boldSize, boldSize, context, Constant.NotifyType.Normal);
                builder.append(" - ");
            }

        }
        return builder.delete(builder.length() - 3, builder.length() - 1);
    }

    static private void createTablePart(String label, String value,
                                        SpannableStringBuilder builder, float normalSize, float boldSize, Context context, int type) {
        if (Constant.NotifyType.Normal == type) {
            createStatsTitle(label, builder, normalSize, context, R.color.order_green);
            createStatsValue(value, builder, boldSize, context, R.color.order_green);
        } else if (Constant.NotifyType.Short == type) {
            createStatsTitle(label, builder, normalSize, context, R.color.holoOrangeLight);
            createStatsValue(value, builder, boldSize, context, R.color.holoOrangeLight);
        } else if (Constant.NotifyType.Warning == type) {
            createStatsTitle(label, builder, normalSize, context, R.color.colorChef);
            createStatsValue(value, builder, boldSize, context, R.color.colorChef);
        }
    }

    private static void createStatsValue(String value,
                                         SpannableStringBuilder builder, float boldSize, Context context, int color) {
        ColorStateList colorStateList;
        TextAppearanceSpan textAppearanceSpan;
        SpannableString spannableString;
//        R.color.order_green
        colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context, color));
        textAppearanceSpan = new TextAppearanceSpan(null,
                Typeface.NORMAL, pixelUnitWithSPUnit(boldSize, context), colorStateList, null);
        spannableString = new SpannableString(value);
        spannableString.setSpan(textAppearanceSpan, 0, value.length(), 0);
        builder.append(spannableString);
    }

    private static void createStatsTitle(String label,
                                         SpannableStringBuilder builder, float normalSize, Context context, int color) {
        ColorStateList colorStateList;
        TextAppearanceSpan textAppearanceSpan;
        colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context, color));
        textAppearanceSpan = new TextAppearanceSpan(null,
                Typeface.NORMAL, pixelUnitWithSPUnit(normalSize, context), colorStateList, null);
        SpannableString spannableString = new SpannableString(label);
        spannableString.setSpan(textAppearanceSpan, 0, label.length(), 0);
        builder.append(spannableString);
    }

    public static int pixelUnitWithSPUnit(float spValue, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, getDisplayMetrics(context));
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics;
    }

    public static String getLanguage(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("BOE", Context.MODE_PRIVATE);
        return sharedPreferences.getString("LANGUAGE_INFO", Constant.VI_LANGUAGE_STRING);
    }

    @SuppressWarnings("deprecation")
    public static void handleSelectLanguage(Activity activity, String lang) {
        String languageToLoad = lang; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
//        Context context  = createConfigurationContext(config);
//        Resources resources = context.getResources();
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
    }

    public static String formatVNDecimal(float money) {
        Locale local = new Locale("vi","VN");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(local);
//        DecimalFormat decimalFormat = new DecimalFormat("###.###.###");
        return currencyFormat.format(money);
    }
}

