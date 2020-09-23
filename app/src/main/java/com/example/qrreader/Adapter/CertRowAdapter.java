package com.example.qrreader.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrreader.Models.CertRes;
import com.example.qrreader.R;

/**
 * Adapter for RecyclerView, which contains results for 1 url's
 */
public class CertRowAdapter extends RecyclerView.Adapter<CertRowAdapter.CertResViewHolder> {

    CertRes data;
    Context context;

    public CertRowAdapter(Context ct, CertRes data)
    {
        this.data = data;
        context = ct;
    }

    @NonNull
    @Override
    public CertResViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cert_result_row, parent, false);
        return new CertResViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CertResViewHolder holder, int position) {
    holder.link.setText(data.getCert(position));
    if(data.getStatus(position))
        holder.status.setImageResource(R.drawable.ic_checked);
    else {holder.status.setImageResource(R.drawable.ic_cancel);}
    }

    @Override
    public int getItemCount() {
        return data.getLength();
    }

    public class CertResViewHolder extends RecyclerView.ViewHolder {

        TextView link;
        ImageView status;

        public CertResViewHolder(@NonNull View itemView) {
            super(itemView);
            link = itemView.findViewById(R.id.cert_name);
            status = itemView.findViewById(R.id.cert_status);
        }
    }
}
