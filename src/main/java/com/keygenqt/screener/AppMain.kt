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

package com.keygenqt.screener

import com.keygenqt.screener.base.AppTray
import com.keygenqt.screener.base.Checker
import com.keygenqt.screener.base.ConnectDb
import com.keygenqt.screener.base.LockInstance
import com.keygenqt.screener.base.mvp.BaseFrame
import com.keygenqt.screener.components.others.Helper
import com.keygenqt.screener.models.Settings
import com.keygenqt.screener.ui.select.SelectFrame
import com.keygenqt.screener.utils.*
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.concurrent.schedule

fun main(args: Array<String>) {

    System.setProperty("com.j256.ormlite.logger.type", "LOCAL")
    System.setProperty("com.j256.ormlite.logger.level", "ERROR")

    disableWarning()
    Checker.tempDir()
    ConnectDb.open(MODELS)

//    Gtk.init(args)
//    Notify.init(APPLICATION_NAME)

    if (!Files.isDirectory(Paths.get(Settings.getSetting().dir))) {
        FileUtils.forceMkdir(File(Settings.getSetting().dir))
    }

    if (args.isNotEmpty()) {
        for (value in args) {
            if (value == ARGS_SELECT) {
                BaseFrame.open(SelectFrame(), hashMapOf("menu" to TRAY_SELECTION, "console" to true))
                break
            } else if (value == ARGS_SEARCH) {
                BaseFrame.open(SelectFrame(), hashMapOf("menu" to TRAY_SEARCH_IN_GOOGLE, "console" to true))
                break
            } else if (value == ARGS_VISION) {
                BaseFrame.open(SelectFrame(), hashMapOf("menu" to TRAY_CLOUD_VISION, "console" to true))
                break
            } else if (value == ARGS_TRANSLATE) {
                BaseFrame.open(SelectFrame(), hashMapOf("menu" to TRAY_CLOUD_TRANSLATE, "console" to true))
                break
            } else if (value == ARGS_DESKTOP) {
                Helper.desktop()
                Timer().schedule(10000) {
                    exit()
                }
                break
            }
        }
    } else {
        val run = LockInstance.lockInstance("${Settings.getSetting().dir}/.screener.lock")
        if (run) AppTray().init()
    }

//    Gtk.main()
    ConnectDb.close()
}