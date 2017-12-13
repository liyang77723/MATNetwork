package cn.meiauto.matnetwork.sample;

import android.content.Context;
import android.util.Log;

import cn.meiauto.matnetwork.download.DownloadStatus;
import cn.meiauto.matnetwork.download.NotifyNotifyClickReceiver;
import cn.meiauto.matutils.LogUtil;

/**
 * <pre>
 *  author : LiYang
 *  email  : yang.li@meiauto.cn
 *  time   : 2017-08-24
 */
public class NotificationNotifyClickReceiver extends NotifyNotifyClickReceiver {
    @Override
    public void onClick(Context context, int taskId, @DownloadStatus int status) {
        super.onClick(context, taskId, status);
    }

}
