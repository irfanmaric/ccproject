package com.madev.virtualwaitingroom.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.madev.virtualwaitingroom.Callback.ICategoryCallbackListener;
import com.madev.virtualwaitingroom.Common.Common;
import com.madev.virtualwaitingroom.Model.CategoryModel;

import java.time.chrono.MinguoEra;
import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel implements ICategoryCallbackListener {

    private MutableLiveData<List<CategoryModel>> categoryListMutable;
    private MutableLiveData<String> messageError = new MutableLiveData<>();
    private ICategoryCallbackListener categoryCallbackListener;

    public HomeViewModel() {
        categoryCallbackListener = this;
    }

    public MutableLiveData<List<CategoryModel>> getCategoryListMutable() {
        if(categoryListMutable == null){
            categoryListMutable = new MutableLiveData<>();
            messageError = new MutableLiveData<>();
            loadCategories();
        }
        return categoryListMutable;
    }

    private void loadCategories() {
        List<CategoryModel> tempList = new ArrayList<>();
        DatabaseReference categoryReference = FirebaseDatabase.getInstance().getReference(Common.CATEGORY_REF);
        categoryReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnapshot:snapshot.getChildren()){ CategoryModel categoryModel = itemSnapshot.getValue(CategoryModel.class);
                    categoryModel.setMenu_id(itemSnapshot.getKey());
                    tempList.add(categoryModel);
                }
                categoryCallbackListener.onCategoryLoadSuccess(tempList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                categoryCallbackListener.onCategoryLoadFailed(error.getMessage());
            }
        });
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    @Override
    public void onCategoryLoadSuccess(List<CategoryModel> categoryModelList) {
        categoryListMutable.setValue(categoryModelList);
    }

    @Override
    public void onCategoryLoadFailed(String message) {
        messageError.setValue(message);
    }
}