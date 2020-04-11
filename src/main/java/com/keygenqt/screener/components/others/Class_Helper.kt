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

package com.keygenqt.screener.components.others

import com.keygenqt.screener.base.Info
import com.keygenqt.screener.components.ImageSelection
import com.keygenqt.screener.components.uploads.UploadImgurImage
import com.keygenqt.screener.models.Settings
import com.keygenqt.screener.utils.IMAGE_NAME
import com.keygenqt.screener.utils.IMAGE_SAVE_IN_CLIPBOARD
import com.keygenqt.screener.utils.URL_SAVE_IN_CLIPBOARD
import com.keygenqt.screener.utils.exit
import java.awt.Image
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import javax.swing.ImageIcon

class Helper {
    companion object {
        fun getImage(path: String): ImageIcon {
            val img: BufferedImage = ImageIO.read(this::class.java.getResourceAsStream(path))
            return ImageIcon(img.getScaledInstance(100, 100, Image.SCALE_SMOOTH))
        }

        fun getLastIndex(path: String, imageName: String): Int {
            return File(path).listFiles()!!.filter { it.name.contains(imageName) }.map {
                val name = it.name.replace("[^0-9]+".toRegex(), "")
                if (name.isEmpty()) 0 else name.toInt()
            }.toList().max() ?: 0
        }

        fun createDesktopScreenshot(): String {
            val image = Robot().createScreenCapture(Rectangle(Toolkit.getDefaultToolkit().screenSize))
            val dir = Settings.getSetting().dir
            val index = getLastIndex(dir, IMAGE_NAME)
            val path = "${Settings.getSetting().dir}/$IMAGE_NAME-${index + 1}.png"
            ImageIO.write(image, "png", File(path))
            return path
        }

        fun copyToClipboard(text: String) {
            val stringSelection = StringSelection(text)
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            clipboard.setContents(stringSelection, null)
        }

        fun copyToClipboardImage(path: String) {
            val imageSelection = ImageSelection(ImageIO.read(File(path)))
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            clipboard.setContents(imageSelection, null)
        }

        fun desktop() {
            val path = createDesktopScreenshot()
            if (Settings.getSetting().imgur) {
                val url = UploadImgurImage.upload(path)
                Info.notification("$URL_SAVE_IN_CLIPBOARD<br>$url") {
//                    Desktop.getDesktop().browse(URI(url)) @todo snap!!!
                }
                copyToClipboard(url)
            } else {
                Info.notification("$IMAGE_SAVE_IN_CLIPBOARD<br>$path") {
//                    Desktop.getDesktop().open(File(path)) @todo snap!!!
                }
                copyToClipboardImage(path)
            }
        }

        fun restartApp() {
            val command = ArrayList<String>()
            command.add("/usr/bin/screener")
            val builder = ProcessBuilder(command)
            builder.start()
            exit()
        }
    }
}