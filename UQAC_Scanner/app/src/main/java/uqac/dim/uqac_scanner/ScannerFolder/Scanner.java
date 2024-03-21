package uqac.dim.uqac_scanner.ScannerFolder;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uqac.dim.uqac_scanner.R;

public class Scanner extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("LOG", "Scanner onCreateView");
        return inflater.inflate(R.layout.fragment_scanner, container, false);
    }
}