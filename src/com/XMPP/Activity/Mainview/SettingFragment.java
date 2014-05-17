package com.XMPP.Activity.Mainview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.XMPP.R;
import com.XMPP.Model.SettingItemOnTouchListener;
import com.XMPP.smack.Smack;

public class SettingFragment extends Fragment {
	Smack smack;
	int[] ResId = { R.id.relative_1, R.id.relative_2, R.id.relative_3,
			R.id.relative_4, R.id.relative_5, R.id.relative_6, R.id.relative_7,
			R.id.relative_8, R.id.relative_9, R.id.relative_10,
			R.id.relative_11, R.id.relative_12,

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_setting, container,
				false);
		init(view);

		return view;
	}

	public void init(View view) {
		SettingItemOnTouchListener mTouch;
		View area;
		for (int i = 0; i < 12; i++) {
			if (i == 0 || i == 2 || i == 5 || i == 9) {
				area = view.findViewById(ResId[i]);
				area.setOnTouchListener(new SettingItemOnTouchListener(area,
						R.drawable.round_press_corner_mid,
						R.drawable.round_corner_top));
				continue;
			}
			if(i == 3 || i == 6 || i == 7){
				area = view.findViewById(ResId[i]);
				area.setOnTouchListener(new SettingItemOnTouchListener(area,
						R.drawable.round_press_corner_mid,
						R.drawable.round_corner_mid));
				continue;
			}
			if(i == 1 || i == 4 || i == 8 || i == 10){
				area = view.findViewById(ResId[i]);
				area.setOnTouchListener(new SettingItemOnTouchListener(area,
						R.drawable.round_press_corner_mid,
						R.drawable.round_corner_buttom));
				continue;
			}
			if(i == 11){
				area = view.findViewById(ResId[i]);
				area.setOnTouchListener(new SettingItemOnTouchListener(area,
						R.drawable.round_press_corner_mid,
						R.drawable.round_corner));
				continue;
			}
		}

	}

}