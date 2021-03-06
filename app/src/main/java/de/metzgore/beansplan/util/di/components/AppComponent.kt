package de.metzgore.beansplan.util.di.components

import dagger.Component
import de.metzgore.beansplan.BeansPlanApp
import de.metzgore.beansplan.util.di.modules.*
import javax.inject.Singleton

@Singleton
@Component(modules = [(ClockModule::class), (RepositoryModule::class), (ScheduleDaoModule::class),
    (ContextModule::class), (MainActivityComponent::class), (WeeklyScheduleFragmentComponent::class),
    (DailyScheduleFragmentComponent::class), (RemindersFragmentComponent::class),
    (ReminderDeletionDialogFragmentComponent::class), (ReminderTimePickerDialogFragmentComponent::class),
    (ReminderDeletionOrUpdateDialogFragmentComponent::class), (NotificationPublisherComponent::class),
    (ReminderBootReceiverComponent::class), (SettingsModule::class), (SettingsFragmentComponent::class),
    (SettingsActivityComponent::class)])
interface AppComponent {
    fun inject(app: BeansPlanApp)
}
