package com.evilgeniuses.vulgaritytest.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.evilgeniuses.vulgaritytest.R;
import com.evilgeniuses.vulgaritytest.interfaces.SwitchFragmentInterface;

public class StartFragment extends Fragment {

    public SwitchFragmentInterface switchFragmentInterface;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_start, viewGroup, false);
        ((Button) inflate.findViewById(R.id.buttonStart)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StartFragment.this.switchFragmentInterface.setFragment(TestFragment.newInstance());
            }
        });
        return inflate;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SwitchFragmentInterface) {
            this.switchFragmentInterface = (SwitchFragmentInterface) context;
        }
    }

    public static StartFragment newInstance() {
        return new StartFragment();
    }
}