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
import java.awt.Frame
import java.awt.Graphics2D
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.image.BufferedImage
import javax.swing.*

class Selector(
    private val imageBuffer: BufferedImage,
    private val frame: Frame,
    private val param: (BufferedImage?) -> Unit
) : MouseListener,
    KeyListener {

    companion object {
        fun init(image: BufferedImage, frame: Frame, param: (BufferedImage?) -> Unit) {
            Selector(image, frame, param).start()
        }
    }

    private lateinit var imageLabel: BufferedImage
    private var drawings = arrayListOf<DrawingSelector?>()
    private val label = JLabel()

    private val drawingSelect = DrawingSelect({ drawing ->
        drawings[0] = null
        repaintImage(drawing)
    }, { drawing ->
        drawings[0] = drawing
        repaintImage(null)
    })

    private val drawingRectangle = DrawingRectangle({ drawing ->
        repaintImage(drawing)
    }, { drawing ->
        drawings.add(drawing)
        repaintImage(null)
    })

    private val drawingCircle = DrawingCircle({ drawing ->
        repaintImage(drawing)
    }, { drawing ->
        drawings.add(drawing)
        repaintImage(null)
    })

    private val drawingOval = DrawingOval({ drawing ->
        repaintImage(drawing)
    }, { drawing ->
        drawings.add(drawing)
        repaintImage(null)
    })

    private lateinit var drawingZoom: DrawingZoom

    fun start() {
        frame.add(label)
        frame.addKeyListener(this)

        imageLabel = BufferedImage(imageBuffer.width, imageBuffer.height, BufferedImage.TYPE_INT_RGB)
        label.icon = ImageIcon(imageLabel)

        drawingZoom = DrawingZoom(imageBuffer) {
            repaintImage(null)
        }

        repaintImage(null)

        label.addMouseListener(this)

        drawings.add(drawingSelect)
        label.addMouseListener(drawingSelect)
        label.addMouseMotionListener(drawingSelect)

        label.addMouseListener(drawingRectangle)
        label.addMouseMotionListener(drawingRectangle)

        label.addMouseListener(drawingCircle)
        label.addMouseMotionListener(drawingCircle)

        label.addMouseListener(drawingOval)
        label.addMouseMotionListener(drawingOval)

        label.addMouseMotionListener(drawingZoom)

        disableAllMouseMotionListener()
        drawingSelect.enable()
    }

    private fun repaintImage(drawing: DrawingSelector?) {
        label.repaintBg(imageBuffer, imageLabel, drawingZoom, drawings, drawing)
    }

    private fun disableAllMouseMotionListener() {
        for (listener in label.mouseMotionListeners) {
            if (listener is DrawingSelector) {
                listener.disable()
            }
        }
    }

    override fun mouseReleased(p0: MouseEvent?) {

    }

    override fun mouseEntered(p0: MouseEvent?) {

    }

    override fun mouseClicked(p0: MouseEvent?) {
        if (p0 != null) {
            if (SwingUtilities.isRightMouseButton(p0)) {
                val menu = JPopupMenu("Menu")
                val select = JMenuItem("Select")
                val rectangle = JMenuItem("Rectangle")
                val circle = JMenuItem("Circle")
                val oval = JMenuItem("Oval")

                menu.add(select)
                menu.add(rectangle)
                menu.add(circle)
                menu.add(oval)

                select.addActionListener {
                    disableAllMouseMotionListener()
                    drawingSelect.enable()
                }

                rectangle.addActionListener {
                    disableAllMouseMotionListener()
                    drawingRectangle.enable()
                }

                circle.addActionListener {
                    disableAllMouseMotionListener()
                    drawingCircle.enable()
                }

                oval.addActionListener {
                    disableAllMouseMotionListener()
                    drawingOval.enable()
                }

                menu.show(label, p0.x, p0.y)
            }
        }
    }

    override fun mouseExited(p0: MouseEvent?) {

    }

    override fun mousePressed(p0: MouseEvent?) {

    }

    override fun keyTyped(p0: KeyEvent?) {

    }

    override fun keyPressed(p0: KeyEvent?) {
        if (p0 != null) {
            if (p0.keyCode == KeyEvent.VK_DELETE || p0.keyCode == KeyEvent.VK_BACK_SPACE) {
                if (drawings.size > 1) {
                    drawings.removeAt(drawings.lastIndex)
                    repaintImage(null)
                }
            } else if (p0.keyCode == KeyEvent.VK_ENTER || p0.keyCode == KeyEvent.VK_SPACE) {

                label.repaintBg(imageBuffer, imageLabel, null, drawings, null)

                drawingSelect.rectangle?.let {
                    val imageCrop = BufferedImage(
                        it.width,
                        it.height,
                        BufferedImage.TYPE_INT_RGB
                    )
                    val g: Graphics2D = imageCrop.createGraphics()
                    g.drawImage(
                        imageLabel,
                        it.x * -1,
                        it.y * -1,
                        imageLabel.width,
                        imageLabel.height,
                        null
                    )
                    param.invoke(imageCrop)
                }
            } else {
                param.invoke(null)
            }
        }
    }

    override fun keyReleased(p0: KeyEvent?) {

    }
}