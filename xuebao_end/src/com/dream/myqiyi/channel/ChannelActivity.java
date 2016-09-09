package com.dream.myqiyi.channel;

import com.dang.treasure.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ChannelActivity extends Activity {
	TextView mTitleView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aiqiyi_channel_activity);
		prepareView();
		mTitleView.setText(R.string.category_channel);
	}

	private void prepareView() {
		mTitleView = (TextView) findViewById(R.id.title_text);
	}
}
