package uqac.dim.uqac_scanner.HistoryFolder;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;

import java.util.List;

import uqac.dim.uqac_scanner.Helpers.DataBaseHelper;
import uqac.dim.uqac_scanner.Helpers.HistoryAdapter;
import uqac.dim.uqac_scanner.Models.QrCodeModel;
import uqac.dim.uqac_scanner.R;
import uqac.dim.uqac_scanner.ScannerFolder.Affichage;

public class History extends Fragment {
    DataBaseHelper dbHelper;
    List<QrCodeModel> listQrCode;
    ListView list;
    HistoryAdapter historyAdapter;
    RadioGroup filterSwitch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        Log.i("LOG", "History onCreateView");
        dbHelper = new DataBaseHelper(getActivity());
        listQrCode = dbHelper.getListQR(0);
        list = (ListView)view.findViewById(R.id.history_list);
        filterSwitch = view.findViewById(R.id.filterHistory);
        createListener();
        createAdapter();

        return view;
    }

    private void createListener(){
        filterSwitch.check(R.id.created_radio);
        filterSwitch.setOnCheckedChangeListener((obj, idChecked) -> {
            if (idChecked == R.id.created_radio) {
                listQrCode = dbHelper.getListQR(0);
            } else {

                listQrCode = dbHelper.getListQR(1);
            }
            createAdapter();
            historyAdapter.notifyDataSetChanged();
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QrCodeModel model = (QrCodeModel) parent.getItemAtPosition(position);
                Log.e("DIM", "INFO");
                //Change InfoQr.class pour la classe d'edit
                Intent intent = new Intent(getActivity(), Affichage.class);
                intent.putExtra("QR_ID_SELECTED", model.getID());
                startActivity(intent);
            }
        });

    }
    private void createAdapter() {
        historyAdapter = new HistoryAdapter(getActivity(), listQrCode);
        list.setAdapter(historyAdapter);
    }
}