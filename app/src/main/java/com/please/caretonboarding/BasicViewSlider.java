package com.please.caretonboarding;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.smarteist.autoimageslider.SliderViewAdapter;


public class BasicViewSlider extends SliderViewAdapter<BasicViewSlider.SliderAdapterVH> {
	private final Context context;
	private final Drawable[] imageDrawables;

	public BasicViewSlider(Context c) {
		context = c;
		imageDrawables = new Drawable[] {
				ContextCompat.getDrawable(context,R.drawable.sc_1),
						ContextCompat.getDrawable(context,R.drawable.sc_2),
								ContextCompat.getDrawable(context,R.drawable.sc_3),
										ContextCompat.getDrawable(context,R.drawable.sc_4),
												ContextCompat.getDrawable(context,R.drawable.sc_5),
														ContextCompat.getDrawable(context,R.drawable.sc_6),
																ContextCompat.getDrawable(context,R.drawable.sc_7)
		};
	}

	@Override
	public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider_view, null);
		return new SliderAdapterVH(view);
	}

	@Override
	public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
		viewHolder.picImageView.setImageDrawable( imageDrawables[ position ] );
	}

	@Override
	public int getCount() {
		return imageDrawables.length;
	}

	static class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
		AppCompatImageView picImageView;

		public SliderAdapterVH(View itemView) {
			super(itemView);
			picImageView = itemView.findViewById(R.id.pic_holder);
		}
	}

}
