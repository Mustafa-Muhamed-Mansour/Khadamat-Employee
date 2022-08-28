package com.khadamatemployee.review;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.khadamatemployee.adapter.ReviewsAdapter;
import com.khadamatemployee.databinding.ReviewFragmentBinding;
import com.khadamatemployee.interfaces.ClickedDisLike;
import com.khadamatemployee.interfaces.ClickedLike;
import com.khadamatemployee.model.ReviewModel;

import java.util.ArrayList;

public class ReviewFragment extends Fragment implements ClickedLike, ClickedDisLike
{

    private ReviewFragmentBinding binding;
    private ReviewViewModel reviewViewModel;

    private ReviewsAdapter reviewsAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = ReviewFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);



        clickedView();
        initViewModel();
        retriveViewModel();


    }

    private void initViewModel()
    {
        reviewViewModel = new ViewModelProvider(requireActivity()).get(ReviewViewModel.class);
    }


    private void clickedView()
    {
    }

    private void retriveViewModel()
    {

        binding.loadingReview.setVisibility(View.VISIBLE);
        reviewViewModel
                .retriveReviews()
                .observe(getViewLifecycleOwner(), new Observer<ArrayList<ReviewModel>>()
                {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChanged(ArrayList<ReviewModel> reviewModels)
                    {
                        binding.loadingReview.setVisibility(View.GONE);

                        reviewsAdapter = new ReviewsAdapter(reviewModels, ReviewFragment.this, ReviewFragment.this);
                        binding.rV.setAdapter(reviewsAdapter);
                        binding.rV.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
                        binding.rV.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
                        reviewsAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void like(View view, ReviewModel reviewModel)
    {

        int number = reviewModel.getNumberLikes();
        number ++;

        reviewViewModel
                .clickLike(reviewModel.getId(), reviewModel.getRandomKey(), reviewModel.getImage(), reviewModel.getName(), reviewModel.getLocation(), reviewModel.getOpinion(), number, reviewModel.getNumberDislikes())
                .observe(getViewLifecycleOwner(), new Observer<Boolean>()
                {
                            @Override
                            public void onChanged(Boolean aBoolean)
                            {
                                if (aBoolean)
                                {
                                    Toast.makeText(requireActivity(), "Done", Toast.LENGTH_SHORT).show();
                                }

                                else
                                {
                                }
                            }
                        });
    }

    @Override
    public void disLike(View view, ReviewModel reviewModel)
    {
        int number = reviewModel.getNumberDislikes();
        number ++;

        reviewViewModel
                .clickDisLike(reviewModel.getId(), reviewModel.getRandomKey(), reviewModel.getImage(), reviewModel.getName(), reviewModel.getLocation(), reviewModel.getOpinion(), reviewModel.getNumberLikes(), number)
                .observe(getViewLifecycleOwner(), new Observer<Boolean>()
                {
                    @Override
                    public void onChanged(Boolean aBoolean)
                    {
                        if (aBoolean)
                        {
                            Toast.makeText(requireActivity(), "Done", Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                        }
                    }
                });
    }
}