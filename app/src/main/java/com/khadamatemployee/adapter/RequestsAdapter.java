package com.khadamatemployee.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khadamatemployee.R;
import com.khadamatemployee.databinding.ItemRequestBinding;
import com.khadamatemployee.interfaces.ClickedRequestOfUser;
import com.khadamatemployee.model.RequestModel;

import java.util.ArrayList;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestsVH>
{

    private ArrayList<RequestModel> requestModels;

    private ClickedRequestOfUser requestOfUser;


    public RequestsAdapter(ArrayList<RequestModel> requestModels, ClickedRequestOfUser requestOfUser)
    {
        this.requestModels = requestModels;
        this.requestOfUser = requestOfUser;
    }

    @NonNull
    @Override
    public RequestsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new RequestsVH(ItemRequestBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RequestsVH holder, int position)
    {
        RequestModel model = requestModels.get(position);

        Glide
                .with(holder.itemView.getContext())
                .load(model.getImage())
                .placeholder(R.drawable.ic_loader_image)
                .into(holder.binding.imgItemRequest);
        holder.binding.txtNameItemRequest.setText(model.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                requestOfUser.requestOfUser(view , model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestModels.size();
    }

    public class RequestsVH extends RecyclerView.ViewHolder
    {

        private ItemRequestBinding binding;

        public RequestsVH(@NonNull ItemRequestBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
