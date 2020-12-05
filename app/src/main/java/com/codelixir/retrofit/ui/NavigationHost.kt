package com.codelixir.retrofit.ui

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

interface NavigationHost {
    fun getNavController():NavController
    fun navigateTo(directions: NavDirections, navOptions: NavOptions?=null){
        getNavController().navigate(directions, navOptions)
    }
    fun navigateTo(@IdRes resId:Int, args: Bundle?=null, navOptions: NavOptions?=null, navigatorExtras: Navigator.Extras?=null){
        getNavController().navigate(resId, args, navOptions, navigatorExtras)
    }
}