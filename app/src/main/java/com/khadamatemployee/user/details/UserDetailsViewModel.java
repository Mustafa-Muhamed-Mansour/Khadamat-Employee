package com.khadamatemployee.user.details;

import android.widget.RadioButton;

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
import com.khadamatemployee.model.EmployeeModel;
import com.khadamatemployee.model.ReasonModel;
import com.khadamatemployee.model.RequestModel;

public class UserDetailsViewModel extends ViewModel
{

    private MutableLiveData<String> stringMutableLiveData;
    private MutableLiveData<Boolean> booleanMutableLiveData;
    private MutableLiveData<EmployeeModel> employeeModelMutableLiveData;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference requestRef;
    private String randomKey;

    public UserDetailsViewModel()
    {
        stringMutableLiveData = new MutableLiveData<>();
        booleanMutableLiveData = new MutableLiveData<>();
        employeeModelMutableLiveData = new MutableLiveData<>();
        firebaseAuth = FirebaseAuth.getInstance();
        requestRef = FirebaseDatabase.getInstance().getReference();
        randomKey = FirebaseDatabase.getInstance().getReference().push().getKey();
    }

    public LiveData<EmployeeModel> retriveEmployee()
    {
        requestRef
                .child("Employees")
                .child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if (snapshot.exists())
                        {
                            EmployeeModel employeeModel = snapshot.getValue(EmployeeModel.class);
                            employeeModelMutableLiveData.postValue(employeeModel);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        stringMutableLiveData.setValue(error.getMessage());
                    }
                });

        return employeeModelMutableLiveData;
    }

    public LiveData<Boolean> requestEmployee(String id, String reaosn, String radioButton)
    {
        requestRef
                .child("Employees")
                .child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if (snapshot.exists())
                        {
                            EmployeeModel employeeModel = snapshot.getValue(EmployeeModel.class);

                            ReasonModel reasonModel = new ReasonModel(randomKey, employeeModel.getId(), employeeModel.getImage(), employeeModel.getFirstName(), employeeModel.getLastName(), employeeModel.getLocation(), employeeModel.getPhoneNumber(), employeeModel.getEmail(), employeeModel.getJob(), reaosn, radioButton);

                            if (radioButton.equals("قبول الطلب"))
                            {
                                requestRef
                                        .child("Requests")
                                        .child(id)
                                        .child(randomKey)
                                        .setValue(reasonModel);
                            }

                            else
                            {
                                requestRef
                                        .child("Requests")
                                        .child(id)
                                        .child(randomKey)
                                        .setValue(reasonModel);
                            }

                            employeeModelMutableLiveData.postValue(employeeModel);

                            booleanMutableLiveData.postValue(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        stringMutableLiveData.setValue(error.getMessage());
                    }
                });


        return booleanMutableLiveData;
    }

    public void deleteReservaion(String randomKey)
    {
        requestRef
                .child("Reservations")
                .child(firebaseAuth.getUid())
                .child(randomKey)
                .removeValue();
    }
}