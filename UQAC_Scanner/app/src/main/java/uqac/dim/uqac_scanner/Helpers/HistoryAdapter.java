package uqac.dim.uqac_scanner.Helpers;

import android.app.ActivityOptions;
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
import uqac.dim.uqac_scanner.EditFolder.Edit;

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
                    Intent intent = new Intent(getContext(), Edit.class);
                    //TODO TEST ICI POUR L'ID
                    int test = qrCodeModel.getID();
                    String test2 = qrCodeModel.getName();
                    intent.putExtra("QR_ID_SELECTED", String.valueOf(qrCodeModel.getID()));
                    getContext().startActivity(intent,
                            ActivityOptions.makeCustomAnimation(getContext(),R.anim.slide_in_right_to_center,R.anim.slide_out_center_to_left).toBundle());
                }
            });
        }
        TextView listText= view.findViewById(R.id.listText);
        listText.setText(qrCodeModel.getName());
        return view;
    }
}
