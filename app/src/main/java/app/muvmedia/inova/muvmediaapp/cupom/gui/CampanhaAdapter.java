package app.muvmedia.inova.muvmediaapp.cupom.gui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.cupom.dominio.Campaign;

public class CampanhaAdapter extends BaseAdapter {

    private Context context;
    private List<Campaign> campanhas;

    public CampanhaAdapter(Context context, List<Campaign> campanhas) {
        this.context = context;
        this.campanhas = campanhas;
    }

    @Override
    public int getCount() {
        return campanhas.size();
    }

    @Override
    public Object getItem(int position) {
        return campanhas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(view ==null){
            view = View.inflate(context, R.layout.list_cupons,null);
        }
        TextView nome = view.findViewById(R.id.textViewNomeCapanha);
        TextView descricao = view.findViewById(R.id.descricao);
        TextView dataInicio = view.findViewById(R.id.dataInicio);
        Campaign cupom = campanhas.get(position);
        nome.setText(cupom.getName());
        descricao.setText(cupom.getDescription());
        dataInicio.setText(cupom.getInitDate());
        return view;
    }
}
