package com.khadamatemployee.profile;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.khadamatemployee.R;
import com.khadamatemployee.databinding.ProfileFragmentBinding;
import com.khadamatemployee.model.EmployeeModel;

public class ProfileFragment extends Fragment
{

    private ProfileFragmentBinding binding;
    private ProfileViewModel profileViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = ProfileFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);


        clickedViews();
        initViewModel();
        retriveViewModel();

    }

    private void clickedViews()
    {
        binding.btnSaveProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_homeFragment);
            }
        });

        binding.btnExitProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FirebaseAuth.getInstance().signOut();
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_loginFragment);
            }
        });

//        binding.btnNightProfile.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            }
//        });
    }

    private void initViewModel()
    {
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
    }

    private void retriveViewModel()
    {

        profileViewModel
                .RetriveData()
                .observe(getViewLifecycleOwner(), new Observer<EmployeeModel>()
                {
                    @Override
                    public void onChanged(EmployeeModel employeeModel)
                    {
                        Glide
                                .with(getActivity())
                                .load(employeeModel.getImage())
                                .placeholder(R.drawable.ic_loader_image)
                                .error(R.drawable.ic_no_photo)
                                .into(binding.imgProfile);

                        if (employeeModel.getGender().equals("Male"))
                        {
                            binding.editGenderProfile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_symbol_male, 0, 0, 0);
                            binding.editGenderProfile.setCompoundDrawablePadding(10);
                            binding.editGenderProfile.setText(employeeModel.getGender());
                        }
                        if (employeeModel.getGender().equals("Female"))
                        {
                            binding.editGenderProfile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_symbol_woman, 0, 0, 0);
                            binding.editGenderProfile.setCompoundDrawablePadding(10);
                            binding.editGenderProfile.setText(employeeModel.getGender());
                        }


                        binding.editJobProfile.setText(employeeModel.getJob());
                        binding.editFnProfile.setText(employeeModel.getFirstName());
                        binding.editLnProfile.setText(employeeModel.getLastName());
                        binding.editPhoneProfile.setText(employeeModel.getPhoneNumber());
                        binding.editLocationProfile.setText(employeeModel.getLocation());
                        binding.editEmailProfile.setText(employeeModel.getEmail());
                    }
                });
    }

//    @Override
//    public void onDestroyView()
//    {
//        super.onDestroyView();
//
//        profileViewModel.RetriveData().removeObservers(getViewLifecycleOwner());
//    }

}