
package com.example.sodrock.firebase_db_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * @Project : Firebase_DB_Test
 * @Author : ChanHyeok Jeong
 * @Date :  2017-01-26
 * @Usage : main - 데이터 전송,삭제 및 표시
 */

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

        //splash 화면
        startActivity(new Intent(this,Splash.class));

        //뷰 초기화
        initView();

        //db의 root/Callvan의 child 이벤트 리스너
        myDR.addChildEventListener(new ChildEventListener() {
                                       @Override
                                       public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                           String callvanName = dataSnapshot.getValue(String.class);
                                           //Log.d("테스트","콜밴 데이터 다운로드 완료 "+ callvanName +" : "+ dataSnapshot.getKey());

                                           //arrayList에 각각 이름과 키값을 차곡차곡 넣어줌
                                           callvanNames.add(callvanName);
                                           callvanKeys.add(dataSnapshot.getKey());

                                           m_Adapter.notifyDataSetChanged();
                                       }

                                       @Override
                                       public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                       }

                                       @Override
                                       public void onChildRemoved(DataSnapshot dataSnapshot) {
                                           //Log.d("test","지워진 데이터의 키값 " + dataSnapshot.getKey());
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

    /*
        뷰 초기화
     */
    public void initView(){
        et = (EditText) findViewById(R.id.dataInputET);

        m_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, callvanNames);
        m_ListView = (ListView) findViewById(R.id.dataList);
        m_ListView.setAdapter(m_Adapter);
        m_ListView.setOnItemClickListener(onClickListItem);
    }

    /*
        전송 버튼 onClick 리스너
     */
    public void onSendBtnClick(View view) {
        if (view.getId() == R.id.sendBtn) {//전송 버튼
            myDR.push().setValue(et.getText().toString());
        }
    }

    /*
        리스트뷰 아이템 이벤트 리스너
     */
    private AdapterView.OnItemClickListener onClickListItem = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Log.d("test","전송 데이터 : " + callvanNames.get(position));
            //Log.d("test","데이터 포지션 : " + position);

            //클릭된 아이템의 DB key값을 로드
            String callvanKey=callvanKeys.get(position);

            //arrayList에서 제거
            callvanKeys.remove(position);
            callvanNames.remove(position);

            //DB에서 데이터 삭제 -> onChildRemoved()메서드 호출
            myDR.child(callvanKey).removeValue();

            m_Adapter.notifyDataSetChanged();
        }
    };
}

