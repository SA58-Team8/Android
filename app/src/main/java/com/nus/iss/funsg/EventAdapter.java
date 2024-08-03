package com.nus.iss.funsg;

import android.content.Context;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>{
    private List<AuthEventsResponse> eventList;
    private Context context;
    private boolean isSearch;

    public EventAdapter(Context context, List<AuthEventsResponse> eventList) {
        this.context = context;
        this.eventList = eventList;
    }
    public EventAdapter(Context context, List<AuthEventsResponse> eventList,boolean isSearch) {
        this.context = context;
        this.eventList = eventList;
        this.isSearch=isSearch;
    }
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        if(isSearch){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item_search,parent,false);
            return new EventViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_upcoming,parent,false);
            return new EventViewHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position){
        AuthEventsResponse event=eventList.get(position);
        holder.eventTitle.setText(event.getName());
        holder.eventDescription.setText(event.getDescription());
        holder.eventTime.setText(DateUtils.formatDateString(event.getStart()));
        Glide.with(context)
                .load(event.getProfileImagePath())
                .into(holder.eventImage);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EventPage.class);
            intent.putExtra("eventId", event.getId());
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return eventList.size();
    }
    public static class EventViewHolder extends RecyclerView.ViewHolder{
        ImageView eventImage;
        TextView eventTitle;
        TextView eventTime;
        TextView eventDescription;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.event_image);
            eventTitle = itemView.findViewById(R.id.event_title);
            eventTime = itemView.findViewById(R.id.event_time);
            eventDescription = itemView.findViewById(R.id.event_description);
        }
    }
}
