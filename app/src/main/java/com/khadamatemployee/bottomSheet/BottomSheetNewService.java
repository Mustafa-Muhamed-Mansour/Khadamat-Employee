package com.khadamatemployee.bottomSheet;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.khadamatemployee.databinding.BottomSheetServiceBinding;

public class BottomSheetNewService extends BottomSheetDialogFragment
{

    private BottomSheetDialog dialog;
    private BottomSheetServiceBinding bottomSheetServiceBinding;
    private BottomSheetNewServiceViewModel bottomSheetNewServiceViewModel;

    private ActivityResultLauncher<Intent> someResultLauncher;

    private String image;

    private Uri resultURI;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        bottomSheetServiceBinding = BottomSheetServiceBinding.inflate(inflater, container, false);
        return bottomSheetServiceBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);


        dialog = new BottomSheetDialog(requireActivity());

        bottomSheetNewServiceViewModel = new ViewModelProvider(BottomSheetNewService.this).get(BottomSheetNewServiceViewModel.class);

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
                    bottomSheetServiceBinding.imgService.setImageURI(resultURI);
                }
            }
        });

        bottomSheetServiceBinding.imgService.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openGallery();
            }
        });

        bottomSheetServiceBinding.btnCreateService.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                bottomSheetNewServiceViewModel.addNewService(bottomSheetServiceBinding.editTitleService.getText().toString(), image).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean)
                    {
                        if (aBoolean)
                        {
                            Toast.makeText(requireActivity(), "Done", Toast.LENGTH_SHORT).show();
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

    private void openGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        someResultLauncher.launch(intent);
    }
}
