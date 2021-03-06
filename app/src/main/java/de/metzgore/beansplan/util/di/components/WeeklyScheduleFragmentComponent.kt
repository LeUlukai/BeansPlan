package de.metzgore.beansplan.util.di.components

import android.support.v4.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import de.metzgore.beansplan.weeklyschedule.WeeklyScheduleFragment

@Module(subcomponents = [(WeeklyScheduleFragmentSubComponent::class)])
abstract class WeeklyScheduleFragmentComponent {

    @Binds
    @IntoMap
    @FragmentKey(WeeklyScheduleFragment::class)
    abstract fun bindWeeklyScheduleFragment(builder: WeeklyScheduleFragmentSubComponent.Builder): AndroidInjector.Factory<out Fragment>

}
