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

import com.keygenqt.screener.PARAMS
import com.keygenqt.screener.base.Configuration
import com.keygenqt.screener.base.Info
import com.keygenqt.screener.base.mvp.BaseFrameMvp
import com.keygenqt.screener.base.mvp.InjectPresenter
import com.keygenqt.screener.components.others.Helper
import com.keygenqt.screener.components.selector.Selector
import com.keygenqt.screener.utils.*
import java.awt.GraphicsEnvironment
import java.awt.Image
import java.awt.image.BufferedImage
import java.util.*
import kotlin.concurrent.schedule

class SelectFrame : BaseFrameMvp(), ViewSelect {

    @InjectPresenter
    lateinit var presenter: SelectPresenter
    lateinit var arg: String

    override fun getIcon(): Image {
        return Helper.getImage("/logo.png").image
    }

    override fun onCreate(bundle: HashMap<String, Any>) {
        arg = bundle["args"] as String
        presenter.loadImage()
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        val gfxEnv = GraphicsEnvironment.getLocalGraphicsEnvironment()
        val gfxDev = gfxEnv.defaultScreenDevice
        gfxDev.fullScreenWindow = this
    }

    override fun setImage(image: BufferedImage) {
        Selector.init(image, this) { img ->
            if (img != null) {
                when (arg) {
                    ARGS_SELECT -> {
                        if (Configuration.isImgur()) {
                            presenter.uploadToImglur(presenter.saveImage(img))
                        } else {
                            val path = presenter.saveImage(img)
                            notificationInfo(path)
                            Helper.copyToClipboardImage(path)
                        }
                    }
                    ARGS_SEARCH -> {
                        presenter.uploadToImglur(presenter.saveImage(img))
                    }
                    ARGS_VISION -> {
                        presenter.uploadToVision(presenter.saveImage(img))
                    }
                    ARGS_TRANSLATE -> {
                        presenter.uploadToTranslate(presenter.saveImage(img))
                    }
                }
            } else {
                Timer().schedule("${PARAMS[ARGS_CLOSE_TIME]}".toLong()) { exit() }
            }
            this.isVisible = false
        }
    }

    override fun notificationInfo(data: String) {
        when (arg) {
            ARGS_SELECT -> {
                if (data.contains("http")) {
                    Info.showInfo(URL_SAVE_IN_CLIPBOARD.replace("{data}", data))
                } else {
                    Info.showInfo(IMAGE_SAVE_IN_CLIPBOARD.replace("{data}", data))
                }
                Helper.copyToClipboard(data)
            }
            ARGS_SEARCH -> {
                Info.showInfo(STRING_LINK_FOR_SEARCH_READY)
                Helper.copyToClipboard("${URL_GOOGLE_SEARCH}${data}")
            }
            ARGS_VISION -> {
                Info.showInfo(STRING_CLOUD_VISION_SUCCESS.replace("{data}", data.trim()))
                Helper.copyToClipboard(data)
            }
            ARGS_TRANSLATE -> {
                Info.showInfo(STRING_CLOUD_VISION_TRANSLATE_SUCCESS.replace("{data}", data.trim()))
                Helper.copyToClipboard(data)
            }
        }
        Timer().schedule("${PARAMS[ARGS_CLOSE_TIME]}".toLong()) { exit() }
    }
}