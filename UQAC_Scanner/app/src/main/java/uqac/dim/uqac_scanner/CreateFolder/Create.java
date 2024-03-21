package uqac.dim.uqac_scanner.CreateFolder;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import uqac.dim.uqac_scanner.Helpers.DataBaseHelper;
import uqac.dim.uqac_scanner.MainActivity;
import uqac.dim.uqac_scanner.R;

public class Create extends Fragment {

    private DataBaseHelper dataBaseHelper;
    private EditText NameEditText;
    private EditText URLEditText;
    private EditText DescEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("LOG", "Create1");
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        dataBaseHelper = new DataBaseHelper(getActivity());


        ((Button)view.findViewById(R.id.but_confirm_create))
                .setOnClickListener(this::onClickConfirmCreate);




        NameEditText = (EditText) view.findViewById(R.id.field_name);
        URLEditText = (EditText) view.findViewById(R.id.field_url);
        DescEditText = (EditText) view.findViewById(R.id.field_desc);

        //return inflater.inflate(R.layout.fragment_create, container, false);
        return view;
    }


    private void onClickConfirmCreate(View view) {
        Log.i("LOG", "Create2");

    }

}