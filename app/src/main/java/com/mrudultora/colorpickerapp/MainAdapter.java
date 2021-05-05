/*
Copyright 2020 Mrudul Tora (ToraLabs)
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.mrudultora.colorpickerapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * @author Mrudul Tora (mrudultora@gmail.com)
 */
public class MainAdapter extends FragmentStatePagerAdapter {
    private final Context context;

    public MainAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ColorPickerPopUpFragment();
                break;
            case 1:
                fragment = new ColorPickerDialogsFragment();
                break;
        }
        assert fragment != null;
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        switch (position) {
            case 0:
                title = context.getResources().getString(R.string.pop_up);
                break;
            case 1:
                title = context.getResources().getString(R.string.dialogs);
                break;
        }
        return title;
    }
}
