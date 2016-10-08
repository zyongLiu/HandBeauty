package com.liu.handbeauty.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.liu.handbeauty.R;
import com.liu.handbeauty.bean.Pictures;
import com.liu.handbeauty.system.SystemPath;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Liu on 2016-06-10.
 */
public class ImagesAdapter extends PagerAdapter{
    private Context context;
    private Pictures datas;
    private List<View> viewList;
    private OnSetClickListener listener;

    public interface OnSetClickListener{
        void onClick(String url);
    }

    public void setOnSetClickListener(OnSetClickListener listener){
        this.listener=listener;
    }

    public ImagesAdapter(Context context, final Pictures datas) {
        this.context=context;
        this.datas=datas;
        viewList=new ArrayList<>();
        for (int i=0;i<datas.getList().size();i++){
            View view= LayoutInflater.from(context).inflate(R.layout.item_vp_image,null);
            PhotoView photoView= (PhotoView) view.findViewById(R.id.pv_image);
            Button btn= (Button) view.findViewById(R.id.btn_set);

            Picasso.with(context).load(SystemPath.imgUrl
                    +datas.getList().get(i).getSrc())
                    .placeholder(R.drawable.beauty_loading).error(R.drawable.beauty_loading)
                    .memoryPolicy(MemoryPolicy.NO_STORE)
                    .into(photoView);
            final int copy=i;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null)
                        listener.onClick(datas.getList().get(copy).getSrc());
                }
            });

            viewList.add(view);
        }
    }

    @Override
    public int getCount() {
        return datas.getSize();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position%datas.getList().size()));
        return viewList.get(position%datas.getList().size());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position%datas.getList().size()));
    }
}
