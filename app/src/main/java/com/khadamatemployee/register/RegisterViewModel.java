package com.khadamatemployee.register;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.khadamatemployee.model.EmployeeModel;

public class RegisterViewModel extends ViewModel
{

    private MutableLiveData<Boolean> booleanMutableLiveData;
    private MutableLiveData<String> stringMutableLiveData;
    private String randomKey;
    private FirebaseAuth firebaseAuth;
    private StorageReference imageRef;
    private DatabaseReference dataBaseRefe;

    public RegisterViewModel()
    {
        booleanMutableLiveData = new MutableLiveData<>();
        stringMutableLiveData = new MutableLiveData<>();
        randomKey = FirebaseDatabase.getInstance().getReference().push().getKey();
        firebaseAuth = FirebaseAuth.getInstance();
        imageRef = FirebaseStorage.getInstance().getReference().child("Images").child("Image_Employees").child(randomKey);
        dataBaseRefe = FirebaseDatabase.getInstance().getReference();
    }

    public LiveData<Boolean> registerEmployee(String image, String firstName, String lastName, String location, String phone, String email, String password, String job, String gender)
    {

        firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            booleanMutableLiveData.postValue(true);

                            imageRef
                                    .putFile(Uri.parse(image))
                                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                booleanMutableLiveData.postValue(true);

                                                imageRef
                                                        .getDownloadUrl()
                                                        .addOnSuccessListener(new OnSuccessListener<Uri>()
                                                        {
                                                            @Override
                                                            public void onSuccess(Uri uri)
                                                            {

                                                                EmployeeModel employeeModel = new EmployeeModel(randomKey, firebaseAuth.getUid(), uri.toString(), firstName, lastName, location, phone, email, job, gender);

                                                                dataBaseRefe
                                                                        .child("Employees")
                                                                        .child(firebaseAuth.getUid())
                                                                        .setValue(employeeModel);

                                                                dataBaseRefe
                                                                        .child("Jobs")
                                                                        .child(job)
                                                                        .child(randomKey)
                                                                        .setValue(employeeModel);

                                                                dataBaseRefe
                                                                        .child("Search for Employees")
                                                                        .child(randomKey)
                                                                        .setValue(employeeModel);

                                                                booleanMutableLiveData.postValue(true);

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
                                            }

                                            else
                                            {
                                                booleanMutableLiveData.postValue(false);
                                                stringMutableLiveData.setValue(task.getException().getMessage());
                                            }
                                        }
                                    });

                        }

                        else
                        {
                            booleanMutableLiveData.postValue(false);
                            stringMutableLiveData.setValue(task.getException().getMessage());
                        }
                    }
                });

        return booleanMutableLiveData;
    }
}