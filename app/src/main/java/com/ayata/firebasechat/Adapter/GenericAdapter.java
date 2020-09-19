package com.ayata.firebasechat.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class GenericAdapter<T> extends RecyclerView.Adapter<GenericAdapter.myViewHolder> {
    private List<T> items;
    private Context context;
    private OnViewHolderClick<T> listener;
    private Boolean isChat=false;

    public interface OnViewHolderClick<T> {
        void onClick(View view, int position, T item);
    }
    public GenericAdapter(Context context,OnViewHolderClick<T> listener) {
        this.context = context;
        this.items = new ArrayList<>();
        this.listener=listener;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(createView(context, parent, viewType), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericAdapter.myViewHolder holder, int position) {
        bindView(getItem(position), holder);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Map<Integer, View> views;
        public myViewHolder(View view, OnViewHolderClick<T> listener) {
            super(view);
            views = new HashMap<>();
            views.put(0, view);
            if (listener != null)
                view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null)
                listener.onClick(view, getAdapterPosition(), getItem(getAdapterPosition()));
        }
    }

    private T getItem(int index) {
        return (T)((items != null && index < items.size()) ? items.get(index) : null);
    }

    protected abstract View createView(Context context, ViewGroup viewGroup, int viewType);

    protected abstract void bindView(T item, GenericAdapter.myViewHolder viewHolder);
    public void setList(List<T> list) {
        items = list;
    }

    public List<T> getList() {
        return items;
    }
    public void setList(List<T> list,Boolean isChat) {
        items = list;
        this.isChat=isChat;
    }

    public void setClickListener(OnViewHolderClick listener) {
        this.listener = listener;
    }

    public void addAll(List<T> list) {
        items.addAll(list);
        notifyDataSetChanged();
    }

    public void reset() {
        items.clear();
        notifyDataSetChanged();
    }

    public Boolean getChat() {
        return isChat;
    }
}
