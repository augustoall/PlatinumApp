package br.com.app.platinumapp.appvendas.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.util.List;

import br.com.app.platinumapp.appvendas.Model.ImageLoaderProdutoBean;
import br.com.app.platinumapp.appvendas.R;


public class ImageLoaderAdapter extends BaseAdapter {

    private List<ImageLoaderProdutoBean> list;
    private LayoutInflater inflater;
    private ImageLoader mImageLoader;


    public ImageLoaderAdapter(Context context, List<ImageLoaderProdutoBean> list, ImageLoader mImageLoader) {
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mImageLoader = mImageLoader;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item, null);
            holder = new ViewHolder();
            convertView.setTag(holder);

            holder.ivPost = (ImageView) convertView.findViewById(R.id.ivPost);
            holder.tvPost = (TextView) convertView.findViewById(R.id.tvPost);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvPost.setText(list.get(position).getDescricao_produto());

        // IMAGE LOADER
        mImageLoader.displayImage(list.get(position).getUrl_image(),
                holder.ivPost,
                null,
                new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {


                    }

                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String uri, View view, int current, int total) {
                        Log.i("Script", "onProgressUpdate(" + uri + " : " + total + " : " + current + ")");
                    }
                });

        return convertView;

    }


    private class ViewHolder {
        public ImageView ivPost;
        public TextView tvPost;
    }
}
