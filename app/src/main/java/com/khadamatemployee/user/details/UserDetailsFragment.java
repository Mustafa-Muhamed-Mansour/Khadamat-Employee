package com.khadamatemployee.user.details;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khadamatemployee.R;
import com.khadamatemployee.databinding.BottomSheetRequestBinding;
import com.khadamatemployee.databinding.UserDetailsFragmentBinding;
import com.khadamatemployee.model.EmployeeModel;

public class UserDetailsFragment extends Fragment
{

    private UserDetailsFragmentArgs detailsFragmentArgs;
    private UserDetailsFragmentBinding binding;
    private UserDetailsViewModel userDetailsViewModel;

    private RadioButton radioButton;

    private String reason;

    private BottomSheetDialog sheetDialog;
    private BottomSheetRequestBinding sheetRequestBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = UserDetailsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);


        initArgs();
        initBottomSheet();
        initViewModel();
        clickedViews();
        retriveDataFromViewModel();

    }

    private void initArgs()
    {
        detailsFragmentArgs = UserDetailsFragmentArgs.fromBundle(getArguments());

        Glide
                .with(requireActivity())
                .load(detailsFragmentArgs.getUserDetails().getImage())
                .placeholder(R.drawable.ic_loader_image)
                .into(binding.imgUser);
        binding.txtNameUser.setText(detailsFragmentArgs.getUserDetails().getName());
        binding.txtDayUser.setText(detailsFragmentArgs.getUserDetails().getDay());
        binding.txtTimeUser.setText(detailsFragmentArgs.getUserDetails().getTime());
    }

    private void initBottomSheet()
    {
        sheetDialog = new BottomSheetDialog(requireActivity());
        sheetRequestBinding = DataBindingUtil.inflate(LayoutInflater.from(requireActivity()), R.layout.bottom_sheet_request, requireActivity().findViewById(R.id.relative_bottom_sheet), false);
        sheetDialog.setContentView(sheetRequestBinding.getRoot());
    }

    private void initViewModel()
    {
        userDetailsViewModel = new ViewModelProvider(this).get(UserDetailsViewModel.class);
    }

    private void clickedViews()
    {

        binding
                .imgClose
                .setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Navigation.findNavController(view).navigate(R.id.action_userDetailsFragment_to_userHomeFragment);
                    }
                });

        binding.radioAccept.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int i = binding.radioGroup.getCheckedRadioButtonId();
                radioButton = view.findViewById(i);
            }
        });

        binding.radioReject.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int i = binding.radioGroup.getCheckedRadioButtonId();
                radioButton = view.findViewById(i);
            }
        });

        binding
                .btnSentToRequest
                .setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {

                        if ((!binding.radioAccept.isChecked()) && (!binding.radioReject.isChecked()))
                        {
                            Toast.makeText(requireActivity(), "من فضلك قم بإختيار أحد الإختيارات ( قبول الطلب أو رفض الطلب )", Toast.LENGTH_LONG).show();
                            return;
                        }

                        else
                        {
                            if (binding.radioReject.isChecked())
                            {
                                binding.reason.setVisibility(View.VISIBLE);
                                reason = binding.editReason.getText().toString();

                                if (TextUtils.isEmpty(reason))
                                {
                                    Toast.makeText(requireActivity(), "من فضلك قم بكتابة السبب المناسب لرفض الطلب", Toast.LENGTH_SHORT).show();
                                    binding.editReason.requestFocus();
                                    return;
                                }

                                clickedViewsOfBottomSheet(reason);
                                sheetDialog.show();
                            }

                            if (binding.radioAccept.isChecked())
                            {
                                clickedViewsOfBottomSheet(reason);
                                sheetDialog.show();
                            }
                        }
                    }
                });

        binding
                .fabUserPhone
                .setOnClickListener(new View.OnClickListener()
                {
            @Override
            public void onClick(View view)
            {
                requestPermission();
            }
        });
    }

    private void retriveDataFromViewModel()
    {
        userDetailsViewModel
                .retriveEmployee()
                .observe(getViewLifecycleOwner(), new Observer<EmployeeModel>()
                {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onChanged(EmployeeModel employeeModel)
                    {
                        Glide
                                .with(requireActivity())
                                .load(employeeModel.getImage())
                                .placeholder(R.drawable.ic_loader_image)
                                .into(sheetRequestBinding.imgUser);
                        sheetRequestBinding.txtNameUser.setText(employeeModel.getFirstName() + " " + employeeModel.getLastName());
                        sheetRequestBinding.txtLocationUser.setText(employeeModel.getLocation());
                        sheetRequestBinding.txtPhoneUser.setText(employeeModel.getPhoneNumber());
                        sheetRequestBinding.txtEmailUser.setText(employeeModel.getEmail());
                    }
                });
    }

    private void requestPermission()
    {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, 100);
        }

        else
        {
            String phone = detailsFragmentArgs.getUserDetails().getPhone();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phone));
            startActivity(intent);
        }
    }


    private void clickedViewsOfBottomSheet(String reason)
    {
        sheetRequestBinding
                .imgCloseBottomSheet
                .setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        sheetDialog.dismiss();
                    }
                });

        sheetRequestBinding
                .btnDone
                .setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        userDetailsViewModel
                                .requestEmployee(detailsFragmentArgs.getUserDetails().getId(), reason , radioButton.getText().toString())
                                .observe(getViewLifecycleOwner(), new Observer<Boolean>()
                                {
                                    @Override
                                    public void onChanged(Boolean aBoolean)
                                    {
                                        if (aBoolean)
                                        {

                                            sheetDialog.dismiss();
                                            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_userDetailsFragment_to_homeFragment);

                                            retriveDeleteOfViewModel();
                                            Toast.makeText(requireActivity(), "تم الإرسال", Toast.LENGTH_LONG).show();
                                        }

                                        else
                                        {
                                            Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
    }


    private void retriveDeleteOfViewModel()
    {
        userDetailsViewModel
                .deleteReservaion(detailsFragmentArgs.getUserDetails().getRandomKey());
    }
}