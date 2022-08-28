package com.khadamatemployee.bottomSheet;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.khadamatemployee.model.ServiceModel;

public class BottomSheetNewServiceViewModel extends ViewModel
{

    private MutableLiveData<Boolean> booleanMutableLiveData;
    private MutableLiveData<String> stringMutableLiveData;
    private DatabaseReference jobReference;
    private StorageReference jobRef;
    private String randomKey;


    public BottomSheetNewServiceViewModel()
    {
        booleanMutableLiveData = new MutableLiveData<>();
        stringMutableLiveData = new MutableLiveData<>();
        jobReference = FirebaseDatabase.getInstance().getReference();
        randomKey = FirebaseDatabase.getInstance().getReference().push().getKey();
        jobRef = FirebaseStorage.getInstance().getReference().child(" Images").child("Image_Services").child(randomKey);
    }

    public LiveData<Boolean> addNewService(String title, String image)
    {

        jobRef
                .putFile(Uri.parse(image))
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            jobRef
                                    .getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>()
                                    {
                                        @Override
                                        public void onSuccess(Uri uri)
                                        {
                                            ServiceModel serviceModel = new ServiceModel(uri.toString(), title);
                                            jobReference.child("Services").child(randomKey).setValue(serviceModel);
                                        }
                                    }).addOnFailureListener(new OnFailureListener()
                                    {
                                        @Override
                                        public void onFailure(@NonNull Exception e)
                                        {
                                            stringMutableLiveData.setValue(e.getMessage());
                                        }
                                    });
                        }
                    }
                });

        return booleanMutableLiveData;
    }
}
