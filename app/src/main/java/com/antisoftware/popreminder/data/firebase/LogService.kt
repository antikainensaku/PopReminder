package com.antisoftware.popreminder.data.firebase

interface LogService {
  fun logNonFatalCrash(throwable: Throwable)
}
