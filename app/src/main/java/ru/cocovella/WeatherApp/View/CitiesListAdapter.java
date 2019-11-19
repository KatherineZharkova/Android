package ru.cocovella.WeatherApp.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import ru.cocovella.WeatherApp.R;


public class CitiesListAdapter extends RecyclerView.Adapter<CitiesListAdapter.Item> {
    private ArrayList<String> dataList;
    private OnItemClickListener itemClickListener;


    CitiesListAdapter(ArrayList<String> data) {
        if(data != null) dataList = data;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        return new Item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.setText(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public interface OnItemClickListener {
        void onItemClick(String text, int position);
    }

    void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }


    class Item extends RecyclerView.ViewHolder {
        private TextView textView;

        Item(@NonNull View view) {
            super(view);
            textView = view.findViewById(R.id.city);
            textView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(
                            textView.getText().toString(),
                            Item.this.getAdapterPosition());
                }
            });
        }

        void setText(String text) {
            textView.setText(text);
        }
    }
}
