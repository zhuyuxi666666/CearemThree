package com.example.administrator.cearemthree;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lenovo on 2017/5/25.
 */

public class RecycleView_Adapter extends RecyclerView.Adapter<RecycleView_Adapter.ViewHolder> {
    private List<Bitmap> mDatas;
    private LayoutInflater layoutInflater;

    private ButtonInterface buttonInterface;

    /**
     * 按钮点击事件需要的方法
     */
    public void buttonSetOnclick(ButtonInterface buttonInterface) {
        this.buttonInterface = buttonInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface ButtonInterface {
        public void onclick(View view, int position);
    }

    //构造方法
    public RecycleView_Adapter(List<Bitmap> mDatas) {
        this.mDatas = mDatas;
    }

    //关联子布局
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycleview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.iv_item.setImageBitmap(mDatas.get(position));
        holder.tv_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除自带默认动画
                removeData(position);

            }
        });
    }

    // 删除数据
    public void removeData(int position) {
        mDatas.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    //创建ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_item;
        private TextView tv_item_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_item = (ImageView) itemView.findViewById(R.id.mRecycleview_item);
            tv_item_delete = itemView.findViewById(R.id.mRecycleview_item_delete);
        }
    }
}
