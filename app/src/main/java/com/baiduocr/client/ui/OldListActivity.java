package com.baiduocr.client.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baiduocr.client.R;
import com.baiduocr.client.db.DBHelper;
import com.baiduocr.client.db.OldList;
import com.baiduocr.client.utils.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * class desc :
 */
public class OldListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_list);

        setToolBar("识别历史", true);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new ListAdapter(DBHelper.getOldList()));
    }

    class ListAdapter extends BaseQuickAdapter<OldList, BaseViewHolder> {

        private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        public ListAdapter(List<OldList> data) {
            super(R.layout.item_old_list, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, OldList oldList) {
            baseViewHolder.setText(R.id.tv_num, String.format("%s、", baseViewHolder.getAdapterPosition() + 1))
                    .setText(R.id.tv_time, simpleDateFormat.format(oldList.getCreateTime()))
                    .setText(R.id.tv_content, oldList.getContent());
            baseViewHolder.getView(R.id.btn_copy).setOnClickListener(v -> {
                StringUtil.setClipboard(baseViewHolder.itemView.getContext(), oldList.getContent());
                Toast.makeText(OldListActivity.this, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
            });
            baseViewHolder.getView(R.id.btn_delete).setOnClickListener(v -> {
                DBHelper.deleteOldList(oldList.getId());
                Toast.makeText(OldListActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                remove(baseViewHolder.getAdapterPosition());
                notifyItemRemoved(baseViewHolder.getAdapterPosition());
            });
        }
    }
}
