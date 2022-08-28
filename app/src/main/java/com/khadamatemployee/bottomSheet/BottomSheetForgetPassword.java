package com.khadamatemployee.bottomSheet;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.khadamatemployee.databinding.BottomSheetForgetPasswordBinding;

public class BottomSheetForgetPassword extends BottomSheetDialogFragment
{

    private BottomSheetDialog dialog;

    private BottomSheetForgetPasswordBinding binding;
    private BottomSheetForgetPasswordViewModel bottomSheetForgetPasswordViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = BottomSheetForgetPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        initView();
        initViewModel();
        clickedViews();
    }


    private void initView()
    {
        dialog = new BottomSheetDialog(requireActivity());
    }

    private void initViewModel()
    {
        bottomSheetForgetPasswordViewModel = new ViewModelProvider(this).get(BottomSheetForgetPasswordViewModel.class);
    }

    private void clickedViews()
    {

        binding
                .btnReset
                .setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        String email = binding.editEmail.getText().toString();

                        if (TextUtils.isEmpty(email))
                        {
                            Toast.makeText(requireActivity(), "برجاء إدخال البريد الإلكتروني لتغيير كلمة المرور", Toast.LENGTH_SHORT).show();
                            binding.editEmail.requestFocus();
                            return;
                        }

                        else
                        {

                            binding.loadingLogin.setVisibility(View.VISIBLE);
                            binding.btnReset.setVisibility(View.GONE);

                            bottomSheetForgetPasswordViewModel
                                    .forgetPassword(email)
                                    .observe(getViewLifecycleOwner(), new Observer<Boolean>()
                                    {
                                                @Override
                                                public void onChanged(Boolean aBoolean)
                                                {
                                                    if (aBoolean)
                                                    {
                                                        binding.loadingLogin.setVisibility(View.GONE);
                                                        binding.btnReset.setVisibility(View.VISIBLE);

                                                        Toast.makeText(requireActivity(), "يرجي مراجعة البريد الإلكتروني حيث تم إرسال إليك لينك لتغيير كلمة المرور", Toast.LENGTH_LONG).show();
                                                    }

                                                    else
                                                    {
                                                        binding.loadingLogin.setVisibility(View.GONE);
                                                        binding.btnReset.setVisibility(View.VISIBLE);

                                                        Toast.makeText(requireActivity(), "يرجي المحاولة في وقت لاحق ...", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                        }
                    }
                });
    }
}
