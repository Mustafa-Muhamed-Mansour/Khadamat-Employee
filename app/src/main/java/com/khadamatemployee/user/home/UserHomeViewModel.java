package com.khadamatemployee.user.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khadamatemployee.model.RequestModel;

import java.util.ArrayList;

public class UserHomeViewModel extends ViewModel
{
    private MutableLiveData<Boolean> booleanMutableLiveData;
    private MutableLiveData<String> stringMutableLiveData;
    private MutableLiveData<ArrayList<RequestModel>> requestListMutableLiveData;

    private FirebaseAuth auth;
    private DatabaseReference retRef;

    private ArrayList<RequestModel> requestModels;


    public UserHomeViewModel()
    {
        booleanMutableLiveData = new MutableLiveData<>();
        stringMutableLiveData = new MutableLiveData<>();
        requestListMutableLiveData = new MutableLiveData<>();
        auth = FirebaseAuth.getInstance();
        retRef = FirebaseDatabase.getInstance().getReference();
        requestModels = new ArrayList<>();
    }

    public LiveData<ArrayList<RequestModel>> retriveReservations()
    {
        retRef
                .child("Reservations")
                .child(auth.getUid())
                .addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        requestModels.clear();

                        if (snapshot.exists())
                        {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren())
                            {
                                RequestModel model = dataSnapshot.getValue(RequestModel.class);
                                requestModels.add(model);
                            }
                            requestListMutableLiveData.postValue(requestModels);
                        }

                        booleanMutableLiveData.postValue(true);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        booleanMutableLiveData.postValue(false);
                        stringMutableLiveData.setValue(error.getMessage());
                    }
                });

        return requestListMutableLiveData;
    }
}