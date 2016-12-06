package com.tavous.eartraining.musicaleartraining;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapterMainActivity extends RecyclerView.Adapter<RecyclerAdapterMainActivity.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<Lesson> items;
    private int lastPosition = -1;
    private final static int FADE_DURATION = 500; // in milliseconds

    public RecyclerAdapterMainActivity(Context context, List<Lesson> lessons) {
        this.context = context;
        this.items = lessons;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.main_recycler_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Picasso.with(context)
                .load("file:///android_asset/lesson_images/"+ items.get(position).Name +".jpg")
                .error(R.drawable.image_pre_view)
                .placeholder(R.drawable.image_pre_view)
                .into(holder.thumbnail);

        holder.title.setText(items.get(position).Title);
        holder.desc.setText(items.get(position).Desc);
        //setAnimation(holder.cardView, position);
        //setScaleAnimation(holder.cardView);
        setFadeAnimation(holder.cardView);
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;
        TextView desc;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            title = (TextView) itemView.findViewById(R.id.title);
            desc = (TextView) itemView.findViewById(R.id.desc);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }
}
