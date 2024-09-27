package com.example.githubproject.usif.main

import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.example.githubproject.R

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest{

    @Before
    fun setup(){
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun isFavoriteTabOpen(){
        onView(withId(R.id.favButton)).check(matches(isDisplayed())) //cek visiility tombol
        onView(withId(R.id.favButton)).perform(click()) //klik tombol
        onView(withId(R.id.favUserLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun isSettingTabOpen(){
        onView(withId(R.id.setting)).check(matches(isDisplayed())) //cek visiility tombol
        onView(withId(R.id.setting)).perform(click()) //klik tombol
        onView(withId(R.id.settingLayout)).check(matches(isDisplayed())) //cek apakah layout setting tampil
    }

    @Test
    fun isDetailTabOpen(){
        onView(withId(R.id.rvUserlist)).check(matches(isDisplayed())) //cek visiility tombol
        onView(withId(R.id.rvUserlist)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())) //klik item paling atas
        onView(withId(R.id.detailLayout)).check(matches(isDisplayed())) //cek apakah layout detail user tampil
    }
}