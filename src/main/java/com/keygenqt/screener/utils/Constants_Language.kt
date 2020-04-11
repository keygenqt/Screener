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

package com.keygenqt.screener.utils

import javax.swing.DefaultListModel

// https://cloud.google.com/translate/docs/languages

val LANGUAGES_DATA = object : HashMap<String, String>() {
    init {
        put("Afrikaans", "af")
        put("Albanian", "sq")
        put("Amharic", "am")
        put("Arabic", "ar")
        put("Armenian", "hy")
        put("Azerbaijani", "az")
        put("Basque", "eu")
        put("Belarusian", "be")
        put("Bengali", "bn")
        put("Bosnian", "bs")
        put("Bulgarian", "bg")
        put("Catalan", "ca")
        put("Corsican", "co")
        put("Croatian", "hr")
        put("Czech", "cs")
        put("Danish", "da")
        put("Dutch", "nl")
        put("English", "en")
        put("Esperanto", "eo")
        put("Estonian", "et")
        put("Finnish", "fi")
        put("French", "fr")
        put("Frisian", "fy")
        put("Galician", "gl")
        put("Georgian", "ka")
        put("German", "de")
        put("Greek", "el")
        put("Gujarati", "gu")
        put("Haitian Creole", "ht")
        put("Hausa", "ha")
        put("Hungarian", "hu")
        put("Icelandic", "is")
        put("Igbo", "ig")
        put("Indonesian", "id")
        put("Irish", "ga")
        put("Italian", "it")
        put("Japanese", "ja")
        put("Javanese", "jv")
        put("Kannada", "kn")
        put("Kazakh", "kk")
        put("Khmer", "km")
        put("Korean", "ko")
        put("Kurdish", "ku")
        put("Kyrgyz", "ky")
        put("Lao", "lo")
        put("Latin", "la")
        put("Latvian", "lv")
        put("Lithuanian", "lt")
        put("Luxembourgish", "lb")
        put("Macedonian", "mk")
        put("Malagasy", "mg")
        put("Malay", "ms")
        put("Malayalam", "ml")
        put("Maltese", "mt")
        put("Maori", "mi")
        put("Marathi", "mr")
        put("Mongolian", "mn")
        put("Myanmar (Burmese)", "my")
        put("Nepali", "ne")
        put("Norwegian", "no")
        put("Nyanja (Chichewa)", "ny")
        put("Pashto", "ps")
        put("Persian", "fa")
        put("Polish", "pl")
        put("Portuguese (Portugal, Brazil)", "pt")
        put("Punjabi", "pa")
        put("Romanian", "ro")
        put("Russian", "ru")
        put("Samoan", "sm")
        put("Scots Gaelic", "gd")
        put("Serbian", "sr")
        put("Sesotho", "st")
        put("Shona", "sn")
        put("Sindhi", "sd")
        put("Sinhala (Sinhalese)", "si")
        put("Slovak", "sk")
        put("Slovenian", "sl")
        put("Somali", "so")
        put("Spanish", "es")
        put("Sundanese", "su")
        put("Swahili", "sw")
        put("Swedish", "sv")
        put("Tagalog (Filipino)", "tl")
        put("Tajik", "tg")
        put("Tamil", "ta")
        put("Telugu", "te")
        put("Thai", "th")
        put("Turkish", "tr")
        put("Ukrainian", "uk")
        put("Urdu", "ur")
        put("Uzbek", "uz")
        put("Vietnamese", "vi")
        put("Welsh", "cy")
        put("Xhosa", "xh")
        put("Yiddish", "yi")
        put("Yoruba", "yo")
        put("Zulu", "zu")
    }
}

val LANGUAGES = object : DefaultListModel<Any?>() {
    init {
        for (item in LANGUAGES_DATA) {
            addElement(item.key)
        }
    }
}