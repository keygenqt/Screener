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

import com.keygenqt.screener.base.Checker
import com.keygenqt.screener.base.Configuration
import com.keygenqt.screener.base.Info
import com.keygenqt.screener.base.mvp.BaseFrame
import com.keygenqt.screener.components.others.Helper
import com.keygenqt.screener.ui.select.SelectFrame
import com.keygenqt.screener.utils.*
import java.util.*
import kotlin.concurrent.schedule

val PARAMS = hashMapOf(
    ARGS_SELECT_DELAY to "5",
    ARGS_DEBUG to "false"
)

fun main(args: Array<String>) {

    System.err.close()
    System.setErr(System.out)

    Checker.tempDir()

    if (args.isEmpty()) {
        Info.showHelp()
    }

    for (item in args) {
        when (item) {
            ARGS_SELECT -> BaseFrame.open(SelectFrame(), hashMapOf("args" to ARGS_SELECT))
            ARGS_SEARCH -> BaseFrame.open(SelectFrame(), hashMapOf("args" to ARGS_SEARCH))
            ARGS_VISION -> {
                Checker.paramsCloud(Configuration.getCredentials())
                BaseFrame.open(SelectFrame(), hashMapOf("args" to ARGS_VISION))
            }
            ARGS_TRANSLATE -> {
                Checker.paramsCloud(Configuration.getCredentials())
                BaseFrame.open(SelectFrame(), hashMapOf("args" to ARGS_TRANSLATE))
            }
            ARGS_DEBUG -> PARAMS[ARGS_DEBUG] = "true"
            ARGS_VERSION -> Info.showVersion()
            ARGS_HELP -> Info.showHelp()
            ARGS_DESKTOP -> {
                Helper.desktop()
                Timer().schedule(10000) { exit() }
            }
            else -> {
                when {
                    item.contains("^$ARGS_SELECT_DELAY\\=.+".toRegex()) -> {
                        PARAMS[ARGS_SELECT_DELAY] = item.replace("$ARGS_SELECT_DELAY=", "")
                        Checker.delay("${PARAMS[ARGS_SELECT_DELAY]}")
                        val delay = "${PARAMS[ARGS_SELECT_DELAY]}".toInt()
                        var count = 0
                        Timer().schedule(1000L) {
                            val self = this
                            count++
                            println("timer: $count")
                            if (count != delay) {
                                Timer().schedule(1000L) { self.run() }
                            } else {
                                BaseFrame.open(SelectFrame(), hashMapOf("args" to ARGS_SELECT))
                            }
                        }
                    }
                }
            }
        }
    }
}