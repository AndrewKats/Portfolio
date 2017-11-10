package com.example.andrewkats.networkbattleship;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by AndrewKats on 11/17/2016.
 */
public class BeginTabletFragment extends Fragment
{
    public static BeginTabletFragment newInstance()
    {
        return new BeginTabletFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        TextView selectText = new TextView(getActivity());
        selectText.setText("Select or start a game");
        selectText.setTextSize(20);
        selectText.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        return selectText;
    }
}
