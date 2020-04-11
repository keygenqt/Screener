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

package com.keygenqt.screener.ui.notification

import com.keygenqt.screener.base.mvp.BaseFrameMvp
import com.keygenqt.screener.base.mvp.InjectPresenter
import com.keygenqt.screener.components.others.Helper
import com.keygenqt.screener.utils.APPLICATION_NAME
import java.awt.Color
import java.awt.FlowLayout
import java.awt.GraphicsEnvironment
import java.awt.Image
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.*
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.EmptyBorder
import kotlin.concurrent.schedule


class NotificationFrame : BaseFrameMvp("About $APPLICATION_NAME"), NotificationView {

    @InjectPresenter
    lateinit var presenter: NotificationPresenter

    override fun getIcon(): Image {
        return Helper.getImage("/logo.png").image
    }

    override fun setLocation() {
        val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        val defaultScreen = ge.defaultScreenDevice
        val rect = defaultScreen.defaultConfiguration.bounds
        val x: Int = rect.maxX.toInt() - width - 25
        setLocation(x, 50)
    }

    override fun onCreate(bundle: HashMap<String, Any>) {

        val panel = JPanel()
        val layout = panel.layout as FlowLayout
        layout.vgap = 0
        layout.hgap = 0
        panel.background = Color(25, 25, 25)
        panel.border = BorderFactory.createLineBorder(Color.BLACK);

        this.layout = layout

        val jLabel = JLabel("<html><b>${APPLICATION_NAME}</b> Info:<br><br>${bundle["message"]}</html>")
        jLabel.border = EmptyBorder(10, 10, 10, 10)
        jLabel.foreground = Color.WHITE;

        jLabel.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                isVisible = false
                if (bundle.containsKey("click")) {
                    val click = bundle["click"] as () -> Unit
                    click.invoke()
                }
                bundle["message"]
            }

            override fun mouseEntered(e: MouseEvent?) {
            }

            override fun mouseExited(e: MouseEvent?) {
            }
        })

        panel.add(jLabel);
        add(panel);
        defaultCloseOperation = EXIT_ON_CLOSE;
        isUndecorated = true;
        pack();
        setLocationRelativeTo(null);

        isResizable = false
        opacity = 0.80f

        Timer().schedule(3000L) {
            isVisible = false
        }.run()
    }
}