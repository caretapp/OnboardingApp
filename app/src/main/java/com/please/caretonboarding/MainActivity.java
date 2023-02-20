package com.please.caretonboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Context context = getApplicationContext();
		getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));

		ViewPager viewPager = findViewById(R.id.view_pager_card);

		LayoutInflater inflater = LayoutInflater.from(this);
		@SuppressLint("InflateParams")
		View[] cardViews = new View[] {
			inflater.inflate(R.layout.card_splash, null),
			inflater.inflate(R.layout.card_game, null),
			inflater.inflate(R.layout.card_what_is, null),
			inflater.inflate(R.layout.card_meta, null),
			inflater.inflate(R.layout.card_previews, null), //has stuff - 4
			inflater.inflate(R.layout.card_reqs, null),
			inflater.inflate(R.layout.card_purchase, null), //has stuff - 6
			inflater.inflate(R.layout.card_install, null),
			inflater.inflate(R.layout.card_end, null) //has stuff - 8
		};
		cardAdapter adapter = new cardAdapter(context, cardViews);
		viewPager.setAdapter( adapter );

		TabLayout tabLayout = findViewById(R.id.tab_layout_card);
		tabLayout.setupWithViewPager(viewPager, true);
	}

}