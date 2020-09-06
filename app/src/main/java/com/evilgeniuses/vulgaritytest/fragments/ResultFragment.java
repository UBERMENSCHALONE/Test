package com.evilgeniuses.vulgaritytest.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.evilgeniuses.vulgaritytest.R;
import com.evilgeniuses.vulgaritytest.interfaces.SwitchFragmentInterface;

public class ResultFragment extends Fragment implements View.OnClickListener {
    private ImageView mImageViewEstimate;
    private ImageView mImageViewRefresh;
    private ImageView mImageViewShare;
    private TextView mTextViewPercent;
    private int mTotalPoints;
    private SwitchFragmentInterface switchFragmentInterface;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_result, viewGroup, false);
        this.mTextViewPercent = (TextView) inflate.findViewById(R.id.textViewPercent);
        this.mImageViewShare = (ImageView) inflate.findViewById(R.id.imageViewShare);
        this.mImageViewRefresh = (ImageView) inflate.findViewById(R.id.imageViewRefresh);
        this.mImageViewEstimate = (ImageView) inflate.findViewById(R.id.imageViewEstimate);
        this.mImageViewShare.setOnClickListener(this);
        this.mImageViewRefresh.setOnClickListener(this);
        this.mImageViewEstimate.setOnClickListener(this);
        String str = "percent";
        this.mTotalPoints = getActivity().getSharedPreferences(str, 0).getInt(str, 0);
        TextView textView = this.mTextViewPercent;
        StringBuilder sb = new StringBuilder();
        sb.append(this.mTotalPoints);
        sb.append("%");
        textView.setText(sb.toString());
        return inflate;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewEstimate:
                estimateTheApp();
                return;
            case R.id.imageViewRefresh:
                this.switchFragmentInterface.setFragment(StartFragment.newInstance());
                return;
            case R.id.imageViewShare:
                shareResult();
                return;
            default:
                return;
        }
    }

    private void shareResult() {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plan");
        StringBuilder sb = new StringBuilder();
        sb.append("Мой результат в приложении \"Тест на пошлость\" равен ");
        sb.append(Math.round((float) this.mTotalPoints));
        sb.append("%. Скачать приложение можно тут: ");
        sb.append("https://play.google.com/store/apps/details?id=com.evilgeniuses.vulgaritytest");
        intent.putExtra("android.intent.extra.TEXT", sb.toString());
        try {
            startActivity(Intent.createChooser(intent, "Поделится"));
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(getActivity(), "Неизвестная ошибка", Toast.LENGTH_LONG).show();
        }
    }

    public void estimateTheApp() {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.evilgeniuses.vulgaritytest"));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(getActivity(), "Приложение не найдено", Toast.LENGTH_LONG).show();
        }
        startActivity(intent);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SwitchFragmentInterface) {
            this.switchFragmentInterface = (SwitchFragmentInterface) context;
        }
    }

    public static ResultFragment newInstance() {
        return new ResultFragment();
    }
}
