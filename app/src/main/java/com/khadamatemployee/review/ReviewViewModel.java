package com.khadamatemployee.review;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khadamatemployee.model.EmployeeModel;
import com.khadamatemployee.model.ReviewModel;

import java.util.ArrayList;

public class ReviewViewModel extends ViewModel
{
    private MutableLiveData<Boolean> booleanMutableLiveData;
    private MutableLiveData<String> stringMutableLiveData;
    private MutableLiveData<ArrayList<ReviewModel>> reviewListMutableLiveData;
    private ArrayList<ReviewModel> reviewModels;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference retReviews;
    private String key;

    public ReviewViewModel()
    {
        booleanMutableLiveData = new MutableLiveData<>();
        stringMutableLiveData = new MutableLiveData<>();
        reviewListMutableLiveData = new MutableLiveData<>();
        reviewModels = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        retReviews = FirebaseDatabase.getInstance().getReference();
        key = FirebaseDatabase.getInstance().getReference().push().getKey();
    }

    public LiveData<ArrayList<ReviewModel>> retriveReviews()
    {

        retReviews
                .child("Employees")
                .child(firebaseAuth.getUid())
                .child("Reviews")
                .addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        reviewModels.clear();

                        if (snapshot.exists())
                        {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren())
                            {
                                ReviewModel model = dataSnapshot.getValue(ReviewModel.class);
                                reviewModels.add(model);
                            }
                            reviewListMutableLiveData.postValue(reviewModels);
                        }

                        booleanMutableLiveData.postValue(true);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        stringMutableLiveData.setValue(error.getMessage());
                        booleanMutableLiveData.postValue(false);
                    }
                });

        return reviewListMutableLiveData;
    }

    public LiveData<Boolean> clickLike(String id, String randomKey, String image, String name, String location, String opinion, int numberLike, int numberDisLike)
    {

        ReviewModel model = new ReviewModel(id, randomKey, image, name, location, opinion, numberLike, numberDisLike);

        retReviews
                .child("Employees")
                .child(firebaseAuth.getUid())
                .child("Reviews")
                .child(randomKey)
                .setValue(model)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            booleanMutableLiveData.postValue(true);
                        }

                        else
                        {
                            stringMutableLiveData.setValue(task.getException().getMessage());
                            booleanMutableLiveData.postValue(false);
                        }
                    }
                });

        return booleanMutableLiveData;
    }

    public LiveData<Boolean> clickDisLike(String id, String randomKey, String image, String name, String location, String opinion, int numberLike, int numberDisLike)
    {

        ReviewModel model = new ReviewModel(id, randomKey, image, name, location, opinion, numberLike, numberDisLike);

        retReviews
                .child("Employees")
                .child(firebaseAuth.getUid())
                .child("Reviews")
                .child(randomKey)
                .setValue(model)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            booleanMutableLiveData.postValue(true);
                        }

                        else
                        {
                            stringMutableLiveData.setValue(task.getException().getMessage());
                            booleanMutableLiveData.postValue(false);
                        }
                    }
                });

        return booleanMutableLiveData;
    }
}