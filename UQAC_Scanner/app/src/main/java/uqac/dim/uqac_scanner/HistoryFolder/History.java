package uqac.dim.uqac_scanner.HistoryFolder;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uqac.dim.uqac_scanner.R;

public class History extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("LOG", "History onCreateView");
        return inflater.inflate(R.layout.fragment_history, container, false);
    }
}