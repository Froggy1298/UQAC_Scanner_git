package uqac.dim.uqac_scanner.Helpers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uqac.dim.uqac_scanner.Models.QrCodeModel;
import uqac.dim.uqac_scanner.R;

public class HistoryAdapter extends ArrayAdapter<QrCodeModel> {

    public HistoryAdapter(@NonNull Context context, List<QrCodeModel> resource) {
        super(context, R.layout.list_item ,resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        QrCodeModel qrCodeModel = getItem(position);
        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            ImageView imageView = view.findViewById(R.id.editQR);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("DIM", "edit") ;
                    /*Intent intent = new Intent(getContext(), InfoQr.class);
                    intent.putExtra("QR_ID_SELECTED", qrCodeModel.getID());
                    getContext().startActivity(intent);*/
                }
            });
        }

        TextView listText= view.findViewById(R.id.listText);
        listText.setText(qrCodeModel.getName());
        return view;
    }
}
