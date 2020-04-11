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

package com.keygenqt.screener.ui.about

import com.keygenqt.screener.base.mvp.BaseFrameMvp
import com.keygenqt.screener.base.mvp.InjectPresenter
import com.keygenqt.screener.components.others.Helper
import com.keygenqt.screener.utils.APPLICATION_NAME
import com.keygenqt.screener.utils.VERSION
import java.awt.Component
import java.awt.Image
import javax.swing.*

class AboutFrame : BaseFrameMvp("About $APPLICATION_NAME"), ViewAbout {

    @InjectPresenter
    lateinit var presenter: AboutPresenter

    override fun getIcon(): Image {
        return Helper.getImage("/logo.png").image
    }

    override fun onCreate(bundle: HashMap<String, Any>) {
        val contentPanel = JPanel()
        contentPanel.layout = BoxLayout(contentPanel, BoxLayout.Y_AXIS)
        val padding = BorderFactory.createEmptyBorder(30, 30, 30, 30)
        contentPanel.border = padding

        val label = JLabel()
        label.icon = Helper.getImage("/screenshot.png")
        label.alignmentX = Component.CENTER_ALIGNMENT
        contentPanel.add(label)

        contentPanel.add(JLabel("<html><br></html>"))
        contentPanel.add(JLabel("<html><br></html>"))

        val label2 = JLabel("Screener")
        label2.alignmentX = Component.CENTER_ALIGNMENT
        contentPanel.add(label2)

        val label3 = JLabel("version: $VERSION")
        label3.alignmentX = Component.CENTER_ALIGNMENT
        contentPanel.add(label3)

        contentPanel.add(JLabel("<html><br></html>"))

        val label4 = JLabel("(2020)")
        label4.alignmentX = Component.CENTER_ALIGNMENT
        contentPanel.add(label4)

        contentPanel.add(JLabel("<html><br></html>"))
        contentPanel.add(JLabel("<html><br></html>"))

        val button3 = JButton("Ok")
        button3.alignmentX = Component.CENTER_ALIGNMENT
        contentPanel.add(button3)

        button3.addActionListener { isVisible = false }

        add(contentPanel)

        setSize(550, 340)
        isResizable = false
    }
}