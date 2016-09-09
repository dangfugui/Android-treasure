package com.uvchip.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.uvchip.files.FileManager.ViewMode;
import com.uvchip.mediacenter.MApplication;
import com.dang.treasure.R;
import com.uvchip.utils.Helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FilesAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private FileItemSet mdata;
    private Context mContext;
    ViewHolder holder = null;
    private ViewMode mViewMode;
    public void setViewMode(ViewMode mode){
    	mViewMode = mode;
    }
    
    public FilesAdapter(Context context, FileItemSet data){
        super();
        this.mContext = context;
        this.mdata = data;
        mInflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public int getCount() {
        return mdata.getFileItems().size();
    }

    @Override
    public Object getItem(int position) {
        return mdata.getFileItems().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(mViewMode != FileManager.ViewMode.GRIDVIEW){
            return getListViewItem(position, convertView);
        }else{
            return getGridViewItem(position, convertView);
        }
    }
    private View getListViewItem(int position, View convertView){
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_view_item_file, null);
            holder.img_head = (ImageView)convertView.findViewById(R.id.paraList_imgHead);
            holder.title = (TextView)convertView.findViewById(R.id.paraList_title);
            holder.data=(TextView)convertView.findViewById(R.id.paraList_data);
            holder.info = (TextView)convertView.findViewById(R.id.paraList_updatetime);
            convertView.setTag(holder);
            
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        FileItemForOperation file = mdata.getFileItems().get(position);
        FileItem fileItem = file.getFileItem();
        setImageView(holder.img_head,fileItem);
        String displayName = fileItem.getFileName();
        holder.title.setText(displayName);
        holder.title.setPadding(10, 0, 0, 0);
        String mydate="";
        File tmpFile = new File(fileItem.getFilePath());
        long lastModifyDate = tmpFile.lastModified();
        if(lastModifyDate != 0){ Date date = new Date(lastModifyDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("yyyy-MM-dd HH:mm");
        mydate= dateFormat.format(date);
        }
        long size= fileItem.getFileSize();
        if(size!=-1){
        	size=size/1024;
        	 mydate=mydate+"  "+size+"KB";
        }
        holder.data.setText(mydate);
        holder.info.setText(Helper.formatFromSize(fileItem.getFileSize()));
 
        switch (file.getSelectState()) {
            case FileItemForOperation.SELECT_STATE_CUT:
                holder.title.setTextAppearance(mContext, R.style.tvInListViewCut);
                break;
            case FileItemForOperation.SELECT_STATE_NOR:
                holder.title.setTextAppearance(mContext, R.style.tvInListView);
                break;
            case FileItemForOperation.SELECT_STATE_SEL:
                holder.title.setTextAppearance(mContext, R.style.tvInListViewSelected);
                break;
            default:
                break;
        }
        return convertView;
    }
    private View getGridViewItem(int position, View convertView){
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.grid_view_item, null);
            holder.img_head = (ImageView)convertView.findViewById(R.id.ivOfGVItem);
            holder.title = (TextView)convertView.findViewById(R.id.tvOfGVItem);
            convertView.setTag(holder);
            
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        FileItemForOperation file = mdata.getFileItems().get(position);
        FileItem fileItem = file.getFileItem();
        setImageView(holder.img_head,fileItem);
        String displayName = fileItem.getFileName();
        holder.title.setText(displayName);
        
        switch (file.getSelectState()) {
            case FileItemForOperation.SELECT_STATE_CUT:
                holder.title.setTextAppearance(mContext, R.style.tvInGridViewCut);
                break;
            case FileItemForOperation.SELECT_STATE_NOR:
                holder.title.setTextAppearance(mContext, R.style.tvInGridView);
                break;
            case FileItemForOperation.SELECT_STATE_SEL:
                holder.title.setTextAppearance(mContext, R.style.tvInGridViewSelected);
                break;
            default:
                break;
        }
        return convertView;
    }
    
    private void setImageView(ImageView iv,FileItem fileItem){
    	if (fileItem.getIcon() != null) {
			iv.setImageBitmap(fileItem.getIcon());
			return;
		}
    	int iconId = fileItem.getIconId();
        if(iconId > 0){
            iv.setImageResource(iconId);
        }
		switch (iconId) {
		case R.drawable.app_default_icon:
			new AsyncLoadApkicon().execute(fileItem);
			break;
		case R.drawable.picture:
			new AsyncLoadImage().execute(fileItem);
			break;
		case R.drawable.videofile:
			break;
		default:
			break;
		}
    }
    
    private final class ViewHolder{
        public ImageView img_head;
        public TextView title;
        public TextView data;
        public TextView info;
    }
    
    class AsyncLoadImage extends AsyncTask<FileItem, Void, Object> {
        @SuppressLint("NewApi")
		@Override
        protected Object doInBackground(FileItem... params) {
            String path = MApplication.CACHE_PATH;
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            } else {
            	Bitmap bitmap;
                Bitmap newBitmap;
            	FileItem item = params[0];
            	File thumbFile = new File(path + item.getFileName().replace(".", ""));
            	if (thumbFile.exists()) {
					newBitmap = BitmapFactory.decodeFile(thumbFile.getAbsolutePath());
					item.setIcon(newBitmap);
					publishProgress();
				}
            	else {
            		try {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 14;
                        bitmap = BitmapFactory.decodeFile(item.getFilePath(), options);
                        newBitmap = ThumbnailUtils.extractThumbnail(bitmap, 64,64);
                        bitmap.recycle();
                        if (newBitmap != null) {
                        	item.setIcon(newBitmap);
                        	thumbFile.createNewFile();
                        	OutputStream out = new FileOutputStream(thumbFile);
                        	newBitmap.compress(CompressFormat.JPEG, 26, out);
                            publishProgress();
                            Thread.sleep(200);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
				}
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... value) {
        	FilesAdapter.this.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Object result) {
        	
        }
    }
    class AsyncLoadApkicon extends AsyncTask<FileItem, Void, Object> {
        @Override
        protected Object doInBackground(FileItem... params) {
            String path = MApplication.CACHE_PATH;
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            } else {
                Bitmap bm;
            	FileItem item = params[0];
            	File thumbFile = new File(path + item.getFileName().replace(".", ""));
            	if (thumbFile.exists()) {
            		bm = BitmapFactory.decodeFile(thumbFile.getAbsolutePath());
					item.setIcon(bm);
					publishProgress();
				}
            	else {
            		try {
            			Drawable dw = Helper.showUninstallAPKIcon(mContext, item.getFilePath());
                		if(dw!=null){
                			BitmapDrawable bd = (BitmapDrawable)dw;
                			bm = bd.getBitmap();
                			item.setIcon(bm);
                			publishProgress();
                			Thread.sleep(200);
                		}
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
				}
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... value) {
        	FilesAdapter.this.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Object result) {
        	
        }
    }
}
