package ninja.zilles.chat;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by zilles on 11/10/16.
 */

@IgnoreExtraProperties
public class ChatMessage {
    public String message;
    public String author;

    // re-wrote to avoid non-default constructor
//    public ChatMessage() { // if you include a non-default constructor,
//    }                      // this is necessary for de-serialization
//
//    public ChatMessage(String message, String author) {
//        this.message = message;
//        this.author = author;
//    }

}
