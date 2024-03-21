package uqac.dim.uqac_scanner.Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.View;
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
        }

        TextView listText= view.findViewById(R.id.listText);
        listText.setText(qrCodeModel.getName());
        return view;
    }
}
