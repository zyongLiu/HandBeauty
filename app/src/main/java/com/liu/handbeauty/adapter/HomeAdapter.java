package com.liu.handbeauty.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liu.handbeauty.R;
import com.liu.handbeauty.bean.Gallery;
import com.liu.handbeauty.fragment.TestFragment;
import com.liu.handbeauty.system.SystemPath;
import com.liu.handbeauty.utils.DisplayUtils;
import com.liu.handbeauty.utils.LogUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by Liu on 2016-05-25.
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private List<Gallery> datas;

    public final static int TYPE_FOOTER = 2;//底部--往往是loading_more
    public final static int TYPE_NORMAL = 1; // 正常的一条文章

    public HomeAdapter(Context context, List<Gallery> datas) {
        this.context=context;
        this.datas=datas;
    }

    public OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    @Override
    public int getItemViewType(int position) {
        Gallery article = datas.get(position);
        if (article == null) {
            return TYPE_FOOTER;
        }  else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        RecyclerView.ViewHolder holder;
        switch (viewType){
            case TYPE_NORMAL:
                holder = new MyViewHolder(LayoutInflater.from(
                        context).inflate(R.layout.item_beauty, parent,
                        false));
                break;
            case TYPE_FOOTER:
            default:
                holder = new FooterViewHolder(LayoutInflater.from(
                        context).inflate(R.layout.item_footer, parent,
                        false));
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        //这时候 article是 null，先把 footer 处理了
        if (holder instanceof FooterViewHolder) {
//            ((FooterViewHolder) holder).rcvLoadMore.spin();
            return;
        }else if (holder instanceof MyViewHolder){
        int width=DisplayUtils.getDisplayMetrics(context).widthPixels/2;
        String url=SystemPath.imgUrl+datas.get(position).getImg()
//                +"_"+ width+"x"+width
                ;
        Log.i("loading",url);
            if (TestFragment.isLoadImg)
        Picasso
                .with(context)
                .load(url)
                .error(R.drawable.beauty_loading)
                .placeholder(R.drawable.beauty_loading)
                .config(Bitmap.Config.RGB_565)
                .into(((MyViewHolder)holder).iv, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.i("loading","onSuccess");
                    }

                    @Override
                    public void onError() {
                        Log.i("loading","onError");
                    }
                });
        String title=datas.get(position).getTitle();
        String count="(共"+Integer.toString(datas.get(position).getSize())+"张)";

        ForegroundColorSpan greenSpan = new ForegroundColorSpan(Color.GREEN);
        ForegroundColorSpan yellowSpan = new ForegroundColorSpan(Color.RED);
        SpannableStringBuilder builder = new SpannableStringBuilder(title+count);
        builder.setSpan(greenSpan, 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(yellowSpan, title.length(), (title+count).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(new RelativeSizeSpan(0.7f), title.length(), (title+count).length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            ((MyViewHolder)holder).tv_title.setText(builder);}
    }

    @Override
    public int getItemCount()
    {
        return datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        ImageView iv;
        TextView tv_title;

        public MyViewHolder(View view)
        {
            super(view);
            iv = (ImageView) view.findViewById(R.id.imv);
            tv_title= (TextView) view.findViewById(R.id.tv_title);
            iv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener!=null){
                itemClickListener.onItemClick(v,getAdapterPosition());
            }
        }
    }

    /**
     * 底部加载更多
     */
    class FooterViewHolder extends RecyclerView.ViewHolder {

        ProgressBar pgb_load_more;

        public FooterViewHolder(View itemView) {
            super(itemView);
            pgb_load_more= (ProgressBar) itemView.findViewById(R.id.pg_load_more);
        }
    }

}
