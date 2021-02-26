package vip.ablog.confession.utils

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Vibrator

class VibratorUtil {

    companion object {
        lateinit var vibrator: Vibrator;
        /**
         * 初始化震动
         * @param context
         */
        fun initViarbtor(context: Context) {
            if (context == null) return;
            vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator;
            var filter = IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            vibrator.vibrate(
                longArrayOf(0, 20),
                -1
            );    //等待3s,震动0.1s,等待0.1s,震动 1S;0表示一直震动

        }

        fun onStop() {
            vibrator.cancel()
        }
    }

}