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

package com.keygenqt.screener.base

import com.keygenqt.screener.utils.VERSION
import com.keygenqt.screener.utils.exit

class Info {
    companion object {

        fun showInfo(text: String) {
            println()
            println(text)
        }

        fun error(text: String) {
            println()
            println(text)
            exit()
        }

        fun showHelp() {
            println(
                """
Usage: screener COMMAND=ARG...

Linux app for easy screenshot

Options
    --select                Select area
    --select-delay          Select area with delay
    --desktop               Make screenshot desktop
    --search                Search screenshot in Google search
    --vision                Get text from screenshot
    --translate             Translate text from screenshot

    --debug                 Enable processes logging terminal
    --version               Show the version and exit
    --help                  Show help
            """.trimIndent()
            )
            exit()
        }

        fun showVersion() {
            println("Screener Ver $VERSION")
            exit()
        }
    }
}

// https://cloud.google.com/translate/docs/languages