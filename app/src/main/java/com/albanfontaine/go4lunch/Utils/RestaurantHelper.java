package com.albanfontaine.go4lunch.Utils;

import com.albanfontaine.go4lunch.Models.FirebaseRestaurant;
import com.albanfontaine.go4lunch.Models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class RestaurantHelper {


    public static CollectionReference getRestaurantsCollection(){
        return FirebaseFirestore.getInstance().collection(Constants.COLLECTION_NAME_RESTAURANT);
    }

    public static Task<QuerySnapshot> getWhoLikedRestaurant(String name){
        return RestaurantHelper.getRestaurantsCollection().document(name).collection(Constants.COLLECTION_NAME_USERS_WHO_LIKED).get();
    }

    public static Task<QuerySnapshot> getWhoJoinedRestaurant(String name){
        return RestaurantHelper.getRestaurantsCollection().document(name).collection(Constants.COLLECTION_NAME_USERS_WHO_JOINED).get();
    }

    public static Task<Void> createRestaurant(String name, String details){
        FirebaseRestaurant restaurantToCreate = new FirebaseRestaurant(name, details);
        return RestaurantHelper.getRestaurantsCollection().document(name).set(restaurantToCreate);
    }

    public static Task<Void> addUserToJoinList(String name, String uid, User user){
        return RestaurantHelper.getRestaurantsCollection().document(name).collection(Constants.COLLECTION_NAME_USERS_WHO_JOINED).document(uid).set(user);
    }

    public static Task<Void> removeUserToJoinList(String name, String uid){
        return RestaurantHelper.getRestaurantsCollection().document(name).collection(Constants.COLLECTION_NAME_USERS_WHO_JOINED).document(uid).delete();
    }

    public static Task<Void> addUserToLikedList(String name, String uid, User user){
        return RestaurantHelper.getRestaurantsCollection().document(name).collection(Constants.COLLECTION_NAME_USERS_WHO_LIKED).document(uid).set(user);
    }

    public static Task<Void> removeUserToLikedList(String name, String uid){
        return RestaurantHelper.getRestaurantsCollection().document(name).collection(Constants.COLLECTION_NAME_USERS_WHO_LIKED).document(uid).delete();
    }
}
