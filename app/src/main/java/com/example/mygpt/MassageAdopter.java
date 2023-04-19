package com.example.mygpt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MassageAdopter  extends RecyclerView.Adapter<MassageAdopter.MyViewHolder>{
    List<Massage> massageList;
    public MassageAdopter(List<Massage>massageList) {
        this.massageList=massageList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View chatView= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,null);
      MyViewHolder myViewHolder=new MyViewHolder(chatView);
      return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Massage massage=massageList.get(position);
        if(massage.getSendby().equals(Massage.SENT_BY_ME)){
            holder.leftChatView.setVisibility(View.GONE);
            holder.rightChatView.setVisibility(View.VISIBLE);
            holder.rightTextView.setText(massage.getMassage());


        }else{
            holder.leftChatView.setVisibility(View.VISIBLE);
            holder.rightChatView.setVisibility(View.GONE);
            holder.leftTextView.setText(massage.getMassage());

        }

    }

    @Override
    public int getItemCount() {
        return massageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftChatView,rightChatView;
        TextView leftTextView,rightTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatView=itemView.findViewById(R.id.left_chat_view);
            rightChatView=itemView.findViewById(R.id.right_chat_view);
            leftTextView=itemView.findViewById(R.id.left_chat_text_view);
            rightTextView=itemView.findViewById(R.id.right_chat_text_view);
        }


    }
}
