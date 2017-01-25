package com.example.sodrock.firebase_db_test;

import android.content.Context;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView m_ListView;//콜밴 목록을 띄울 리스트뷰
    private ArrayAdapter<String> m_Adapter;//리스트뷰 어댑터
    private ArrayList<String> callvanNames= new ArrayList<String>();;//콜밴 이름들을 저장할 arraylist
    private ArrayList<String> callvanKeys= new ArrayList<String>();;//콜밴의 각 키값을 저장할 arraylist
    private EditText et;//전송할 데이터 입력 창

    //응 파베디비쓸거야~
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myDR = database.getReference("Callvan"); //myDR이 Callvan 테이블을 참조

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        myDR.addChildEventListener(new ChildEventListener() {
                                       @Override
                                       public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                           String callvanName = dataSnapshot.getValue(String.class);
                                           Log.d("","콜밴 데이터 다운로드 완료 "+ callvanName +" : "+ dataSnapshot.getKey());
                                           //arrayList에 각각 이름과 키값을 차곡차곡 넣어줌
                                           callvanNames.add(callvanName);
                                           callvanKeys.add(dataSnapshot.getKey());

                                           refreshListView();
                                       }

                                       @Override
                                       public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                       }

                                       @Override
                                       public void onChildRemoved(DataSnapshot dataSnapshot) {
                                           Log.d("","지워진 데이터의 키값 " + dataSnapshot.getKey());
                                       }

                                       @Override
                                       public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                                       }

                                       @Override
                                       public void onCancelled(DatabaseError databaseError) {
                                       }
                                   }
        );
    }

    public void initView(){
        et = (EditText) findViewById(R.id.et1);
        refreshListView();
    }

    public void refreshListView() {


        m_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, callvanNames);
        m_ListView = (ListView) findViewById(R.id.list1);
        m_ListView.setAdapter(m_Adapter);
        m_ListView.setOnItemClickListener(onClickListItem);

    }

    public void onClick(View view) {
        if (view.getId() == R.id.bt1) {//전송 버튼
            myDR.push().setValue(et.getText().toString());
        }
    }

/*
리스트뷰 이벤트 리스너
 */
    private AdapterView.OnItemClickListener onClickListItem = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            System.out.println("텍스트 : " + callvanNames.get(position));
            System.out.println("포지션 : " + position);

            String callvanKey=callvanKeys.get(position);

            callvanKeys.remove(position);
            callvanNames.remove(position);
            myDR.child(callvanKey).removeValue();

            refreshListView();
        }
    };
}

