package com.lll.demo.fanlayoutmanager.view

import android.content.Context
import android.os.Build
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.ChangeTransform
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import androidx.transition.Transition
import androidx.transition.TransitionSet

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class SharedTransitionSet : TransitionSet {
    constructor() {
        initTransition()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initTransition()
    }


    private fun initTransition() {
        ordering = android.transition.TransitionSet.ORDERING_TOGETHER
        addTransition(ChangeBounds() as Transition).addTransition(ChangeTransform() as Transition).addTransition(ChangeImageTransform() as Transition)
    }
}