package com.lll.demo.fanlayoutmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lll.demo.fanlayoutmanager.fragment.MainFragment
import com.lll.layoutmanager.demo.R

/**
 * Version 1.0
 * Created by lll on 2019/1/7.
 * Description
 * copyright generalray4239@gmail.com
 */
class FanlayoutManagerActivity : AppCompatActivity() {

    private lateinit var mMainFragment: MainFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fanlayoutmanager)
        if (savedInstanceState == null) {
            mMainFragment = MainFragment.newInstance()
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.root, mMainFragment)
                    .commit()
        } else {
            val fragment = supportFragmentManager.findFragmentById(R.id.root)
            if (fragment != null && fragment is MainFragment) {
                mMainFragment = fragment
            }
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.root)!!
        if (fragment != null && fragment is MainFragment) {
            mMainFragment = fragment
        }
        if (!mMainFragment.isAdded() || !mMainFragment.deselectIfSelected()) {
            super.onBackPressed()
        }
    }
}