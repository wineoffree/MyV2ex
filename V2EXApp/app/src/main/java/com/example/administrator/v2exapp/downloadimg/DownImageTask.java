package com.example.administrator.v2exapp.downloadimg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.example.administrator.v2exapp.save.CacheImage;
import com.example.administrator.v2exapp.save.SaveToFile;

/**
 * Created by Administrator on 2016/7/12.
 */
public class DownImageTask extends AsyncTask<String, Integer, Bitmap> {
    private String imageUrl;
    private ImageView imageView;
    private boolean ifAvoid;//判断是否需要防止错位

    private int index;
    public DownImageTask(ImageView imageView,boolean ifAvoid,int index) {
        this.imageView = imageView;
        this.ifAvoid=ifAvoid;

        this.index=index;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        imageUrl = params[0];
        //通过缓存查找
        Bitmap bitmap=CacheImage.getBitmap(imageUrl.replaceAll("/","").replace(".png?m=","").replace("_",""));;
        try {
            if(bitmap!=null){
                return bitmap;
            }
            else {
                //通过文件查找
                bitmap= BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/myv2ex/"+
                        imageUrl.replaceAll("/","").replace(".png?m=","").replace("_","")+".png");
                if(bitmap!=null){
                    return  bitmap;
                }
                else {
                    //通过网络下载
                    DownImage downImage=new DownImage(imageUrl);
                    bitmap=downImage.loadImage();
                }
            }

        }
        catch (Exception e){e.printStackTrace();
        }

        return bitmap;
    }



    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);

        if (result != null) {

            imageView.setImageBitmap(result);

            try {
                //图片缓存
                CacheImage.putBitmap(imageUrl.replaceAll("/","").replace(".png?m=","").replace("_",""),result);
                //图片存入文件
                SaveToFile saveToFile=new SaveToFile(index);
                saveToFile.setBitmap(result);
                saveToFile.setpath(imageUrl.replaceAll("/","").replace(".png?m=","").replace("_",""));
                saveToFile.saveBitmap();
                //图片缓存
                CacheImage.putBitmap(imageUrl.replaceAll("/","").replace(".png?m=","").replace("_",""),result);
            }
            catch (Exception e){e.printStackTrace();}
            Log.d("wiwiwi",result.toString());

        }}


}

