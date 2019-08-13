package com.github.cdflynn.android.scrollsample;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.github.cdflynn.android.scrollsample.mock.Contact;
import com.github.cdflynn.android.scrollsample.mock.Section;

import java.util.List;

import cdflynn.android.library.scroller.BubbleScroller;
import cdflynn.android.library.scroller.ScrollerListener;
import cdflynn.android.library.scroller.StickyDecoration;
import cdflynn.android.library.scroller.listener.GroupListener;
import cdflynn.android.library.scroller.listener.OnGroupClickListener;

public class MainActivity extends AppCompatActivity {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String TAG = MainActivity.class.getSimpleName();
    private List<Contact> contactList;

    static class Views {
        BubbleScroller scroller;
        RecyclerView recycler;

        Views(MainActivity activity) {
            scroller = (BubbleScroller) activity.findViewById(R.id.bubble_scroller);
            recycler = (RecyclerView) activity.findViewById(R.id.recycler);
        }
    }

    private Views mViews;
    private ContactScrollerAdapter mContactScrollerAdapter;
    private ContactAdapter mContactAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean mProgrammaticScroll = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViews = new Views(this);
        contactList = Contact.mocks(this);
        StickyDecoration.Builder builder = StickyDecoration.Builder
                .init(new GroupListener() {
                    @Override
                    public String getGroupName(int position) {
                        //组名回调
                        if (contactList.size() > position && position > -1) {
                            //获取组名，用于判断是否是同一组
                            return contactList.get(position).getFirstName().substring(0,1);
                        }
                        return null;
                    }
                })
                //背景色
                .setGroupBackground(Color.parseColor("#48BDFF"))
                //高度
                .setGroupHeight(DensityUtil.dip2px(this, 35))
                //分割线颜色
                .setDivideColor(Color.parseColor("#EE96BC"))
                //分割线高度 (默认没有分割线)
//                .setDivideHeight(DensityUtil.dip2px(this, 2))
                //字体颜色 （默认）
                .setGroupTextColor(Color.BLACK)
                //字体大小
                .setGroupTextSize(DensityUtil.sp2px(this, 15))
                // 边距   靠左时为左边距  靠右时为右边距
                .setTextSideMargin(DensityUtil.dip2px(this, 10))
                // header数量（默认0）
                //.setHeaderCount(1)
                //Group点击事件
                .setOnClickListener(new OnGroupClickListener() {
                    @Override
                    public void onClick(int position, int id) {
                        //点击事件，返回当前分组下的第一个item的position
                        String content = "onGroupClick --> " + position + " " +  contactList.get(position).getFirstName();
                        Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();
                    }
                });

        StickyDecoration decoration = builder.build();
//        mContactScrollerAdapter = new ContactScrollerAdapter(contactList);
        mContactAdapter = new ContactAdapter(this, contactList);
        mLayoutManager = new LinearLayoutManager(this);

        mViews.scroller.setScrollerListener(mScrollerListener);
//        mViews.scroller.setSectionScrollAdapter(mContactScrollerAdapter);
        mViews.recycler.setLayoutManager(mLayoutManager);
        mViews.recycler.addItemDecoration(decoration);
        mViews.recycler.setAdapter(mContactAdapter);
        mViews.scroller.showSectionHighlight(0);
        mViews.recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mProgrammaticScroll) {
                    mProgrammaticScroll = false;
                    return;
                }
                final int firstVisibleItemPosition = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                String letter = contactList.get(firstVisibleItemPosition).getFirstName().substring(0, 1);
                mViews.scroller.showSectionHighlight(ALPHABET.indexOf(letter));
//                mViews.scroller.showSectionHighlight(
//                        mContactScrollerAdapter.sectionFromPosition(firstVisibleItemPosition));
            }
        });
    }


    private final ScrollerListener mScrollerListener = new ScrollerListener() {
        @Override
        public void onSectionClicked(int sectionPosition,String letter) {
            // 移动到首个
            for (int i=0;i<contactList.size();i++)
            {
                if (contactList.get(i).getFirstName().substring(0,1).equals(letter))
                {
                    ((LinearLayoutManager)  mViews.recycler.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                    return;
                }
            }
//            ((LinearLayoutManager)  mViews.recycler.getLayoutManager()).scrollToPositionWithOffset(mContactScrollerAdapter.positionFromSection(sectionPosition), 0);
//
//            mViews.recycler.smoothScrollToPosition(
//                    mContactScrollerAdapter.positionFromSection(sectionPosition));
            mProgrammaticScroll = true;
        }

        @Override
        public void onScrollPositionChanged(float percentage, int sectionPosition,String letter) {
            // 移动到首个
            for (int i=0;i<contactList.size();i++)
            {
                if (contactList.get(i).getFirstName().substring(0,1).equals(letter))
                {
                    ((LinearLayoutManager)  mViews.recycler.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                    return;
                }
            }
            // 移动到首个
//            ((LinearLayoutManager)  mViews.recycler.getLayoutManager()).scrollToPositionWithOffset(mContactScrollerAdapter.positionFromSection(sectionPosition), 0);
//
//            mViews.recycler.smoothScrollToPosition(
//                    mContactScrollerAdapter.positionFromSection(sectionPosition));
            mProgrammaticScroll = true;
        }
    };
}
