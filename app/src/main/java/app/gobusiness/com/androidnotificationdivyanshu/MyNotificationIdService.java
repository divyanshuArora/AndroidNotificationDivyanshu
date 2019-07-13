package app.gobusiness.com.androidnotificationdivyanshu;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyNotificationIdService extends Service {

    public MyNotificationIdService() {
        //super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
