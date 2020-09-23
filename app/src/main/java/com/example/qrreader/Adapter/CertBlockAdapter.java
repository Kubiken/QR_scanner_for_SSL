package com.example.qrreader.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrreader.Models.CertRes;
import com.example.qrreader.R;

import java.util.ArrayList;

/**
 * Adapter for RecyclerView, which contains results for all url's
 */

public class CertBlockAdapter extends RecyclerView.Adapter<CertBlockAdapter.CertBlockViewHolder> {

    ArrayList<CertRes> block = new ArrayList<CertRes>();
    RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
    Context context;

    public CertBlockAdapter(ArrayList<CertRes> block, Context context)
    {
        this.block=block;
        this.context=context;
    }

    @NonNull
    @Override
    public CertBlockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cert_result_block, parent, false);
        return new CertBlockAdapter.CertBlockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CertBlockViewHolder holder, int position) {
    holder.site.setText(block.get(position).getLink());
    CertRowAdapter cla = new CertRowAdapter(context, block.get(position));
    holder.certListRow.setAdapter(cla);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        layoutManager.setInitialPrefetchItemCount(block.get(position).getLength());
        holder.certListRow.setLayoutManager(layoutManager);
        holder.certListRow.setRecycledViewPool(pool);
    }

    @Override
    public int getItemCount() {
        return block.size();
    }

    public class CertBlockViewHolder extends RecyclerView.ViewHolder {

        TextView site;
        RecyclerView certListRow;
        public CertBlockViewHolder(@NonNull View itemView) {
            super(itemView);
            site = itemView.findViewById(R.id.siteUrl);
            certListRow = itemView.findViewById(R.id.cert_res_row);
        }
    }
}
