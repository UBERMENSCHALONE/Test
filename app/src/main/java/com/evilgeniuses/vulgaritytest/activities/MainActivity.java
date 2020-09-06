package com.evilgeniuses.vulgaritytest.activities;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.evilgeniuses.vulgaritytest.R;
import com.evilgeniuses.vulgaritytest.fragments.StartFragment;
import com.evilgeniuses.vulgaritytest.interfaces.SwitchFragmentInterface;

public class MainActivity extends AppCompatActivity implements SwitchFragmentInterface {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        setFragment(StartFragment.newInstance());
    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.container, fragment);
        beginTransaction.commit();
    }
}