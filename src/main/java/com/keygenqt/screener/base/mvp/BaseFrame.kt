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

package com.keygenqt.screener.base.mvp

import java.awt.Frame

open class BaseFrame {
    companion object {
        @Suppress("UNCHECKED_CAST")
        fun open(frame: BaseFrameMvp, bundle: HashMap<String, Any> = hashMapOf()) {
            frame::class.java.declaredFields.forEach { field ->
                field.getAnnotation(InjectPresenter::class.java) ?: return@forEach
                field.isAccessible = true
                val presenter: Any = Class.forName(field.type.canonicalName).newInstance() as Any
                (presenter as BasePresenterMvp<BaseFrameMvp>).view = frame
                field.set(frame, presenter)
                presenter.onFirstViewAttach()

                // base config
                frame.iconImage = frame.getIcon()

                frame.onCreate(bundle)

                frame.toFront()
                frame.requestFocus()
                frame.state = Frame.NORMAL

                frame.setLocation()

                frame.isVisible = true
            }
        }
    }
}