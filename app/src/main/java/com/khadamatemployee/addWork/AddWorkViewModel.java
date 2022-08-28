package com.khadamatemployee.addWork;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.khadamatemployee.model.WorkModel;

public class AddWorkViewModel extends ViewModel
{

    private MutableLiveData<Boolean> booleanMutableLiveData;
    private MutableLiveData<String> stringMutableLiveData;
    private FirebaseAuth firebaseAuth;
    private StorageReference imageRef;
    private DatabaseReference workRef;
    private String randomKey;

    public AddWorkViewModel()
    {
        booleanMutableLiveData = new MutableLiveData<>();
        stringMutableLiveData = new MutableLiveData<>();
        firebaseAuth = FirebaseAuth.getInstance();
        workRef = FirebaseDatabase.getInstance().getReference();
        randomKey = workRef.push().getKey();
        imageRef = FirebaseStorage.getInstance().getReference().child("Images").child("Employee_Work_Image").child(randomKey);
    }

    public LiveData<Boolean> addWorks(String image, String title)
    {

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
                                           WorkModel workModel = new WorkModel(randomKey, firebaseAuth.getUid(), uri.toString(), title);

                                           workRef
                                                   .child("Works")
                                                   .child(randomKey)
                                                   .setValue(workModel);

                                           workRef
                                                   .child("Employees")
                                                   .child(firebaseAuth.getUid())
                                                   .child("Works")
                                                   .child(randomKey)
                                                   .setValue(workModel);

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

        return booleanMutableLiveData;
    }

}