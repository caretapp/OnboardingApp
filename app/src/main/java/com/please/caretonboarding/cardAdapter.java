package com.please.caretonboarding;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class cardAdapter extends PagerAdapter {
	private Context context;
	private final View[] viewList;
	private String key = "error";

	public cardAdapter(Context c, View[] v) {
		context = c;
		this.viewList = v;
	}

	@SuppressLint("QueryPermissionsNeeded")
	@NonNull
	@Override
	public Object instantiateItem(@NonNull ViewGroup collection, int position) {
		switch ( position ) {
			case 0:
			case 1:
			case 2:
			case 3:
				break;
			case 4:
				SliderView sliderView = viewList[position].findViewById(R.id.imageSlider);
				sliderView.setSliderAdapter(new BasicViewSlider(context));
				sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
				sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
				sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
				sliderView.setScrollTimeInSec(3);
				sliderView.startAutoCycle();
				break;
			case 6:
				EditText keyText = viewList[position].findViewById(R.id.key_edit_text);
				StringBuilder cpu = new StringBuilder();
				final String[][] supportedABIS = new String[][]{Build.SUPPORTED_ABIS};
				for ( String[] s : supportedABIS ) {
					for ( String z : s ) {
						cpu.append(z);
					}
				}
				String tmpKey = cpu + Build.BOARD + Build.BRAND + Build.DEVICE + Build.MANUFACTURER + Build.MODEL + Build.PRODUCT + "";
				try {
					MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
					digest.update(tmpKey.getBytes());
					byte[] messageDigest = digest.digest();
					StringBuilder hexString = new StringBuilder();
					for (byte b : messageDigest) hexString.append(Integer.toHexString(0xFF & b));
					key = hexString.toString();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
					key = "error";
				}

				keyText.setText(key);
				AppCompatImageView copyKeyButton = viewList[position].findViewById(R.id.copy_key_button);
				copyKeyButton.setOnClickListener(v -> {
					if (keyText.getText() != null && !keyText.getText().toString().isEmpty()) {
						ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
						ClipData clip = ClipData.newPlainText("id", keyText.getText().toString());
						clipboard.setPrimaryClip(clip);
						Toast.makeText( context, "Key copied to clipboard!", Toast.LENGTH_SHORT ).show();
					}
				});
				View venmoButton = viewList[position].findViewById(R.id.venmo_view);
				venmoButton.setOnClickListener(v -> {
					try {
						//app intent
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(context.getResources().getString(R.string.purchase_venmo_url)));
						i.setComponent(new ComponentName("com.venmo", "com.venmo.web.VenmoURLActivity"));
						i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(i);
					} catch ( Exception e ) {
						try {
							//browser intent
							Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://example.com"));
							ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(i,PackageManager.MATCH_DEFAULT_ONLY);
							String packageName = resolveInfo.activityInfo.packageName;
							//create browser intent with our url
							i = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getResources().getString(R.string.purchase_venmo_url)));
							i.setPackage( packageName );
							context.startActivity(i);
						} catch ( Exception f ){
							f.printStackTrace();
						}
					}
				});
				View cashAppButton = viewList[position].findViewById(R.id.cash_app_view);
				cashAppButton.setOnClickListener(v -> {
					try {
						//app intent
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(context.getResources().getString(R.string.purchase_cashapp_url)));
						i.setComponent(new ComponentName("com.squareup.cash", "com.squareup.cash.ui.MainActivity"));
						i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(i);
					} catch ( Exception e ) {
						try {
							//browser intent
							Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://example.com"));
							ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(i, PackageManager.MATCH_DEFAULT_ONLY);
							String packageName = resolveInfo.activityInfo.packageName;
							//create browser intent with our url
							i = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getResources().getString(R.string.purchase_cashapp_url)));
							i.setPackage( packageName );
							context.startActivity(i);
						} catch ( Exception f ){
							f.printStackTrace();
						}
					}
				});
				View contactUsButton1 = viewList[position].findViewById(R.id.contact_us_purchase);
				contactUsButton1.setOnClickListener(v -> {
					Intent intent = new Intent(Intent.ACTION_SENDTO).setData(Uri.parse("mailto:"));
					intent.putExtra(Intent.EXTRA_EMAIL, new String[]{context.getResources().getString(R.string.email)});
					intent.putExtra(Intent.EXTRA_SUBJECT, String.format(Locale.getDefault(), "Purchase Question: %s", key));
					if (intent.resolveActivity(context.getPackageManager()) != null) {
						context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
					}
				});
				break;
			case 8:
				View contactUsButton2 = viewList[position].findViewById(R.id.contact_us_purchase);
				contactUsButton2.setOnClickListener(v -> {
					Intent intent = new Intent(Intent.ACTION_SENDTO).setData(Uri.parse("mailto:"));
					intent.putExtra(Intent.EXTRA_EMAIL, new String[]{context.getResources().getString(R.string.email)});
					intent.putExtra(Intent.EXTRA_SUBJECT, String.format(Locale.getDefault(), "Contact Question: %s", key));
					if (intent.resolveActivity(context.getPackageManager()) != null) {
						context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
					}
				});
				break;
		}
		((ViewPager) collection).addView(viewList[ position ]);
		return viewList[ position ];
	}

	@Override
	public void destroyItem(ViewGroup collection, int position, @NonNull Object view) {
		collection.removeView(viewList[ position ]);
	}

	@Override
	public int getCount() {
		return viewList.length;
	}

	@Override
	public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
		return view == object;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "";
	}
}

/*
extends FragmentStateAdapter
private final List<Fragment> mFragmentList = new ArrayList<>();
	public cardAdapter(@NonNull FragmentManager f, @NonNull Lifecycle lifecycle) {
		super(f, lifecycle);
	}
	public void clearFragments() {
		mFragmentList.clear();
	}
	public void addFragment(Fragment fragment) {
		mFragmentList.add(fragment);
	}
	public void setFragment(int which, Fragment fragment) {
		mFragmentList.set( which, fragment);
	}
	@Override
	public int getItemCount() {
		return mFragmentList.size();
	}
	@NonNull
	@Override
	public Fragment createFragment(int position) {
		return mFragmentList.get(position);
	}
 */