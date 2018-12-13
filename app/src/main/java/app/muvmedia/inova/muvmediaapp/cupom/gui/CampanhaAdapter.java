package app.muvmedia.inova.muvmediaapp.cupom.gui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        try {
            dataInicio.setText(formateDate(cupom.getInitDate()));
        } catch (ParseException pe) {
            System.out.println(pe);
        }
        return view;
    }

    private String formateDate(String oldstring) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(oldstring);
        String newstring = new SimpleDateFormat("dd/MM/yyyy").format(date);
       return newstring;
    }
}
