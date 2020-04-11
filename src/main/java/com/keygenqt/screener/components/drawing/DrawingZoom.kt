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

package com.keygenqt.screener.components.drawing

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Image
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionListener
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage

class DrawingZoom(
    private var imageBuffer: BufferedImage,
    private val repaint: () -> Unit
) : MouseMotionListener {

    private var x: Int = 0
    private var y: Int = 0

    override fun mouseMoved(e: MouseEvent) {
        x = e.x
        y = e.y
        repaint.invoke()
    }

    override fun mouseDragged(e: MouseEvent) {
        x = e.x
        y = e.y
    }

    fun repaint(g: Graphics2D) {
        val size = 160
        val sizeZoom = 80
        val xMin = x - sizeZoom / 2
        val yMin = y - sizeZoom / 2

        if (xMin > 0) {
            g.color = Color.white
            val imgCrop: BufferedImage = imageBuffer.getSubimage(xMin, yMin, sizeZoom, sizeZoom)
            g.stroke = BasicStroke(1f)
            g.clip = Ellipse2D.Float(x.toFloat(), y.toFloat(), size.toFloat(), size.toFloat())
            g.drawImage(imgCrop.getScaledInstance(size, size, Image.SCALE_SMOOTH), x, y, size, size, null)
            g.drawLine(x + sizeZoom, y, x + sizeZoom, y + size)
            g.drawLine(x, y + sizeZoom, x + size, y + sizeZoom)
        }
    }
}