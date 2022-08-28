package com.khadamatemployee.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khadamatemployee.R;
import com.khadamatemployee.databinding.ItemReviewBinding;
import com.khadamatemployee.interfaces.ClickedDisLike;
import com.khadamatemployee.interfaces.ClickedLike;
import com.khadamatemployee.model.ReviewModel;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder>
{

    private ArrayList<ReviewModel> reviewModels;
    private ClickedLike clickedLike;
    private ClickedDisLike clickedDisLike;

    public ReviewsAdapter(ArrayList<ReviewModel> reviewModels, ClickedLike clickedLike, ClickedDisLike clickedDisLike)
    {
        this.reviewModels = reviewModels;
        this.clickedLike = clickedLike;
        this.clickedDisLike = clickedDisLike;
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ReviewsViewHolder(ItemReviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position)
    {
        ReviewModel model = reviewModels.get(position);

        Glide
                .with(holder.itemView.getContext())
                .load(model.getImage())
                .placeholder(R.drawable.ic_loader_image)
                .into(holder.binding.imgItemReview);
        holder.binding.txtNameItemReview.setText(model.getName());
        holder.binding.txtLocationItemReview.setText(model.getLocation());
        holder.binding.txtOpinionItemReview.setText(model.getOpinion());
        holder.binding.txtNumberLikeItemReview.setText(String.valueOf(model.getNumberLikes()));
        holder.binding.txtNumberDislikeItemReview.setText(String.valueOf(model.getNumberDislikes()));

        holder.binding.imgLikeItemReview.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view)
            {
                if (model.getNumberLikes() == 0)
                {
                    clickedLike.like(view, model);
                }

                else
                {
                    holder.binding.imgLikeItemReview.setClickable(false);
                }
            }
        });

        holder.binding.imgDislikeItemReview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (model.getNumberDislikes() == 0)
                {
                    clickedDisLike.disLike(view, model);
                }

                else
                {
                    holder.binding.imgDislikeItemReview.setClickable(false);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return reviewModels.size();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder
    {

        private ItemReviewBinding binding;

        public ReviewsViewHolder(@NonNull ItemReviewBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
