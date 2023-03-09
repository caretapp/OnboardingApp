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
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class cardAdapter extends PagerAdapter {
	private final Context context;
	private final View[] viewList;
	private String key = "error";

	public cardAdapter(Context c, View[] v) {
		context = c;
		this.viewList = v;
	}

	private void emailCaret(int subject, String body){
		Intent intent = new Intent(Intent.ACTION_SENDTO).setData(Uri.parse("mailto:"));
		intent.putExtra(Intent.EXTRA_EMAIL, new String[]{context.getResources().getString(R.string.email)});
		intent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(subject));
		intent.putExtra(Intent.EXTRA_TEXT, body);
		if (intent.resolveActivity(context.getPackageManager()) != null) {
			context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		} else {
			try {
				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.setType("message/rfc822");
				emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{context.getResources().getString(R.string.email)});
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(subject));
				emailIntent.putExtra(Intent.EXTRA_TEXT, body);
				context.startActivity(Intent.createChooser(emailIntent, "Send Email to Caret").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
			} catch (Exception e) {
				Toast.makeText(context, "No app found to email!", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}
	}

	private String getAbis() {
		StringBuilder cpu = new StringBuilder();
		final String[][] supportedABIS = new String[][]{Build.SUPPORTED_ABIS};
		for ( String[] s : supportedABIS ) {
			for ( String z : s ) {
				cpu.append(z);
			}
		}
		return cpu.toString();
	}

	private String hashThis( String details ) {
		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(details.getBytes());
			byte[] messageDigest = digest.digest();
			StringBuilder hexString = new StringBuilder();
			for (byte b : messageDigest) hexString.append(Integer.toHexString(0xFF & b));
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			Toast.makeText(context, "Key generation error. Contact support!", Toast.LENGTH_LONG).show();
			return "error";
		}
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
			case 5:
				TextView androidVersionTxt = viewList[position].findViewById(R.id.textView_android_version);
				Map<int[], String> versionMap = new HashMap<>();
				versionMap.put(new int[]{8,27},"Oreo");
				versionMap.put(new int[]{9,28},"Pie");
				versionMap.put(new int[]{10,29},"");
				versionMap.put(new int[]{11,30},"");
				versionMap.put(new int[]{12,31},"");
				versionMap.put(new int[]{13,32},"");
				versionMap.put(new int[]{14,33},"");
				for (Map.Entry<int[], String> entry : versionMap.entrySet()) {
					try {
						if ( Build.VERSION.SDK_INT == entry.getKey()[1] ) {
							if ( entry.getKey()[0] >= 8 && entry.getKey()[0] <= 13 ) {
								androidVersionTxt.setTextColor( ContextCompat.getColor( context, R.color.optional ) );
							} else {
								androidVersionTxt.setTextColor( ContextCompat.getColor( context, R.color.required ) );
							}
							androidVersionTxt.setText( String.format( Locale.getDefault(), "Android %d %s", entry.getKey()[0], entry.getValue() ) );
							break;
						}
					} catch (Exception e) {
						Toast.makeText(context, "Failed to find Android version!", Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
				}
				break;
			case 6:
				EditText keyText = viewList[position].findViewById(R.id.key_edit_text);
				key = hashThis( getAbis() + Build.BOARD + Build.BRAND + Build.DEVICE + Build.MANUFACTURER + Build.MODEL + Build.PRODUCT + "" );
				keyText.setText(key);
				AppCompatImageView copyKeyButton = viewList[position].findViewById(R.id.copy_key_button);
				copyKeyButton.setOnClickListener(v -> {
					if (keyText.getText() != null && !keyText.getText().toString().isEmpty()) {
						ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
						ClipData clip = ClipData.newPlainText("id", keyText.getText().toString());
						clipboard.setPrimaryClip(clip);
						Toast.makeText( context, "Key copied to clipboard!", Toast.LENGTH_SHORT ).show();
					} else {
						Toast.makeText(context, "Your key is empty?", Toast.LENGTH_LONG).show();
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
							Toast.makeText(context, "No app found! Please do so manually.", Toast.LENGTH_LONG).show();
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
							Toast.makeText(context, "No app found! Please do so manually.", Toast.LENGTH_LONG).show();
							f.printStackTrace();
						}
					}
				});
				View contactUsButton1 = viewList[position].findViewById(R.id.contact_us_purchase);
				contactUsButton1.setOnClickListener(v -> emailCaret( R.string.email_purchase,  String.format(Locale.getDefault(), "Key: %s", key)));
				break;
			case 8:
				View contactUsButton2 = viewList[position].findViewById(R.id.contact_us_purchase);
				contactUsButton2.setOnClickListener(v -> emailCaret( R.string.email_contact,  String.format(Locale.getDefault(), "Key: %s", key)));
				AppCompatImageView debug = viewList[position].findViewById(R.id.debug_);
				debug.setOnLongClickListener(view -> {
					StringBuilder d = new StringBuilder();
					d.append( String.format(Locale.getDefault(), "ABIS: %s\n", getAbis() ) );
					d.append( String.format(Locale.getDefault(), "BOARD: %s\n", Build.BOARD ) );
					d.append( String.format(Locale.getDefault(), "BRAND: %s\n", Build.BRAND ) );
					d.append( String.format(Locale.getDefault(), "DEVICE: %s\n", Build.DEVICE ) );
					d.append( String.format(Locale.getDefault(), "MANUFACTURER: %s\n", Build.MANUFACTURER ) );
					d.append( String.format(Locale.getDefault(), "PRODUCT: %s\n", Build.PRODUCT ) );
					d.append( String.format(Locale.getDefault(), "KEY: %s", hashThis(
							getAbis() + Build.BOARD + Build.BRAND + Build.DEVICE + Build.MANUFACTURER + Build.MODEL + Build.PRODUCT + "" )
					) );
					Toast.makeText(context, "Info copied to clipboard!", Toast.LENGTH_LONG).show();
					emailCaret( R.string.email_purchase,  String.format(Locale.getDefault(), "%s", d));
					return true;
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