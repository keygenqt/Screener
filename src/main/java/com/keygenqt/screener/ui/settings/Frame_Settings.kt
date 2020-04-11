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

package com.keygenqt.screener.ui.settings

import com.keygenqt.screener.base.AppTray
import com.keygenqt.screener.base.mvp.BaseFrameMvp
import com.keygenqt.screener.base.mvp.InjectPresenter
import com.keygenqt.screener.components.others.Helper
import com.keygenqt.screener.models.Settings
import com.keygenqt.screener.utils.*
import java.awt.*
import java.awt.event.ItemEvent
import java.io.File
import java.util.*
import javax.swing.*
import javax.swing.BoxLayout
import javax.swing.filechooser.FileNameExtensionFilter

class SettingsFrame : BaseFrameMvp("Settings $APPLICATION_NAME"), ViewSettings {

    @InjectPresenter
    lateinit var presenter: SettingsPresenter

    override fun getIcon(): Image {
        return Helper.getImage("/logo.png").image
    }

    override fun onCreate(bundle: HashMap<String, Any>) {
        setSize(550, 380)
        isResizable = false

        val tabbedPane = JTabbedPane()

        val panel1 = JPanel()
        tabbedPane.addTab(STRING_TAB_GENERAL, null, panel1)
        initGeneral(panel1)
        add(tabbedPane)
    }

    private fun initGeneral(component: JPanel) {

        val generalPanel = fun(): JPanel {
            val settings = Settings.getSetting()
            val dim = Dimension(530, 65)
            val panel = JPanel()
            val label = Label(settings.dir)
            val btn = JButton(STRING_CHANGE)

            label.size = label.preferredSize;
            label.preferredSize = Dimension(400, 25)
            label.maximumSize = Dimension(400, 25)

            btn.addActionListener {
                val chooser = JFileChooser()
                chooser.currentDirectory = File(System.getProperty("user.home"))
                chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
                chooser.isAcceptAllFileFilterUsed = false;
                if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    settings.dir = chooser.selectedFile.toString();
                    settings.save()
                    label.text = settings.dir
                }
            }

            panel.add(label)
            panel.add(btn)

            panel.alignmentX = Component.LEFT_ALIGNMENT
            panel.layout = FlowLayout()
            panel.border = BorderFactory.createTitledBorder(STRING_PANEL_GENERAL)
            panel.preferredSize = dim
            panel.maximumSize = dim

            return panel
        }

        val GOOGLE_APPLICATION_CREDENTIALS = fun(): JPanel {

            val settings = Settings.getSetting()
            val panel = JPanel()
            val label = Label(if (settings.credentials.isBlank()) STRING_EMPTY else settings.credentials)
            val btn = JButton(STRING_ADD)
            val btnClear = JButton(STRING_CLEAR)

            panel.preferredSize = Dimension(530, 65)
            panel.maximumSize = Dimension(530, 65)

            btn.preferredSize = Dimension(90, 25)
            btnClear.preferredSize = Dimension(90, 25)

            label.size = label.preferredSize;
            label.preferredSize = Dimension(400, 25)
            label.maximumSize = Dimension(400, 25)

            val setClear = fun(status: Boolean) {
                btnClear.isVisible = status
                btn.isVisible = !status
            }

            setClear(settings.credentials.isNotEmpty())

            btnClear.addActionListener {
                label.text = STRING_EMPTY
                settings.credentials = ""
                settings.save()
                setClear(false)
                AppTray.statusCloud(settings)
            }

            btn.addActionListener {
                val chooser = JFileChooser()
                val filter = FileNameExtensionFilter("JSON FILES", "json", "json")

                chooser.fileFilter = filter
                chooser.currentDirectory = File(System.getProperty("user.home"))
                chooser.fileSelectionMode = JFileChooser.FILES_ONLY
                chooser.isAcceptAllFileFilterUsed = false;
                if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    settings.credentials = chooser.selectedFile.toString()
                    settings.save()
                    label.text = settings.credentials
                    setClear(true)
                    AppTray.statusCloud(settings)
                }
            }

            panel.add(label)
            panel.add(btnClear)
            panel.add(btn)

            panel.alignmentX = Component.LEFT_ALIGNMENT
            panel.layout = FlowLayout()
            panel.border = BorderFactory.createTitledBorder(STRING_PANEL_GOOGLE_APPLICATION_CREDENTIALS)

            return panel
        }

        val language = fun(): JPanel {

            val settings = Settings.getSetting()
            val panel = JPanel()
            panel.preferredSize = Dimension(530, 95)
            panel.maximumSize = Dimension(530, 95)

            val list = JList(LANGUAGES)
            list.minimumSize = Dimension(510, 65)
            list.selectionMode = ListSelectionModel.SINGLE_SELECTION;

            val scrollPane = JScrollPane(list)
            scrollPane.preferredSize = Dimension(510, 65)
            scrollPane.horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER

            list.selectedIndex = LANGUAGES_DATA.values.indexOf(settings.languageTranslate)

            list.addListSelectionListener {
                settings.languageTranslate = LANGUAGES_DATA[LANGUAGES[list.selectedIndex].toString()] ?: "ru"
                settings.save()
            }

            panel.add(scrollPane)

            panel.alignmentX = Component.LEFT_ALIGNMENT
            panel.layout = FlowLayout()
            panel.border = BorderFactory.createTitledBorder(STRING_LANGUAGE_TRANSLATE)

            return panel
        }

        val imgurPanel = fun(): JPanel {
            val settings = Settings.getSetting()
            val dim = Dimension(530, 65)
            val panel = JPanel()
            val checkBox2 = JCheckBox(STRING_UPLOAD_IMGUR, true)

            checkBox2.setBounds(100, 150, 50, 50)
            checkBox2.isSelected = settings.imgur
            checkBox2.addItemListener { itemEvent ->
                settings.imgur = (itemEvent.stateChange == ItemEvent.SELECTED)
                settings.save()
            }

            panel.add(checkBox2)
            panel.alignmentX = Component.LEFT_ALIGNMENT
            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
            panel.border = BorderFactory.createTitledBorder(STRING_PANEL_IMGUR)
            panel.preferredSize = dim
            panel.maximumSize = dim

            return panel
        }

        component.add(generalPanel())
        component.add(GOOGLE_APPLICATION_CREDENTIALS())
        component.add(language())
        component.add(imgurPanel())
    }
}