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

import com.keygenqt.screener.base.mvp.BaseFrame
import com.keygenqt.screener.components.others.DesktopApi
import com.keygenqt.screener.components.others.Helper.Companion.desktop
import com.keygenqt.screener.models.Settings
import com.keygenqt.screener.ui.about.AboutFrame
import com.keygenqt.screener.ui.select.SelectFrame
import com.keygenqt.screener.ui.settings.SettingsFrame
import com.keygenqt.screener.utils.*
import dorkbox.systemTray.MenuItem
import dorkbox.systemTray.Separator
import dorkbox.systemTray.SystemTray
import org.apache.commons.io.FileUtils
import java.awt.event.ActionListener
import java.io.File
import java.util.*
import javax.swing.JOptionPane
import kotlin.concurrent.schedule

class AppTray {

    private var isFlickering = false

    companion object {
        const val TIMER = 200L
        const val TIMER_DELAY = 5000L
        private const val TRAY_CLOUD_VISION_INDEX = 4
        private const val TRAY_CLOUD_TRANSLATE_INDEX = 5

        fun statusCloud(model: Settings) {
            var stat = model.credentials.isNotEmpty()

            if (stat) {
                val f = File(Settings.getSetting().credentials)
                if (!f.exists() || f.isDirectory) {
                    stat = false
                    Info.notification("GOOGLE_APPLICATION_CREDENTIALS not found! File not exist.")
                    model.credentials = ""
                    model.save()
                }
            }

            (SystemTray.get().menu[TRAY_CLOUD_VISION_INDEX] as MenuItem).enabled = stat
            (SystemTray.get().menu[TRAY_CLOUD_TRANSLATE_INDEX] as MenuItem).enabled = stat
        }
    }

    fun init() {
        SystemTray.get().setImage(SystemTray::class.java.getResource("/logo.png"))
        setMenu(SystemTray.get())
        statusCloud(Settings.getSetting())
    }

    private fun setMenu(systemTray: SystemTray) {
        systemTray.menu.add(
            MenuItem(
                TRAY_SELECTION,
                ActionListener {
                    Timer().schedule(TIMER) {
                        BaseFrame.open(SelectFrame(), hashMapOf("menu" to TRAY_SELECTION))
                    }
                }
            )
        )
        systemTray.menu.add(
            MenuItem(
                TRAY_DELAY_SELECTION,
                ActionListener {
                    runFlickering()
                    Timer().schedule(TIMER_DELAY) {
                        stopFlickering()
                        BaseFrame.open(SelectFrame(), hashMapOf("menu" to TRAY_SELECTION))
                    }
                }
            )
        )
        systemTray.menu.add(
            MenuItem(TRAY_DESKTOP,
                ActionListener {
                    Timer().schedule(TIMER) {
                        desktop()
                    }
                }
            )
        )
        systemTray.menu.add(
            MenuItem(TRAY_SEARCH_IN_GOOGLE,
                ActionListener {
                    Timer().schedule(TIMER) {
                        BaseFrame.open(SelectFrame(), hashMapOf("menu" to TRAY_SEARCH_IN_GOOGLE))
                    }
                }
            )
        )
        systemTray.menu.add(
            MenuItem(TRAY_CLOUD_VISION,
                ActionListener {
                    Timer().schedule(TIMER) {
                        BaseFrame.open(SelectFrame(), hashMapOf("menu" to TRAY_CLOUD_VISION))
                    }
                }
            )
        )
        systemTray.menu.add(
            MenuItem(TRAY_CLOUD_TRANSLATE,
                ActionListener {
                    Timer().schedule(TIMER) {
                        BaseFrame.open(SelectFrame(), hashMapOf("menu" to TRAY_CLOUD_TRANSLATE))
                    }
                }
            )
        )
        systemTray.menu.add(Separator())
        systemTray.menu.add(
            MenuItem(TRAY_OPEN_DIR,
                ActionListener {
                    DesktopApi.open(File(Settings.getSetting().dir))
                }
            )
        )
        systemTray.menu.add(
            MenuItem(
                TRAY_CLEAR_DIR,
                ActionListener {
                    val input = JOptionPane.showOptionDialog(
                        null,
                        "The folder will be cleared!!!! \n You definitely want to clear the entire directory \n ${Settings.getSetting().dir}",
                        "Click a button",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        arrayOf("Clear Dir", "Close"),
                        null
                    )
                    if (input == JOptionPane.OK_OPTION) {
                        FileUtils.deleteDirectory(File(Settings.getSetting().dir))
                        FileUtils.forceMkdir(File(Settings.getSetting().dir))
                    }
                }
            )
        )
        systemTray.menu.add(Separator())
        systemTray.menu.add(
            MenuItem(TRAY_SETTINGS,
                ActionListener {
                    BaseFrame.open(SettingsFrame())
                }
            )
        )
        systemTray.menu.add(
            MenuItem(TRAY_ABOUT,
                ActionListener {
                    BaseFrame.open(AboutFrame())
                }
            )
        )
        systemTray.menu.add(
            MenuItem(
                TRAY_QUIT,
                ActionListener {
                    exit()
                }
            )
        )
    }

    fun stopFlickering() {
        isFlickering = false
        SystemTray.get().setImage(SystemTray::class.java.getResource("/logo.png"))
    }

    fun runFlickering() {
        isFlickering = true
        var second = 0;
        Timer().schedule(1000L) {
            val self = this
            second++
            if (isFlickering) {
                if ((second % 2) == 0) {
                    SystemTray.get().setImage(SystemTray::class.java.getResource("/logo.png"))
                } else {
                    SystemTray.get().setImage(SystemTray::class.java.getResource("/logo_action.png"))
                }
                Timer().schedule(1000L) { self.run() }
            }
        }
    }
}