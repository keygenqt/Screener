/*
 * Copyright 2020 Vitaliy Zarubin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keygenqt.screener.utils

import com.keygenqt.screener.base.ConnectDb
import com.keygenqt.screener.base.SnapDirs
import kotlin.system.exitProcess

const val APPLICATION_NAME = "Screener"
const val VERSION = "0.0.10"
const val INNER_APP_NAME = "screener"

const val IMAGE_NAME = "screenshot"
const val URL_GOOGLE_SEARCH = "https://www.google.com/searchbyimage?site=search&image_url="

val PATH_APP_TEMP_DIR = SnapDirs.getEnv(SNAP_USER_COMMON)
val PATH_APP_DB = "$PATH_APP_TEMP_DIR/.cache.db"

fun exit() {
    ConnectDb.close()
    exitProcess(0)
}

fun disableWarning() {
    System.err.close()
    System.setErr(System.out)
}