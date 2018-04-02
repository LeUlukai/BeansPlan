package de.metzgore.rbtvschedule.shared;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vincentbrison.openlibraries.android.dualcache.Builder;
import com.vincentbrison.openlibraries.android.dualcache.DualCache;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.inject.Singleton;

import de.metzgore.rbtvschedule.AppExecutors;
import de.metzgore.rbtvschedule.RBTVScheduleApp;
import de.metzgore.rbtvschedule.api.ApiResponse;
import de.metzgore.rbtvschedule.api.RBTVScheduleApi;
import de.metzgore.rbtvschedule.data.Resource;
import de.metzgore.rbtvschedule.data.Schedule;
import de.metzgore.rbtvschedule.data.WeeklySchedule;
import de.metzgore.rbtvschedule.util.GsonSerializer;
import de.metzgore.rbtvschedule.util.NetworkBoundResource;
import de.metzgore.rbtvschedule.util.di.Injector;

@Singleton
public class ScheduleRepository {

    private final String TAG = ScheduleRepository.class.getSimpleName();
    private final RBTVScheduleApi api;
    private final AppExecutors appExecutors;
    private DualCache<Schedule> scheduleCache;
    private DualCache<WeeklySchedule> weeklyScheduleCache;
    private MutableLiveData<Schedule> scheduleCacheData = new MutableLiveData<>();
    private MutableLiveData<WeeklySchedule> weeklyScheduleCacheData = new MutableLiveData<>();

    //TODO dagger
    //@Inject
    public ScheduleRepository(/*RBTVScheduleApi api, AppExecutors appExecutors*/) {
        api = Injector.provideRBTVScheduleApi();
        this.appExecutors = new AppExecutors();
        //TODO name
        scheduleCache = new Builder<Schedule>("test", 1)
                .enableLog()
                .useSerializerInRam(1000000, new GsonSerializer<>(Schedule.class))
                .useSerializerInDisk(1000000, true, new GsonSerializer<>(Schedule.class), RBTVScheduleApp.getAppContext())
                .build();

        weeklyScheduleCache = new Builder<WeeklySchedule>("test1", 1)
                .enableLog()
                .useSerializerInRam(1000000, new GsonSerializer<>(WeeklySchedule.class))
                .useSerializerInDisk(1000000, true, new GsonSerializer<>(WeeklySchedule.class), RBTVScheduleApp.getAppContext())
                .build();
    }

    public LiveData<Resource<Schedule>> loadScheduleOfToday(boolean forceRefresh) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return loadScheduleOfDay(forceRefresh, year, month, day);
    }

    public LiveData<Resource<Schedule>> loadScheduleOfDay(boolean forceRefresh, int year, int month, int day) {
        String formattedDay = String.format("%02d", day);
        String formattedMonth = String.format("%02d", month);

        return new NetworkBoundResource<Schedule, Schedule>(appExecutors, forceRefresh) {
            @Override
            protected void saveCallResult(@NonNull Schedule item) {
                //TODO key
                scheduleCache.put("schedule", item);
            }

            @Override
            protected boolean shouldFetch(@Nullable Schedule data) {
                return forceRefresh;
            }

            @NonNull
            @Override
            protected LiveData<Schedule> loadFromDb() {
                //TODO key
                scheduleCacheData.setValue(scheduleCache.get("schedule"));
                return scheduleCacheData;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Schedule>> createCall() {
                return api.scheduleOfDay(year, formattedMonth, formattedDay);
            }
        }.asLiveData();
    }

    public LiveData<Resource<WeeklySchedule>> loadWeeklySchedule(boolean forceRefresh) {
        return new NetworkBoundResource<WeeklySchedule, WeeklySchedule>(appExecutors, forceRefresh) {
            @Override
            protected void saveCallResult(@NonNull WeeklySchedule item) {
                //TODO key
                weeklyScheduleCache.put("weeklyschedule", item);
            }

            @Override
            protected boolean shouldFetch(@Nullable WeeklySchedule data) {
                return forceRefresh;
            }

            @NonNull
            @Override
            protected LiveData<WeeklySchedule> loadFromDb() {
                //TODO key
                weeklyScheduleCacheData.setValue(weeklyScheduleCache.get("weeklyschedule"));
                return weeklyScheduleCacheData;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<WeeklySchedule>> createCall() {
                return api.scheduleOfCurrentWeek();
            }
        }.asLiveData();
    }
}