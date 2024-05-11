package com.android.guacamole.ui.interfaces

import android.text.Editable
import android.text.TextWatcher
import com.android.guacamole.data.enums.ActionTextWatcher

interface TextWatcherListener : TextWatcher {

    override fun beforeTextChanged(
        charSequence: CharSequence?, start: Int, count: Int, after: Int
    ) {
        action(ActionTextWatcher.BEFORE_TEXT_CHANGED, charSequence, start, count, after)
    }

    override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
        action(ActionTextWatcher.ON_TEXT_CHANGED, charSequence, start, count, before)
    }

    override fun afterTextChanged(editable: Editable?) {
        action(ActionTextWatcher.AFTER_TEXT_CHANGED, editable, 0, 0, 0)
    }

    fun action(
        action: ActionTextWatcher, charSequence: CharSequence?,
        start: Int, count: Int, afterOrBefore: Int
    )
}