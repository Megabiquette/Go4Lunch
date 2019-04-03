package com.albanfontaine.go4lunch.Utils;

import com.albanfontaine.go4lunch.Models.FirebaseRestaurant;
import com.albanfontaine.go4lunch.Models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RestaurantHelper {

    public static CollectionReference getRestaurantsCollection(){
        return FirebaseFirestore.getInstance().collection(Constants.COLLECTION_NAME_RESTAURANT);
    }

    public static Task<Void> createRestaurant(String name, List<User> usersWhoLiked, List<User> usersWhoJoined){
        FirebaseRestaurant restaurantToCreate = new FirebaseRestaurant(name, usersWhoLiked, usersWhoJoined);

        return RestaurantHelper.getRestaurantsCollection().document(name).set(restaurantToCreate);
    }
}
