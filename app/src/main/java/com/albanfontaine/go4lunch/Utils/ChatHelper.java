package com.albanfontaine.go4lunch.Utils;

import com.albanfontaine.go4lunch.Models.Message;
import com.albanfontaine.go4lunch.Models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ChatHelper {

    public static CollectionReference getChatCollection(){
        return FirebaseFirestore.getInstance().collection(Constants.COLLECTION_NAME_CHAT);
    }

    public static Query getAllMessagesForChat(String chat){
        return ChatHelper.getChatCollection().orderBy("dateCreated").limit(50);
    }

    public static Task<DocumentReference> createMessage(String text, User user){
        Message message = new Message(text, user);

        return ChatHelper.getChatCollection().add(message);
    }
}
