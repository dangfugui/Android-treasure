package com.diary.activity;

import android.app.Activity;
import android.os.Bundle;

import com.dang.treasure.R;

public class About extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.com_diary_about);
		setTitle(getString(R.string.app_name)+"-"+getString(R.string.about));
	}
}
