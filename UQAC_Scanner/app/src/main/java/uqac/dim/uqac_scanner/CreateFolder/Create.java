package uqac.dim.uqac_scanner.CreateFolder;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import uqac.dim.uqac_scanner.Helpers.BitMapHelper;
import uqac.dim.uqac_scanner.Helpers.DataBaseHelper;
import uqac.dim.uqac_scanner.Helpers.GeneralHelper;
import uqac.dim.uqac_scanner.Models.QrCodeModel;
import uqac.dim.uqac_scanner.R;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.Toast;

public class Create extends Fragment {

    private DataBaseHelper dataBaseHelper;
    private EditText NameEditText;
    private EditText URLEditText;
    private EditText DescEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        //Check that all field
        if(TextUtils.isEmpty(NameEditText.getText().toString().trim()) |
                TextUtils.isEmpty(URLEditText.getText().toString().trim()) |
                TextUtils.isEmpty(DescEditText.getText().toString().trim()))
        {
            Toast.makeText(getActivity(), "Information manquante", Toast.LENGTH_LONG).show();
            return;
        }

        Bitmap tempBitmap = BitMapHelper.CreateBitMapFromString(URLEditText.getText().toString());
        byte[] tempBitmapByteArray = BitMapHelper.getBytes(tempBitmap);

        QrCodeModel qrCode = new QrCodeModel(
                NameEditText.getText().toString().trim(),
                URLEditText.getText().toString().trim(),
                DescEditText.getText().toString().trim(),
                tempBitmapByteArray,
                GeneralHelper.getCurrentTimeDate(),
                GeneralHelper.getCurrentTimeDate(),
                false);

        dataBaseHelper.addCreatedQR(qrCode);

        NameEditText.getText().clear();
        URLEditText.getText().clear();
        DescEditText.getText().clear();
        GeneralHelper.hideKeyboard(getActivity());
        GeneralHelper.showToast(getActivity(),"Code QR Créé", 5);
    }




}