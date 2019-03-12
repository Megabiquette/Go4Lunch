package com.albanfontaine.go4lunch.Utils;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ChatHelper {

    public static CollectionReference getChatCollection(){
        return FirebaseFirestore.getInstance().collection(Constants.COLLECTION_NAME_CHAT);
    }

    public static Query getAllMessagesForChat(String chat){
        return ChatHelper.getChatCollection().orderBy("dateCreated").limit(50);
    }
}
