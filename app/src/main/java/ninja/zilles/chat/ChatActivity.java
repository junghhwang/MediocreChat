package ninja.zilles.chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {
    public static final String EXTRA_CHAT_KEY = "CHAT_KEY";

    private RecyclerView mRecyclerView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mChatRef;
    private EditText mNewPostText;
    private FirebaseRecyclerAdapter<ChatMessage, ChatMessageViewHolder> mChatViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        String chatKey = getIntent().getStringExtra(EXTRA_CHAT_KEY);
        mChatRef = database.getReference(chatKey);

        mRecyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNewPostText = (EditText) findViewById(R.id.newPostText);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mChatViewAdapter = new FirebaseRecyclerAdapter<ChatMessage,
                ChatMessageViewHolder>(ChatMessage.class,
               R.layout.chat_message, ChatMessageViewHolder.class, mChatRef) {
           @Override
           protected void populateViewHolder(ChatMessageViewHolder viewHolder,
                                             ChatMessage model, int position) {
               viewHolder.bind(model);
           }
       };
        mRecyclerView.setAdapter(mChatViewAdapter);
    }

    public static class ChatMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView mNameTextView;
        private final TextView mMessageTextView;

        public ChatMessageViewHolder(View itemView) {
            super(itemView);
            mMessageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            mNameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
        }

        public void bind(ChatMessage chatMessage) {
            mMessageTextView.setText(chatMessage.message);
            mNameTextView.setText(chatMessage.author);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mChatViewAdapter != null) {
            mChatViewAdapter.cleanup();
        }
    }

    public void newPostClick(View view) {
        String newPostString = mNewPostText.getText().toString();
        mNewPostText.setText("");

        if (mChatRef != null) {
//            ChatMessage chatMessage = new ChatMessage(newPostString, "George");
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.message = newPostString;
            chatMessage.author = "George";

            mChatRef.push().setValue(chatMessage);
        } else {
            Toast.makeText(view.getContext(), "Database not available", Toast.LENGTH_SHORT).show();
        }
    }
}
