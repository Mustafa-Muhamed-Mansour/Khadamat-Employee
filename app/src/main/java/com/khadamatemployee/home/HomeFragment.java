package com.khadamatemployee.home;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khadamatemployee.bottomSheet.BottomSheetNewService;
import com.khadamatemployee.R;
import com.khadamatemployee.adapter.WorkEmployeeAdapter;
import com.khadamatemployee.databinding.HomeFragmentBinding;
import com.khadamatemployee.interfaces.DeleteWork;
import com.khadamatemployee.model.WorkModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements DeleteWork
{

    private HomeFragmentBinding binding;
    private HomeViewModel homeViewModel;
    private WorkEmployeeAdapter workEmployeeAdapter;

//    private FirebaseAuth firebaseAuth;
//    private DatabaseReference deleteRef;

    private BottomSheetNewService bottomSheetNewService;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = HomeFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);


        initView();
//        initDatabase();
        clickedViews();
        initViewModel();
        retriveDataFromViewModel();

    }

    private void initView()
    {
        bottomSheetNewService = new BottomSheetNewService();
    }

//    private void initDatabase()
//    {
//        firebaseAuth = FirebaseAuth.getInstance();
//        deleteRef = FirebaseDatabase.getInstance().getReference();
//    }

    private void clickedViews()
    {
        binding.imgAddWork.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_addWorkFragment);
            }
        });

        binding.imgAddJob.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                bottomSheetNewService.show(getActivity().getSupportFragmentManager(), "BottomSheet");
            }
        });

        binding.imgReview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_reviewFragment);
            }
        });
    }

//    NewService

    private void initViewModel()
    {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    private void retriveDataFromViewModel()
    {

        binding.loadingWorks.setVisibility(View.VISIBLE);
        homeViewModel.RetriveWorks().observe(getViewLifecycleOwner(), new Observer<ArrayList<WorkModel>>()
        {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(ArrayList<WorkModel> workModels)
            {

                if (workModels == null)
                {
                    binding.loadingWorks.setVisibility(View.VISIBLE);
//                    binding.rV.setAdapter(null);
                }

                else
                {
                    binding.loadingWorks.setVisibility(View.GONE);

                    workEmployeeAdapter = new WorkEmployeeAdapter(workModels, HomeFragment.this);
                    binding.rV.setAdapter(workEmployeeAdapter);
                    binding.rV.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
                    binding.rV.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                    binding.rV.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));
                    binding.rV.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
                    workEmployeeAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void deleteWorks(WorkModel workModel, View view)
    {
        PopupMenu deleteMenu = new PopupMenu(requireActivity(), view);
        deleteMenu.getMenuInflater().inflate(R.menu.delete_menu, deleteMenu.getMenu());
        deleteMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.delete:

                        homeViewModel
                                .deleteWorks(workModel.getRandomKey())
                                        .observe(getViewLifecycleOwner(), new Observer<Boolean>()
                                        {
                                            @Override
                                            public void onChanged(Boolean aBoolean)
                                            {
                                                if (aBoolean)
                                                {
                                                    Toast.makeText(requireActivity(), "Done is Sucessfully", Toast.LENGTH_SHORT).show();
                                                    Navigation.findNavController(view).navigate(R.id.homeFragment);
                                                }

                                                else
                                                {
                                                    Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                        break;

                    default:
                        Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        deleteMenu.show();
    }
}