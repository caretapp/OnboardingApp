package com.please.caretonboarding;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

public class getFragment {
	private Context context;
	private int position;
	public getFragment() {
	}
	public getFragment(Context c, int p) {
		this.position = p;
		this.context = c;
	}

}
