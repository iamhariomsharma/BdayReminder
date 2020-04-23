package com.heckteck.birthy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
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

        holder.relativeLayout.setAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_transition_animation));

        if (holder.getAdapterPosition() % 4 == 0) {
            holder.relativeLayout.setBackgroundResource(R.drawable.card_bg2);
        } else if (holder.getAdapterPosition() % 4 == 1) {
            holder.relativeLayout.setBackgroundResource(R.drawable.card_bg1);
        } else if (holder.getAdapterPosition() % 4 == 2) {
            holder.relativeLayout.setBackgroundResource(R.drawable.card_bg4);
        } else if (holder.getAdapterPosition() % 4 == 3) {
            holder.relativeLayout.setBackgroundResource(R.drawable.card_bg3);
        }
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
        RelativeLayout relativeLayout;

        GreetingsHolder(View itemView) {
            super(itemView);
            tv_wish = itemView.findViewById(R.id.tv_wish);
            relativeLayout = itemView.findViewById(R.id.greetings_container);
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    int position = getAdapterPosition();
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
