package com.baidu.BaiduMap.Widget.Music;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadata;
import android.os.Handler;
import android.os.Message;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.ColorUtils;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.BaiduMap.R;
import com.baidu.BaiduMap.Widget.FloatView.WidgetRight;
import com.baidu.BaiduMap.carlifeapplauncher.adapter.Common;
import com.baidu.BaiduMap.carlifeapplauncher.adapter.FakeStart;
import com.baidu.BaiduMap.Widget.FloatView.NavBar;
import com.baidu.BaiduMap.databinding.OverlayMusicBinding;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicUI implements MusicUIInterface {

    private MusicService musicService;
    private static String TAG = "MusicUI";
    private String pkgName = null;
    private boolean show_album;
    private Context context;

    private boolean adaptive;

    private static OverlayMusicBinding binding;
    private MediaMetadataCompat mMetadata;
    private static Timer timer = null;

    private static int prog = 0;
    @SuppressLint("HandlerLeak")
    private static final Handler mHandler = new Handler() {
        // 接收到消息后处理
        public void handleMessage(Message msg) {
            prog += 500;
            binding.musicProgress.setProgress(prog);
            super.handleMessage(msg);
        }
    };

    static class MyTask extends TimerTask {
        public void run() {
            Message message = new Message();
//            if (r) {
//                message.what = 1;
//            } else {
//                message.what = 0;
//            }
            mHandler.sendMessage(message);
        }
    }

    public MusicUI(Context context, LayoutInflater layoutInflater, ViewGroup root) {

        boolean haspermission = NotificationListener.isEnabled(context);

        this.adaptive = false;
        this.context = context;
        this.show_album = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("showalbum", true);
        Log.i(TAG, "MusicUI 实例化");

        binding = OverlayMusicBinding.inflate(layoutInflater);
        root.addView(binding.getRoot());

        if (haspermission) {
            binding.musk.setVisibility(View.GONE);
            musicService = MusicService.getInstance(context.getApplicationContext());
            musicService.setMusicUIInterface(this);


            binding.LastMusic.setOnClickListener(view -> musicService.previous());
            binding.LastMusic.setFocusable(false);

            binding.NextMusic.setOnClickListener(view -> musicService.next());
            binding.NextMusic.setFocusable(false);

            binding.Play.setFocusable(false);


            binding.SwitchSource.setAlpha(0.f);
            binding.SwitchSource.setOnClickListener(view -> {
                //view
                View pop_playerlist_view = View.inflate(context, R.layout.playerpopwindows, null);
                Common.setBgRadius(pop_playerlist_view, 50);


                pop_playerlist_view.setBackgroundColor(context.getColor(R.color.normal_panel));
                RecyclerView plyerlist_rec = pop_playerlist_view.findViewById(R.id.playerlist_rec);
                ((TextView) pop_playerlist_view.findViewById(R.id.textView2)).setTextColor(context.getColor(R.color.text_color));

                //LayoutManager
                LinearLayoutManager lm = new LinearLayoutManager(context);
                lm.setOrientation(LinearLayoutManager.HORIZONTAL);
                plyerlist_rec.setLayoutManager(lm);

                //Set Adapter
                MediaAppListAdapter maa = new MediaAppListAdapter(context);
                plyerlist_rec.setAdapter(maa);

                //Set PopWindows
                PopupWindow playerlist = new PopupWindow(pop_playerlist_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                playerlist.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                playerlist.setHeight(playerlist.getContentView().getMeasuredHeight());
                playerlist.setOutsideTouchable(true);
//                playerlist.setBackgroundDrawable(new ColorDrawable(Color.argb(255, 0, 0, 0)));

                playerlist.showAsDropDown(view);
                maa.linkpop(playerlist);
            });
            binding.SwitchSource.setFocusable(false);

            binding.musicCard.setOnClickListener(v -> {
                if (pkgName != null) {
                    try {
                        NavBar.setRightInvisible();
                        FakeStart.Start(context, pkgName);
                    } catch (Exception e) {

                    }
                } else {
                    try {
                        NavBar.setRightInvisible();
                        FakeStart.Start(context, "com.sec.android.app.music");
                    } catch (Exception e) {

                    }
                }
            });
            binding.musicCard.setFocusable(false);


        } else {
            binding.musk.setVisibility(View.VISIBLE);
            //binding.Pause.setVisibility(View.GONE);
        }


        int musk = context.getResources().getColor(R.color.Album_musk, context.getTheme());
        int red = (musk & 0xff0000) >> 16;
        int green = (musk & 0x00ff00) >> 8;
        int blue = (musk & 0x0000ff);
        int alpha = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("album_musk_alpha", "80"));
        binding.musicmusk.setBackgroundColor(Color.argb(alpha, red, green, blue));
        binding.musicmusk.setAlpha(0.f);
//Color.argb()

//        binding.musicplayerwindows.setVisibility(View.GONE);
        Log.i(TAG, "MusicUI onCreate 完成");

    }

    public void onDestroy() {

//        if (musicService != null) {
//            Log.i(TAG, "MusicService try_disconnect_MediaBrowser 完成");
//            musicService.try_disconnect_MediaBrowser();
//        }

        if (musicService != null) {
            musicService.pause();
            musicService.removeMusicUIInterface();
        }

        Log.i(TAG, "MusicUI onDestroy 完成");
    }


    @Override
    public void onAppNameChanged(String appName) {
        Log.i(TAG, "onAppNameChanged: " + appName);
//        Log.i(TAG, "pkg:"+ pkgName);

        binding.MusicSessionName.setText(appName);
//        binding.appIconMain.set
    }

    @Override
    public void onPackageNameChanged(String pkgName) {
        Log.i(TAG, "onPackageNameChanged : " + pkgName);
        this.pkgName = pkgName;


//
//        AppInfo info = Common.getInstance(context).loadAppInfo(pkgName);
//        if (info.icon != null) {
//            binding.appIconMain.setImageDrawable(info.icon);
//            MainActivityFinal.setBgRadiusOval(binding.appIconMain);
//            binding.appIconMainFrame.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    if (is_Browser_Init) {
////                        binding.adaptivewindows.setVisibility(View.VISIBLE);
////                        return;
////                    }
////                    if (musicService != null) {
////                        if (musicService.try_connect_MediaBrowser()) {
////                            Toast.makeText(context, "连接中", Toast.LENGTH_SHORT).show();
////                        } else {
////                            Toast.makeText(context, "不支持该播放器适配服务", Toast.LENGTH_SHORT).show();
////                        }
////                    }
//                }
//            });
//        }

    }


    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {
        Log.i(TAG, "onMetadataChanged");
        mMetadata = metadata;
        binding.musicProgress.setMax((int) mMetadata.getLong(MediaMetadata.METADATA_KEY_DURATION));
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.MusicName.setText(metadata.getString(MediaMetadata.METADATA_KEY_TITLE));
                binding.MusicArtist.setText(metadata.getString(MediaMetadata.METADATA_KEY_ARTIST));
//                binding.lyrics.setText(metadata.getString("lyrics"));

                if (show_album && metadata.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART) != null) {
                    binding.musicmusk.setVisibility(View.VISIBLE);
                    binding.AlbumImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    if(isLightColored(metadata.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART))){
                        binding.MusicName.setTextColor(context.getColor(R.color.music_name_dark));
                        binding.MusicArtist.setTextColor(context.getColor(R.color.music_name_dark));
                    }else {
                        binding.MusicName.setTextColor(context.getColor(R.color.music_player_text));
                        binding.MusicArtist.setTextColor(context.getColor(R.color.music_player_text));
                    }
                    binding.AlbumImage.setImageBitmap(addShadow(metadata.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART)));
                } else {
                    binding.AlbumImage.setImageBitmap(null);
                    binding.musicmusk.setVisibility(View.GONE);
                }
            }
        });
    }

    public Bitmap addShadow(Bitmap bm) {
        int[] mBackShadowColors = new int[] { 0xA0000000 , 0x00000000};
        GradientDrawable mBackShadowDrawableLR = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP, mBackShadowColors);
        mBackShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mBackShadowDrawableLR.setBounds(0, bm.getHeight()/2, bm.getWidth() , bm.getHeight());
        Canvas canvas = new Canvas(bm);
        mBackShadowDrawableLR.draw(canvas);
        return bm;
    }


    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat state) {
        Log.i(TAG, "onPlaybackStateChanged");
        binding.musicProgress.setProgress((int) state.getPosition());
        prog = (int) state.getPosition();
        if (state.getState() == PlaybackStateCompat.STATE_PLAYING || state.getState() == PlaybackStateCompat.STATE_BUFFERING) {
            if (mMetadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION) != 0) {
                binding.Play.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.pause_fill_ios));
                binding.Play.setOnClickListener(view -> musicService.pause());
                if (timer == null) {
                    timer = new Timer(true);
                    timer.schedule(new MyTask(), 0, 500);
                }
            } else {
                binding.Play.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.play_fill_ios));
                binding.Play.setOnClickListener(view -> musicService.play());
                if (timer != null) {
                    timer.cancel();
                    timer.purge();
                    timer = null;
                }
            }
        } else if (state.getState() == PlaybackStateCompat.STATE_PAUSED || state.getState() == PlaybackStateCompat.STATE_STOPPED || state.getState() == PlaybackStateCompat.STATE_NONE) {
            binding.Play.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.play_fill_ios));
            binding.Play.setOnClickListener(view -> musicService.play());
            if (timer != null) {
                timer.cancel();
                timer.purge();
                timer = null;
            }
        }
        /*switch (state.getState()) {
            case PlaybackStateCompat.STATE_PLAYING:
                timer = new Timer(true);
                timer.schedule(new MyTask(), 0, 1000);
            case PlaybackStateCompat.STATE_BUFFERING:
                binding.Play.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.pause_fill_ios));
                binding.Play.setOnClickListener(view -> musicService.pause());
                //break;
            case PlaybackStateCompat.STATE_NONE:
            case PlaybackStateCompat.STATE_PAUSED:
                if(timer!=null) {
                    timer.cancel();
                    timer.purge();
                    timer = null;
                }
            case PlaybackStateCompat.STATE_STOPPED:
                binding.Play.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.play_fill_ios));
                binding.Play.setOnClickListener(view -> musicService.play());
                if(timer!=null) {
                    timer.cancel();
                    timer.purge();
                    timer = null;
                }
                break;
            default:
        }*/
    }
    public boolean isLightColored(Bitmap bitmap) {
        // 计算图片的平均颜色
        int red = 0, green = 0, blue = 0;
        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                int color = bitmap.getPixel(x, y);
                red += Color.red(color);
                green += Color.green(color);
                blue += Color.blue(color);
            }
        }

        int total = bitmap.getWidth() * bitmap.getHeight();
        red /= total;
        green /= total;
        blue /= total;

        // 计算图片亮度
        double brightness = ColorUtils.calculateLuminance(Color.rgb(red, green, blue));

        // 如果图片的平均颜色的亮度大于0.5，则认为是浅色
        return brightness > 0.95;
    }


//    private boolean is_Browser_Init = false;
//    private MusicListAdapter adaper;
//
//    @Override
//    public void recerive_MediaItems(List<MediaBrowserCompat.MediaItem> items, boolean isRoot) {
//
//        if (isRoot) {
//            for (MediaBrowserCompat.MediaItem item : items) {
//                LinearLayout frame = (LinearLayout) View.inflate(context, R.layout.music_root_item, null);
//                frame.setVerticalGravity(Gravity.CENTER);
//                TextView i = frame.findViewById(R.id.item_title);
//                i.setText(item.getDescription().getTitle().toString());
//                ImageView img = frame.findViewById(R.id.item_icon);
//                img.setImageBitmap(item.getDescription().getIconBitmap());
//                frame.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        musicService.request_MenuItems(item.getMediaId());
//                    }
//                });
//
//                frame.setLayoutParams(new LinearLayout.LayoutParams(-1, ViewGroup.LayoutParams.MATCH_PARENT, 1));
//                binding.rootmenuframe.addView(frame);
//            }
//
//            AppInfo info = Common.getInstance(context).loadAppInfo(pkgName);
//            if (info.icon != null) {
//                binding.appIcon.setImageDrawable(info.icon);
//                MainActivityFinal.setBgRadiusOval(binding.appIcon);
//            }
////            binding.appIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            binding.appIcon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    binding.adaptivewindows.setVisibility(View.GONE);
//                }
//            });
//
//            adaper = new MusicListAdapter(context,musicService);
//            binding.itemListview.setAdapter(adaper);
//            binding.itemListview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
//            binding.adaptivewindows.setVisibility(View.VISIBLE);
//            is_Browser_Init = true;
//        } else {
//            if (adaper != null) {
//                adaper.setItems(items);
//            }
//        }
//    }

}
