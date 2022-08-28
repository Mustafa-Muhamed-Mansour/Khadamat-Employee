package com.khadamatemployee.user.home;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khadamatemployee.interfaces.ClickedRequestOfUser;
import com.khadamatemployee.adapter.RequestsAdapter;
import com.khadamatemployee.databinding.UserHomeFragmentBinding;
import com.khadamatemployee.model.RequestModel;

import java.util.ArrayList;

public class UserHomeFragment extends Fragment implements ClickedRequestOfUser
{

    private UserHomeFragmentBinding binding;
    private UserHomeViewModel userHomeViewModel;

    private RequestsAdapter requestsAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = UserHomeFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);


        initViewModel();
        clickedViews();
        retriveDataFromViewModel();
    }

    private void initViewModel()
    {
        userHomeViewModel = new ViewModelProvider(requireActivity()).get(UserHomeViewModel.class);
    }

    private void clickedViews()
    {
    }

    private void retriveDataFromViewModel()
    {
        binding.loadingRequest.setVisibility(View.VISIBLE);
        userHomeViewModel
                .retriveReservations()
                .observe(getViewLifecycleOwner(), new Observer<ArrayList<RequestModel>>()
                {
                    @Override
                    public void onChanged(ArrayList<RequestModel> requestModels)
                    {
                        binding.loadingRequest.setVisibility(View.GONE);

                        requestsAdapter = new RequestsAdapter(requestModels, UserHomeFragment.this);
                        binding.rV.setAdapter(requestsAdapter);
                        binding.rV.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
                        binding.rV.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
                    }
                });
    }

    @Override
    public void requestOfUser(View view, RequestModel requestModel)
    {
        UserHomeFragmentDirections.ActionUserHomeFragmentToUserDetailsFragment action = UserHomeFragmentDirections.actionUserHomeFragmentToUserDetailsFragment(requestModel);
        action.setUserDetails(requestModel);
        Navigation.findNavController(view).navigate(action);
    }
}