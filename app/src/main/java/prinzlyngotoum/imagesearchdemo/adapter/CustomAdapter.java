package prinzlyngotoum.imagesearchdemo.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import prinzlyngotoum.imagesearchdemo.R;
import prinzlyngotoum.imagesearchdemo.utils.ImageLoader;

/**
 * Created by prinzlyngotoum on 11/9/14.
 */
public class CustomAdapter extends BaseAdapter {

    private Activity mActivity;
    public ArrayList<String> mListImages;
    private static LayoutInflater mInflater=null;
    public ImageLoader mImageLoader;

    public CustomAdapter(Activity a, ArrayList<String> listImages) {
        mActivity = a;
        this.mListImages = listImages;
        mInflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageLoader=new ImageLoader(mActivity.getApplicationContext());
    }

    public int getCount() {
        return mListImages.size();
    }

    public Object getItem(int position) {
        //return position;
        return mListImages.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public ArrayList<String> getListImages() {
        return mListImages;
    }

    //View Holder to optimize the GridView.

    public static class ViewHolder{
        public ImageView mImageView;

        public ViewHolder(View v){
            mImageView=(ImageView)v.findViewById(R.id.itemImage);
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        ViewHolder holder;
        if(vi==null){
            vi = mInflater.inflate(R.layout.gridview_item, null);
            holder=new ViewHolder(vi);
            vi.setTag(holder);
        }
        else
            holder=(ViewHolder)vi.getTag();
        String imageURL=mListImages.get(position);
        holder.mImageView.setTag(imageURL);
        mImageLoader.DisplayImage(imageURL,holder.mImageView);
        return vi;
    }
}
