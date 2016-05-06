package com.example.app_10p5;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jean-loup Beaussart on 02/05/2016.
 */
public class DecimalDigitsInputFilter implements InputFilter {

    Pattern mPattern;

    public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
        mPattern = Pattern.compile("(0|[1-9][0-9]{0,2})?([,.][0-9]{0,2})?");

    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        String result =
                dest.subSequence(0, dstart)
                        + source.toString()
                        + dest.subSequence(dend, dest.length());

        Matcher matcher = mPattern.matcher(result);

        if (!matcher.matches()) return dest.subSequence(dstart, dend);

        return null;
    }

}
