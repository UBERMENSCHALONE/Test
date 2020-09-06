package com.evilgeniuses.vulgaritytest.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.evilgeniuses.vulgaritytest.R;
import com.evilgeniuses.vulgaritytest.database.DatabaseHelper;
import com.evilgeniuses.vulgaritytest.interfaces.SwitchFragmentInterface;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
import java.util.Random;

public class TestFragment extends Fragment implements View.OnClickListener {
    private Button mButtonAnswer1;
    private Button mButtonAnswer2;
    private Button mButtonAnswer3;
    private int mCountAd = 0;
    private int mCountQuestion = 0;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    public InterstitialAd mInterstitialAd;
    private int mPointQuestion = 0;
    private int[] mPositionQuestions;
    private TextView mTextQuestion;
    private TextView mTextQuestionCount;
    private SwitchFragmentInterface switchFragmentInterface;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_test, viewGroup, false);
        this.mButtonAnswer1 = (Button) inflate.findViewById(R.id.mButtonAnswer1);
        this.mButtonAnswer2 = (Button) inflate.findViewById(R.id.mButtonAnswer2);
        this.mButtonAnswer3 = (Button) inflate.findViewById(R.id.mButtonAnswer3);
        this.mTextQuestion = (TextView) inflate.findViewById(R.id.mTextQuestion);
        this.mTextQuestionCount = (TextView) inflate.findViewById(R.id.mTextQuestionCount);
        this.mButtonAnswer1.setOnClickListener(this);
        this.mButtonAnswer2.setOnClickListener(this);
        this.mButtonAnswer3.setOnClickListener(this);
        readDatabase();
        getQuestionCount();

        setQuestion();


        MobileAds.initialize(getContext(), "ca-app-pub-3361690811677955~9342140615");
        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-3361690811677955/6524405582");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        return inflate;
    }

    private void readDatabase() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        this.mDBHelper = databaseHelper;
        try {
            databaseHelper.updateDataBase();
            try {
                this.mDb = this.mDBHelper.getWritableDatabase();
            } catch (SQLException e) {
                throw e;
            }
        } catch (IOException unused) {
            throw new Error("UnableToUpdateDatabase");
        }
    }

    private void getQuestionCount() {
        Cursor rawQuery = this.mDb.rawQuery("SELECT * FROM question", null);
        this.mPositionQuestions = new int[rawQuery.getCount()];
        for (int i = 0; i < rawQuery.getCount(); i++) {
            this.mPositionQuestions[i] = i;
        }
        rawQuery.close();
        shuffleArray(this.mPositionQuestions);
    }

    private static void shuffleArray(int[] iArr) {
        int length = iArr.length;
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < length; i++) {
            swap(iArr, i, random.nextInt(length - i) + i);
        }
    }

    private static void swap(int[] iArr, int i, int i2) {
        int i3 = iArr[i];
        iArr[i] = iArr[i2];
        iArr[i2] = i3;
    }

    private void addPoints(int i) {
        this.mPointQuestion += i;
        showAds();
    }

    private void setQuestion() {
        Cursor rawQuery = this.mDb.rawQuery("SELECT * FROM question", null);
        if (this.mCountQuestion != rawQuery.getCount()) {
            rawQuery.moveToPosition(this.mPositionQuestions[this.mCountQuestion]);
            this.mTextQuestion.setText(rawQuery.getString(1));
            this.mButtonAnswer1.setText(rawQuery.getString(2));
            this.mButtonAnswer2.setText(rawQuery.getString(3));
            this.mButtonAnswer3.setText(rawQuery.getString(4));
            TextView textView = this.mTextQuestionCount;
            StringBuilder sb = new StringBuilder();
            sb.append("Вопрос ");
            sb.append(this.mCountQuestion + 1);
            sb.append(" из ");
            sb.append(rawQuery.getCount());
            textView.setText(sb.toString());
        } else {
            float count = (((float) this.mPointQuestion) * 100.0f) / ((float) (rawQuery.getCount() * 2));
            String str = "percent";
            SharedPreferences.Editor edit = getActivity().getSharedPreferences(str, 0).edit();
            edit.putInt(str, Math.round(count));
            edit.apply();
            this.switchFragmentInterface.setFragment(ResultFragment.newInstance());
        }
        rawQuery.close();
        this.mCountQuestion++;
    }

    private void showAds() {
        int i = this.mCountAd;
        if (i == 13) {
            if (this.mInterstitialAd.isLoaded()) {
                this.mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
            this.mCountAd = 0;
            return;
        }
        this.mCountAd = i + 1;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mButtonAnswer1:
                addPoints(2);
                break;
            case R.id.mButtonAnswer2:
                addPoints(1);
                break;
            case R.id.mButtonAnswer3:
                addPoints(0);
                break;
        }
        setQuestion();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SwitchFragmentInterface) {
            this.switchFragmentInterface = (SwitchFragmentInterface) context;
        }
    }

    public static TestFragment newInstance() {
        return new TestFragment();
    }
}