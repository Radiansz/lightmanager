package com.lightsoft.microwave.lightmanager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by Microwave on 16.02.2015.
 */
public class InputAssister implements TextWatcher {

    EditText origin;
    ArrayList<Type> assistDic;
    boolean missTurn = false;


    public InputAssister(EditText view, ArrayList<Type> dict){
        origin = view;
        assistDic = dict;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.i("myinfo","beforeTextChanged:" + s.toString() + ", start:" + start +" after:" + after + ", count:" + count);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(count == 0)
            missTurn = true;
        Log.i("myinfo","onTextChanged:" + s.toString() + ", start:" + start +" before:" + before + ", count:" + count);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!missTurn) {
            String cur = s.toString();
            int curLen = cur.length();
            String word = null;
            for (Type elem : assistDic) {
                if (elem.type.indexOf(cur) == 0) {
                    word = elem.type;
                    break;
                }
            }
            if(word == null)
                return;
            int fullLen = word.length();
            missTurn = true;
            origin.setText(word);
            origin.setSelection(curLen, fullLen);
        }
        else
            missTurn = false;
    }
}
