package com.liu.handbeauty.activity;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.handbeauty.R;
import com.liu.handbeauty.adapter.ImagesAdapter;
import com.liu.handbeauty.bean.Pictures;
import com.liu.handbeauty.system.SystemPath;
import com.liu.handbeauty.utils.FileUtils;
import com.liu.handbeauty.utils.LogUtils;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片详情页
 * Created by Liu on 2016-06-10.
 */
public class ImagesDisplayActivity extends Activity {
    private Pictures datas;
    private ViewPager vp_imgs;
    private TextView tv_imgs_title;

    private ImagesAdapter myAdapter;
    private static int ERROR = 0;
    private static int CONPLETE = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 1:
                    if (msg.obj != null) {
                        CropImage.activity((Uri) msg.obj)
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(ImagesDisplayActivity.this);
                    } else {
                        Toast.makeText(getApplicationContext(), "解析出错", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imgsdisplay);
        vp_imgs = (ViewPager) findViewById(R.id.vp_imgs);
        tv_imgs_title = (TextView) findViewById(R.id.tv_imgs_title);
        datas = getIntent().getParcelableExtra("data");

        tv_imgs_title.setText(datas.getTitle() + "(1/" + datas.getSize() + ")");
        myAdapter = new ImagesAdapter(this, datas);
        vp_imgs.setAdapter(myAdapter);

        vp_imgs.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_imgs_title.setText(datas.getTitle() + "(" + (position + 1) + "/" + datas.getSize() + ")");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        myAdapter.setOnSetClickListener(new ImagesAdapter.OnSetClickListener() {
            @Override
            public void onClick(final String url) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Bitmap bitmap = Picasso.with(getApplicationContext()).load(SystemPath.imgUrl + url).get();
                            Message msg = Message.obtain();

                            Uri uri = saveBitmap(FileUtils.getFileName(url), bitmap);
                            if (null!=uri) {
                                msg.arg1 = CONPLETE;
                                msg.obj =uri;
                            } else {
                                msg.arg1 = ERROR;
                            }
                            mHandler.sendMessage(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    /**
     * 保存方法
     */
    public Uri saveBitmap(String picName, Bitmap bm) {
        File f = new File(SystemPath.download, picName);

        if (!f.exists()) {
            f.mkdirs();
        }
        f.delete();
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        Uri uri= null;
        try {
            uri = Uri.parse(
                    MediaStore.Images.Media.insertImage(getContentResolver(), f.getAbsolutePath(), "title", "description")
            );
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f.getParentFile())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        LogUtils.i("下载成功");
        return uri;
    }

    public Uri getUriFromPath(String picPath) {
        Uri mUri = Uri.parse("content://media/external/images/media");
        Uri mImageUri = null;
        Cursor cursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String data = cursor.getString(cursor
                    .getColumnIndex(MediaStore.MediaColumns.DATA));
            if (picPath.equals(data)) {
                int ringtoneID = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.MediaColumns._ID));
                mImageUri = Uri.withAppendedPath(mUri, "" + ringtoneID);
                break;
            }
            cursor.moveToNext();
        }
        return mImageUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                setWallpaper(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                error.printStackTrace();
            }
        }
    }

    public void setWallpaper(Uri uri) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Resources res = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(res,
//                getResources().getIdentifier("wallpaper" + imagePosition, "drawable", "com.ch"));
        Bitmap bitmap= null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            wallpaperManager.setBitmap(bitmap);
            Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "设置失败", Toast.LENGTH_SHORT).show();
        }

    }
}
