package com.khadamatemployee.addWork;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.khadamatemployee.R;
import com.khadamatemployee.databinding.AddWorkFragmentBinding;

public class AddWorkFragment extends Fragment
{

    private AddWorkFragmentBinding binding;
    private AddWorkViewModel addWorkViewModel;

    private ActivityResultLauncher<Intent> someResultLauncher;

    private String image;

    private Uri resultURI;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = AddWorkFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);



        initViewModel();
        clickedViews();
        backgroundProcess();
        

    }

    private void initViewModel()
    {
        addWorkViewModel = new ViewModelProvider(requireActivity()).get(AddWorkViewModel.class);
    }

    private void clickedViews()
    {
        binding.addPhotoWork.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openGallery();
            }
        });

        binding.btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String title = binding.editTitle.getText().toString();

                if (resultURI == null)
                {
                    Toast.makeText(requireActivity(), "من فضلك أدخل الصورة , حيث أنه لا يمكن رفع عنوان الصورة بدون الصورة", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(title))
                {
                    Toast.makeText(requireActivity(), "من فضلك أدخل عنوان مناسب لهذة الصورة", Toast.LENGTH_SHORT).show();
                    binding.editTitle.requestFocus();
                    return;
                }

                else
                {
                    retriveViewModel(view, title);
                }
            }
        });
    }

    private void backgroundProcess()
    {
        someResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>()
        {
            @Override
            public void onActivityResult(ActivityResult result)
            {
                Intent data = result.getData();

                if (result.getResultCode() == Activity.RESULT_OK && data != null && data.getData() != null)
                {
                    resultURI = data.getData();
                    image = resultURI.toString();
                    binding.addPhotoWork.setImageURI(resultURI);
                }
            }
        });
    }

    private void openGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        someResultLauncher.launch(intent);
    }

    private void retriveViewModel(View view, String title)
    {
        binding.loadingAddWork.setVisibility(View.VISIBLE);
        binding.btnAdd.setVisibility(View.GONE);

        addWorkViewModel
                .addWorks(image, title)
                .observe(getViewLifecycleOwner(), new Observer<Boolean>()
                {
                    @Override
                    public void onChanged(Boolean aBoolean)
                    {
                        if (aBoolean)
                        {
                            binding.addPhotoWork.setImageURI(null);
                            binding.loadingAddWork.setVisibility(View.GONE);

                            Navigation.findNavController(view).navigate(R.id.action_addWorkFragment_to_homeFragment);
                        }

                        else
                        {
                            binding.loadingAddWork.setVisibility(View.GONE);
                            binding.btnAdd.setVisibility(View.VISIBLE);

                            Toast.makeText(requireActivity(), "يوجد مشكلة , حاول مرة آخري في وقت لاحق", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

//    @Override
//    public void onDestroyView()
//    {
//        super.onDestroyView();
//
//        addWorkViewModel.addWorks(null, null).removeObservers(getViewLifecycleOwner());
//    }
}