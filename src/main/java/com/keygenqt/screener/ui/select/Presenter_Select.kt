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

import com.keygenqt.screener.base.Configuration
import com.keygenqt.screener.base.mvp.BasePresenterMvp
import com.keygenqt.screener.components.others.Helper.Companion.getLastIndex
import com.keygenqt.screener.components.uploads.UploadCloudTranslateImage
import com.keygenqt.screener.components.uploads.UploadCloudVisionImage
import com.keygenqt.screener.components.uploads.UploadImgurImage
import com.keygenqt.screener.utils.IMAGE_NAME
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.concurrent.schedule

class SelectPresenter : BasePresenterMvp<SelectFrame>() {

    override fun onFirstViewAttach() {

    }

    fun loadImage() {
        view.setImage(Robot().createScreenCapture(Rectangle(Toolkit.getDefaultToolkit().screenSize)))
    }

    fun saveImage(image: BufferedImage): String {
        val dir = Configuration.getFolder()
        val index = getLastIndex(dir, IMAGE_NAME)
        val path = "$dir/${IMAGE_NAME}-${index + 1}.png"
        ImageIO.write(image, "png", File(path))
        return path
    }

    fun uploadToImglur(path: String) {
        Timer().schedule(100) {
            val url = UploadImgurImage.upload(path)
            view.notificationInfo(url)
        }
    }

    fun uploadToVision(path: String) {
        Timer().schedule(100) {
            val text = UploadCloudVisionImage.getString(path)
            view.notificationInfo(text)
        }
    }

    fun uploadToTranslate(path: String) {
        Timer().schedule(100) {
            val text = UploadCloudTranslateImage.translate(UploadCloudVisionImage.getString(path))
            view.notificationInfo(text)
        }
    }
}