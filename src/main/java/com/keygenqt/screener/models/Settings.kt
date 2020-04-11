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

package com.keygenqt.screener.models

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.j256.ormlite.table.TableUtils
import com.keygenqt.screener.base.ConnectDb

@DatabaseTable(tableName = "settings")
class Settings(
    @DatabaseField
    var languageTranslate: String = "ru",
    @DatabaseField
    var credentials: String = "",
    @DatabaseField
    var dir: String = System.getProperty("user.home") + "/Pictures/screener",
    @DatabaseField
    var imgur: Boolean = true
) {
    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    var id: Int = 0

    companion object {
        fun getSetting(): Settings {
            val qb = ConnectDb.dao(Settings::class.java).queryBuilder()
            qb.orderByRaw("id DESC LIMIT 1")
            return try {
                ConnectDb.dao(Settings::class.java).query(qb.prepare()).first()
            } catch (ex: Exception) {
                Settings()
            }
        }
    }

    fun save() {
        TableUtils.clearTable(ConnectDb.getConnect(), Settings::class.java)
        ConnectDb.dao(Settings::class.java).create(this)
    }
}