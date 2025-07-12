package com.hantash.notemark.di.app

import dagger.hilt.migration.AliasOf
import javax.inject.Scope
import javax.inject.Singleton

/*
* Renaming the Singleton class into AppScope
* */

@Scope
@AliasOf(Singleton::class)
annotation class AppScope()
