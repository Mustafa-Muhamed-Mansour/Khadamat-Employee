package com.khadamatemployee.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khadamatemployee.R;
import com.khadamatemployee.databinding.ItemWorkBinding;
import com.khadamatemployee.interfaces.DeleteWork;
import com.khadamatemployee.model.WorkModel;

import java.util.ArrayList;

public class WorkEmployeeAdapter extends RecyclerView.Adapter<WorkEmployeeAdapter.WorkEmployeeViewHolder>
{

    private ArrayList<WorkModel> workModels;

    public DeleteWork deleWork;

    public WorkEmployeeAdapter(ArrayList<WorkModel> workModels, DeleteWork deleWork)
    {
        this.workModels = workModels;
        this.deleWork = deleWork;
    }

    @NonNull
    @Override
    public WorkEmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new WorkEmployeeViewHolder(ItemWorkBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WorkEmployeeViewHolder holder, int position)
    {

        WorkModel model = workModels.get(position);

        if (workModels.isEmpty())
        {
            workModels.get(0);
        }

        if (model.getImage() == null)
        {
            Glide
                    .with(holder.itemView.getContext())
                    .load(R.drawable.ic_no_photo)
                    .error(R.drawable.ic_no_photo)
                    .into(holder.binding.itemImgWork);

            holder.binding.itemTxtTitleWork.setText(model.getTitle());
        }

        else
        {
            Glide
                    .with(holder.itemView.getContext())
                    .load(model.getImage())
                    .placeholder(R.drawable.ic_loader_image)
                    .into(holder.binding.itemImgWork);

            holder.binding.itemTxtTitleWork.setText(model.getTitle());
        }

        holder.binding.itemImgBtnMore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                deleWork.deleteWorks(model, view);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return workModels.size();
//        return 0;
    }

    public class WorkEmployeeViewHolder extends RecyclerView.ViewHolder
    {

        private ItemWorkBinding binding;

        public WorkEmployeeViewHolder(@NonNull ItemWorkBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
