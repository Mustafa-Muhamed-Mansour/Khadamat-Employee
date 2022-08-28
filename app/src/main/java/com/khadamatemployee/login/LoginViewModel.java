package com.khadamatemployee.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginViewModel extends ViewModel
{

    private MutableLiveData<Boolean> booleanMutableLiveData;
    private MutableLiveData<String> stringMutableLiveData;
    private FirebaseAuth auth;


    public LoginViewModel()
    {
        booleanMutableLiveData = new MutableLiveData<>();
        stringMutableLiveData = new MutableLiveData<>();
        auth = FirebaseAuth.getInstance();
    }

    public LiveData<Boolean> loginEmployee(String email, String password)
    {
        auth
                .signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>()
                {
                    @Override
                    public void onSuccess(AuthResult authResult)
                    {
                        booleanMutableLiveData.postValue(true);
                        stringMutableLiveData.setValue(authResult.toString());
                    }
                }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                booleanMutableLiveData.postValue(false);
                stringMutableLiveData.setValue(e.getMessage());
            }
        });

        return booleanMutableLiveData;
    }

}