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

package com.keygenqt.screener.components.selector

import com.keygenqt.screener.components.drawing.*
import com.keygenqt.screener.utils.COLOR_BG
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.image.BufferedImage
import javax.swing.JLabel

fun JLabel.repaintBg(
    imageBuffer: BufferedImage,
    imageLabel: BufferedImage,
    drawingZoom: DrawingZoom?,
    drawings: ArrayList<DrawingSelector?>,
    drawing: DrawingSelector? = null
) {

    val d: ArrayList<DrawingSelector?> = ArrayList(drawings)
    val g: Graphics2D = imageLabel.createGraphics()
    g.drawImage(imageBuffer, 0, 0, null)

    if (drawing is DrawingSelect) {
        d[0] = drawing
    } else {
        if (drawings.isEmpty() || (drawings[0] as DrawingSelect).rectangle == null) {
            g.color = COLOR_BG
            g.fill(Rectangle(0, 0, imageBuffer.width, imageBuffer.height))
        }
        d.add(drawing)
    }

    for (item in d) {
        when (item) {
            is DrawingSelect -> {
                item.repaint(g, item.rectangle, imageBuffer.width, imageBuffer.height)
            }
            is DrawingRectangle -> {
                item.repaint(g, item.rectangle)
            }
            is DrawingCircle -> {
                item.repaint(g, item.rectangle)
            }
            is DrawingOval -> {
                item.repaint(g, item.rectangle)
            }
        }
    }

    drawingZoom?.repaint(g, imageBuffer.width, imageBuffer.height)

    repaint(Rectangle(0, 0, imageBuffer.width, imageBuffer.height))
    g.dispose()
}