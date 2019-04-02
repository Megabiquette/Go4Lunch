package com.albanfontaine.go4lunch.Utils;

import com.albanfontaine.go4lunch.Models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class UserHelper {

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(Constants.COLLECTION_NAME_USERS);
    }

    public static Task<Void> createUser(String uid, String username, String avatar, String restaurantChosen){
        User userToCreate = new User(uid, username, avatar, restaurantChosen);

        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    public static Query getAllUsers(){
        return UserHelper.getUsersCollection();
    }

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    public static Task<Void> deleteUser(String uid){
        return UserHelper.getUsersCollection().document(uid).delete();
    }
}
