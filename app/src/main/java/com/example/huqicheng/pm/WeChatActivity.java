package com.example.huqicheng.pm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huqicheng.adapter.ChatMsgViewAdapter;
import com.example.huqicheng.adapter.MsgAdapter;
import com.example.huqicheng.bll.UserBiz;
import com.example.huqicheng.entity.ChatMsgEntity;
import com.example.huqicheng.entity.Group;
import com.example.huqicheng.entity.MsgList;
import com.example.huqicheng.entity.User;
import com.example.huqicheng.message.BaseMsg;
import com.example.huqicheng.nao.MessageNao;
import com.example.huqicheng.service.MsgService;
import com.example.huqicheng.service.MyService;
import com.example.huqicheng.service.OnChatMsgRecievedListener;
import com.example.huqicheng.utils.ClientUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.RunnableFuture;

/**
 * @author way
 */
public class WeChatActivity extends Activity implements OnClickListener {

	private Group group;

	private Button mBtnSend;// 发送btn
	private Button mBtnBack;// 返回btn
	private TextView mTextViewGrpName;
	private EditText mEditTextContent;
	private ListView mListView;
	private ChatMsgViewAdapter mAdapter;// 消息视图的Adapter
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();// 消息对象数组
	private Handler handler;
	private Intent intent;
	private User user;
	private UserBiz userBiz;
	private int lastItemPosition;

	private boolean hasMore;
	private long lastMsg;

	void addMsg(final ChatMsgEntity entity){


		mDataArrays.add(entity);
		mAdapter.notifyDataSetChanged();
		//mListView.setSelection(mListView.getCount() - 1);

	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_main);

		userBiz = new UserBiz(getApplicationContext());

		user = userBiz.readUser();

		group = (Group) this.getIntent().getSerializableExtra("group");


		initView();// 初始化view

		initData();// 初始化数据

		handler = new Handler(){
			@Override
			public void handleMessage(final Message msg) {
				switch (msg.what){
					//a new msg comes
					case 1:
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Thread th = Thread.currentThread();
								Log.d("msg",th.getName());
								mDataArrays.add(new MsgAdapter().BaseMsg2ChatMsgEntity((BaseMsg) msg.obj,user.getUserId()+""));
								mAdapter.notifyDataSetChanged();
								mListView.setSelection(mListView.getCount() - 1);
							}
						});


						break;

					case 2:
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								MsgAdapter msgAdapter = new MsgAdapter();
								MsgList list = (MsgList) msg.obj;
								for(int i=0;i<list.getMsgs().size();i++){
									lastMsg = list.getMsgs().get(i).getDate();

									mDataArrays.add(0,msgAdapter.BaseMsg2ChatMsgEntity(list.getMsgs().get(i),user.getUserId()+""));

								}
								hasMore = list.isHasMore();

								mAdapter.notifyDataSetChanged();

								mListView.setSelection(mListView.getCount() - 1);

							}
						});


						break;
					case 3:
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								MsgAdapter msgAdapter = new MsgAdapter();
								MsgList list = (MsgList) msg.obj;
								for(int i=0;i<list.getMsgs().size();i++){
									lastMsg = list.getMsgs().get(i).getDate();

									mDataArrays.add(0,msgAdapter.BaseMsg2ChatMsgEntity(list.getMsgs().get(i),user.getUserId()+""));

								}
								hasMore = list.isHasMore();

								mAdapter.notifyDataSetChanged();

							}
						});


						break;

				}
			}
		};



		ClientUtils.setListenerForWeChat(new OnChatMsgRecievedListener() {
			@Override
			public void onChatMsgRecieved(BaseMsg msg) {
				//TODO update UI

				Log.d("Msg Recieved", msg.getParams().toString());


				if(msg.getGroupId() == null){
					Log.d("Msg Recieved", "msg group id is null ");
					return;
				}

				if(!msg.getGroupId().equals(getId(1111)+"")){
					Log.d("Msg Recieved", "msg is not for this group ");
					return;
				}

				Log.d("Msg Recieved", msg.getGroupId());


				Message m = Message.obtain();
				m.what = 1;
				m.obj = msg;

				handler.handleMessage(m);

			}


			@Override
			public long getId(long id) {
				return group.getGroupId();
			}
		});

		mListView.setSelection(mListView.getCount() - 1);

		mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView absListView, int state) {
				switch (state){
					case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
						// get the top, load new message lists here
						if(lastItemPosition == 0){

							if(!hasMore) {
								Toast.makeText(WeChatActivity.this,"no more messages",Toast.LENGTH_SHORT).show();
								return;
							}
							Toast.makeText(WeChatActivity.this,"next",Toast.LENGTH_SHORT).show();
							new Thread(){
								@Override
								public void run() {
									MsgList list = getMsgs(lastMsg,10);
									Message msg = Message.obtain();
									msg.what = 3;
									msg.obj = list;
									handler.handleMessage(msg);
								}
							}.start();
						}

				}
			}

			@Override
			public void onScroll(AbsListView absListView, int firstVisible, int visibleCount, int total) {
				Log.d("", "onScroll: "+lastItemPosition);
				lastItemPosition = firstVisible;
			}
		});


	}



	public void initView() {
		mListView = (ListView) findViewById(R.id.listview);
		mBtnSend = (Button) findViewById(R.id.btn_send);
		mBtnSend.setOnClickListener(this);
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		mTextViewGrpName = (TextView) findViewById(R.id.tvGrpName);
		mTextViewGrpName.setText(group.getGroupName());
		mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
		mAdapter = new ChatMsgViewAdapter(WeChatActivity.this, mDataArrays,mListView);
		mListView.setAdapter(mAdapter);
	}


	public void initData() {

		new Thread(){
			@Override
			public void run() {
				//load the latest ten messages from server
				MsgList list = getMsgs(0,10);
				Message msg = Message.obtain();
				msg.what = 2;
				msg.obj = list;
				handler.handleMessage(msg);




			}
		}.start();

	}

	public synchronized MsgList getMsgs(long timestamp, int count){
		return new MessageNao().loadMsgs(group.getGroupId(),count,timestamp);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send:
			send();
			break;
		case R.id.btn_back:
			finish();
			break;
		}
	}


	private void send() {
		String contString = mEditTextContent.getText().toString();
		if (contString.length() > 0) {
			ChatMsgEntity entity = new ChatMsgEntity();
			entity.setName(user.getUsername());
			entity.setDate(getDate());
			entity.setMessage(contString);
			entity.setMsgType(false);

			final ChatMsgEntity temp = entity;

			new Thread(){
				@Override
				public void run() {
					ClientUtils.send(new MsgAdapter().ChatMsgEntity2BaseMsg(temp,group.getGroupId(),user.getAvatar()));
				}
			}.start();

			mEditTextContent.setText("");// clear editor



		}
	}

	/**
	 * get current time
	 *
	 * @return current time
	 */
	private String getDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format.format(new Date());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ClientUtils.setListenerForWeChat(null);
	}
}