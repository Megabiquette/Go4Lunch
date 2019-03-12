package com.albanfontaine.go4lunch.Utils;

import com.albanfontaine.go4lunch.Models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserHelper {

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(Constants.COLLECTION_NAME_USERS);
    }

    public static Task<Void> createUser(String uid, String username, String avatar, String restaurantChosen){
        User userToCreate = new User(uid, username, avatar, restaurantChosen);

        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    public static Task<Void> updateRestaurantChosen(String restaurantChosen, String uid){
        return UserHelper.getUsersCollection().document(uid).update("restaurantChosen", restaurantChosen);
    }

    public static Task<Void> deleteUser(String uid){
        return UserHelper.getUsersCollection().document(uid).delete();
    }
}
