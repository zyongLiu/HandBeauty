package com.liu.handbeauty.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.liu.handbeauty.R;
import com.liu.handbeauty.activity.ImagesDisplayActivity;
import com.liu.handbeauty.adapter.HomeAdapter;
import com.liu.handbeauty.bean.Gallery;
import com.liu.handbeauty.bean.GalleryList;
import com.liu.handbeauty.bean.Pictures;
import com.liu.handbeauty.system.SystemPath;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public final class TestFragment extends Fragment {

    private static String KEY_CONTENT="ID";
    private List<Gallery> data;
    private View view;
    private RecyclerView recyclerView;
    private HomeAdapter mAdapter;
    private SwipeRefreshLayout swprefreshView;

    private boolean loading = false;


    public static TestFragment newInstance(String id) {
        TestFragment fragment = new TestFragment();
        fragment.ID=id;
        return fragment;
    }

    private String ID = "???";
    private int Page = 1;

    /**
     * 添加标志位
     */
    public static boolean isLoadImg=true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            ID = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_test,null);
        recyclerView= (RecyclerView) view.findViewById(R.id.id_recyclerview);
        swprefreshView= (SwipeRefreshLayout) view.findViewById(R.id.swp);
        swprefreshView.setColorSchemeColors(Color.RED,Color.BLUE);
        swprefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Page=1;
                getData();
            }
        });
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        swprefreshView.post(new Runnable() {
            @Override
            public void run() {
                swprefreshView.setRefreshing(true);
                Page=1;
                getData();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState){
                    case RecyclerView.SCROLL_STATE_IDLE:
                        isLoadImg=true;
                        mAdapter.notifyDataSetChanged();
                        break;
                    default:
                        isLoadImg=false;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                int totalItemCount = layoutManager.getItemCount();

                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (!loading && totalItemCount < (lastVisibleItem + 2)) {
                    Page++;
                    getData();
                    loading = true;
                }
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, ID);
    }


//    http://www.tngou.net/tnfs/api/list?id=1
//    http://www.tngou.net/tnfs/api/list?id=2&page=1
    public interface GalleryService {
        @GET("list")
        Call<GalleryList> listRepos(@Query("id") String id,@Query("page") String page);
    }

    //    http://www.tngou.net/tnfs/api/
    public void getData() {
        if (data != null && data.size() > 0) {
            data.add(null);

            // notifyItemInserted(int position)，这个方法是在第position位置
            // 被插入了一条数据的时候可以使用这个方法刷新，
            // 注意这个方法调用后会有插入的动画，这个动画可以使用默认的，也可以自己定义。
            mAdapter.notifyItemInserted(data.size() - 1);
        }


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SystemPath.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GalleryService service = retrofit.create(GalleryService.class);
        Call<GalleryList> repos = service.listRepos(ID,Page+"");
        repos.enqueue(new Callback<GalleryList>() {
            @Override
            public void onResponse(Call<GalleryList> call, Response<GalleryList> response) {
                Log.d("he",response.body().toString());

                if (loading){
                    List<Gallery> moreArticles=response.body().getTngou();
                    if (data.size() == 0) {
                        data.addAll(moreArticles);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        //删除 footer
                        data.remove(data.size() - 1);
                        data.addAll(moreArticles);
                        mAdapter.notifyDataSetChanged();
                        loading = false;
                    }
                }else {
                    swprefreshView.setRefreshing(false);
                    data=response.body().getTngou();
                    mAdapter=new HomeAdapter(getContext(),data);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            getPictures(data.get(position).getId());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GalleryList> call, Throwable t) {
                Log.d("he","加载失败");
            }
        });
    }

    /**
     * 获取图片列表并跳转
     * @param id
     * http://www.tngou.net/tnfs/api/show?id=717
     */
    private void getPictures(int id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SystemPath.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PicturesService service=retrofit.create(PicturesService.class);
        Call<Pictures> repos = service.listRepos(id+"");
        repos.enqueue(new Callback<Pictures>() {
            @Override
            public void onResponse(Call<Pictures> call, Response<Pictures> response) {
                Intent i=new Intent(getActivity(), ImagesDisplayActivity.class);
                i.putExtra("data",response.body());
                startActivity(i);
            }

            @Override
            public void onFailure(Call<Pictures> call, Throwable t) {
                Toast.makeText(getContext(),"加载详细失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface PicturesService {
        @GET("show")
        Call<Pictures> listRepos(@Query("id") String id);
    }
}
