package com.baidu.BaiduMap.RecyclerAdapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.BaiduMap.ConstantData.CarLinkData;
import com.baidu.BaiduMap.R;
import com.baidu.BaiduMap.Bean.AppInfo;
import com.baidu.BaiduMap.Utils.ApplicationUtil;
import com.baidu.BaiduMap.carlifeapplauncher.adapter.Common;
import com.baidu.BaiduMap.carlifeapplauncher.adapter.FakeStart;

import java.util.ArrayList;
import java.util.List;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

public class FullAppListAdapter extends RecyclerView.Adapter<FullAppListAdapter.ViewHolder> {

    private List<String> favoList;
    private List<AppInfo> appsList;
    private Context c;
    private SharedPreferences sharedPreferences;
    private int launcher_icon_size;
    private int launcher_font_size;
    private boolean showlabel;

    public FullAppListAdapter(Context c) {
        this.c = c;
        this.appsList = new ArrayList<AppInfo>();
        this.favoList = CarLinkData.getArrayList(c,CarLinkData.sp_launcher_apps);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        this.launcher_icon_size = CarLinkData.getIntFromString(c,CarLinkData.sp_launcher_icon_size,120);
        this.launcher_font_size = 30;

            try {
                appsList.addAll(ApplicationUtil.getLauncherInfoList(c));
            } catch (Exception e) {
            }


        showlabel = CarLinkData.getBoolean(c,CarLinkData.sp_launcher_name, false);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        public ImageView img;
        public View itemView;
        public View.OnClickListener vocl;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
            img = (ImageView) itemView.findViewById(R.id.img);
            vocl = this;
            this.itemView = itemView;
            itemView.setFocusable(false);
            itemView.setOnClickListener(vocl);
            itemView.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View view, DragEvent dragEvent) {
                    switch (dragEvent.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                                view.invalidate();
                                return true;
                            }
                            return false;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            return true;
                        case DragEvent.ACTION_DRAG_LOCATION:
                            return true;
                        case DragEvent.ACTION_DRAG_EXITED:
                            return true;
                        case DragEvent.ACTION_DROP:
                            ClipData.Item item = dragEvent.getClipData().getItemAt(0);
                            CharSequence dragData = item.getText();
                            switchLocation(Integer.valueOf(dragData.toString()), getAdapterPosition());
                            return true;

                        case DragEvent.ACTION_DRAG_ENDED:
                            return true;
                        default:
                            break;
                    }
                    return false;
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipData.Item item = new ClipData.Item(getAdapterPosition() + "");
                    ClipData dragData = new ClipData(getAdapterPosition() + "",
                            new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                            item);
                    View.DragShadowBuilder myShadow = new MyDragShadowBuilder(itemView);
                    view.startDragAndDrop(dragData,  // The data to be dragged
                            myShadow,  // The drag shadow builder
                            null,      // No need to use local data
                            0          // Flags (not currently used, set to 0)
                    );
                    return true;
                }
            });


        }

        @SuppressLint("WrongConstant")
        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Context context = v.getContext();
            FakeStart.StartUsingDeepLink(context, appsList.get(pos).packageName.toString(), null);
//            if (mode == FullAppListAdapterMode.mode_deeplink) {
//
//            } else {
//                String minimap = PreferenceManager.getDefaultSharedPreferences(context).getString("oneui_shortcut_xc", "false");
//                if (minimap.equals(appsList.get(pos).packageName.toString())) {
//                    FakeStart.StartMiniMapToContext(context);
//                } else {
//                    FakeStart.Start(context, appsList.get(pos).packageName.toString());
//                }
//            }
        }
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(FullAppListAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
        String appLabel = appsList.get(i).label.toString().replace("Samsung ", "");
        String appPackage = appsList.get(i).packageName.toString();
        Drawable appIcon = appsList.get(i).icon;
        TextView textView = viewHolder.textView;
        if (showlabel) {
            textView.setText(appLabel, TextView.BufferType.EDITABLE);
            Spannable sp = (Spannable) textView.getText();
            sp.setSpan(new AbsoluteSizeSpan(launcher_font_size), 0, textView.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //设置字体大小
            //sp.setSpan(new ForegroundColorSpan(c.getColor(R.color.music_player_text)), 0, textView.getText().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            textView.setText(sp);

        } else {
            textView.setVisibility(View.GONE);
        }

        ImageView imageView = viewHolder.img;

        imageView.setImageDrawable(appIcon);
        LinearLayout.LayoutParams imglp = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        imglp.height = launcher_icon_size;
        imglp.width = launcher_icon_size;
        imageView.setLayoutParams(imglp);
        Common.setBgRadiusWithCutOut(imageView, (int) (launcher_icon_size * 0.05), (int) (launcher_icon_size * 0.35));


                viewHolder.itemView.setOnClickListener(viewHolder.vocl);

    }

    @Override
    public int getItemCount() {
        return appsList.size();
    }


    @Override
    public FullAppListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    private void switchLocation(int targetA, int targetB) {
        Common.switchLocation(c, targetA, targetB);
        favoList = Common.app_list(c);
        appsList = new ArrayList<AppInfo>();
        PackageManager pm = c.getPackageManager();
        for (String pkg : favoList) {
            AppInfo app = new AppInfo();
            try {
                ApplicationInfo temp_info = pm.getApplicationInfo(pkg, PackageManager.GET_META_DATA);
                app.label = pm.getApplicationLabel(temp_info);
                app.packageName = pkg;
                app.icon = pm.getApplicationIcon(temp_info);
                appsList.add(app);
            } catch (Exception e) {
            }
        }
        notifyItemChanged(targetA);
        notifyItemChanged(targetB);
    }

    public static class MyDragShadowBuilder extends View.DragShadowBuilder {

        private Drawable shadow;
        private ImageView icon;

        public MyDragShadowBuilder(View v) {
            super(v);

            icon = (ImageView) v.findViewById(R.id.img);
            shadow = icon.getDrawable().getConstantState().newDrawable();
        }

        @Override
        public void onProvideShadowMetrics(Point size, Point touch) {
            int width, height;
            width = icon.getHeight();
            height = icon.getHeight();
            shadow.setBounds(0, 0, width, height);
            size.set(width, height);
            touch.set(width / 2, height / 2);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            shadow.draw(canvas);

        }
    }
}