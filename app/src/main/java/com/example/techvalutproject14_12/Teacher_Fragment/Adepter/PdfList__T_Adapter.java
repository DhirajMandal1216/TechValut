package com.example.techvalutproject14_12.Teacher_Fragment.Adepter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.techvalutproject14_12.R;

import java.util.List;

public class PdfList__T_Adapter extends ArrayAdapter<String> {

    public PdfList__T_Adapter(Context context, List<String> pdfUrls) {
        super(context, 0, pdfUrls);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String pdfUrl = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_pdf, parent, false);
        }

        TextView pdfNameTextView = convertView.findViewById(R.id.pdf_name);
        pdfNameTextView.setText("PDF " + (position + 1));

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(pdfUrl));
            getContext().startActivity(intent);
        });

        return convertView;
    }
}