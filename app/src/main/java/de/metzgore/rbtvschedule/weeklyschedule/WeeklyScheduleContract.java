package de.metzgore.rbtvschedule.weeklyschedule;

import de.metzgore.rbtvschedule.data.WeeklySchedule;

interface WeeklyScheduleContract {

    interface View {
        void showSchedule(WeeklySchedule body);

        void showRetrySnackbar(int messageId);

        void hideSnackbar();

        void showRefreshIndicator(boolean b);

        void showEmptyView(boolean visible);

        void showCurrentDay(int idxOfCurrentDay);

        void showToast(int error_message_no_day_found);
    }

    interface UserActionListener {
        void loadWeeklySchedule();

        void goToCurrentShow();
    }
}
