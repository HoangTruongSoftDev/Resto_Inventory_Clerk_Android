package service;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

public class Service {
    public static void clearAllWidgets(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof EditText) {
                ((EditText)child).setText(null);
            }
            else if (child instanceof RadioGroup) {
                ((RadioGroup)child).clearCheck();
            }
            else if (child instanceof ViewGroup) {
                clearAllWidgets((ViewGroup)child);
            }
        }
    }
}
