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

package com.keygenqt.screener.ui.select

import com.keygenqt.screener.base.Info
import com.keygenqt.screener.base.mvp.BaseFrameMvp
import com.keygenqt.screener.base.mvp.InjectPresenter
import com.keygenqt.screener.components.others.Helper
import com.keygenqt.screener.components.selector.Selector
import com.keygenqt.screener.models.Settings
import com.keygenqt.screener.utils.*
import java.awt.GraphicsEnvironment
import java.awt.Image
import java.awt.image.BufferedImage
import java.util.*
import kotlin.concurrent.schedule

class SelectFrame : BaseFrameMvp(), ViewSelect {

    @InjectPresenter
    lateinit var presenter: SelectPresenter
    lateinit var trayClick: String
    private var console: Boolean = false

    override fun getIcon(): Image {
        return Helper.getImage("/logo.png").image
    }

    override fun onCreate(bundle: HashMap<String, Any>) {

        trayClick = bundle["menu"] as String
        console = if (bundle.containsKey("console")) bundle["console"] as Boolean else false

        presenter.loadImage()

        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        val gfxEnv = GraphicsEnvironment.getLocalGraphicsEnvironment()
        val gfxDev = gfxEnv.defaultScreenDevice
        gfxDev.fullScreenWindow = this

    }

    override fun setImage(image: BufferedImage) {
        Selector.init(image, this) { img ->
            if (img != null) {
                when (trayClick) {
                    TRAY_SELECTION -> {
                        if (Settings.getSetting().imgur) {
                            presenter.uploadToImglur(presenter.saveImage(img))
                        } else {
                            val path = presenter.saveImage(img)
                            notificationInfo(path)
                            Helper.copyToClipboardImage(path)
                        }
                    }
                    TRAY_SEARCH_IN_GOOGLE -> {
                        presenter.uploadToImglur(presenter.saveImage(img))
                    }
                    TRAY_CLOUD_VISION -> {
                        presenter.uploadToVision(presenter.saveImage(img))
                    }
                    TRAY_CLOUD_TRANSLATE -> {
                        presenter.uploadToTranslate(presenter.saveImage(img))
                    }
                }
            }
            this.isVisible = false
        }
    }

    override fun notificationInfo(data: String) {
        when (trayClick) {
            TRAY_SELECTION -> {
                if (data.contains("http")) {
                    Info.notification("$URL_SAVE_IN_CLIPBOARD<br>$data") {
//                        Desktop.getDesktop().browse(URI(data)) @todo snap!!!
                    }
                } else {
                    Info.notification("$IMAGE_SAVE_IN_CLIPBOARD<br>$data") {
//                        Desktop.getDesktop().open(File(data)) @todo snap!!!
                    }
                }
                Helper.copyToClipboard(data)
            }
            TRAY_SEARCH_IN_GOOGLE -> {
                Info.notification(STRING_LINK_FOR_SEARCH_READY)
                Helper.copyToClipboard("${URL_GOOGLE_SEARCH}${data}")
            }
            TRAY_CLOUD_VISION -> {
                Info.notification("$STRING_CLOUD_VISION_SUCCESS<br><br>$data")
                Helper.copyToClipboard(data)
            }
            TRAY_CLOUD_TRANSLATE -> {
                Info.notification("$STRING_CLOUD_VISION_TRANSLATE_SUCCESS<br><br>$data")
                Helper.copyToClipboard(data)
            }
        }

        if (console) {
            Timer().schedule(10000) {
                exit()
            }
        }
    }
}