package com.load.third.jqm.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.load.third.jqm.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/11/25.
 * 底部弹出的选择框
 */
public class PickerVewUtil {

//    传入String[] 类型
    public static void showPickerView(final Context context, String[] strings, TextView tv) {
        setPickView(context, Arrays.asList(strings), tv);
    }

    //    传入List<String> 类型
    public static void showPickerView(final Context context, List<String> strings, TextView tv) {
        setPickView(context, strings, tv);
    }

    private static void setPickView(Context context, final List<String> options1Items, final TextView tv) {
        hideInputKey(context);
        OptionsPickerView pickerView = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener( ) {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                tv.setText(options1Items.get(options1));
            }
        })
                .setSubmitColor(context.getResources( ).getColor(R.color.main_color))
                .setCancelColor(context.getResources( ).getColor(R.color.text_b5b5b5))
                .setTextColorCenter(context.getResources( ).getColor(R.color.main_color)).build( );
        pickerView.setPicker(options1Items);
        pickerView.setSelectOptions(getSelectItem(options1Items, StringUtils.getTextValue(tv)));
        pickerView.show( );
    }

//    选择省市时调用,选择完省市,自动判断区号并显示
    public static void showChoseArea(Context context, List<String> options1Items, final TextView tv_city, OptionsPickerView.OnOptionsSelectListener listener) {
        hideInputKey(context);
        OptionsPickerView pickerView = new OptionsPickerView.Builder(context, listener)
                .setSubmitColor(context.getResources( ).getColor(R.color.main_color))
                .setCancelColor(context.getResources( ).getColor(R.color.text_b5b5b5))
                .setTextColorCenter(context.getResources( ).getColor(R.color.main_color)).build( );
        pickerView.setPicker(options1Items);
        pickerView.setSelectOptions(getSelectItem(options1Items, StringUtils.getTextValue(tv_city)));
        pickerView.show( );
    }

    private static void hideInputKey(Context context) {
        try {
            InputMethodManager mInputKeyBoard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            mInputKeyBoard.hideSoftInputFromWindow(((Activity) context).getCurrentFocus( ).getWindowToken( ), InputMethodManager.HIDE_NOT_ALWAYS);
        }catch (Exception e){
        }
    }

    public static int getSelectItem(List<String> options1Items, String string) {
        int selectItem = 0;
        for (int i = 0; i < options1Items.size( ); i++) {
            if (options1Items.get(i).equals(string))
                selectItem = i;
        }
        return selectItem;
    }
}
