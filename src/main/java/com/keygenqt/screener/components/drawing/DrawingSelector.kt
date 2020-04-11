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

import java.awt.Point
import java.awt.Rectangle
import java.awt.event.MouseEvent

interface DrawingSelector {
    fun disable()
    fun enable()

    fun mouseDragged(pointStart: Point, e: MouseEvent): Rectangle {
        return when {
            pointStart.x < e.point.x && pointStart.y < e.point.y -> {
                Rectangle(pointStart.x, pointStart.y, e.point.x - pointStart.x, e.point.y - pointStart.y)
            }
            pointStart.x < e.point.x && pointStart.y > e.point.y -> {
                Rectangle(pointStart.x, e.point.y, e.point.x - pointStart.x, pointStart.y - e.point.y)
            }
            pointStart.x > e.point.x && pointStart.y > e.point.y -> {
                Rectangle(
                    pointStart.x + (e.point.x - pointStart.x),
                    e.point.y,
                    pointStart.x - e.point.x,
                    pointStart.y - e.point.y
                )
            }
            else -> {
                Rectangle(
                    pointStart.x + (e.point.x - pointStart.x),
                    pointStart.y,
                    pointStart.x - e.point.x,
                    e.point.y - pointStart.y
                )
            }
        }
    }
}