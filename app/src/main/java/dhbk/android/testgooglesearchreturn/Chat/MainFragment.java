package dhbk.android.testgooglesearchreturn.Chat;

import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;

import dhbk.android.testgooglesearchreturn.R;


/**
 * created by Jhordan on 05/07/15.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    public MainFragment() {
    }

    private Firebase mFirebaseReference;
    private List<Chat> chatList;
    private String idDevice;
    private ChatAdapter chatAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Config.getFirebaseInitialize(getActivity());
        mFirebaseReference = Config.getFirebaseReference();
        chatList = new ArrayList<>();
        idDevice = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

    }


    EditText editTxtMessage;
    RecyclerView recyclerViewChat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        initializeView(rootView);
        return rootView;

    }

    private void setUpRecyclerView(RecyclerView recyclerView, ChatAdapter chatAdapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(chatAdapter);
    }

    private void initializeView(View rootView) {
//        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        if (actionBar != null)
//            actionBar.setTitle(getString(R.string.app_name));

        editTxtMessage = (EditText) rootView.findViewById(R.id.edit_txt_message);
        recyclerViewChat = (RecyclerView) rootView.findViewById(R.id.recycler_view_chat);
        ((FloatingActionButton) rootView.findViewById(R.id.button_sent)).setOnClickListener(this);


        chatAdapter = new ChatAdapter(chatList, idDevice);
        setUpRecyclerView(recyclerViewChat, chatAdapter);
        getChatMessages();


    }

    private void getChatMessages() {
        mFirebaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    //Firebase - Convierte una respuesta en un objeto de tipo Chat
                    Chat model = dataSnapshot.getValue(Chat.class);
                    chatList.add(model);
                    recyclerViewChat.scrollToPosition(chatList.size() - 1);
                    chatAdapter.notifyItemInserted(chatList.size() - 1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                firebaseError.getMessage();
            }
        });
    }

    private void getMessageToSent() {
        String message = editTxtMessage.getText().toString();
        if (!message.isEmpty())
            mFirebaseReference.push().setValue(new Chat(message, idDevice));
        editTxtMessage.setText("");
    }

    @Override
    public void onClick(View view) {
        getMessageToSent();
    }
}
