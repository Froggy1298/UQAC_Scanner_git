package uqac.dim.uqac_scanner.EditFolder;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import uqac.dim.uqac_scanner.Helpers.BitMapHelper;
import uqac.dim.uqac_scanner.Helpers.DataBaseHelper;
import uqac.dim.uqac_scanner.Helpers.GeneralHelper;
import uqac.dim.uqac_scanner.Helpers.OnSwipeTouchListener;
import uqac.dim.uqac_scanner.Models.QrCodeModel;
import uqac.dim.uqac_scanner.R;

public class Edit extends AppCompatActivity{

    DataBaseHelper dbHelper;
    QrCodeModel thisCodeQr;
    TextView CreateTextView;
    TextView EditTextView;
    EditText NameEditText;
    EditText URLEditText;
    EditText DescEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        dbHelper = new DataBaseHelper(this);
        String tempStringDataIntent = getIntent().getStringExtra("QR_ID_SELECTED");
        thisCodeQr = dbHelper.getQR(Integer.parseInt(tempStringDataIntent));

        CreateTextView = (TextView) findViewById(R.id.info_created_date);
        EditTextView = (TextView) findViewById(R.id.info_edit_date);

        NameEditText = (EditText) findViewById(R.id.field_name);
        URLEditText = (EditText) findViewById(R.id.field_url);
        DescEditText = (EditText) findViewById(R.id.field_desc);

        ((Button)findViewById(R.id.but_return))
                .setOnClickListener(this::onClickReturn);
        ((Button)findViewById(R.id.but_update))
                .setOnClickListener(this::onClickEdit);

        ((LinearLayout)findViewById(R.id.theBigOne))
                .setOnTouchListener(new OnSwipeTouchListener(this) {
                    public void onSwipeRight() {
                        onClickReturn(null);
                    }
                });


        LoadDataFromQrCode();
    }

    private void LoadDataFromQrCode() {
        CreateTextView.setText("Créé le " + GeneralHelper.DateToString(thisCodeQr.getDateCreation()));
        EditTextView.setText("Modifié le " + GeneralHelper.DateToString(thisCodeQr.getDateEdit()));

        NameEditText.setText(thisCodeQr.getName());
        URLEditText.setText(thisCodeQr.getUrl());
        DescEditText.setText(thisCodeQr.getDescription());
    }

    private void onClickEdit(View view) {
        Log.e("DIM", "Edit") ;
    }

    private void onClickReturn(View view) {
        finish();
        this.overridePendingTransition(R.anim.slide_in_left_to_center, R.anim.slide_out_center_to_right);
    }
}