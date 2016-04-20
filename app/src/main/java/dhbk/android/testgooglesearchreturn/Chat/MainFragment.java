package dhbk.android.testgooglesearchreturn.Chat;

import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

import dhbk.android.testgooglesearchreturn.R;

// TODO: 4/20/16 xóa comment
public class MainFragment extends Fragment {
    private Firebase mFirebaseReference;
//    private List<Chat> chatList;
    private String idDevice;
    private ChatAdapter chatAdapter;
    private EditText editTxtMessage;
//    private RecyclerView recyclerViewChat;

    public static final String TAG = MainFragment.class.getName();
    private ListView listView;
    private FirebaseListAdapter<Chat> adapter;

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Config.getFirebaseInitialize(getActivity());
        mFirebaseReference = Config.getFirebaseReference();
//        chatList = new ArrayList<>();

        // TODO: 4/20/16 thay idDevice bằng tên trên facebook (có thể thêm hình ảnh đại diện)
        idDevice = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initializeView(rootView);
        return rootView;
    }

//    private void setUpRecyclerView(RecyclerView recyclerView, ChatAdapter chatAdapter) {
//        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
//        recyclerView.setAdapter(chatAdapter);
//    }

    private void initializeView(View rootView) {
//        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        if (actionBar != null)
//            actionBar.setTitle(getString(R.string.app_name));

        editTxtMessage = (EditText) rootView.findViewById(R.id.edit_txt_message);
//        recyclerViewChat = (RecyclerView) rootView.findViewById(R.id.recycler_view_chat);
        listView = (ListView)rootView.findViewById(R.id.list_chat);
        ((FloatingActionButton) rootView.findViewById(R.id.button_sent)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMessageToSent();
            }
        });

//        setUpRecyclerView(recyclerViewChat, chatAdapter);
//
        getChatMessages();
    }

    private void getChatMessages() {
        // para cho Adapter là String do từng ô trong list mình muốn chỉ xuất String thôi.
        adapter = new FirebaseListAdapter<Chat>(getActivity(), Chat.class, android.R.layout.two_line_list_item, mFirebaseReference) {
            @Override
            // tham số thứ 2 là String do adapter có para là String
            protected void populateView(View view, Chat chatMes, int position) {
                ((TextView)view.findViewById(android.R.id.text1)).setText(chatMes.getAuthor());
                ((TextView)view.findViewById(android.R.id.text2)).setText(chatMes.getMessage());
            }
        };
        listView.setAdapter(adapter);
//        mFirebaseReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
//                    Chat model = dataSnapshot.getValue(Chat.class);
//                    chatList.add(model);
//                    recyclerViewChat.scrollToPosition(chatList.size() - 1);
//                    chatAdapter.notifyItemInserted(chatList.size() - 1);
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                firebaseError.getMessage();
//            }
//        });
    }

    private void getMessageToSent() {
        String message = editTxtMessage.getText().toString();
        if (!message.isEmpty())
            mFirebaseReference.push().setValue(new Chat(message, idDevice));
        editTxtMessage.setText("");
    }

    // stop listen to database
    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
    }
}
