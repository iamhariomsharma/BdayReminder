package com.heckteck.birthy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.heckteck.birthy.Models.Greeting;
import com.heckteck.birthy.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GreetingsAdapter extends RecyclerView.Adapter<GreetingsAdapter.GreetingsHolder> {


    private List<Greeting> greetings;

    private OnItemClickListener listener;

    private Context mContext;

    public GreetingsAdapter(Context mContext, List<Greeting> greetings) {
        this.mContext = mContext;
        this.greetings = greetings;
    }

    @NonNull
    @Override
    public GreetingsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_wishes, parent, false);
        return new GreetingsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GreetingsHolder holder, int position) {
        Greeting greeting = greetings.get(position);
        holder.tv_wish.setText(greeting.getGreeting());
    }

    @Override
    public int getItemCount() {
        return greetings.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    class GreetingsHolder extends RecyclerView.ViewHolder {
        TextView tv_wish;

        GreetingsHolder(View itemView) {
            super(itemView);
            tv_wish = itemView.findViewById(R.id.tv_wish);
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    int position = GreetingsAdapter.GreetingsHolder.this.getAdapterPosition();
                    if (listener != null && position != -1)
                        listener.onItemClick(greetings.get(position));
                }
            });
        }
    }

//    class null implements View.OnClickListener {
//        public void onClick(View param1View) {
//            int i = this.this$1.getAdapterPosition();
//            if (GreetingsAdapter.this.listener != null && i != -1)
//                GreetingsAdapter.this.listener.onItemClick(GreetingsAdapter.this.greetings.get(i));
//        }
//    }

    public interface OnItemClickListener {
        void onItemClick(Greeting greeting);
    }
}
