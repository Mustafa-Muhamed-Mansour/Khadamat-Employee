package com.khadamatemployee.home;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.khadamatemployee.model.WorkModel;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel
{

    private MutableLiveData<Boolean> booleanMutableLiveData;
    private MutableLiveData<String> stringMutableLiveData;
    private MutableLiveData<ArrayList<WorkModel>> workModelMutableLiveData;
    private ArrayList<WorkModel> workModels;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference workRef , deleteRef;

    public HomeViewModel()
    {
        booleanMutableLiveData = new MutableLiveData<>();
        stringMutableLiveData = new MutableLiveData<>();
        workModelMutableLiveData = new MutableLiveData<>();
        workModels = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        workRef = FirebaseDatabase.getInstance().getReference();
        deleteRef = FirebaseDatabase.getInstance().getReference();
    }

    public LiveData<ArrayList<WorkModel>> RetriveWorks()
    {

        workRef
                .child("Employees")
                .child(firebaseAuth.getUid())
                .child("Works")
                .addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if (snapshot.exists())
                        {
                            workModels.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren())
                            {
                                WorkModel workModel = dataSnapshot.getValue(WorkModel.class);
                                workModels.add(workModel);
                            }

                            workModelMutableLiveData.postValue(workModels);
                        }

                        else
                        {
                            workModelMutableLiveData.postValue(null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        stringMutableLiveData.setValue(error.getMessage());
                    }
                });

        return workModelMutableLiveData;
    }

    public LiveData<Boolean> deleteWorks(String randomKey)
    {
        deleteRef
                .child("Employees")
                .child(firebaseAuth.getUid())
                .child("Works")
                .child(randomKey)
                .removeValue();

        deleteRef
                .child("Works")
                .child(randomKey)
                .removeValue();

        booleanMutableLiveData.postValue(true);

        return booleanMutableLiveData;
    }

}